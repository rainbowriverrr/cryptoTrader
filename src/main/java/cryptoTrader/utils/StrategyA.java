package cryptoTrader.utils;

import java.util.Date;
import java.util.Dictionary;
import java.util.Hashtable;

public class StrategyA extends Strategy {
	
	private String strategyName = "BTC A";
	
	public StrategyA(String strategyName) {
		
		super(strategyName);

	}
	
	public LogItem performTrade(Dictionary requestedCoins) {
		
		String strategyCoin = "BTC";
		
		Object coinValue = requestedCoins.get(strategyCoin);
		
		Date today = new Date();
		
		
		if (coinValue == null) {
			return new LogItem(strategyName, strategyCoin, "FAIL", 0, 0, today);
		}
		
		
		double bitcoinPrice = (double) coinValue;

		LogItem currLogItem;
		if (bitcoinPrice < 59000) {

			currLogItem = new LogItem(strategyName, strategyCoin, "Buy", 5, bitcoinPrice, today);

		} else {
			currLogItem = new LogItem(strategyName, strategyCoin, "Sell", 5, bitcoinPrice, today);
		}
		return currLogItem;

	}

}
