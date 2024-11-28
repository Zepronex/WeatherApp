package main;

// Data model representing weather data.

public class WeatherData {
	private String time;
	private double temperature;
	private long relativeHumidity;
	private double windSpeed;

	// Constructs a WeatherData object
	public WeatherData(String time, double temperature, long relativeHumidity, double windSpeed) {
		this.time = time;
		this.temperature = temperature;
		this.relativeHumidity = relativeHumidity;
		this.windSpeed = windSpeed;
	}

	// Getters
	public String getTime() {
		return time;
	}

	public double getTemperature() {
		return temperature;
	}

	public long getRelativeHumidity() {
		return relativeHumidity;
	}

	public double getWindSpeed() {
		return windSpeed;
	}
}
