package zendeskAPI;

import static org.junit.Assert.assertEquals;
import javax.net.ssl.HttpsURLConnection;
import org.apache.commons.codec.binary.Base64;
import org.junit.Test;

public class TestCases {

	// Authentication before establishing connection
	UserAuthenticationsDetails userAuthenticationsDetails = new UserAuthenticationsDetails();
	String authString = userAuthenticationsDetails.getUsername() + ":" + userAuthenticationsDetails.getPassword();
	byte[] authEncBytes = Base64.encodeBase64(authString.getBytes()); // Base 64 encoded authentication string
	String authStringEnc = new String(authEncBytes);
	String setURL = null;
	HTTPConnection httpConnection = new HTTPConnection();
	HttpsURLConnection connection;

	@Test
	public void testCase1HTTPConnection() throws Exception {
		// Testing Correct API
		setURL = "https://kshitij1772.zendesk.com/api/v2/tickets/count.json";
		connection = httpConnection.setHTTPConnection(authStringEnc, setURL);
		assertEquals(connection.getResponseCode(), 200);
		System.out.println("Everything is working fine!");
	}

	@Test
	public void testCase2HTTPConnection() throws Exception {
		// Testing Wrong API
		setURL = "https://kshitij1772.zendesk.com/api/v2/count.json";
		connection = httpConnection.setHTTPConnection(authStringEnc, setURL);
		assertEquals(connection.getResponseCode(), 404);
	}

	@Test
	public void testAuthentication() throws Exception {
		// Testing Wrong Username and Password
		authStringEnc = "abc@gamil.com:abcd";
		setURL = "https://kshitij1772.zendesk.com/api/v2/tickets/count.json";
		connection = httpConnection.setHTTPConnection(authStringEnc, setURL);
		assertEquals(connection.getResponseCode(), 401);
	}

}
