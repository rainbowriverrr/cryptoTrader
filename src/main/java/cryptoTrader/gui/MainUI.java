package cryptoTrader.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Color;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import cryptoTrader.utils.AvailableCryptoList;
import cryptoTrader.utils.DataVisualizationCreator;
import cryptoTrader.utils.Trader;
import cryptoTrader.utils.TradingClient;
import cryptoTrader.utils.LogItem;
import cryptoTrader.utils.Strategy;
import cryptoTrader.utils.StrategyA;

public class MainUI extends JFrame {

	private static final long serialVersionUID = 1L;

	private static MainUI instance;
	private JPanel stats, chartPanel, tablePanel;

	// Should be a reference to a separate object in actual implementation
	private List<String> selectedList;

	private JTextArea selectedTickerList;
//	private JTextArea tickerList;
	private JTextArea tickerText;
	private JTextArea BrokerText;
	private JComboBox<String> strategyList;
	private Map<String, List<String>> brokersTickers = new HashMap<>();
	private Map<String, String> brokersStrategies = new HashMap<>();
	private List<String> selectedTickers = new ArrayList<>();
	private String selectedStrategy = "";
	private DefaultTableModel dtm;
	private JTable table;
	
	private ArrayList<TradingClient> clientList; // List of trading clients
	private ArrayList<LogItem> log = new ArrayList<LogItem>(); // List of log items to be displayed in a table and histogram on the left
	
	private boolean isUpdatingTable = false; // Flag to prevent table from generating a tableChanged notification.

	public static MainUI getInstance() {
		if (instance == null) instance = new MainUI();
		return instance;
	}

