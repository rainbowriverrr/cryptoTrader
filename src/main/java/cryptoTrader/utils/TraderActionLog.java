package cryptoTrader.utils;

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
	
}