package cryptoTrader.utils;

import java.util.ArrayList;
import java.util.Dictionary;

public class TradingClient {
	
	private String brokerName;
	private String[] cryptoCoins;
	private Strategy traderStrategy;
	
	
	public TradingClient (String brokerName, String coinNames, String traderStrategy) {

		String[] coins = coinNames.split(",");
		for (String coin : coins) coin = coin.trim();
		Strategy strat = StrategyFactory.create(traderStrategy); // TODO change to use StrategyFactory

		this.brokerName = brokerName;
		this.cryptoCoins = coins;
		this.traderStrategy = strat;
		
	}

	public LogItem trade(Dictionary requestedCoins){

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
