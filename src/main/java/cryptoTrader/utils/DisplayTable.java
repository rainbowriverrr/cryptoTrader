package cryptoTrader.utils;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Calendar;

import java.awt.Dimension;
import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.border.TitledBorder;

import cryptoTrader.gui.MainUI;

/**
 * Used to generate the UI table of log items.
 */
public class DisplayTable implements Observer {
	
	private TraderActionLog subject; // DisplayTable is observer to TraderActionLog.
	
	private DefaultTableModel dtm; // DTM for the table
	private JScrollPane scrollPane; // table is in a scroll pane

	/**
	 * Construct a DisplayTable that observes the given subject.
	 * @param subject the TraderActionLog to observe
	 */
	public DisplayTable(TraderActionLog subject) {
		this.subject = subject;
		subject.attach(this);
		
		dtm = new DefaultTableModel(new Object[] {"Trader","Strategy","CryptoCoin","Action","Quantity","Price","Date"}, 0);
		JTable table = new JTable(dtm);
		table.setEnabled(false); // Prevents user from editing the table since it is only for displaying data.
		table.setGridColor(Color.LIGHT_GRAY);
		table.setFillsViewportHeight(true);
		// Set column widths
		table.getColumnModel().getColumn(0).setPreferredWidth(110); // Trader
		table.getColumnModel().getColumn(1).setPreferredWidth(130); // Strategy
		table.getColumnModel().getColumn(2).setPreferredWidth(100); // Coin list
		table.getColumnModel().getColumn(3).setPreferredWidth(60); // Action
		table.getColumnModel().getColumn(4).setPreferredWidth(60); // Quantity
		table.getColumnModel().getColumn(5).setPreferredWidth(100); // Price
		table.getColumnModel().getColumn(6).setPreferredWidth(120); // Date
		// Table is put in a scroll pane.
		scrollPane = new JScrollPane(table);
		scrollPane.setBorder (BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),
				"Trader Actions",
				TitledBorder.CENTER,
				TitledBorder.TOP));
		scrollPane.setPreferredSize(new Dimension(700, 300));
	}

	/**
	 * Triggered when the subject is changed. Calls display() to create the table.
	 */
	@Override
	public void update(Subject changedSubject) {
		if (changedSubject.equals(subject)) display();
	}

	/**
	 * Gets the log from TraderActionLog, creates the table, and passes it to the MainUI.
	 */
	private void display() {
		ArrayList<LogItem> log = subject.getLog();
		
		Calendar cal = Calendar.getInstance(); // Calendar instance to interpret the log Date.

		// Iterates the log items and adds them to the table.
		for (int i = 0; i < log.size(); i++) {
			String[] row = new String[7]; // Row of the table data.
			LogItem item = log.get(i); // LogItem currently being interpreted as a String[] for the table.
			
			// If there was a failed action, notify MainUI to display a pop up.
			if (item.getAction().equals("FAIL")) {
				MainUI.getInstance().showWarning(
					item.getStrategy() + " could not be applied to Trading Client " + item.getTrader()
					+ ".\nPlease ensure the Coin List contains all the coins needed for the strategy and is free of errors."
				);
			}
			
			row[0] = item.getTrader();
			row[1] = item.getStrategy();
			row[2] = item.getCoin();
			row[3] = item.getAction();
			
			// Quantity of 0 indicates a failed action. Quantity and price should read "Null".
			if (item.getQuantity() == 0) {
				row[4] = "Null";
				row[5] = "Null";
			} else {
				row[4] = String.valueOf(item.getQuantity());
				row[5] = String.format("%.2f", item.getPrice());
			}
			
			// Builds the date String from the Date object.
			cal.setTime(item.getDate());
			String dateStr = ""; // Date as a String
			dateStr += cal.get(Calendar.DAY_OF_MONTH);
			dateStr += "-" + cal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
			dateStr += "-" + cal.get(Calendar.YEAR);
			row[6] = dateStr;
			
			dtm.addRow(row); // Add the new row to the table data.
		}
		
		MainUI.getInstance().updateWest(scrollPane); // Adds the new table to the UI.
	}
}
