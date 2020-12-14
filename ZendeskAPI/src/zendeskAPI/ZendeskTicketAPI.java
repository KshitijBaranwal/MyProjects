package zendeskAPI;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import javax.net.ssl.HttpsURLConnection;
import org.apache.commons.codec.binary.Base64;
import org.json.JSONException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import java.util.Scanner;

public class ZendeskTicketAPI {

	// Number of Tickets per page as requested is set to 25
	final public static int ticketsPerPage = 25;
	// Default Domain Name
	final public static String defaultDomain = "kshitij1772.zendesk.com";

	public static void main(String args[]) {

		try {

			System.out.println("Welcome to Zendesk Ticket Viewer System-> \n");
			@SuppressWarnings("resource")
			Scanner scanner = new Scanner(System.in);

			// Authentication before establishing connection
			UserAuthenticationsDetails userAuthenticationsDetails = new UserAuthenticationsDetails();
			String authString = userAuthenticationsDetails.getUsername() + ":"
					+ userAuthenticationsDetails.getPassword();
			byte[] authEncBytes = Base64.encodeBase64(authString.getBytes()); // Base 64 encoded authentication string
			String authStringEnc = new String(authEncBytes);

			// HTTP Connection Request for getting Total Count of Tickets
			HTTPConnection httpConnection = new HTTPConnection();
			String setURL = "https://" + defaultDomain + "/api/v2/tickets/count.json";
			HttpsURLConnection countConnection = httpConnection.setHTTPConnection(authStringEnc, setURL);
			String totalTickets = null;
			if (countConnection.getResponseCode() == 200) {
				BufferedReader buffReader = new BufferedReader(new InputStreamReader(countConnection.getInputStream()));
				String line = buffReader.readLine();
				while (line != null) {
					JSONParser jsonParser = new JSONParser();
					JSONObject jsonObject = (JSONObject) jsonParser.parse(line);
					JSONObject count = (JSONObject) jsonObject.get("count");
					totalTickets = count.get("value").toString();
					System.out.println("The total number of Tickets are: " + totalTickets);
					line = buffReader.readLine();
				}
				buffReader.close();
			}
			countConnection.disconnect();

			int inputOfUser = 1; // Initializing User Input

			// For Displaying Tickets
			DisplayTicket displayTicket = new DisplayTicket();

			while (inputOfUser != 0) {

				System.out.println("\n\n Select from below option:");
				System.out.println(" ->Enter 1 to view all tickets");
				System.out.println(" ->Enter 2 to view ticket of your choice");
				System.out.println(" ->Enter 0 to quit");
				inputOfUser = scanner.nextInt();

				switch (inputOfUser) {
				case 1:
					// View All Tickets
					setURL = "https://" + defaultDomain + "/api/v2/tickets.json?page[size]=" + ticketsPerPage;
					HttpsURLConnection connection = httpConnection.setHTTPConnection(authStringEnc, setURL);
					int totalPageCount = (Integer.parseInt(totalTickets) / ticketsPerPage)
							+ (Integer.parseInt(totalTickets) % ticketsPerPage);
					int currentPageCount = 1;

					if (connection.getResponseCode() == 200) {

						BufferedReader buffReader = new BufferedReader(
								new InputStreamReader(connection.getInputStream()));
						String line = buffReader.readLine();
						while (line != null) {

							JSONParser jsonParser = new JSONParser();
							JSONObject json = (JSONObject) jsonParser.parse(line);
							JSONArray ticket = (JSONArray) json.get("tickets");
							JSONObject links = (JSONObject) json.get("links");
							String nextURL = links.get("next").toString();
							// System.out.println(nextURL);

							System.out.println("\n Page " + currentPageCount + " of: " + totalPageCount + " Pages \n");
							displayTicket.displayMultipleTicket(ticket);

							String userInput = "next"; // Initializing User Input

							// Using Offset Pagination for implementing pagination of Tickets
							while (nextURL != null && userInput.equalsIgnoreCase("next")
									&& currentPageCount != totalPageCount) {

								System.out.println("\n ->Enter 'next' for next page");
								System.out.println(" ->Enter 'back' to return to main options \n");
								userInput = scanner.next();

								switch (userInput) {
								case "next":
									currentPageCount++;
									HttpsURLConnection nextPageConnection = httpConnection
											.setHTTPConnection(authStringEnc, nextURL);
									if (nextPageConnection.getResponseCode() == 200) {

										BufferedReader bufferReader = new BufferedReader(
												new InputStreamReader(nextPageConnection.getInputStream()));
										String readLine = bufferReader.readLine();
										while (readLine != null) {
											JSONParser jParser = new JSONParser();
											JSONObject jsonObject = (JSONObject) jParser.parse(readLine);
											JSONArray nextticket = (JSONArray) jsonObject.get("tickets");
											JSONObject nextlinks = (JSONObject) jsonObject.get("links");
											nextURL = nextlinks.get("next").toString();
											// System.out.println(nextURL);
											System.out.println("\n Page " + currentPageCount + " of: " + totalPageCount
													+ " Pages \n");
											displayTicket.displayMultipleTicket(nextticket);
											readLine = bufferReader.readLine();
										}
										bufferReader.close();
									}
									break;
								case "back":
									break;
								default:
									System.out.println("Invalid Input...Exiting!");
									System.exit(0);
								}
							}

							line = buffReader.readLine();
						}
						buffReader.close();
					}
					break;

				case 2:

					// View Selected Ticket
					System.out.println("Enter Ticket Number of which details to be displayed:");
					int ticketNumber = scanner.nextInt();
					if (ticketNumber <= 0 || ticketNumber > Integer.parseInt(totalTickets)) {
						System.out.println("Invalid Ticket Number");
						break;
					}
					setURL = "https://" + defaultDomain + "/api/v2/tickets/" + ticketNumber + ".json";
					HttpsURLConnection connectionByID = httpConnection.setHTTPConnection(authStringEnc, setURL);

					if (connectionByID.getResponseCode() == 200) {
						BufferedReader buffReader = new BufferedReader(
								new InputStreamReader(connectionByID.getInputStream()));
						String line = buffReader.readLine();
						while (line != null) {
							JSONParser jsonParser = new JSONParser();
							JSONObject jsonObject = (JSONObject) jsonParser.parse(line);
							JSONObject ticket = (JSONObject) jsonObject.get("ticket");
							displayTicket.displaySingleTicket(ticket);
							line = buffReader.readLine();
						}
						buffReader.close();
					}
					break;

				case 0:
					System.out.println("Terminating Ticket Viewer");
					break;
				default:
					System.out.println("Invalid Input...Exiting!");
					System.exit(0);
				}
			}

		} catch (ArithmeticException e) {
			System.out.println("Warning: ArithmeticException");
		} catch (IOException e) {
			System.out.println("Warning: IO Exception");
		} catch (JSONException e) {
			System.out.println("Unexpected JSON exception" + e);
		} catch (Exception e) {
			System.out.println("Program execution failed due to error:");
			e.printStackTrace();
		} finally {
			System.out.println("Ticket Viewer is Closed");
		}

	}

}
