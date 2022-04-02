package cryptoTrader.utils;

import java.util.Dictionary;
import java.util.Hashtable;

public abstract class Strategy {
	
	private String strategyName;
	
	public Strategy (String strategyName) {
		
		this.strategyName = strategyName;
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
	public LogItem performTrade(Dictionary requestedCoins) {
		return null;
		
		
	}

}
