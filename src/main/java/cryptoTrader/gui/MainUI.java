package cryptoTrader.gui;

import java.awt.Component;
import java.awt.BorderLayout;
import java.awt.Dimension;
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

import cryptoTrader.utils.Trader;
import cryptoTrader.utils.TradingClient;
import cryptoTrader.utils.TraderActionLog;
import cryptoTrader.utils.DisplayHistogram;
import cryptoTrader.utils.DisplayTable;

public class MainUI extends JFrame {

	private static final long serialVersionUID = 1L;

	private static MainUI instance; // Single instance of MainUI
	
	private JPanel tablePanel; // Panel with the log item table.
	private JPanel histPanel; // Panel with the histogram.

	private DefaultTableModel dtm; // Underlying data of the UI table.
	
	private ArrayList<TradingClient> clientList; // List of trading clients
	
	private boolean isUpdatingTable = false; // Flag to prevent table listener from acting on changes it makes.

	/**
	 * Constructs the main UI and most of its subcomponents.
	 * Initializes DisplayTable and DisplayHistogram which will create the log item table and histogram when needed.
	 */
	private MainUI() {
		// Set window title
		super("Crypto Trader");

		// Trading client table
		
		dtm = new DefaultTableModel(new Object[] { "Trading Client", "Coin List", "Strategy Name" }, 0);

		// Adds first empty row.
		String[] firstRow = {"", "", "None"};
		dtm.addRow(firstRow);
		
		JTable table = new JTable(dtm); // UI table displays data in dtm
		table.setFillsViewportHeight(true);
		table.setGridColor(Color.LIGHT_GRAY);
		
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Trading Client Actions",
				TitledBorder.CENTER, TitledBorder.TOP));
		scrollPane.setPreferredSize(new Dimension(800, 300));
		
		Vector<String> strategyNames = new Vector<String>();
		strategyNames.add("None");
		strategyNames.add("BTC Strategy A");
		strategyNames.add("ADA Strategy B");
		strategyNames.add("ETH Strategy C");
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
			String[] newRow = {"", "", "None"};
			dtm.addRow(newRow);
		});

		// Remove row
		JButton remRow = new JButton("Remove Row");
		remRow.addActionListener(e -> {
			if (dtm.getRowCount() > 1) {
				int selectedRow = table.getSelectedRow();
				if (selectedRow != -1) dtm.removeRow(selectedRow);
			}
		});
		
		// Buttons panel
		JPanel buttons = new JPanel();
		buttons.setLayout(new BoxLayout(buttons, BoxLayout.X_AXIS));
		buttons.add(addRow);
		buttons.add(remRow);

		// East panel
		JPanel east = new JPanel();
		east.setLayout(new BoxLayout(east, BoxLayout.Y_AXIS));
		east.add(scrollPane);
		east.add(buttons);
		
		// West panel
		JPanel west = new JPanel();
		west.setPreferredSize(new Dimension(700, 650));
		
		// Log table
		tablePanel = new JPanel();
		west.add(tablePanel);
		new DisplayTable(TraderActionLog.getInstance()); // Initialize DisplayTable
		
		// Histogram
		histPanel = new JPanel();
		west.add(histPanel);
		new DisplayHistogram(TraderActionLog.getInstance()); // Initialize DisplayHistogram
		
		// Perform trade
		JButton trade = new JButton("Perform Trade");
		trade.addActionListener(e -> {
			// Call Trader to perform trades if the table is valid.
			if (isValidClientTable()) {
				updateClientList(); // Updates the list from the data in the table.
				Trader.performTrades(clientList);
			}
		});
		JPanel south = new JPanel();
		south.add(trade);

		getContentPane().add(east, BorderLayout.EAST);
		getContentPane().add(south, BorderLayout.SOUTH);
		getContentPane().add(west, BorderLayout.CENTER);
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
		frame.setVisible(true);
	}
	
	/**
	 * Replaces the components in tablePanel with the given component newTable.
	 * @param newTable the new component
	 */
	public void updateLogTable(Component newTable) {
		tablePanel.removeAll();
		tablePanel.add(newTable);
		tablePanel.revalidate();
	}
	
	/**
	 * Replaces the components in histPanel with the given component newHist.
	 * @param newHist the new component
	 */
	public void updateHist(Component newHist) {
		histPanel.removeAll();
		histPanel.add(newHist);
		histPanel.revalidate();
	}
	
	/**
	 * Show a warning pop up with the given message.
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
