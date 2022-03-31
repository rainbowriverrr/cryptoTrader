package cryptoTrader.utils;

import java.util.Date;
import java.util.Hashtable;

public class StrategyA extends Strategy {
	
	private String strategyName;
	
	public StrategyA(String strategyName) {
		
		super(strategyName);
		// TODO Auto-generated constructor stub
	}
	
	public LogItem performTrade(Hashtable<String, Integer> coinPrices) {
		
		String strategyCoin = "BTC";
		
		Object coinValue = coinPrices.get(strategyCoin);
		
		Date today = new Date();
		
		
		if (coinValue == null) {
			LogItem errorLog = new LogItem(strategyName, "BTC", "ERROR", 0, 0, today);
			return errorLog;
		}
		
		
		double bitcoinPrice = (double) coinValue;
		
		if (bitcoinPrice < 59000) {
			
			LogItem currLogItem = new LogItem(strategyName,"BTC", "Buy", 5, bitcoinPrice, today);
			return currLogItem;
			
		}
		return null;
		
	}

}
