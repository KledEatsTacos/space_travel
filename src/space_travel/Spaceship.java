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
    
    /**
     * Creates a new spaceship with the specified attributes
     * 
     * @param name The name of the spaceship
     * @param departurePlanet The planet from which the spaceship departs
     * @param destinationPlanet The planet to which the spaceship travels
     * @param departureDate The date on which the spaceship departs
     * @param travelDuration The duration of travel in hours
     */
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
    
    /**
     * Adds a passenger to the spaceship
     * 
     * @param person The person to add as a passenger
     */
    public void addPassenger(Person person) {
        passengers.add(person);
    }
    
    /**
     * Updates the spaceship status for each hour that passes
     * Decreases remaining travel time and updates passenger life
     * Destroys the ship if all passengers die
     */
    public void passHour() {
        if (inTransit && !destroyed) {
            remainingTravelTime--;
            
            // Update passenger life and remove dead passengers
            List<Person> alive = new ArrayList<>();
            for (Person person : passengers) {
                person.passHour();
                if (person.isAlive()) {
                    alive.add(person);
                }
            }
            
            // Remove dead passengers
            boolean hadPassengers = !passengers.isEmpty();
            passengers = alive;
            
            // If all passengers are dead, ship is destroyed
            if (passengers.isEmpty() && hadPassengers) {
                destroyed = true;
                System.out.println("All passengers on " + name + " have died. The ship has been DESTROYED!");
            }
        }
    }
    
    /**
     * Checks if the spaceship has completed its journey
     * 
     * @return true if the journey is complete, false otherwise
     */
    public boolean isTravelComplete() {
        return inTransit && remainingTravelTime <= 0;
    }
    
    /**
     * Starts the spaceship's journey
     */
    public void startJourney() {
        inTransit = true;
        System.out.println(name + " has departed from " + departurePlanet + " to " + 
                         destinationPlanet + ". Estimated arrival time: " + 
                         travelDuration + " hours.");
    }
    
    /**
     * Ends the spaceship's journey when it reaches its destination
     */
    public void endJourney() {
        inTransit = false;
        System.out.println(name + " has arrived at " + destinationPlanet + "!");
    }
    
    /**
     * Gets the name of the spaceship
     * 
     * @return The spaceship name
     */
    public String getName() {
        return name;
    }
    
    /**
     * Gets the planet from which the spaceship departs
     * 
     * @return The departure planet name
     */
    public String getDeparturePlanet() {
        return departurePlanet;
    }
    
    /**
     * Gets the planet to which the spaceship travels
     * 
     * @return The destination planet name
     */
    public String getDestinationPlanet() {
        return destinationPlanet;
    }
    
    /**
     * Gets the date on which the spaceship departs
     * 
     * @return The departure date
     */
    public String getDepartureDate() {
        return departureDate;
    }
    
    /**
     * Gets the list of passengers on the spaceship
     * 
     * @return The list of passengers
     */
    public List<Person> getPassengers() {
        return passengers;
    }
    
    /**
     * Checks if the spaceship is currently in transit
     * 
     * @return true if in transit, false otherwise
     */
    public boolean isInTransit() {
        return inTransit;
    }
    
    /**
     * Checks if the spaceship has been destroyed
     * 
     * @return true if destroyed, false otherwise
     */
    public boolean isDestroyed() {
        return destroyed;
    }
    
    /**
     * Gets the remaining travel time in hours
     * 
     * @return The remaining travel time
     */
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
