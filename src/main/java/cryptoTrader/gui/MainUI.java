package cryptoTrader.gui;

import java.awt.Component;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Taskbar;
import java.awt.Color;

import java.util.ArrayList;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.ImageIcon;

import cryptoTrader.utils.Trader;
import cryptoTrader.utils.TradingClient;

public class MainUI extends JFrame {

	private static final long serialVersionUID = 1L;

	private static MainUI instance; // Single instance of MainUI
	
	private JPanel west; // Panel with the log table and histogram.
	private JPanel tableButtons; // Panel with the export to csv button.

	private DefaultTableModel dtm; // Underlying data of the UI table.
	
	private ArrayList<TradingClient> clientList; // List of trading clients
	
	private boolean isUpdatingTable = false; // Flag to prevent table listener from acting on changes it makes.

	/**
	 * Constructs the main UI and most of its subcomponents.
	 * Initializes DisplayTable and DisplayHistogram which will create the log item table and histogram when needed.
	 */
	private MainUI() {
		// Set window title and icon
		super("Crypto Trader");
		try {
			// MacOS
			Taskbar.getTaskbar().setIconImage(new ImageIcon(getClass().getResource("icon.png")).getImage());
		} catch (Exception e) {
			// Windows
			try {
				setIconImage(new ImageIcon(getClass().getResource("icon.png")).getImage());
			} catch (Exception ex) {
				
			}
		}

		// Trading client table
		
		dtm = new DefaultTableModel(new Object[] { "Trading Client", "Coin List", "Strategy Name" }, 0);
		
		// Adds first empty row.
		String[] firstRow = {"", "", "None"};
		dtm.addRow(firstRow);
		
		// UI table displays data in dtm
		JTable table = new JTable(dtm);
		table.setFillsViewportHeight(true);
		table.setGridColor(Color.LIGHT_GRAY);
		
		// Table is put in a scrollPane
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Trading Client Actions",
				TitledBorder.CENTER, TitledBorder.TOP));
		scrollPane.setPreferredSize(new Dimension(700, 600));
		
		// Strategy selection drop-down menu
		Vector<String> strategyNames = new Vector<String>();
		strategyNames.add("None");
		strategyNames.add("BTC Strategy A");
		strategyNames.add("ADA Strategy B");
		strategyNames.add("ETH,BTC Strategy C");
		strategyNames.add("DOGE Strategy D");
		strategyNames.add("ADA Strategy E");
		TableColumn strategyColumn = table.getColumnModel().getColumn(2);
		JComboBox<String> strategyList = new JComboBox<String>(strategyNames);
		strategyColumn.setCellEditor(new DefaultCellEditor(strategyList));
		
		// Listens for changes to the table.
		dtm.addTableModelListener(e -> {
			if (!isUpdatingTable) { // Will not act if table is already being updated by this listener.
				isUpdatingTable = true; // Set updating flag.
				int rowChanged = e.getFirstRow(); // The row of the value that was changed.
				int colChanged = e.getColumn(); // The column of the value that was changed.
				// If the user entered a client name or coin list.
				if (colChanged == 0 || colChanged == 1) {
					String newValue = (String) dtm.getValueAt(rowChanged, colChanged); // New value that the user entered.
					// Trims the String that was entered by the user.
					newValue = newValue.trim();
					dtm.setValueAt(newValue, rowChanged, colChanged);
					// If a client name was entered, check if it already exists.
					if (colChanged == 0) {
						// Iterate rows and check for a conflicting name.
						for (int i = 0; i < dtm.getRowCount(); i++) {
							String existingName = (String) dtm.getValueAt(i, 0); // Existing name in the current row.
							if (i != rowChanged && !existingName.isEmpty() && newValue.equals(existingName)) {
								showWarning("Trading Client " + newValue + " already exists on row " + (i + 1)
											+ ".\nPlease enter a different name or delete the row.");
								dtm.setValueAt("", rowChanged, 0); // Clears the conflicting name from the table.
								break; // Stop iterating rows when conflict is found and resolved.
							}
						}
					}
					// If a coin list was entered, change it to capital letters.
					if (colChanged == 1) {
						dtm.setValueAt(((String)dtm.getValueAt(rowChanged, 1)).toUpperCase(), rowChanged, colChanged);
					}
				}
				isUpdatingTable = false; // Clear updating flag.
			}
		});
		
		// Add row
		JButton addRow = new JButton("Add Row");
		addRow.addActionListener(e -> {
			// Prevents errors caused by clicking the button while the table is being edited.
			if (table.isEditing()) table.getCellEditor().stopCellEditing();
			String[] newRow = {"", "", "None"};
			dtm.addRow(newRow);
		});

		// Remove row
		JButton remRow = new JButton("Remove Row");
		remRow.addActionListener(e -> {
			// Prevents errors caused by clicking the button while the table is being edited.
			if (table.isEditing()) table.getCellEditor().stopCellEditing();
			if (dtm.getRowCount() == 1) {
				dtm.removeRow(0);
				dtm.addRow(firstRow);
			} else {
				int selectedRow = table.getSelectedRow();
				if (selectedRow != -1) dtm.removeRow(selectedRow);
			}
		});

		// Clear All
		JButton clearAll = new JButton("Clear All");
		clearAll.addActionListener(e -> {
			int currRow = dtm.getRowCount();
			while (currRow > 0) {
				dtm.removeRow(currRow-1);
				currRow --;
			}
			dtm.addRow(firstRow);
		});
				
		// Add/remove/clear row buttons panel
		JPanel buttons = new JPanel();
		buttons.setLayout(new BoxLayout(buttons, BoxLayout.X_AXIS));
		buttons.add(addRow);
		buttons.add(remRow);
		buttons.add(clearAll);

		// east panel with client table and buttons
		JPanel east = new JPanel();
		east.setLayout(new BoxLayout(east, BoxLayout.Y_AXIS));
		east.add(scrollPane);
		east.add(buttons);
		
		// west panel with log table and histogram
		west = new JPanel();
		west.setLayout(new BoxLayout(west, BoxLayout.Y_AXIS));
		west.setPreferredSize(new Dimension(700, 650));

		// Initialize DisplayTable and DisplayHistogram which will create the log table and histogram when needed.
		new DisplayTable(TraderActionLog.getInstance());
		new DisplayHistogram(TraderActionLog.getInstance());
		
		// Export To CSV
		JButton exportToCSV = new JButton("Export Table To CSV");
		exportToCSV.addActionListener(e -> {
			TraderActionLog.getInstance().writeToCSV();
		});
		tableButtons = new JPanel();
		tableButtons.setLayout(new BoxLayout(tableButtons, BoxLayout.X_AXIS));
		tableButtons.add(exportToCSV);

		
		// Perform trade
		JButton trade = new JButton("Perform Trade");
		trade.addActionListener(e -> {
			// Prevents errors caused by clicking the button while the table is being edited.
			if (table.isEditing()) table.getCellEditor().stopCellEditing();
			// Call Trader to perform trades if the table is valid.
			if (isValidClientTable()) {
				updateClientList(); // Updates the list from the data in the table.
				Trader.performTrades(clientList);
			}
		});
		
		// south panel with perform trade button
		JPanel south = new JPanel();
		south.add(trade);
		
		// center panel with log table, histogram, and client table.
		JPanel center = new JPanel();
		center.setLayout(new BoxLayout(center, BoxLayout.X_AXIS));
		center.add(west);
		center.add(east);
		
		// Add to content pane
		getContentPane().add(center, BorderLayout.CENTER); // Components in CENTER will automatically resize to fit the window.
		getContentPane().add(south, BorderLayout.SOUTH);
		
	}
	
	/**
	 * Gets the single instance of MainUI. Creates it if it does not exist yet.
	 * @return instance of MainUI
	 */
	public static MainUI getInstance() {
		if (instance == null) instance = new MainUI();
		return instance;
	}
	
	/**
	 * Starts the application by creating the main UI window.
	 */
	public void startApp() {
		JFrame frame = getInstance();
		frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
		frame.pack();
		frame.setLocationRelativeTo(null); // Move to middle of the screen
		frame.setVisible(true);
	}
	
	/**
	 * Used to add/update the log table and histogram. Adds the component if it isn't there yet, then revalidates.
	 * @param newComponent the log table or histogram
	 */
	public void updateWest(Component newComponent) {
		if (west.getComponents().length == 0 || west.getComponents().length == 2) west.add(newComponent);
		if (west.getComponents().length == 1) west.add(tableButtons);
		west.revalidate();
	}
	
	/**
	 * Shows a warning pop up with the given message.
	 * @param message
	 */
	public void showWarning(String message) {
		JOptionPane.showMessageDialog(this, message, "Crypto Trader", JOptionPane.WARNING_MESSAGE);
	}
	
	/**
	 * Updates clientList from the data in the table.
	 * Called only before the trades are performed.
	 */
	private void updateClientList() {
		clientList = new ArrayList<TradingClient>(); // Creates new list, replaces the old list if there is one.
		// Iterates the rows of the table, creates a TradingClient for each one and adds it to clientList.
		for (int row = 0; row < dtm.getRowCount(); row++) {
			String name = (String) dtm.getValueAt(row, 0); // Name of the trading client
			String coins = (String) dtm.getValueAt(row, 1); // Coin list of the client
			String strat = (String) dtm.getValueAt(row, 2); // Strategy of the client
			clientList.add(new TradingClient(name, coins, strat));
		}
	}
	
	/**
	 * Checks if the trading client table is valid (no missing values). Notifies the user to fix the first missing value in the table.
	 * @return true if the table is valid, false otherwise
	 */
	private boolean isValidClientTable() {
		// Check for empty values in each row.
		for (int row = 0; row < dtm.getRowCount(); row++) {
			String name = (String) dtm.getValueAt(row, 0);
			if (name.isEmpty()) {
				showWarning("Please fill in the Trading Client name in row " + (row + 1) + ".");
				return false;
			}
			String coins = (String) dtm.getValueAt(row, 1);
			if (coins.isEmpty()) {
				showWarning("Please fill in the Coin List for client " + name + ".");
				return false;
			}
			String strategy = (String) dtm.getValueAt(row, 2);
			if (strategy.equals("None")) {
				showWarning("Please select a strategy for client " + name + ".");
				return false;
			}
		}
		return true;
	}

}
