package space_travel;

import java.util.List;

/**
 * @author Space Travel Simulation
 * @since March 26, 2025
 * <p>
 * This is the main class that starts the space travel simulation.
 * It reads data from input files and initializes the simulation.
 * </p>
 */
public class Main {
    public static void main(String[] args) {
        // File paths for input data
        String planetsFile = "Gezegenler.txt";
        String shipsFile = "Araclar.txt";
        String peopleFile = "Kisiler.txt";
        
        System.out.println("Starting Space Travel Simulation...");
        System.out.println("Reading input files...");
        
        // Read data from input files
        List<Planet> planets = FileReader.readPlanets(planetsFile);
        List<Spaceship> ships = FileReader.readSpaceships(shipsFile);
        List<Person> people = FileReader.readPeople(peopleFile);
        
        // Place people on their starting planets or ships
        FileReader.placePeople(people, planets, ships);
        
        System.out.println("Initialization complete.");
        System.out.println("Loaded " + planets.size() + " planets, " + 
                        ships.size() + " spaceships, and " + 
                        people.size() + " people.");
        
        // Create and start simulation
        Simulation simulation = new Simulation(planets, ships);
        simulation.start();
    }
}