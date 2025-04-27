/**
 * @author Mustafa Masri
 * @since March 26, 2025
 * <p>
 * This class manages time and date progression based on a planet's day length.
 * It handles different day lengths for different planets and advances time accordingly.
 * </p>
 */

package space_travel;

public class Time {
    private int day;
    private int month;
    private int year;
    private int hour;
    private int dayLength;
    private static final int DAYS_PER_MONTH = 30;
    
    public Time(String date, int dayLength) {
        String[] parts = date.split("\\.");
        this.day = Integer.parseInt(parts[0]);
        this.month = Integer.parseInt(parts[1]);
        this.year = Integer.parseInt(parts[2]);
        this.hour = 0;
        this.dayLength = dayLength;
    }
    
    public void increaseHour() {
        hour++;
        if (hour >= dayLength) {
            hour = 0;
            day++;
            
            if (day > DAYS_PER_MONTH) {
                day = 1;
                month++;
                
                if (month > 12) {
                    month = 1;
                    year++;
                }
            }
        }
    }
    
    /**
     * Adds a specified number of hours to the current time at once.
     * This is more efficient than calling increaseHour repeatedly.
     * 
     * @param hours The number of hours to add
     */
    public void addHours(int hours) {
        int totalHours = hour + hours;
        
        int daysToAdd = totalHours / dayLength;
        
        hour = totalHours % dayLength;
        
        day += daysToAdd;
        
        while (day > DAYS_PER_MONTH) {
            day -= DAYS_PER_MONTH;
            month++;
            
            if (month > 12) {
                month = 1;
                year++;
            }
        }
    }
    
    public String getDate() {
        return String.format("%02d.%02d.%d", day, month, year);
    }
    
    public String getHour() {
        return String.format("%02d:00", hour);
    }
    
    public String getFullTime() {
        return getDate() + " " + getHour();
    }
    
    public boolean dateMatches(String date) {
        return getDate().equals(date);
    }
    
    public int getDayLength() {
        return dayLength;
    }
}
