package space_travel;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 
 * @since March 26, 2025
 * <p>
 * This class handles file reading operations for the space travel simulation.
 * It reads and parses data from input files for planets, spaceships, and people.
 * </p>
 */
public class FileReader {
    
    public static List<Planet> readPlanets(String filePath) {
        List<Planet> planets = new ArrayList<>();
        
        try (BufferedReader br = new BufferedReader(new java.io.FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Use simple parsing with minimal checking
                String[] parts = line.split("#");
                if (parts.length >= 3) {
                    String name = parts[0].trim();
                    int dayLength = Integer.parseInt(parts[1].trim());
                    String date = parts[2].trim();
                    
                    Planet planet = new Planet(name, dayLength, date);
                    planets.add(planet);
                    System.out.println("Added planet: " + name);
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
        List<Spaceship> ships = new ArrayList<>();
        
        try (BufferedReader br = new BufferedReader(new java.io.FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
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
                    System.out.println("Added spaceship: " + name);
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
        List<Person> people = new ArrayList<>();
        
        try (BufferedReader br = new BufferedReader(new java.io.FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Use simple parsing with minimal checking
                String[] parts = line.split("#");
                if (parts.length >= 4) {
                    String name = parts[0].trim();
                    int age = Integer.parseInt(parts[1].trim());
                    int lifeRemaining = Integer.parseInt(parts[2].trim());
                    String currentVehicle = parts[3].trim();
                    
                    Person person = new Person(name, age, lifeRemaining, currentVehicle);
                    people.add(person);
                    System.out.println("Added person: " + name);
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
        Map<String, Spaceship> shipMap = new HashMap<>();
        Map<String, Planet> planetMap = new HashMap<>();
        
        for (Spaceship ship : ships) {
            shipMap.put(ship.getName(), ship);
        }
        
        for (Planet planet : planets) {
            planetMap.put(planet.getName(), planet);
        }
        
        for (Person person : people) {
            String vehicleName = person.getCurrentVehicle();
            
            if (shipMap.containsKey(vehicleName)) {
                Spaceship ship = shipMap.get(vehicleName);
                ship.addPassenger(person);
            } else {
                if (planetMap.containsKey(vehicleName)) {
                    planetMap.get(vehicleName).addPerson(person);
                } else if (!planets.isEmpty()) {
                    planets.get(0).addPerson(person);
                    person.setCurrentVehicle(planets.get(0).getName());
                    System.out.println("Warning: " + vehicleName + " not found for " + person.getName() + 
                                     ". Placed on first planet.");
                }
            }
        }
    }
}
