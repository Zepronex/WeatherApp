package main;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

//Service class responsible for fetching location data from the API.

public class LocationService {

	// Retrieves the latitude and longitude for a given city name.

	public CityLocation getLocationData(String city) throws Exception {
		// Replace spaces with '+' for URL encoding
		city = city.replaceAll(" ", "+");

		// Construct the API URL
		String urlString = "https://geocoding-api.open-meteo.com/v1/search?name=" + city
				+ "&count=1&language=en&format=json";

		// Fetch API response
		HttpURLConnection apiConnection = fetchApiResponse(urlString);

		// Check if the response code is HTTP OK
		if (apiConnection.getResponseCode() != HttpURLConnection.HTTP_OK) {
			throw new Exception("Error: Could not connect to API");
		}

		// Read the API response
		String jsonResponse = readApiResponse(apiConnection);

		// Parse JSON response
		JSONParser parser = new JSONParser();
		JSONObject resultsJsonObj = (JSONObject) parser.parse(jsonResponse);

		// Get the 'results' array from the JSON object
		JSONArray locationData = (JSONArray) resultsJsonObj.get("results");

		// If no results are found, return null
		if (locationData == null || locationData.isEmpty()) {
			return null;
		}

		// Get the first result from the array
		JSONObject cityLocationData = (JSONObject) locationData.get(0);

		// Extract latitude and longitude
		double latitude = (double) cityLocationData.get("latitude");
		double longitude = (double) cityLocationData.get("longitude");

		// Return a new CityLocation object
		return new CityLocation(city, latitude, longitude);
	}

	// Reads the API response and returns it as a String.

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

	/**
	 * Establishes an HTTP connection to the given URL.
	 *
	 * @param urlString The URL as a String.
	 * @return The HttpURLConnection object.
	 * @throws Exception If an error occurs during connection.
	 */
	private HttpURLConnection fetchApiResponse(String urlString) throws Exception {
		URL url = new URL(urlString);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();

		// Set request method to GET
		conn.setRequestMethod("GET");

		return conn;
	}
}
