package cryptoTrader.utils;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 * Represents the log of new trader actions.
 * Subject to observers DisplayTable and DisplayHistogram. Notifies observers when the log is changed.
 */
public class TraderActionLog extends Subject {
	
	private static TraderActionLog instance; // Single instance of TraderActionLog.
	private ArrayList<LogItem> log = new ArrayList<LogItem>(); // List of new log items to be added to the table and histogram on the left

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
		log = newLog;
		notifyObservers(); // Notifies observers to create the UI log table and histogram.
	}

	/**
	 * Returns the log.
	 * @return list of log items
	 */
	public ArrayList<LogItem> getLog() {
		return log;
	}
	
	public void writeToCSV(TraderActionLog currInstance) {
		
		ArrayList<LogItem> exportLog = currInstance.getLog();
		PrintWriter writer;
		try {
			writer = new PrintWriter ("TradingLog.csv");
			
			
			StringBuilder line = new StringBuilder();
			line.append("Trader,Strategy,CryptoCoin,Action,Quantity,Price,Date\n");
			for (LogItem currLog : exportLog) {
				
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
				line.append(currLog.getPrice());
				line.append(",");
				line.append(currLog.getDate());
				line.append("\n");
				
				System.out.println(line);
			}
			
			writer.write(line.toString());
			writer.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
}