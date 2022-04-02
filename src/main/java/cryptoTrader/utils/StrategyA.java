package cryptoTrader.utils;

import java.util.Date;
import java.util.Dictionary;
import java.util.Hashtable;

public class StrategyA extends Strategy {
	
	private String strategyName;
	
	public StrategyA(String strategyName) {
		
		super(strategyName);
		// TODO Auto-generated constructor stub
	}
	
	public LogItem performTrade(Dictionary requestedCoins) {
		
		String strategyCoin = "btc";
		
		Object coinValue = requestedCoins.get(strategyCoin);
		
		Date today = new Date();
		
		
		if (coinValue == null) {
			LogItem errorLog = new LogItem(strategyName, "BTC", "ERROR", 0, 0, today);
			return errorLog;
		}
		
		
		double bitcoinPrice = (double) coinValue;
		
		if (bitcoinPrice < 59000) {
			
			LogItem currLogItem = new LogItem(strategyName,"BTC", "Buy", 5, bitcoinPrice, today);
			return currLogItem;
			
		} else {
			LogItem currLogItem = new LogItem(strategyName,"BTC", "Sell", 5, bitcoinPrice, today);
			return currLogItem;
		}
		
	}

}
