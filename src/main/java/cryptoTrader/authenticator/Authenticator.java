package cryptoTrader.authenticator;

import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

/**
 * Used to check if a username and password is valid.
 * @author Julian Koksal
 */
public class Authenticator implements ILogin {
	
	private static Authenticator instance; // Single instance of Authenticator
	private ArrayList<String[]> userList; // List of valid username and password combinations represented as String arrays [username, password]
	
	/**
	 * Constructor reads the file containing valid username and password combinations and adds them all to userList.
	 * @throws IOException
	 */
	public Authenticator() throws IOException {
		userList = new ArrayList<String[]>();
		BufferedReader reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("users.txt")));
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
	 * Checks if the given username and password are valid.
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
	 * Get the single instance of Authenticator.
	 * @return instance of Authenticator, creates it if it does not exist
	 * @throws IOException
	 */
	public static Authenticator getInstance() throws IOException {
		if (instance == null) instance = new Authenticator();
		return instance;
	}
	
}
