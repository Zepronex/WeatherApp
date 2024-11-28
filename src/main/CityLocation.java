package main;

// Data model representing the city location.

public class CityLocation {
	private String cityName;
	private double latitude;
	private double longitude;

	// Constructs a CityLocation object.

	public CityLocation(String cityName, double latitude, double longitude) {
		this.cityName = cityName;
		this.latitude = latitude;
		this.longitude = longitude;
	}

	// Getters
	public String getCityName() {
		return cityName;
	}

	public double getLatitude() {
		return latitude;
	}

	public double getLongitude() {
		return longitude;
	}
}
