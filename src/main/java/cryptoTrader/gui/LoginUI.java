package cryptoTrader.gui;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.ImageIcon;

import java.awt.Container;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Color;
import java.awt.Taskbar;

import java.io.IOException;

import cryptoTrader.authentication.Authenticator;

/**
 * The login window where users can input their username and password.
 * @author Julian Koksal
 */
public class LoginUI extends JFrame {
	
	private static final long serialVersionUID = 1L;

	private static LoginUI instance; // Single instance of the LoginUI
	
	private JPasswordField passField; // The password field
	private JTextField userField; // The username field
	private JLabel message; // The message at the bottom of the window
	
	private LoginUI() {
		
		// Set the window title and icon.
		super("Crypto Trader");
		try {
			// MacOS
			Taskbar.getTaskbar().setIconImage(new ImageIcon(getClass().getResource("icon.png")).getImage());
		} catch (Exception e) {
			// Windows
			setIconImage(new ImageIcon(getClass().getResource("icon.png")).getImage());
		}
		
		Container pane = getContentPane(); // The content pane of the login window.
		pane.setLayout(new GridBagLayout());
		
		// Header
		JLabel header = new JLabel("Login");
		GridBagConstraints headerC = new GridBagConstraints();
		headerC.gridx = 0;
		headerC.gridy = 0;
		headerC.gridwidth = 2;
		headerC.insets = new Insets(10, 10, 0, 10);
		pane.add(header, headerC);
		
		// User label
		JLabel userLabel = new JLabel("Username: ");
		GridBagConstraints userLabelC = new GridBagConstraints();
		userLabelC.gridx = 0;
		userLabelC.gridy = 1;
		userLabelC.insets = new Insets(10, 10, 0, 5);
		pane.add(userLabel, userLabelC);
		
		// User field
		userField = new JTextField();
		userField.setColumns(20);
		GridBagConstraints userFieldC = new GridBagConstraints();
		userFieldC.gridx = 1;
		userFieldC.gridy = 1;
		userFieldC.insets = new Insets(10, 5, 0, 10);
		pane.add(userField, userFieldC);
		userField.addActionListener(e -> passField.grabFocus()); // Press enter to switch input focus to the password field.
		
		// Pass label
		JLabel passLabel = new JLabel("Password: ");
		GridBagConstraints passLabelC = new GridBagConstraints();
		passLabelC.gridx = 0;
		passLabelC.gridy = 2;
		passLabelC.insets = new Insets(10, 10, 0, 5);
		pane.add(passLabel, passLabelC);
		
		// Pass field
		passField = new JPasswordField();
		passField.setColumns(20);
		GridBagConstraints passFieldC = new GridBagConstraints();
		passFieldC.gridx = 1;
		passFieldC.gridy = 2;
		passFieldC.insets = new Insets(10, 5, 0, 10);
		pane.add(passField, passFieldC);
		passField.addActionListener(e -> {
			if (passField.getPassword().length == 0) userField.grabFocus();
			else login();
		}); // Press enter to submit or switch focus to username field if pass field is empty.
		
		// Buttons panel
		JPanel buttons = new JPanel();
		GridBagConstraints buttonsC = new GridBagConstraints();
		buttonsC.gridx = 0;
		buttonsC.gridy = 3;
		buttonsC.gridwidth = 2;
		buttonsC.insets = new Insets(10, 10, 10, 10);
		pane.add(buttons, buttonsC);
		
		// Create profile button
		JButton create = new JButton("Create new profile");
		buttons.add(create);
		create.addActionListener(e -> {
			try {
				if (userField.getText().isEmpty() || passField.getPassword().length == 0) {
					setMessage("Please enter a username and password.", Color.RED);
				} else if (Authenticator.getInstance().createProfile(userField.getText(), String.valueOf(passField.getPassword()))) {
					setMessage("New profile created.", Color.BLACK);
				} else {
					setMessage("A profile with that username already exists.", Color.RED);
				}
			} catch (IOException ex) {
				// Thrown by Authenticator when the username and password file is missing.
				dispose();
			}
		});
		
		// Submit button
		JButton submit = new JButton("Submit");
		buttons.add(submit);
		submit.addActionListener(e -> login()); // Click to login.
		
		// Message
		message = new JLabel();
		GridBagConstraints messageC = new GridBagConstraints();
		messageC.gridx = 0;
		messageC.gridy = 4;
		messageC.gridwidth = 2;
		messageC.insets = new Insets(-10, 10, 10, 10);
		message.setVisible(false);
		pane.add(message, messageC);
		
	}
	
	/**
	 * Get the single instance of LoginUI. Creates it if it does not exist yet.
	 * @return instance of LoginUI
	 */
	private static LoginUI getInstance() {
		if (instance == null) instance = new LoginUI();
		return instance;
	}
	
	/**
	 * Creates the login window.
	 * @param args
	 */
	public static void main(String[] args) {
		JFrame frame = getInstance();
		frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
		frame.pack();
		frame.setResizable(false);
		frame.setLocationRelativeTo(null); // Moves to the middle of the screen.
		frame.setVisible(true);
	}
	
	/**
	 * Login with the username and password that the user has entered into the fields.
	 * Creates the main UI and disposes of the login window if the login is valid, otherwise terminates the application.
	 */
	private void login() {
		// Displays message and does not submit if one of the fields is empty.
		if (userField.getText().isEmpty() || passField.getPassword().length == 0) {
			setMessage("Please enter a username and password.", Color.RED);
		} else {
			// Checks if the entered username and password is valid and starts or terminates the application.
			boolean isValidLogin = false;
			try {
				isValidLogin = Authenticator.getInstance().checkLoginCredentials(userField.getText(), String.valueOf(passField.getPassword()));
			} catch (IOException ex) {
				// Thrown by Authenticator when the username and password file is missing.
				// Terminates the application.
				dispose();
			}
			if(isValidLogin) {
				MainUI.getInstance().startApp(); // Initialize MainUI and start the app.
				dispose(); // Close login UI.
			} else {
				JOptionPane.showMessageDialog(
					this,
					"The username or password is incorrect.\nThe application will terminate.",
					"Crypto Trader",
					JOptionPane.ERROR_MESSAGE
				);
				dispose();
			}
		}
	}
	
	/**
	 * Sets the text and colour of the message at the bottom of the login UI.
	 * @param text text of the message
	 * @param c colour of the text
	 */
	private void setMessage(String text, Color c) {
		message.setForeground(c);
		message.setText(text);
		message.setVisible(true);
		getInstance().pack();
	}

}
