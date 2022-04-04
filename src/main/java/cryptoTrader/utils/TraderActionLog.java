package cryptoTrader.utils;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

/**
 * Represents the log of new trader actions.
 * Subject to observers DisplayTable and DisplayHistogram. Notifies observers when the log is changed.
 */
public class TraderActionLog extends Subject {
	
	private static TraderActionLog instance; // Single instance of TraderActionLog.
	private ArrayList<LogItem> log = new ArrayList<LogItem>(); // List of all logs
	private ArrayList<LogItem> newLogs = new ArrayList<LogItem>(); // List of new log items to be added to the table and histogram on the left

	/**
	 * Gets the single instance of TraderActionLog.
	 * @return instance of TraderActionLog
	 */
	public static TraderActionLog getInstance() {
		if (instance == null) instance = new TraderActionLog();
		return instance;
	}
	
	/**
	 * Replaces log with newLog.
	 * @param newLogs list of new log items
	 */
	public void updateLog(ArrayList<LogItem> newLog) {
		newLogs = newLog;
		log.addAll(newLog);
		notifyObservers(); // Notifies observers to create the UI log table and histogram.
	}
	
	/**
	 * Returns all the logs.
	 * @return list of log items
	 */
	public ArrayList<LogItem> getLog() {
		return log;
	}

	/**
	 * Returns the new logs.
	 * @return list of new log items
	 */
	public ArrayList<LogItem> getNewLogs() {
		return newLogs;
	}
	
	public void writeToCSV() {
		PrintWriter writer;
		try {
			writer = new PrintWriter (System.getProperty("user.home") + System.getProperty("file.separator") + "TradingLog.csv");
			
			StringBuilder line = new StringBuilder();
			line.append("Trader,Strategy,CryptoCoin,Action,Quantity,Price,Date\n");
			for (LogItem currLog : log) {
				
				line.append(currLog.getTrader());
				line.append(",");
				line.append(currLog.getStrategy());
				line.append(",");
				line.append(currLog.getCoin());
				line.append(",");
				line.append(currLog.getAction());
				line.append(",");
				line.append(currLog.getQuantity());
				line.append(",");
				line.append(String.format("%.2f", currLog.getPrice()));
				line.append(",");
				
				// Builds the date String from the Date object.
				Calendar cal = Calendar.getInstance();
				cal.setTime(currLog.getDate());
				String dateStr = ""; // Date as a String
				dateStr += cal.get(Calendar.DAY_OF_MONTH);
				dateStr += "-" + cal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
				dateStr += "-" + cal.get(Calendar.YEAR);
				line.append(dateStr);
				line.append("\n");

			}
			
			writer.write(line.toString());
			writer.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
}