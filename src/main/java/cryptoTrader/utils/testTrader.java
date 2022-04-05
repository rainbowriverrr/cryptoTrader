package cryptoTrader.utils;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;

public class testTrader {

    public static <HashTable> void main(String[] args){
        System.out.println("Testing Trader...");

        TradingClient testClient = new TradingClient("Test", "DOGE, BTC", "StrategyA");
        TradingClient testClient2 = new TradingClient("Test2", "ADA, ETH", "StrategyB");

        ArrayList<TradingClient> clients = new ArrayList<TradingClient>();
        clients.add(testClient);
        clients.add(testClient2);

        Trader.performTrades(clients);

        Strategy testStrategy = new EthBtcC("Testing");
        Dictionary<String, Double> requestedCoins = new Hashtable<String, Double>();
        requestedCoins.put("BTC", 50000.00);
        requestedCoins.put("ETH", 4500.00);
        LogItem testLog = testStrategy.performTrade(requestedCoins);
        if (testLog.getAction().equals("Sell") && testLog.getCoin().equals("BTC") && testLog.getQuantity() == 1){
            System.out.println("Strategy test 1 Passed");
        }

        requestedCoins.remove("BTC");
        requestedCoins.remove("ETH");
        testLog = testStrategy.performTrade(requestedCoins);
        if(testLog.getAction().equals("FAIL")){
            System.out.println("Strategy test 2 Passed");
        }
    }

}
