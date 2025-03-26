package space_travel;

/**
 * @author Space Travel Simulation
 * @since March 26, 2025
 * <p>
 * This class manages time and date progression based on a planet's day length.
 * It handles different day lengths for different planets and advances time accordingly.
 * </p>
 */
public class Time {
    private int day;
    private int month;
    private int year;
    private int hour;
    private int dayLength;
    
    /**
     * Creates a new Time instance with the specified date and day length
     * 
     * @param date The starting date in format "dd.mm.yyyy"
     * @param dayLength The length of a day in hours for this time system
     */
    public Time(String date, int dayLength) {
        String[] parts = date.split("\\.");
        this.day = Integer.parseInt(parts[0]);
        this.month = Integer.parseInt(parts[1]);
        this.year = Integer.parseInt(parts[2]);
        this.hour = 0;
        this.dayLength = dayLength;
    }
    
    /**
     * Increases the time by one hour, updating day, month, and year as needed
     */
    public void increaseHour() {
        hour++;
        if (hour >= dayLength) {
            hour = 0;
            day++;
            
            // Handle month transitions based on days in each month
            if ((month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12) && day > 31) {
                day = 1;
                month++;
            } else if ((month == 4 || month == 6 || month == 9 || month == 11) && day > 30) {
                day = 1;
                month++;
            } else if (month == 2) {
                boolean leapYear = (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
                if ((leapYear && day > 29) || (!leapYear && day > 28)) {
                    day = 1;
                    month++;
                }
            }
            
            // Handle year transition
            if (month > 12) {
                month = 1;
                year++;
            }
        }
    }
    
    /**
     * Gets the current date in format "dd.mm.yyyy"
     * 
     * @return The current date as a string
     */
    public String getDate() {
        return String.format("%d.%d.%d", day, month, year);
    }
    
    /**
     * Gets the current hour in format "HH:00"
     * 
     * @return The current hour as a string
     */
    public String getHour() {
        return String.format("%02d:00", hour);
    }
    
    /**
     * Gets the full time (date and hour)
     * 
     * @return The full time as a string
     */
    public String getFullTime() {
        return getDate() + " " + getHour();
    }
    
    /**
     * Checks if the current date matches the specified date
     * 
     * @param date The date to compare against
     * @return true if dates match, false otherwise
     */
    public boolean dateMatches(String date) {
        return getDate().equals(date);
    }
    
    /**
     * Gets the length of a day in hours
     * 
     * @return The day length in hours
     */
    public int getDayLength() {
        return dayLength;
    }
}
