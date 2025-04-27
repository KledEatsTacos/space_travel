/**
 * @author Mustafa Masri
 * @since March 26, 2025
 * <p>
 * This is the main class that starts the space travel simulation.
 * It reads data from input files and initializes the simulation.
 * </p>
 */

package space_travel;

public class Main {
    public static void main(String[] args) {

        final String PLANETS_FILE = "Gezegenler.txt";
        final String SHIPS_FILE = "Araclar.txt";
        final String PEOPLE_FILE = "Kisiler.txt";
        

        var planets = FileReader.readPlanets(PLANETS_FILE);
        var ships = FileReader.readSpaceships(SHIPS_FILE);
        var people = FileReader.readPeople(PEOPLE_FILE);
        

        FileReader.placePeople(people, planets, ships);
        new Simulation(planets, ships, people).start();
    }
}