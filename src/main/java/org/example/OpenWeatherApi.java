package org.example;

import javafx.scene.control.Label;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class OpenWeatherApi {
    private static final String API_KEY = "23da4fdc8ca8e3a95891becfad16a120";
    private static final String API_ENDPOINT = "http://api.openweathermap.org/data/2.5/weather";

    public static void main(String[] args) {
        String cityName = "Iquitos";
        getWeather(cityName);
    }

    public static String getWeather(String cityName) {
        try {
            String apiUrl = String.format("%s?q=%s&appid=%s", API_ENDPOINT, cityName, API_KEY);
            URL url = new URL(apiUrl);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                StringBuilder response = new StringBuilder();

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                // Now 'response' contains the JSON response from the API.
                return response.toString();
            } else {
                throw new RuntimeException();
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
