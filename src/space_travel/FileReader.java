/**
 * @author 
 * @since March 26, 2025
 * <p>
 * This class handles file reading operations for the space travel simulation.
 * It reads and parses data from input files for planets, spaceships, and people.
 * </p>
 */

package space_travel;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileReader {
    
    // Larger buffer size for reading large files
    private static final int BUFFER_SIZE = 8192;
    
    public static List<Planet> readPlanets(String filePath) {
        List<Planet> planets = new ArrayList<>(100); // Pre-allocate for better performance
        
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath)), BUFFER_SIZE)) {
            String line;
            while ((line = br.readLine()) != null) {
                // Skip empty lines
                if (line.trim().isEmpty()) continue;
                
                // Use simple parsing with minimal checking
                String[] parts = line.split("#");
                if (parts.length >= 3) {
                    String name = parts[0].trim();
                    int dayLength = Integer.parseInt(parts[1].trim());
                    String date = parts[2].trim();
                    
                    Planet planet = new Planet(name, dayLength, date);
                    planets.add(planet);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading planet file: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error processing planet file: " + e.getMessage());
        }
        
        return planets;
    }
    
    public static List<Spaceship> readSpaceships(String filePath) {
        List<Spaceship> ships = new ArrayList<>(100); // Pre-allocate for better performance
        
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath)), BUFFER_SIZE)) {
            String line;
            while ((line = br.readLine()) != null) {
                // Skip empty lines
                if (line.trim().isEmpty()) continue;
                
                // Use simple parsing with minimal checking
                String[] parts = line.split("#");
                if (parts.length >= 5) {
                    String name = parts[0].trim();
                    String departurePlanet = parts[1].trim();
                    String destinationPlanet = parts[2].trim();
                    String departureDate = parts[3].trim();
                    int travelDuration = Integer.parseInt(parts[4].trim());
                    
                    Spaceship ship = new Spaceship(name, departurePlanet, destinationPlanet, departureDate, travelDuration);
                    ships.add(ship);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading spaceship file: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error processing spaceship file: " + e.getMessage());
        }
        
        return ships;
    }
    
    public static List<Person> readPeople(String filePath) {
        List<Person> people = new ArrayList<>(1000); // Pre-allocate for better performance
        
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath)), BUFFER_SIZE)) {
            String line;
            while ((line = br.readLine()) != null) {
                // Skip empty lines
                if (line.trim().isEmpty()) continue;
                
                // Use simple parsing with minimal checking
                String[] parts = line.split("#");
                if (parts.length >= 4) {
                    String name = parts[0].trim();
                    int age = Integer.parseInt(parts[1].trim());
                    int lifeRemaining = Integer.parseInt(parts[2].trim());
                    String currentVehicle = parts[3].trim();
                    
                    Person person = new Person(name, age, lifeRemaining, currentVehicle);
                    people.add(person);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading person file: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error processing person file: " + e.getMessage());
        }
        
        return people;
    }
    
    public static void placePeople(List<Person> people, List<Planet> planets, List<Spaceship> ships) {
        // Create maps for faster lookups
        Map<String, Spaceship> shipMap = new HashMap<>(ships.size() * 2);
        Map<String, Planet> planetMap = new HashMap<>(planets.size() * 2);
        
        for (Spaceship ship : ships) {
            shipMap.put(ship.getName(), ship);
        }
        
        for (Planet planet : planets) {
            planetMap.put(planet.getName(), planet);
        }
        
        for (Person person : people) {
            String vehicleName = person.getCurrentVehicle();
            
            // Check if the vehicle name matches a ship
            Spaceship ship = shipMap.get(vehicleName);
            if (ship != null) {
                // CORRECT LOGIC: Place person on the ship's departure planet
                Planet departurePlanet = planetMap.get(ship.getDeparturePlanet());
                if (departurePlanet != null) {
                    departurePlanet.addPerson(person);
                    // Keep person.currentVehicle as the ship name for boarding later
                } else {
                    // Handle case where departure planet doesn't exist (optional)
                    System.err.println("Warning: Departure planet '" + ship.getDeparturePlanet() + "' not found for ship '" + ship.getName() + "'. Person '" + person.getName() + "' cannot be placed.");
                }
                continue; // Move to next person
            }
            
            // Otherwise, assign to matching planet if one exists
            Planet planet = planetMap.get(vehicleName);
            if (planet != null) {
                planet.addPerson(person);
                continue; // Move to next person
            }
            
            // If vehicleName matches neither a ship nor a planet, the person is ignored
            // (as per removal of the defaultPlanet fallback in previous steps)
            System.err.println("Warning: Initial location '" + vehicleName + "' not found for person '" + person.getName() + "'. Person ignored.");
        }
    }
}
