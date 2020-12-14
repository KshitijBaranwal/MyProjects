package zendeskAPI;

import java.text.SimpleDateFormat;
import java.util.Date;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class DisplayTicket {

	// Setting Multiple Ticket Display

	void displayMultipleTicket(JSONArray ticket) {

		System.out.println("Total number of tickets displayed are: " + ticket.size() + "\n");

		System.out.println(
				"----------------------------------------------------------------------------------------------------------");
		System.out.println("| Ticket No |" + "      | Subjects |  " + "                        | Opened By |       "
				+ "            | Date |     ");
		System.out.println(
				"----------------------------------------------------------------------------------------------------------");

		for (int i = 0; i < ticket.size(); i++) {
			JSONObject data = (JSONObject) ticket.get(i);

			// Handling Date Display Logic

			SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
			SimpleDateFormat output = new SimpleDateFormat("dd MMMM yyyy HH:mm");
			Date d = null;
			try {
				d = input.parse(((String) data.get("updated_at")));
			} catch (Exception e) {
				e.printStackTrace();
			}
			System.out.println("Ticket: " + data.get("id") + " subject: '" + data.get("subject") + "' opened by: "
					+ data.get("assignee_id") + " on: " + output.format(d));
		}

	}

	// Setting Single Ticket Display

	void displaySingleTicket(JSONObject ticket) {

		System.out.println(
				"----------------------------------------------------------------------------------------------------------");
		System.out.println("| Ticket No |" + "      | Subjects |  " + "                        | Opened By |       "
				+ "            | Date |     ");
		System.out.println(
				"----------------------------------------------------------------------------------------------------------");

		// Handling Date Display Logic

		SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		SimpleDateFormat output = new SimpleDateFormat("dd MMMM yyyy HH:mm");
		Date d = null;
		try {
			d = input.parse(((String) ticket.get("updated_at")));
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Ticket: " + ticket.get("id") + " subject: '" + ticket.get("subject") + "' opened by: "
				+ ticket.get("assignee_id") + " on: " + output.format(d));

	}

}
