package cryptoTrader.authentication;

import java.io.IOException;

public interface ILogin {

	public boolean checkLoginCredentials(String user, String pass);
	
	public boolean createProfile(String user, String pass) throws IOException;
	
}
