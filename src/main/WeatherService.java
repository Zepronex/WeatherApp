package main;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

/**
 * Service class responsible for fetching weather data based on latitude and
 * longitude.
 */

public class WeatherService {

	// Retrieves weather data for the given latitude and longitude.

	public WeatherData getWeatherData(double latitude, double longitude) throws Exception {

		// Construct the API URL with necessary parameters
		String url = "https://api.open-meteo.com/v1/forecast?latitude=" + latitude + "&longitude=" + longitude
				+ "&current_weather=true&hourly=relativehumidity_2m";

		// Fetch API response
		HttpURLConnection apiConnection = fetchApiResponse(url);

		// Check if the response code is HTTP OK
		if (apiConnection.getResponseCode() != HttpURLConnection.HTTP_OK) {
			throw new Exception("Error: Could not connect to API");
		}

		// Read the API response
		String jsonResponse = readApiResponse(apiConnection);

		// Parse JSON response
		JSONParser parser = new JSONParser();
		JSONObject jsonObject = (JSONObject) parser.parse(jsonResponse);

		// Get the 'current_weather' object
		JSONObject currentWeatherJson = (JSONObject) jsonObject.get("current_weather");

		if (currentWeatherJson == null) {
			return null;
		}

		// Extract weather data
		String time = (String) currentWeatherJson.get("time");
		double temperature = ((Number) currentWeatherJson.get("temperature")).doubleValue();
		double windSpeed = ((Number) currentWeatherJson.get("windspeed")).doubleValue();

		// Parse relative humidity from 'hourly' data
		JSONObject hourly = (JSONObject) jsonObject.get("hourly");
		JSONArray timeArray = (JSONArray) hourly.get("time");
		JSONArray humidityArray = (JSONArray) hourly.get("relativehumidity_2m");

		// Find the index of the current time in the hourly data
		int index = -1;
		for (int i = 0; i < timeArray.size(); i++) {
			if (timeArray.get(i).equals(time)) {
				index = i;
				break;
			}
		}

		long relativeHumidity = 0;
		if (index >= 0) {
			relativeHumidity = ((Number) humidityArray.get(index)).longValue();
		} else {
			// If current time not found, set to -1 or handle accordingly
			relativeHumidity = -1;
		}

		// Return a new WeatherData object
		return new WeatherData(time, temperature, relativeHumidity, windSpeed);
	}

	// Reads API response and returns it as a String.
	private String readApiResponse(HttpURLConnection apiConnection) throws Exception {
		StringBuilder resultJson = new StringBuilder();

		// Read from the InputStream of the connection
		Scanner scanner = new Scanner(apiConnection.getInputStream());

		// Append each line to the StringBuilder
		while (scanner.hasNext()) {
			resultJson.append(scanner.nextLine());
		}

		scanner.close();

		return resultJson.toString();
	}

	// Establishes an HTTP connection to the given URL.
	private HttpURLConnection fetchApiResponse(String urlString) throws Exception {
		URL url = new URL(urlString);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();

		// Set request method to GET
		conn.setRequestMethod("GET");

		return conn;
	}
}
