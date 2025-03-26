package space_travel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Space Travel Simulation
 * @since March 26, 2025
 * <p>
 * This class represents a planet in the space travel simulation.
 * It manages the planet's time, population, and interactions with spaceships.
 * </p>
 */
public class Planet {
    private String name;
    private int dayLength;
    private Time time;
    private List<Person> population;
    
    /**
     * Creates a new planet with the specified attributes
     * 
     * @param name The name of the planet
     * @param dayLength The length of a day on the planet in hours
     * @param date The current date on the planet
     */
    public Planet(String name, int dayLength, String date) {
        this.name = name;
        this.dayLength = dayLength;
        this.time = new Time(date, dayLength);
        this.population = new ArrayList<>();
    }
    
    /**
     * Updates the planet for each hour that passes
     * Advances time and updates the population
     */
    public void passHour() {
        time.increaseHour();
        
        // Update population and remove dead people
        List<Person> alive = new ArrayList<>();
        for (Person person : population) {
            person.passHour();
            if (person.isAlive()) {
                alive.add(person);
            }
        }
        
        // Remove dead people
        population = alive;
    }
    
    /**
     * Adds a person to the planet's population
     * 
     * @param person The person to add
     */
    public void addPerson(Person person) {
        population.add(person);
    }
    
    /**
     * Removes people assigned to a specific spaceship from the planet
     * 
     * @param shipName The name of the spaceship
     * @return A list of people who were removed
     */
    public List<Person> removePeople(String shipName) {
        List<Person> passengersOnShip = new ArrayList<>();
        List<Person> remainingPopulation = new ArrayList<>();
        
        for (Person person : population) {
            if (person.getCurrentVehicle().equals(shipName)) {
                passengersOnShip.add(person);
            } else {
                remainingPopulation.add(person);
            }
        }
        
        population = remainingPopulation;
        return passengersOnShip;
    }
    
    /**
     * Gets the name of the planet
     * 
     * @return The planet name
     */
    public String getName() {
        return name;
    }
    
    /**
     * Gets the length of a day on the planet in hours
     * 
     * @return The day length in hours
     */
    public int getDayLength() {
        return dayLength;
    }
    
    /**
     * Gets the time system of the planet
     * 
     * @return The planet's time
     */
    public Time getTime() {
        return time;
    }
    
    /**
     * Gets the population of the planet
     * 
     * @return A list of people on the planet
     */
    public List<Person> getPopulation() {
        return population;
    }
    
    @Override
    public String toString() {
        return String.format("%s (Time: %s, Population: %d people)",
                name, time.getFullTime(), population.size());
    }
}
