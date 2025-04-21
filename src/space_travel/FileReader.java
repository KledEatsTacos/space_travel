/**
 * @author Mustafa Masri
 * @since March 26, 2025
 * <p>
 * This class handles file reading operations for the space travel simulation.
 * It reads and parses data from input files for planets, spaceships, and people.
 * </p>
 */

package space_travel;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class FileReader {
    
    public static List<Planet> readPlanets(String filePath) {
        return readFile(filePath, line -> {
            String[] parts = line.split("#");
            if (parts.length >= 3) {
                String name = parts[0].trim();
                int dayLength = Integer.parseInt(parts[1].trim());
                String date = parts[2].trim();
                
                return new Planet(name, dayLength, date);
            }
            return null;
        });
    }
    
    public static List<Spaceship> readSpaceships(String filePath) {
        return readFile(filePath, line -> {
            String[] parts = line.split("#");
            if (parts.length >= 5) {
                String name = parts[0].trim();
                String departurePlanet = parts[1].trim();
                String destinationPlanet = parts[2].trim();
                String departureDate = parts[3].trim();
                int travelDuration = Integer.parseInt(parts[4].trim());
                
                return new Spaceship(name, departurePlanet, destinationPlanet, departureDate, travelDuration);
            }
            return null;
        });
    }
    
    public static List<Person> readPeople(String filePath) {
        return readFile(filePath, line -> {
            String[] parts = line.split("#");
            if (parts.length >= 4) {
                String name = parts[0].trim();
                int age = Integer.parseInt(parts[1].trim());
                int lifeRemaining = Integer.parseInt(parts[2].trim());
                String currentVehicle = parts[3].trim();
                
                return new Person(name, age, lifeRemaining, currentVehicle);
            }
            return null;
        });
    }
    
    /**
     * Generic file reading method that handles file operations and error handling
     * @param filePath Path to the file to read
     * @param lineParser Function that parses a line into an object of type T
     * @return List of parsed objects
     */
    private static <T> List<T> readFile(String filePath, Function<String, T> lineParser) {
        List<T> results = new ArrayList<>();
        
        try (BufferedReader br = new BufferedReader(new java.io.FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                T obj = lineParser.apply(line);
                if (obj != null) {
                    results.add(obj);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading file " + filePath + ": " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error processing file " + filePath + ": " + e.getMessage());
        }
        
        return results;
    }
    
    public static void placePeople(List<Person> people, List<Planet> planets, List<Spaceship> ships) {
        Map<String, Spaceship> shipMap = new HashMap<>();
        Map<String, Planet> planetMap = new HashMap<>();
        
        // Create maps for faster lookups
        ships.forEach(ship -> shipMap.put(ship.getName(), ship));
        planets.forEach(planet -> planetMap.put(planet.getName(), planet));
        
        for (Person person : people) {
            String vehicleName = person.getCurrentVehicle();
            
            if (shipMap.containsKey(vehicleName)) {
                shipMap.get(vehicleName).addPassenger(person);
            } else if (planetMap.containsKey(vehicleName)) {
                planetMap.get(vehicleName).addPerson(person);
            } else if (!planets.isEmpty()) {
                planets.get(0).addPerson(person);
                person.setCurrentVehicle(planets.get(0).getName());
            }
        }
    }
}
