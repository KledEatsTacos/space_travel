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

public class Simulation {
    private List<Planet> planets;
    private List<Spaceship> ships;
    private int hourCounter;
    private boolean simulationComplete;
    
    public Simulation(List<Planet> planets, List<Spaceship> ships) {
        this.planets = planets;
        this.ships = ships;
        this.hourCounter = 0;
        this.simulationComplete = false;
    }
    
    public void start() {
        while (!simulationComplete) {
            displayState();
            hourCounter++;
            System.out.println("=== SPACE TRAVEL SIMULATION ===");
            System.out.println("Simulation Hour: " + hourCounter);
            System.out.println();
            
            updatePlanetStatus();
            updateShipStatus();
            checkSimulationComplete();
            
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        
        printFinalReport();
    }
    
    private void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
    
    private void updatePlanetStatus() {
        System.out.println("PLANET STATUS:");
        System.out.println("------------------");
        
        for (Planet planet : planets) {
            planet.passHour();
            System.out.println(planet);
        }
        
        System.out.println();
    }
    
    private void updateShipStatus() {
        System.out.println("SPACESHIP STATUS:");
        System.out.println("---------------------");
        
        Map<String, Planet> planetMap = new HashMap<>();
        for (Planet planet : planets) {
            planetMap.put(planet.getName(), planet);
        }
        
        for (Spaceship ship : ships) {
            if (!ship.isInTransit() && !ship.isDestroyed()) {
                Planet departurePlanet = planetMap.get(ship.getDeparturePlanet());
                
                if (departurePlanet != null && departurePlanet.getTime().dateMatches(ship.getDepartureDate())) {
                    List<Person> passengers = departurePlanet.removePeople(ship.getName());
                    
                    for (Person person : passengers) {
                        ship.addPassenger(person);
                    }
                    
                    ship.startJourney();
                }
            }
            
            ship.passHour();
            
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
            
            System.out.println(ship);
        }
        
        System.out.println();
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
    
    private void printFinalReport() {
        clearScreen();
        
        System.out.println("=== SIMULATION FINAL REPORT ===");
        System.out.println("Total Hours Elapsed: " + hourCounter);
        System.out.println();
        
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
            if (ship.isDestroyed()) {
                destroyedShips++;
            }
        }
        
        System.out.println("\nSUMMARY:");
        System.out.println("Total Population: " + totalPopulation + " people");
        System.out.println("Destroyed Ships: " + destroyedShips);
    }

    private void displayState() {
        clearScreen();

        // Display planets section in tabular format
        System.out.println("Planets:");
        
        // Print header row for planets - use fixed spacing between columns
        System.out.printf("%-10s", "");
        for (Planet planet : planets) {
            System.out.printf(" %-17s ", "--- " + planet.getName() + " ---");
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
        
        // Display spaceships section in tabular format
        System.out.println("Spaceships:");
        System.out.printf("%-15s %-15s %-15s %-15s %-25s %-25s%n", 
                "Ship Name", "Status", "Departure", "Destination", "Hours Remaining", "Arrival Date");
        System.out.println();
        
        for (Spaceship ship : ships) {
            String status;
            if (ship.isDestroyed()) {
                status = "DESTROYED";
            } else if (ship.hasArrived()) {
                status = "Arrived";
            } else if (ship.isInTransit()) {
                status = "In Transit";
            } else {
                status = "Waiting";
            }

            // Calculate arrival date
            String arrivalDate;
            
            // Always calculate the expected arrival date regardless of ship status
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
            } else {
                arrivalDate = "--";
            }
            
            // If the ship has already arrived, use the current date of the destination planet
            if (ship.hasArrived()) {
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

            System.out.printf(
                "%-15s %-15s %-15s %-15s %-25d %-25s%n",
                ship.getName(),
                status,
                ship.getDeparturePlanet(),
                ship.getDestinationPlanet(),
                ship.getRemainingTravelTime(),
                arrivalDate
            );
        }
    }
}
