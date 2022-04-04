package cryptoTrader.utils;

import java.util.Dictionary;

public abstract class Strategy {
	
	private String strategyName;

	/**
	 * Constructs a strategy with a name provided
	 * @param strategyName
	 */
	public Strategy (String strategyName) {
		this.strategyName = strategyName;
	}

	/**
	 * Takes the input of a dictionary of coin and coin price pairs in the form of String and double respectively.
	 * Returns a LogItem with the result of the strategy computation.  The brokerName field is not filled out.  This
	 * is to be filled out by the TradingClient.  Returns an error log if the strategy does not have the required coins.
	 * The error log is depicted by "FAIL" in the action field.
	 * @param requestedCoins Dictionary of String, double pairs.  The String being the coin symbol, and the double
	 *                       being the coin price.
	 * @return LogItem with result of strategy logic.
	 */
	protected LogItem performTrade(Dictionary requestedCoins) {
		return null;
	}

	public String getName(){
		return strategyName;
	}

}
