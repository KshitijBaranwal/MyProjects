package zendeskAPI;

import java.io.IOException;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;

public class HTTPConnection {

	public HttpsURLConnection setHTTPConnection(String authStringEnc, String setURL) throws IOException {

		// Setting Connection parameters
		HttpsURLConnection connection = null;
		String requestMethod = "GET";

		try {
			// Establishing Connection
			URL zendeskURL = new URL(setURL);
			connection = (HttpsURLConnection) zendeskURL.openConnection();
			connection.setRequestMethod(requestMethod);
			connection.setRequestProperty("Authorization", "Basic " + authStringEnc);
			connection.setUseCaches(false);

			// Handling Response Codes
			if (connection.getResponseCode() == 200) {
				return connection;
			} else if (connection.getResponseCode() == 401) {
				System.out.println("Response Code: " + connection.getResponseCode());
				System.out.println("Couldn't Authenticate You");
			} else if (connection.getResponseCode() == 404) {
				System.out.println("Response Code: " + connection.getResponseCode());
				System.out.println("API unavailable");
			} else if (connection.getResponseCode() == 500) {
				System.out.println("Response Code: " + connection.getResponseCode());
				System.out.println("Internal Server Error");
			} else if (connection.getResponseCode() == 503) {
				System.out.println("Response Code: " + connection.getResponseCode());
				System.out.println("Service Unavailable");
			} else {
				System.out.println("Unique response code found:" + connection.getResponseCode());
				System.out.println("Problem with the request. Exiting.");
				System.exit(0);
			}
		} catch (Exception e) {
			// Handling Connection Exceptions
			System.out.println("HTTP Connection failed due to error:");
			e.printStackTrace();
		}
		return null;

	}

}
