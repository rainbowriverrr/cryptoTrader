package cryptoTrader.utils;

import java.util.ArrayList;

public class TradingClient {
	
	private String brokerName;
	private String[] cryptoCoins;
	private Strategy traderStrategy;
	
	
	public TradingClient (String brokerName, String coinNames, String traderStrategy) {

        String[] coins = coinNames.split(",");
        for (String coin : coins) coin = coin.trim();
        Strategy strat = new StrategyA(traderStrategy); // TODO change to use StrategyFactory

        this.brokerName = brokerName;
        this.cryptoCoins = coins;
        this.traderStrategy = strat;
        
    }
	

	public static void main(String[] args) {
		// TODO Auto-generated method stub

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
