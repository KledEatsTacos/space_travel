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
    
    public String getDate() {
        return String.format("%d.%d.%d", day, month, year);
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
