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
    
    public Planet(String name, int dayLength, String date) {
        this.name = name;
        this.dayLength = dayLength;
        this.time = new Time(date, dayLength);
        this.population = new ArrayList<>();
    }
    
    public void passHour() {
        time.increaseHour();
        
        List<Person> alive = new ArrayList<>();
        for (Person person : population) {
            person.passHour();
            if (person.isAlive()) {
                alive.add(person);
            }
        }
        
        population = alive;
    }
    
    public void addPerson(Person person) {
        population.add(person);
    }
    
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
    
    public String getName() {
        return name;
    }
    
    public int getDayLength() {
        return dayLength;
    }
    
    public Time getTime() {
        return time;
    }
    
    public List<Person> getPopulation() {
        return population;
    }
    
    @Override
    public String toString() {
        return String.format("%s (Time: %s, Population: %d people)",
                name, time.getFullTime(), population.size());
    }
}
