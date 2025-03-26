package space_travel;

/**
 * @author Space Travel Simulation
 * @since March 26, 2025
 * <p>
 * This class represents a person in the space travel simulation.
 * It tracks the person's name, age, remaining life, and current location.
 * </p>
 */
public class Person {
    private String name;
    private int age;
    private int lifeRemaining;
    private String currentVehicle;
    
    /**
     * Creates a new person with the specified attributes
     * 
     * @param name The person's name
     * @param age The person's age in years
     * @param lifeRemaining The person's remaining life in hours
     * @param currentVehicle The name of the vehicle or planet where the person is located
     */
    public Person(String name, int age, int lifeRemaining, String currentVehicle) {
        this.name = name;
        this.age = age;
        this.lifeRemaining = lifeRemaining;
        this.currentVehicle = currentVehicle;
    }
    
    /**
     * Decreases the person's remaining life by one hour
     */
    public void passHour() {
        lifeRemaining--;
    }
    
    /**
     * Checks if the person is still alive
     * 
     * @return true if the person is alive, false if they have died
     */
    public boolean isAlive() {
        return lifeRemaining > 0;
    }
    
    /**
     * Gets the person's name
     * 
     * @return The person's name
     */
    public String getName() {
        return name;
    }
    
    /**
     * Gets the person's age in years
     * 
     * @return The person's age
     */
    public int getAge() {
        return age;
    }
    
    /**
     * Gets the person's remaining life in hours
     * 
     * @return The remaining life hours
     */
    public int getLifeRemaining() {
        return lifeRemaining;
    }
    
    /**
     * Gets the name of the vehicle or planet where the person is currently located
     * 
     * @return The current location name
     */
    public String getCurrentVehicle() {
        return currentVehicle;
    }
    
    /**
     * Sets the name of the vehicle or planet where the person is located
     * 
     * @param currentVehicle The new location name
     */
    public void setCurrentVehicle(String currentVehicle) {
        this.currentVehicle = currentVehicle;
    }
    
    @Override
    public String toString() {
        return String.format("%s (Age: %d, Life Remaining: %d hours, Location: %s)",
                name, age, lifeRemaining, currentVehicle);
    }
}
