package cryptoTrader.authentication;

import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Used to access the username and password data.
 * @author Julian Koksal
 */
public class Authenticator implements ILogin {
	
	private static Authenticator instance; // Single instance of Authenticator
	private ArrayList<String[]> userList; // List of valid username and password combinations represented as String arrays [username, password]
	
	/**
	 * Constructor reads the file containing valid username and password combinations and adds them all to userList.
	 * @throws IOException if the file is missing
	 */
	private Authenticator() throws IOException {
		userList = new ArrayList<String[]>();
		BufferedReader reader = new BufferedReader(new FileReader("src/main/java/cryptoTrader/authentication/users.txt"));
		String line = reader.readLine();
		while(line != null) {
			String[] user = new String[2]; // Username and password combination
			user[0] = line; // First line is the username
			line = reader.readLine();
			user[1] = line; // Next line is the associated password
			line = reader.readLine();
			userList.add(user);
		}
		reader.close();
	}
	
	/**
	 * Checks if the given username and password combination is valid.
	 * @param user username
	 * @param pass password
	 * @return true if the username and password combination is valid, false otherwise
	 */
	public boolean checkLoginCredentials(String user, String pass) {
		for (int i = 0; i < userList.size(); i++) {
			String[] userCredentials = userList.get(i);
			if (user.equals(userCredentials[0]) && pass.equals(userCredentials[1])) return true;
		}
		return false;
	}
	
	/**
	 * Checks if a profile with the given username already exists.
	 * @param newUser username to check
	 * @return true if a profile with username newUser exists, false otherwise.
	 */
	private boolean isExistingUser(String newUser) {
		for (int i = 0; i < userList.size(); i++) {
			String user = userList.get(i)[0];
			if (user.equals(newUser)) return true;
		}
		return false;
	}
	
	/**
	 * Creates a new profile with the given username and password and saves it to file.
	 * @param user username of the new profile
	 * @param pass password of the new profile
	 * @return true if success, false if a profile with that username already exists
	 * @throws IOException if the file is missing
	 */
	public boolean createProfile(String user, String pass) throws IOException {
		if (isExistingUser(user)) return false;
		// Writes the new profile to file.
		BufferedWriter writer = new BufferedWriter(new FileWriter("src/main/java/cryptoTrader/authentication/users.txt", true));
		writer.newLine();
		writer.write(user);
		writer.newLine();
		writer.write(pass);
		writer.close();
		// Adds the new profile to the list in memory so it can be used without reading the file again.
		String[] newUserCredentials = {user, pass};
		userList.add(newUserCredentials);
		return true;
	}
	
	/**
	 * Get the single instance of Authenticator.
	 * @return instance of Authenticator, creates it if it does not exist
	 * @throws IOException if the file containing usernames and passwords was missing
	 */
	public static Authenticator getInstance() throws IOException {
		if (instance == null) instance = new Authenticator();
		return instance;
	}
	
}
