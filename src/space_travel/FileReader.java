package space_travel;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Space...
 * @since March 26, 2025
 * <p>
 * This class handles file reading operations for the space travel simulation.
 * It reads and parses data from input files for planets, spaceships, and people.
 * </p>
 */
public class FileReader {
    
    /**
     * Reads planet data from a file
     * 
     * @param filePath The path to the planets file
     * @return A list of planets
     */
    public static List<Planet> readPlanets(String filePath) {
        List<Planet> planets = new ArrayList<>();
        
        try (BufferedReader br = new BufferedReader(new java.io.FileReader(filePath))) {
            String line;
            int lineNumber = 0;
            while ((line = br.readLine()) != null) {
                lineNumber++;
                // Skip comment lines or empty lines
                if (line.trim().startsWith("//") || line.trim().isEmpty()) {
                    continue;
                }
                
                try {
                    String[] parts = line.split("#");
                    if (parts.length >= 3) {
                        String name = parts[0].trim().replace("\0", ""); // Remove NULL chars
                        
                        // Handle the day length with special care
                        String dayLengthStr = parts[1].trim();
                        System.out.println("Original day length string: '" + dayLengthStr + "'");
                        
                        // Remove NULL characters and show the fixed string
                        String fixedDayLength = dayLengthStr.replace("\0", "");
                        System.out.println("Fixed day length string: '" + fixedDayLength + "'");
                        
                        int dayLength = Integer.parseInt(fixedDayLength);
                        String date = parts[2].trim().replace("\0", ""); // Remove NULL chars
                        
                        Planet planet = new Planet(name, dayLength, date);
                        planets.add(planet);
                        System.out.println("Added planet: " + planet);
                    } else {
                        System.err.println("Warning: Line " + lineNumber + " has invalid format: " + line);
                    }
                } catch (NumberFormatException e) {
                    System.err.println("Error parsing number at line " + lineNumber + ": " + line);
                    System.err.println("Error details: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading planet file: " + e.getMessage());
        }
        
        return planets;
    }
    
    /**
     * Reads spaceship data from a file
     * 
     * @param filePath The path to the spaceships file
     * @return A list of spaceships
     */
    public static List<Spaceship> readSpaceships(String filePath) {
        List<Spaceship> ships = new ArrayList<>();
        
        try (BufferedReader br = new BufferedReader(new java.io.FileReader(filePath))) {
            String line;
            int lineNumber = 0;
            while ((line = br.readLine()) != null) {
                lineNumber++;
                // Skip comment lines or empty lines
                if (line.trim().startsWith("//") || line.trim().isEmpty()) {
                    continue;
                }
                
                try {
                    String[] parts = line.split("#");
                    if (parts.length >= 5) {
                        String name = parts[0].trim().replace("\0", "");
                        String departurePlanet = parts[1].trim().replace("\0", "");
                        String destinationPlanet = parts[2].trim().replace("\0", "");
                        String departureDate = parts[3].trim().replace("\0", "");
                        
                        // Handle travel duration with special care
                        String travelDurationStr = parts[4].trim();
                        System.out.println("Original travel duration: '" + travelDurationStr + "'");
                        
                        String fixedDuration = travelDurationStr.replace("\0", "");
                        System.out.println("Fixed travel duration: '" + fixedDuration + "'");
                        
                        int travelDuration = Integer.parseInt(fixedDuration);
                        
                        Spaceship ship = new Spaceship(name, departurePlanet, destinationPlanet, departureDate, travelDuration);
                        ships.add(ship);
                        System.out.println("Added spaceship: " + ship);
                    } else {
                        System.err.println("Warning: Line " + lineNumber + " has invalid format: " + line);
                    }
                } catch (NumberFormatException e) {
                    System.err.println("Error parsing number at line " + lineNumber + ": " + line);
                    System.err.println("Error details: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading spaceship file: " + e.getMessage());
        }
        
        return ships;
    }
    
    /**
     * Reads person data from a file
     * 
     * @param filePath The path to the people file
     * @return A list of people
     */
    public static List<Person> readPeople(String filePath) {
        List<Person> people = new ArrayList<>();
        
        try (BufferedReader br = new BufferedReader(new java.io.FileReader(filePath))) {
            String line;
            int lineNumber = 0;
            while ((line = br.readLine()) != null) {
                lineNumber++;
                // Skip comment lines or empty lines
                if (line.trim().startsWith("//") || line.trim().isEmpty()) {
                    continue;
                }
                
                try {
                    String[] parts = line.split("#");
                    if (parts.length >= 4) {
                        String name = parts[0].trim().replace("\0", "");
                        
                        // Handle age with special care for NULL characters
                        String ageStr = parts[1].trim();
                        System.out.println("Original age string: '" + ageStr + "'");
                        String fixedAge = ageStr.replace("\0", "");
                        System.out.println("Fixed age string: '" + fixedAge + "'");
                        int age = Integer.parseInt(fixedAge);
                        
                        // Handle life remaining with special care
                        String lifeStr = parts[2].trim();
                        System.out.println("Original life string: '" + lifeStr + "'");
                        String fixedLife = lifeStr.replace("\0", "");
                        System.out.println("Fixed life string: '" + fixedLife + "'");
                        int lifeRemaining = Integer.parseInt(fixedLife);
                        
                        String currentVehicle = parts[3].trim().replace("\0", "");
                        
                        Person person = new Person(name, age, lifeRemaining, currentVehicle);
                        people.add(person);
                        System.out.println("Added person: " + person);
                    } else {
                        System.err.println("Warning: Line " + lineNumber + " has invalid format: " + line);
                    }
                } catch (NumberFormatException e) {
                    System.err.println("Error parsing number at line " + lineNumber + ": " + line);
                    System.err.println("Error details: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading person file: " + e.getMessage());
        }
        
        return people;
    }
    
    /**
     * Places people on their respective planets or spaceships
     * 
     * @param people The list of people to place
     * @param planets The list of planets
     * @param ships The list of spaceships
     */
    public static void placePeople(List<Person> people, List<Planet> planets, List<Spaceship> ships) {
        Map<String, Spaceship> shipMap = new HashMap<>();
        Map<String, Planet> planetMap = new HashMap<>();
        
        // Create maps for quick lookups
        for (Spaceship ship : ships) {
            shipMap.put(ship.getName(), ship);
        }
        
        for (Planet planet : planets) {
            planetMap.put(planet.getName(), planet);
        }
        
        // Place each person
        for (Person person : people) {
            String vehicleName = person.getCurrentVehicle();
            
            // If person is on a ship
            if (shipMap.containsKey(vehicleName)) {
                Spaceship ship = shipMap.get(vehicleName);
                ship.addPassenger(person);
            } else {
                // If person is on a planet (or undefined vehicle, place on first planet)
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
