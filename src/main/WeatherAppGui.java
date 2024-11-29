package main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// Manages the GUI components and user interactions for the Weather App.

class WeatherAppGUI extends JFrame {

	private JTextField cityTextField; // Input field for city name
	private JTextArea resultTextArea; // Area to display weather results
	private JButton getWeatherButton; // Button to trigger weather data fetch

	public WeatherAppGUI() {
		super("Weather App");

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		initComponents(); // Initialize GUI components
	}

	public void createAndShowGUI() {
		pack(); // Adjust window size based on components
		setLocationRelativeTo(null); // Center the window on the screen
		setVisible(true); // Make the GUI visible
	}

	private void initComponents() {
		// Create components
		cityTextField = new JTextField(20);
		getWeatherButton = new JButton("Get Weather");

		// Input panel with titled border and GridBagLayout for better alignment
		JPanel inputPanel = new JPanel();
		inputPanel.setBorder(BorderFactory.createTitledBorder("Enter City"));
		inputPanel.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();

		gbc.insets = new Insets(5, 5, 5, 5); // Add padding between components

		gbc.gridx = 0;
		gbc.gridy = 0;
		inputPanel.add(new JLabel("City: "), gbc);

		gbc.gridx = 1;
		gbc.gridy = 0;
		inputPanel.add(cityTextField, gbc);

		gbc.gridx = 2;
		gbc.gridy = 0;
		inputPanel.add(getWeatherButton, gbc);

		// Result area with titled border and adjusted font
		resultTextArea = new JTextArea(10, 30);
		resultTextArea.setEditable(false); // Make result area read-only
		resultTextArea.setFont(new Font("Serif", Font.PLAIN, 14));
		JScrollPane scrollPane = new JScrollPane(resultTextArea);
		scrollPane.setBorder(BorderFactory.createTitledBorder("Weather Data"));

		// Add action listener to the button
		getWeatherButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String city = cityTextField.getText();
				if (city == null || city.trim().isEmpty()) {
					// Show error message if city name is empty
					JOptionPane.showMessageDialog(WeatherAppGUI.this, "Please enter a city name.");
					return;
				}

				// Fetch and display weather data for the entered city
				fetchAndDisplayWeatherData(city);
			}
		});

		// Set layout manager for the frame and add components
		getContentPane().setLayout(new BorderLayout(10, 10));
		getContentPane().add(inputPanel, BorderLayout.NORTH);
		getContentPane().add(scrollPane, BorderLayout.CENTER);
	}

	private void fetchAndDisplayWeatherData(String city) {
		try {
			// Fetch location data
			LocationService locationService = new LocationService();
			CityLocation cityLocation = locationService.getLocationData(city);

			if (cityLocation == null) {
				// Show error message if location data is not found
				JOptionPane.showMessageDialog(this, "Could not find location for city: " + city);
				return;
			}

			// Fetch weather data
			WeatherService weatherService = new WeatherService();
			WeatherData weatherData = weatherService.getWeatherData(cityLocation.getLatitude(),
					cityLocation.getLongitude());

			if (weatherData == null) {
				// Show error message if weather data is not available
				JOptionPane.showMessageDialog(this, "Could not retrieve weather data for city: " + city);
				return;
			}

			// Display weather data in the result text area
			displayWeatherData(weatherData);

		} catch (Exception ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(this, "An error occurred while fetching weather data.");
		}
	}

	private void displayWeatherData(WeatherData weatherData) {
		StringBuilder sb = new StringBuilder();
		sb.append("Current Time: ").append(weatherData.getTime()).append("\n");
		sb.append("Temperature (°C): ").append(weatherData.getTemperature()).append("\n");
		sb.append("Relative Humidity: ").append(weatherData.getRelativeHumidity()).append("%\n");
		sb.append("Wind Speed: ").append(weatherData.getWindSpeed()).append(" km/h\n");

		resultTextArea.setText(sb.toString());
	}
}
