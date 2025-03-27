package space_travel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Space Travel Simulation
 * @since March 26, 2025
 * <p>
 * This class controls the main simulation loop and updates for the space travel simulation.
 * It manages the time progression, updates planets and spaceships, and displays the simulation status.
 * </p>
 */
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
        boolean allShipsArrived = true;
        
        for (Spaceship ship : ships) {
            if (ship.isInTransit() && !ship.isDestroyed()) {
                allShipsArrived = false;
                break;
            }
        }
        
        simulationComplete = allShipsArrived;
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

        System.out.println("Planets:");
        for (Planet planet : planets) {
            System.out.printf("--- %s ---%n", planet.getName());
            System.out.printf("Date: %s%n", planet.getTime().getDate());
            System.out.printf("Population: %d%n%n", planet.getPopulation().size());
        }

        System.out.println("Spaceships:");
        System.out.printf("%-10s %-10s %-10s %-10s %-20s %-20s%n", 
                "Ship Name", "Status", "Departure", "Destination", "Hours Remaining", "Arrival Date");
        for (Spaceship ship : ships) {
            String status;
            if (ship.isDestroyed()) {
                status = "DESTROYED";
            } else if (ship.isInTransit()) {
                status = "In Transit";
            } else if (ship.isTravelComplete()) {
                status = "Arrived";
            } else {
                status = "Waiting";
            }

            String arrivalDate = ship.isInTransit() ? ship.getDestinationPlanet() : "--";
            if (ship.isTravelComplete()) {
                arrivalDate = ship.getDestinationPlanet();
            }

            System.out.printf(
                "%-10s %-10s %-10s %-10s %-20d %-20s%n",
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
