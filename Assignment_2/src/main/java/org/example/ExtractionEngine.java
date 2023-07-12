package org.example;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 *   The ExtractionEngine class is responsible for fetching data from the NewsAPI
 *   and triggering the DataProcessingEngine for further processing.
 */
public class ExtractionEngine {

    private static final String API_KEY = "6e0c806ffaa44e6fbb40498da9516373";
    private static final String BASE_URL = "https://newsapi.org/v2/everything?";

    /**
     * Fetches news articles data from the NewsAPI and calls the DataProcessingEngine
     *  to parse and write the articles.
     * @param args
     * @throws UnsupportedEncodingException
     * @throws MalformedURLException
     */
    public static void main(String[] args) throws UnsupportedEncodingException, MalformedURLException {

        String[] keywords = {"Canada", "University", "Dalhousie", "Halifax", "Canada Education", "Moncton", "hockey", "Fredericton", "celebration"};

        //join all keywords with "OR"
        String query = String.join(" OR ", keywords);
        String encodedQuery = URLEncoder.encode(query, "UTF-8");

        //URL to hit for fetching all results
        String urlStr = "https://newsapi.org/v2/everything?q=" + encodedQuery + "&apiKey=6e0c806ffaa44e6fbb40498da9516373";
        URL url = new URL(urlStr);
        StringBuilder jsonData = new StringBuilder();

        try {

            System.out.println("Request URL: " + url.toString()); // Print the URL being requested

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            System.out.println("Response Code: " + responseCode);

            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;

                while ((inputLine = in.readLine()) != null) {
                    jsonData.append(inputLine);
                }
                in.close();

                // Print the response JSON
                System.out.println("Response JSON: " + jsonData.toString());

                // Call DataProcessingEngine.parseAndWriteArticles(jsonData)
                DataProcessingEngine.parseAndWriteArticles(jsonData.toString());

            } else {
                System.out.println("Unable to fetch data");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
