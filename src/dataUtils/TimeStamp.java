package dataUtils;

public class TimeStamp {
	
	private int days = 0, hours = 0, minutes = 0, seconds = 0;
	private long creation;
	
	public TimeStamp(){
		this.creation = System.nanoTime();
	}
	
	public TimeStamp(long creation) {
		this.creation = creation;
	}
	
	public TimeStamp(int days, int hours, int minutes, int seconds){
		this.days = days;
		this.hours = hours;
		this.minutes = minutes;
		this.seconds = seconds;
		this.creation = System.nanoTime();
	}
	
	public TimeStamp(int days, int hours, int minutes, int seconds, long creation){
		this.days = days;
		this.hours = hours;
		this.minutes = minutes;
		this.seconds = seconds;
		this.creation = creation;
	}
	
	public void addSecond(){
		seconds++;
		if(seconds>=60){minutes++; seconds = 0;}
		if(minutes>=60){hours++; minutes = 0;}
		if(hours>=24){days++; hours = 0;}
	}
	
	public void addSeconds(int n){
		for (int i = 0; i < n; i++) {
			addSecond();
		}
	}
	
	public int getDays() {
		return days;
	}
	
	public int getHours() {
		return hours;
	}
	
	public int getMinutes() {
		return minutes;
	}
	
	public int getSeconds() {
		return seconds;
	}
	
	public long getCreation() {
		return creation;
	}
	
}
