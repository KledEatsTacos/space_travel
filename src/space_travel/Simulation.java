/**
 * @author Space Travel Simulation
 * @since March 26, 2025
 * <p>
 * This class controls the main simulation loop and updates for the space travel simulation.
 * It manages the time progression, updates planets and spaceships, and displays the simulation status.
 * </p>
 */

package space_travel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Simulation {
    private List<Planet> planets;
    private List<Spaceship> ships;
    private int hourCounter;
    private boolean simulationComplete;
    private Map<String, Planet> planetMap;
    
    public Simulation(List<Planet> planets, List<Spaceship> ships) {
        this.planets = planets;
        this.ships = ships;
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
        Scanner scanner = new Scanner(System.in);
        
        while (!simulationComplete) {
            displayState();
            
            String input = scanner.nextLine().trim();
            int hoursToAdvance = parseInput(input);
            
            if (hoursToAdvance == -1) break;  // Exit command
            if (hoursToAdvance == 0) continue; // Invalid input
            
            advanceTime(hoursToAdvance);
        }
        
        scanner.close();
        printFinalReport();
    }
    
    private int parseInput(String input) {
        if (input.equals("0")) return -1;  // Exit code
        if (input.equals("1")) return 1;
        if (input.equals("2")) return 10;
        if (input.equals("3")) return 50;
        
        System.out.println("Invalid input. Please enter 0, 1, 2, or 3.");
        return 0; // Invalid input code
    }
    
    private void advanceTime(int hours) {
        for (int i = 0; i < hours; i++) {
            hourCounter++;
            updatePlanetStatus();
            updateShipStatus();
            
            if (checkSimulationComplete()) break;
        }
    }
    
    private void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
    
    private void updatePlanetStatus() {
        for (Planet planet : planets) {
            planet.passHour();
        }
    }
    
    private void updateShipStatus() {
        for (Spaceship ship : ships) {
            processShipDeparture(ship);
            ship.passHour();
            processShipArrival(ship);
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
        if (!ship.isTravelComplete()) return;
        
        Planet destinationPlanet = planetMap.get(ship.getDestinationPlanet());
        if (destinationPlanet == null) return;
        
        for (Person person : ship.getPassengers()) {
            person.setCurrentVehicle(destinationPlanet.getName());
            destinationPlanet.addPerson(person);
        }
        ship.endJourney();
    }
    
    private boolean checkSimulationComplete() {
        for (Spaceship ship : ships) {
            if (!ship.isDestroyed() && !ship.hasArrived()) {
                return false;
            }
        }
        simulationComplete = true;
        return true;
    }
    
    private void printFinalReport() {
        clearScreen();
        System.out.println("=== SIMULATION FINAL REPORT ===");
        System.out.println("Total Hours Elapsed: " + hourCounter + "\n");
        
        int totalPopulation = 0;
        System.out.println("PLANET STATUS:");
        for (Planet planet : planets) {
            System.out.println(planet);
            System.out.println("  Population: " + planet.getPopulation().size() + " people");
            for (Person person : planet.getPopulation()) {
                System.out.println("    - " + person);
            }
            totalPopulation += planet.getPopulation().size();
        }
        
        System.out.println("\nSPACESHIP STATUS:");
        int destroyedShips = 0;
        for (Spaceship ship : ships) {
            System.out.println(ship);
            if (ship.isDestroyed()) destroyedShips++;
        }
        
        System.out.println("\nSUMMARY:");
        System.out.println("Total Population: " + totalPopulation + " people");
        System.out.println("Destroyed Ships: " + destroyedShips);
    }

    private void displayState() {
        clearScreen();
        
        // Display planet info
        System.out.println("Planets:");
        
        // Header row
        System.out.printf("%-10s", "");
        for (Planet planet : planets) {
            System.out.printf(" %-17s ", "--- " + planet.getName() + " ---");
        }
        System.out.println();
        
        // Date row
        System.out.printf("%-10s", "Date");
        for (Planet planet : planets) {
            System.out.printf(" %-17s ", planet.getTime().getDate());
        }
        System.out.println();
        
        // Population row
        System.out.printf("%-10s", "Population");
        for (Planet planet : planets) {
            System.out.printf(" %-17d ", planet.getPopulation().size());
        }
        System.out.println("\n");
        
        // Display spaceship info
        System.out.println("Spaceships:");
        System.out.printf("%-15s %-15s %-15s %-15s %-25s %-25s%n", 
                "Ship Name", "Status", "Departure", "Destination", "Hours Remaining", "Arrival Date");
        System.out.println();
        
        for (Spaceship ship : ships) {
            String status = getShipStatus(ship);
            String arrivalDate = calculateArrivalDate(ship);

            System.out.printf("%-15s %-15s %-15s %-15s %-25d %-25s%n",
                    ship.getName(), status, ship.getDeparturePlanet(),
                    ship.getDestinationPlanet(), ship.getRemainingTravelTime(), arrivalDate);
        }
        
        displayControls();
    }
    
    private void displayControls() {
        System.out.println("\n-------------------------------------------------\n");
        System.out.println("Current Hour: " + hourCounter);
        System.out.println("\nControls:");
        System.out.println("1 - Advance 1 hour");
        System.out.println("2 - Advance 10 hours");
        System.out.println("3 - Advance 50 hours");
        System.out.println("0 - Exit simulation");
        System.out.print("\nEnter your choice: ");
    }
    
    // Helper method to determine ship status
    private String getShipStatus(Spaceship ship) {
        if (ship.isDestroyed()) {
            return "DESTROYED";
        } else if (ship.isInTransit()) {
            return "In Transit";
        } else if (ship.isTravelComplete()) {
            return "Arrived";
        } else {
            return "Waiting";
        }
    }
    
    // Helper method to calculate arrival date
    private String calculateArrivalDate(Spaceship ship) {
        String arrivalDate = "--";
        if (ship.isInTransit()) {
            // Get departure planet to use its day length for calculations
            Planet departurePlanet = null;
            for (Planet planet : planets) {
                if (planet.getName().equals(ship.getDeparturePlanet())) {
                    departurePlanet = planet;
                    break;
                }
            }
            
            if (departurePlanet != null) {
                // Create a temporary Time object based on the departure date
                Time arrivalTime = new Time(ship.getDepartureDate(), departurePlanet.getDayLength());
                
                // Calculate how many hours to add for the journey
                int hoursToAdd = ship.getTravelDuration();
                
                // Add hours to reach arrival date
                for (int i = 0; i < hoursToAdd; i++) {
                    arrivalTime.increaseHour();
                }
                
                arrivalDate = arrivalTime.getDate();
            }
        } else if (ship.isTravelComplete()) {
            Planet destinationPlanet = null;
            for (Planet planet : planets) {
                if (planet.getName().equals(ship.getDestinationPlanet())) {
                    destinationPlanet = planet;
                    break;
                }
            }
            
            if (destinationPlanet != null) {
                arrivalDate = destinationPlanet.getTime().getDate();
            }
        }
        
        return arrivalDate;
    }
}
