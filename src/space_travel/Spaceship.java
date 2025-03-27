package space_travel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Space Travel Simulation
 * @since March 26, 2025
 * <p>
 * This class represents a spaceship in the space travel simulation.
 * It manages space travel between planets and handles passengers.
 * </p>
 */
public class Spaceship {
    private String name;
    private String departurePlanet;
    private String destinationPlanet;
    private String departureDate;
    private int travelDuration;
    private int remainingTravelTime;
    private List<Person> passengers;
    private boolean inTransit;
    private boolean destroyed;
    
    public Spaceship(String name, String departurePlanet, String destinationPlanet, String departureDate, int travelDuration) {
        this.name = name;
        this.departurePlanet = departurePlanet;
        this.destinationPlanet = destinationPlanet;
        this.departureDate = departureDate;
        this.travelDuration = travelDuration;
        this.remainingTravelTime = travelDuration;
        this.passengers = new ArrayList<>();
        this.inTransit = false;
        this.destroyed = false;
    }
    
    public void addPassenger(Person person) {
        passengers.add(person);
    }
    
    public void passHour() {
        if (inTransit && !destroyed) {
            remainingTravelTime--;
            
            List<Person> alive = new ArrayList<>();
            for (Person person : passengers) {
                person.passHour();
                if (person.isAlive()) {
                    alive.add(person);
                }
            }
            
            boolean hadPassengers = !passengers.isEmpty();
            passengers = alive;
            
            if (passengers.isEmpty() && hadPassengers) {
                destroyed = true;
                System.out.println("All passengers on " + name + " have died. The ship has been DESTROYED!");
            }
        }
    }
    
    public boolean isTravelComplete() {
        return inTransit && remainingTravelTime <= 0;
    }
    
    public void startJourney() {
        inTransit = true;
        System.out.println(name + " has departed from " + departurePlanet + " to " + 
                         destinationPlanet + ". Estimated arrival time: " + 
                         travelDuration + " hours.");
    }
    
    public void endJourney() {
        inTransit = false;
        System.out.println(name + " has arrived at " + destinationPlanet + "!");
    }
    
    public String getName() {
        return name;
    }
    
    public String getDeparturePlanet() {
        return departurePlanet;
    }
    
    public String getDestinationPlanet() {
        return destinationPlanet;
    }
    
    public String getDepartureDate() {
        return departureDate;
    }
    
    public List<Person> getPassengers() {
        return passengers;
    }
    
    public boolean isInTransit() {
        return inTransit;
    }
    
    public boolean isDestroyed() {
        return destroyed;
    }
    
    public int getRemainingTravelTime() {
        return remainingTravelTime;
    }
    
    @Override
    public String toString() {
        if (destroyed) {
            return name + " (DESTROYED)";
        } else if (inTransit) {
            return String.format("%s (%s -> %s, Time remaining: %d hours, Passengers: %d)",
                    name, departurePlanet, destinationPlanet, remainingTravelTime, passengers.size());
        } else {
            return String.format("%s (On planet %s, Passengers: %d)",
                    name, departurePlanet, passengers.size());
        }
    }
}
