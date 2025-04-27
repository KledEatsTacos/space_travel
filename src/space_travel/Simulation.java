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
import java.util.Map;

public class Simulation {
    private final List<Planet> planets;
    private final List<Spaceship> ships;
    private final List<Person> allPeople;
    private int hourCounter;
    private boolean simulationComplete;
    private Map<String, Planet> planetMap;
    
    public Simulation(List<Planet> planets, List<Spaceship> ships, List<Person> allPeople) {
        this.planets = planets;
        this.ships = ships;
        this.allPeople = new ArrayList<>(allPeople);
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
            updateAllPeopleLife();
            
            updatePlanetStatus();
            updateShipStatus();  
            checkSimulationComplete();
            
            clearScreen();
            displayState();
            
            hourCounter++;
        }

        clearScreen();
        displayState();
        System.out.println("\nSimulation complete after " + hourCounter + " hours.");
    }
    
    private void clearScreen() {
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("windows")) {
            try {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } catch (Exception e) {
                for (int i = 0; i < 50; i++) System.out.println();
            }
        } else {
            System.out.print("\033[H\033[2J");
            System.out.flush();
        }
    }
    
    private void updateAllPeopleLife() {
        for (Person person : allPeople) {
            person.passHour();
        }
    }
    
    private void updatePlanetStatus() {
        for (Planet planet : planets) {
            planet.passHour();
        }
    }
    
    private void updateShipStatus() {
        for (Spaceship ship : ships) {
            if (!ship.isDestroyed()) { 
                processShipDeparture(ship);
                ship.passHour();
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
            ship.setOriginalPassengers(passengers);
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
                ship.setActualArrivalDate(destinationPlanet.getTime().getDate()); 
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
        
        final int planetColumnWidth = 18; 
        System.out.printf("%-10s", "");
        for (Planet planet : planets) {
            String header = String.format("--- %s ---", planet.getName());
            System.out.printf("%-" + planetColumnWidth + "s", header); 
        }
        System.out.println();

        System.out.printf("%-10s", "Date");
        System.out.print("  ");
        for (Planet planet : planets) {
            System.out.printf("%-" + planetColumnWidth + "s", planet.getTime().getDate());
        }
        System.out.println();

        System.out.printf("%-10s", "Population");
        System.out.print("  ");
        for (Planet planet : planets) {
            System.out.printf("%-" + planetColumnWidth + "d", planet.getPopulation().size());
        }
        System.out.println("\n");

        System.out.println("Spaceships:");
        
        System.out.printf("%-12s %-12s %-10s %-10s %-20s %-20s%n",
                "Ship Name", "Status", "Departure", "Destination", "Hours Remaining", "Arrival Date");
        
        for (Spaceship ship : ships) {
            String status;
            if (ship.isDestroyed()) {
                status = "Destroyed"; 
            } else if (ship.hasArrived()) {
                boolean allOriginalsDead = true;
                if (ship.getOriginalPassengers() != null) {
                    for (Person p : ship.getOriginalPassengers()) {
                        if (p.isAlive()) {
                            allOriginalsDead = false;
                            break;
                        }
                    }
                } else {
                    allOriginalsDead = false; 
                }
                if (allOriginalsDead && ship.getOriginalPassengers() != null && !ship.getOriginalPassengers().isEmpty()) {
                    status = "Destroyed"; 
                } else {
                    status = "Arrived";   
                }
            } else if (ship.isInTransit()) {
                status = "In Transit";
            } else {
                status = "Waiting";   
            }

            String arrivalDate = calculateArrivalDate(ship);

            String displayRemainingTimeStr;
            if (status.equals("Destroyed")) { 
                displayRemainingTimeStr = "--"; 
            } else if (status.equals("Arrived")) {
                displayRemainingTimeStr = "0"; 
            } else {
                displayRemainingTimeStr = String.valueOf(ship.getRemainingTravelTime()); 
            }

            System.out.printf(
                "%-12s %-12s %-10s %-10s %-20s %-20s%n",
                ship.getName(),
                status,
                ship.getDeparturePlanet(),
                ship.getDestinationPlanet(),
                displayRemainingTimeStr, 
                arrivalDate
            );
        }
    }

    private String calculateArrivalDate(Spaceship ship) {
        String arrivalDate = "--";
        
        boolean isEffectivelyDestroyed = ship.isDestroyed();
        if (!isEffectivelyDestroyed && ship.hasArrived() && ship.getOriginalPassengers() != null && !ship.getOriginalPassengers().isEmpty()) { 
            boolean allOriginalsDead = true;
            for (Person p : ship.getOriginalPassengers()) {
                if (p.isAlive()) {
                    allOriginalsDead = false;
                    break;
                }
            }
            if (allOriginalsDead) {
                isEffectivelyDestroyed = true;
            }
        }
        
        if (isEffectivelyDestroyed) {
             arrivalDate = "--";
        }
        else if (ship.hasArrived()) { 
            arrivalDate = ship.getActualArrivalDate();
            if (arrivalDate == null) arrivalDate = "Error";
        } 
        else { 
            Planet departurePlanet = planetMap.get(ship.getDeparturePlanet());
            if (departurePlanet != null) {
                Time arrivalTime = new Time(ship.getDepartureDate(), departurePlanet.getDayLength());
                arrivalTime.addHours(ship.getTravelDuration());
                arrivalDate = arrivalTime.getDate();
            }
        }
        
        return arrivalDate;
    }
}
