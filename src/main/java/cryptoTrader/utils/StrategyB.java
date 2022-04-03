package cryptoTrader.utils;

import java.util.Date;
import java.util.Dictionary;
import java.util.Hashtable;

public class StrategyB extends Strategy {
	
	private String strategyName = "StrategyB";
	
	public StrategyB() {
		super();
	}
	
public LogItem performTrade(Dictionary coinPrices) {
		
		String strategyCoin = "ADA";
		
		Object coinValue = coinPrices.get(strategyCoin);
		
		Date today = new Date();
		
		
		if (coinValue == null) {
			LogItem errorLog = new LogItem(strategyName, "ADA", "FAIL", 0, 0, today);
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

