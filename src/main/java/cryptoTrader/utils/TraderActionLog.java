package cryptoTrader.utils;

import java.util.ArrayList;

public class TraderActionLog extends Subject{
	
	private static TraderActionLog instance;
	private ArrayList<LogItem> log = new ArrayList<LogItem>(); // List of log items to be displayed in a table and histogram on the left

	public static TraderActionLog getInstance() {
		if (instance == null) instance = new TraderActionLog();
		return instance;
	}
	
	/**
	 * Add newLogs to the log.
	 * @param newLogs list of new log items
	 */
	public void updateLog(ArrayList<LogItem> newLogs) {
		log.addAll(newLogs);
		notifyObservers();
	}

	
	/**
	 * Returns the log.
	 */
	public ArrayList<LogItem> getLog() {
		return log;
	}
	
	
}