package main;

/**
 * The entry point of the Weather App application.
 */
public class WeatherApp {
    public static void main(String[] args) {
        // Initialize the GUI on the Event Dispatch Thread
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                WeatherAppGUI gui = new WeatherAppGUI();
                gui.createAndShowGUI(); // Display GUI after initializing
            }
        });
    }
}
