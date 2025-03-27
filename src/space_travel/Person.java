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
    
    public Person(String name, int age, int lifeRemaining, String currentVehicle) {
        this.name = name;
        this.age = age;
        this.lifeRemaining = lifeRemaining;
        this.currentVehicle = currentVehicle;
    }
    
    public void passHour() {
        lifeRemaining--;
    }
    
    public boolean isAlive() {
        return lifeRemaining > 0;
    }
    
    public String getName() {
        return name;
    }
    
    public int getAge() {
        return age;
    }
    
    public int getLifeRemaining() {
        return lifeRemaining;
    }
    
    public String getCurrentVehicle() {
        return currentVehicle;
    }
    
    public void setCurrentVehicle(String currentVehicle) {
        this.currentVehicle = currentVehicle;
    }
    
    @Override
    public String toString() {
        return String.format("%s (Age: %d, Life Remaining: %d hours, Location: %s)",
                name, age, lifeRemaining, currentVehicle);
    }
}
