package zendeskAPI;

import java.util.Scanner;

public class UserAuthenticationsDetails {

	private String username;
	private String password;

	// Setting getters and setters for Username and Password

	public void setUsername(String username) {
		if (username.contains("@"))
			this.username = username;
		else
			System.out.println("Invalid username");
	}

	public String getUsername() {
		return username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPassword() {
		return password;
	}

	public UserAuthenticationsDetails() {
		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(System.in);

		System.out.println("Press -> 1 for entering your credentials");
		System.out.println("      -> 0 for using default credentials");
		int input = scanner.nextInt();

		switch (input) {

		case 1:

			// Asking user for credentials
			System.out.println("Enter username:");
			String username = scanner.next();
			System.out.println("Enter password:");
			String password = scanner.next();
			this.setUsername(username);
			this.setPassword(password);
			break;

		case 0:

			// Setting default credentials
			this.setUsername("3kshitij@gmail.com");
			this.setPassword("Juna@9969");

			break;

		default:

			System.out.println("Invalid Input");
		}

	}

}
