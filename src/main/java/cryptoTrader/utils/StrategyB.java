package cryptoTrader.utils;

import java.util.Date;
import java.util.Hashtable;

public class StrategyB extends Strategy {
	
	private String strategyName;
	
	public StrategyB(String strategyName) {
		super(strategyName);
	}
	
public LogItem performTrade(Hashtable<String, Integer> coinPrices) {
		
		String strategyCoin = "ADA";
		
		Object coinValue = coinPrices.get(strategyCoin);
		
		Date today = new Date();
		
		
		if (coinValue == null) {
			LogItem errorLog = new LogItem(strategyName, "ADA", "ERROR", 0, 0, today);
			return errorLog;
		}
		
		
		double cardanoPrice = (double) coinValue;
		
		if (cardanoPrice < 2) {
			
			LogItem currLogItem = new LogItem(strategyName,"ADA", "Buy", 10, cardanoPrice, today);
			return currLogItem;
			
		}  else {
			LogItem currLogItem = new LogItem(strategyName, "ADA", "Sell", 10, cardanoPrice, today);
			return currLogItem;
		}
		
	}

}

