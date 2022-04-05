package cryptoTrader.utils;

import java.util.Date;
import java.util.Dictionary;

public class StrategyA extends Strategy {
	
	public StrategyA(String strategyName) {
		super(strategyName);

	}
	
	protected LogItem performTrade(Dictionary<String, Double> requestedCoins) {
		
		String strategyCoin = "BTC";
		
		Object coinValue = requestedCoins.get(strategyCoin);
		
		Date today = new Date();
		
		
		if (coinValue == null) {
			return new LogItem(super.getName(), strategyCoin, "FAIL", 0, 0, today);
		}
		
		
		double bitcoinPrice = (double) coinValue;

		LogItem currLogItem;
		if (bitcoinPrice < 59000) {

			currLogItem = new LogItem(super.getName(), strategyCoin, "Buy", 5, bitcoinPrice, today);

		} else {
			currLogItem = new LogItem(super.getName(), strategyCoin, "Sell", 5, bitcoinPrice, today);
		}
		return currLogItem;

	}

}