	private MainUI() {
		// Set window title
		super("Crypto Trader");
		
		// Perform trade
		JButton trade = new JButton("Perform Trade");
		trade.addActionListener(e -> {
			// Call Trader to perform trades if the table is valid.
			if (isValidClientTable()) {
				updateClientList();
				Trader.performTrades(clientList);
			}
		});
		JPanel south = new JPanel();
		south.add(trade);

		// Trading client table
		
		dtm = new DefaultTableModel(new Object[] { "Trading Client", "Coin List", "Strategy Name" }, 0);
		dtm.addTableModelListener(e -> {
			int rowChanged = e.getFirstRow(); // The row of the value that was changed.
			int colChanged = e.getColumn(); // The column of the value that was changed.
			// Checks if table is being updated (by this listener) to prevent infinite loop.
			if (isUpdatingTable == false && colChanged >= 0) {
				isUpdatingTable = true;
				// Trims the String that was entered by the user.
				String newValue = (String) dtm.getValueAt(rowChanged, colChanged); // New value that the user entered.
				if (newValue != null) {
					newValue = newValue.trim();
					dtm.setValueAt(newValue, rowChanged, colChanged);
				}
				// If a client name was entered, check if it already exists.
				if (colChanged == 0) {
					for (int i = 0; i < dtm.getRowCount(); i++) {
						String existingName = (String) dtm.getValueAt(i, 0);
						if (i != rowChanged && !existingName.isBlank() && newValue.equals(existingName)) {
							JOptionPane.showMessageDialog(
								this,
								"The Trading Client you have entered already exists. Please enter a different name or delete the row.",
								"Crypto Trader",
								JOptionPane.WARNING_MESSAGE
							);
							dtm.setValueAt(null, rowChanged, 0); // Clears the conflicting name from the table.
						}
					}
				}
				isUpdatingTable = false;
			}
		});
		// Adds first empty row.
		String[] firstRow = {"", "", "None"};
		dtm.addRow(firstRow);
		
		table = new JTable(dtm);
		table.setFillsViewportHeight(true);
		table.setGridColor(Color.LIGHT_GRAY);
		
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Trading Client Actions",
				TitledBorder.CENTER, TitledBorder.TOP));
		scrollPane.setPreferredSize(new Dimension(800, 300));
		
		Vector<String> strategyNames = new Vector<String>();
		strategyNames.add("None");
		strategyNames.add("Strategy A");
		strategyNames.add("Strategy B");
		strategyNames.add("Strategy C");
		strategyNames.add("Strategy D");
		TableColumn strategyColumn = table.getColumnModel().getColumn(2);
		strategyList = new JComboBox<String>(strategyNames);
		strategyColumn.setCellEditor(new DefaultCellEditor(strategyList));
		
		// Add row
		JButton addRow = new JButton("Add Row");
		addRow.addActionListener(e -> {
			String[] newRow = {"", "", "None"};
			dtm.addRow(newRow);
		});

		// Remove row
		JButton remRow = new JButton("Remove Row");
		remRow.addActionListener(e -> {
			int selectedRow = table.getSelectedRow();
			if (selectedRow != -1 && !strategyList.isPopupVisible()) dtm.removeRow(selectedRow);
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
//		east.add(selectedTickerListLabel);
//		east.add(selectedTickersScrollPane);

		// Set charts region
		JPanel west = new JPanel();
		west.setPreferredSize(new Dimension(1250, 650));
		stats = new JPanel();
		stats.setLayout(new GridLayout(2, 2));

		west.add(stats);

		getContentPane().add(east, BorderLayout.EAST);
		getContentPane().add(west, BorderLayout.CENTER);
		getContentPane().add(south, BorderLayout.SOUTH);
		getContentPane().add(west, BorderLayout.WEST);
	}

	public void updateStats(JComponent component) {
		stats.add(component);
		stats.revalidate();
	}

	/**
	 * Starts the application by creating the main UI window.
	 */
	public void startApp() {
		JFrame frame = MainUI.getInstance();
		frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}
	
	/**
	 * Add newLogs to the log.
	 * @param newLogs list of new log items
	 */
	public void updateLog(ArrayList<LogItem> newLogs) {
		log.addAll(newLogs);
	}
	
	/**
	 * Updates clientList from the data in the table.
	 */
	private void updateClientList() {
		clientList = new ArrayList<TradingClient>();
		for (int row = 0; row < dtm.getRowCount(); row++) {
			String name = (String) dtm.getValueAt(row, 0);
			String[] coins = ((String) dtm.getValueAt(row, 1)).split(",");
			for (String coin : coins) coin = coin.trim();
			Strategy strat = new StrategyA((String) dtm.getValueAt(row, 2));
			TradingClient client = new TradingClient(name, coins, strat);
			clientList.add(client);
		}
	}
	
	/**
	 * Checks if the trading client table is valid (No missing or blank values). Notifies the user to fix the first invalid value in the table.
	 * @return true if the table is valid, false otherwise
	 */
	private boolean isValidClientTable() {
		// Check for empty table.
		if (dtm.getRowCount() == 0) {
			JOptionPane.showMessageDialog(this, "Please add a Trading Client before perfoming trades.", "Crypto Trader", JOptionPane.WARNING_MESSAGE);
			return false;
		}
		// Check for empty values in each row.
		for (int row = 0; row < dtm.getRowCount(); row++) {
			String name = (String) dtm.getValueAt(row, 0);
			if (name == null || name.isBlank()) {
				JOptionPane.showMessageDialog(this, "Please fill in the Trading Client name in row " + (row + 1) + ".", "Crypto Trader", JOptionPane.WARNING_MESSAGE);
				return false;
			}
			String coinsString = (String) dtm.getValueAt(row, 1);
			if (coinsString == null || coinsString.isBlank()) {
				JOptionPane.showMessageDialog(this, "Please fill in the Coin List for client " + name + ".", "Crypto Trader", JOptionPane.WARNING_MESSAGE);
				return false;
			}
			String strategy = (String) dtm.getValueAt(row, 2);
			if (strategy.equals("None")) {
				JOptionPane.showMessageDialog(this, "Please select a strategy for client " + name + ".", "Crypto Trader", JOptionPane.WARNING_MESSAGE);
				return false;
			}
		}
		return true;
	}

}
