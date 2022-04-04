package cryptoTrader.utils;

import java.util.Date;
import java.util.Dictionary;
import java.util.Hashtable;

public class StrategyB extends Strategy {
	
	public StrategyB(String strategyName) {
		super(strategyName);
	}
	
	protected LogItem performTrade(Dictionary coinPrices) {
		
		String strategyCoin = "ADA";
		
		Object coinValue = coinPrices.get(strategyCoin);
		
		Date today = new Date();
		
		
		if (coinValue == null) {
			return new LogItem(super.getName(), strategyCoin, "FAIL", 0, 0, today);
		}
		
		
		double cardanoPrice = (double) coinValue;

		LogItem currLogItem;
		if (cardanoPrice < 2) {

			currLogItem = new LogItem(super.getName(), strategyCoin, "Buy", 10, cardanoPrice, today);

		}  else {
			currLogItem = new LogItem(super.getName(), strategyCoin, "Sell", 10, cardanoPrice, today);
		}
		return currLogItem;

	}

}

