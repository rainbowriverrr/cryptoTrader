package cryptoTrader.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Calendar;

import java.awt.Dimension;
import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;

import cryptoTrader.gui.MainUI;

/**
 * Used to generate the UI table of log items.
 */
public class DisplayTable implements Observer {
	
	private TraderActionLog subject; // DisplayTable is observer to TraderActionLog.

	/**
	 * Construct a DisplayTable that observes the given subject.
	 * @param subject the TraderActionLog to observe
	 */
	public DisplayTable(TraderActionLog subject) {
		this.subject = subject;
		subject.attach(this);
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
		
		Object[] columnNames = {"Trader","Strategy","CryptoCoin","Action","Quantity","Price","Date"}; // Names of the table columns.
		
		//ArrayList<LogItem> log = TraderActionLog.getInstance().getLog(); TODO put back
		
		// Temporary fake log for testing. TODO remove
		ArrayList<LogItem> log = new ArrayList<LogItem>();
		LogItem i1 = new LogItem("Strategy-A", "BTC", "Buy", 200, 101.60, new Date());
		i1.setTrader("Bob");
		LogItem i2 = new LogItem("Strategy-B", "ETH", "Sell", 300, 69.69, new Date());
		i2.setTrader("Sally");
		LogItem i3 = new LogItem("Strategy-C", "DOGE", "Buy", 150, 94.22, new Date());
		i3.setTrader("Jimmy");
		log.add(i1);
		log.add(i2);
		log.add(i3);
		
		Object[][] data = new String[log.size()][7]; // Data of the table. Row for each log item, 7 columns.
		Calendar cal = Calendar.getInstance(); // Calendar instance to interpret the log Date.
		// Iterates the log items and adds them to the table.
		for (int i = 0; i < log.size(); i++) {
			String[] row = new String[7]; // Row of the table data.
			LogItem item = log.get(i); // LogItem currently being interpreted as a String[] for the table.
			row[0] = item.getTrader();
			row[1] = item.getStrategy();
			row[2] = item.getCoin();
			row[3] = item.getAction();
			row[4] = String.valueOf(item.getQuantity()); // int to String
			row[5] = String.valueOf(item.getPrice()); // double to String
			
			// Builds the date String from the Date object.
			cal.setTime(item.getDate());
			String dateStr = ""; // Date as a String
			dateStr += cal.get(Calendar.DAY_OF_MONTH);
			dateStr += "-" + cal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
			dateStr += "-" + cal.get(Calendar.YEAR);
			row[6] = dateStr;
			
			data[i] = row; // Add the new row to the table data.
		}
		
		JTable table = new JTable(data, columnNames); // The UI table of log items.
		table.setEnabled(false); // Prevents user from editing the table since it is only for displaying data.
		table.setGridColor(Color.LIGHT_GRAY);
		table.setFillsViewportHeight(true);
		
		// Table is put in a scroll pane.
		JScrollPane tablePane = new JScrollPane(table);
		tablePane.setBorder (BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),
				"Trader Actions",
				TitledBorder.CENTER,
				TitledBorder.TOP));
		tablePane.setPreferredSize(new Dimension(600, 300));
		
		MainUI.getInstance().updateLogTable(tablePane); // Adds the new table to the UI.
	}
}
