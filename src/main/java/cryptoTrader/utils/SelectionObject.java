package cryptoTrader.utils;

import java.util.ArrayList;

public class SelectionObject {
	
	
	public ArrayList<String> brokers;
	public ArrayList<String[]> coins;
	public ArrayList<String> strategies;
	
	public SelectionObject() {
		this.brokers = new ArrayList<String>();
		this.coins = new ArrayList<String[]>();
		this.strategies = new ArrayList<String>();
		
		
	}
	public void addToSelectionObject (String brokerName, String[] string, String strategies) {
		
		this.brokers.add(brokerName);
		this.coins.add(string);
		this.strategies.add(strategies);
		
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
