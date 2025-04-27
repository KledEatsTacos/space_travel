/**
 * @author Space Travel Simulation
 * @since March 26, 2025
 * <p>
 * This class controls the main simulation loop and updates for the space travel simulation.
 * It manages the time progression, updates planets and spaceships, and displays the simulation status.
 * </p>
 */

package space_travel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map; // Import ArrayList

public class Simulation {
    private final List<Planet> planets;
    private final List<Spaceship> ships;
    private final List<Person> allPeople; // Added master list of all people
    private int hourCounter;
    private boolean simulationComplete;
    private Map<String, Planet> planetMap;
    
    // Updated constructor to accept allPeople list
    public Simulation(List<Planet> planets, List<Spaceship> ships, List<Person> allPeople) {
        this.planets = planets;
        this.ships = ships;
        this.allPeople = new ArrayList<>(allPeople); // Store a copy of the list
        this.hourCounter = 0;
        this.simulationComplete = false;
        initPlanetMap();
    }
    
    private void initPlanetMap() {
        planetMap = new HashMap<>();
        for (Planet planet : planets) {
            planetMap.put(planet.getName(), planet);
        }
    }
    
    public void start() {
        while (!simulationComplete) {
            // Decrease life for ALL people globally first
            updateAllPeopleLife();
            
            // advance simulation logic for planets and active ships
            updatePlanetStatus(); // Planet.passHour now only advances time and removes dead
            updateShipStatus();   // Spaceship.passHour now only advances time and removes dead during transit
            checkSimulationComplete();
            
            // render updated state
            clearScreen();
            displayState();
            
            // increment simulation time without delay
            hourCounter++;
        }
        
        // final update and completion message
        clearScreen();
        displayState();
        System.out.println("\nSimulation complete after " + hourCounter + " hours.");
    }
    
    private void clearScreen() {
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("windows")) {
            try {
                // Use cls command for Windows cmd
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } catch (Exception e) {
                // Fallback if cls fails (e.g., permissions issue)
                for (int i = 0; i < 50; i++) System.out.println();
            }
        } else {
            // Use ANSI escape codes on supported terminals (Linux, macOS, VSCode integrated)
            System.out.print("\033[H\033[2J");
            System.out.flush();
        }
    }
    
    // New method to decrease life for everyone
    private void updateAllPeopleLife() {
        for (Person person : allPeople) {
            person.passHour();
        }
    }
    
    private void updatePlanetStatus() {
        for (Planet planet : planets) {
            planet.passHour(); // Planet.passHour no longer calls person.passHour()
        }
    }
    
    private void updateShipStatus() {
        for (Spaceship ship : ships) {
            // Only process departure/arrival/transit for non-destroyed ships
            if (!ship.isDestroyed()) { 
                processShipDeparture(ship);
                ship.passHour(); // Spaceship.passHour no longer calls person.passHour()
                processShipArrival(ship);
            }
        }
    }
    
    private void processShipDeparture(Spaceship ship) {
        if (ship.isInTransit() || ship.isDestroyed()) return;
        
        Planet departurePlanet = planetMap.get(ship.getDeparturePlanet());
        if (departurePlanet == null) return;
        
        if (departurePlanet.getTime().dateMatches(ship.getDepartureDate())) {
            List<Person> passengers = departurePlanet.removePeople(ship.getName());
            for (Person person : passengers) {
                ship.addPassenger(person);
            }
            ship.startJourney();
        }
    }
    
    private void processShipArrival(Spaceship ship) {
        if (ship.isTravelComplete()) {
            Planet destinationPlanet = planetMap.get(ship.getDestinationPlanet());
            
            if (destinationPlanet != null) {
                for (Person person : ship.getPassengers()) {
                    person.setCurrentVehicle(destinationPlanet.getName());
                    destinationPlanet.addPerson(person);
                }
                
                ship.endJourney();
            }
        }
    }
    
    private void checkSimulationComplete() {
        simulationComplete = true;

        for(Spaceship ship : ships) {
            if (!ship.isDestroyed() && !ship.hasArrived()) {
                simulationComplete = false;
                return;
            }
        }
    }
    
    private void displayState() {
        System.out.println("Simulation Hour: " + hourCounter);
        System.out.println("Planets:");
        
        // Print header row for planets
        System.out.printf("%-10s", "");
        for (Planet planet : planets) {
            System.out.printf(" %s %s %s ", "---", planet.getName(), "---");
        }
        System.out.println();
        
        // Print date row
        System.out.printf("%-10s", "Date");
        for (Planet planet : planets) {
            System.out.printf(" %-17s ", planet.getTime().getDate());
        }
        System.out.println();
        
        // Print population row
        System.out.printf("%-10s", "Population");
        for (Planet planet : planets) {
            System.out.printf(" %-17d ", planet.getPopulation().size());
        }
        System.out.println("\n");
        
        // Display spaceships section
        System.out.printf("%-12s %-12s %-12s %-12s %-20s %-20s%n", 
                "Ship Name", "Status", "Departure", "Destination", "Hours Remaining", "Arrival Date");
        
        for (Spaceship ship : ships) {
            String status;
            if (ship.isDestroyed()) {
                status = "Destroyed"; // Ensure English
            } else if (ship.hasArrived()) {
                status = "Arrived";   // Ensure English
            } else if (ship.isInTransit()) {
                status = "In Transit"; // Ensure English
            } else {
                status = "Waiting";    // Ensure English
            }

            // Calculate arrival date
            String arrivalDate = calculateArrivalDate(ship);

            // Ensure remaining time is 0 for Arrived/Destroyed ships in display
            int displayRemainingTime = (status.equals("Arrived") || status.equals("Destroyed")) ? 0 : ship.getRemainingTravelTime();

            System.out.printf(
                "%-12s %-12s %-12s %-12s %-20d %-20s%n",
                ship.getName(),
                status,
                ship.getDeparturePlanet(),
                ship.getDestinationPlanet(),
                displayRemainingTime, // Use adjusted remaining time
                arrivalDate
            );
        }
    }

    // Helper method to calculate arrival date - Optimize by caching calculations
    private final Map<Spaceship, String> arrivalDateCache = new HashMap<>();
    
    private String calculateArrivalDate(Spaceship ship) {
        // Check if we have a cached result for unchanged ships
        if (arrivalDateCache.containsKey(ship) && !ship.isInTransit()) {
            return arrivalDateCache.get(ship);
        }
        
        String arrivalDate = "--";
        
        // For ships that are destroyed
        if (ship.isDestroyed()) {
            arrivalDate = "--"; // Show -- for destroyed ships
        } 
        // For ships that are waiting to depart or in transit - calculate expected arrival
        else if (!ship.isTravelComplete()) {
            // Get departure planet to use its day length for calculations
            Planet departurePlanet = planetMap.get(ship.getDeparturePlanet());
            
            if (departurePlanet != null) {
                // Create a temporary Time object based on the departure date
                Time arrivalTime = new Time(ship.getDepartureDate(), departurePlanet.getDayLength());
                
                // Use the addHours method to efficiently calculate arrival date
                arrivalTime.addHours(ship.getTravelDuration());
                
                arrivalDate = arrivalTime.getDate();
            }
        } 
        // For ships that have already arrived
        else if (ship.isTravelComplete()) {
            Planet destinationPlanet = planetMap.get(ship.getDestinationPlanet());
            
            if (destinationPlanet != null) {
                arrivalDate = destinationPlanet.getTime().getDate();
            }
        }
        
        // Update cache
        arrivalDateCache.put(ship, arrivalDate);
        
        return arrivalDate;
    }
}
