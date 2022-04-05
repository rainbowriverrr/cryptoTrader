package cryptoTrader.utils;

import java.util.Dictionary;

/**
 * @author rainbowriverrr
 */
public class TradingClient {
	
	private String brokerName;
	private String[] cryptoCoins;
	private Strategy traderStrategy;

	/**
	 * This initializes the object
	 * @param brokerName A string that will be set as the name of the TradingClient
	 * @param coinNames A comma separated string of coin symbols
	 * @param traderStrategy A string name of the strategy that will be used by a factory class
	 * @see StrategyFactory
	 */
	public TradingClient (String brokerName, String coinNames, String traderStrategy) {

		String[] coins = coinNames.split(",");
		for (int i = 0; i < coins.length; i++)
			coins[i] = coins[i].trim().toUpperCase();
		Strategy strat = StrategyFactory.create(traderStrategy); // TODO change to use StrategyFactory

		this.brokerName = brokerName;
		this.cryptoCoins = coins;
		this.traderStrategy = strat;
		
	}

	/**
	 * Pushes a dictionary of coin, price pairs to the Strategy associated with the TradingClient.
	 * Takes the LogItem return from the Strategy, adds its brokerName to the LogItem, then returns it.
	 * @param requestedCoins
	 * @return LogItem with completed information
	 * @see Strategy
	 * @see Trader
	 */
	protected LogItem trade(Dictionary<String, Double> requestedCoins){

		LogItem toReturn = traderStrategy.performTrade(requestedCoins);

		toReturn.setTrader(brokerName);

		return toReturn;
	}

	public String getBrokerName() {
		return this.brokerName;
	}


	public void setBrokerName(String brokerName) {
		this.brokerName = brokerName;
	}


	public String[] getCryptoCoins() {
		return this.cryptoCoins;
	}


	public void setCryptoCoins(String[] cryptoCoins) {
		this.cryptoCoins = cryptoCoins;
	}


	public Strategy getTraderStrategy() {
		return traderStrategy;
	}


	public void setTraderStrategy(Strategy traderStrategy) {
		this.traderStrategy = traderStrategy;
	}

}
