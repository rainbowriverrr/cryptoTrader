package cryptoTrader.utils;

import java.util.Dictionary;
import java.util.Hashtable;

public abstract class Strategy {
	
	private String strategyName;
	
	public Strategy (String strategyName) {
		this.strategyName = strategyName;
	}
	
	public LogItem performTrade(Dictionary requestedCoins) {
		return null;
	}

}
