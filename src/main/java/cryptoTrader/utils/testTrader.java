package cryptoTrader.utils;

import java.util.ArrayList;

public class testTrader {

    public static void main(String[] args){
        System.out.println("Testing Trader...");

        String[] coins = new String[2];
        coins[0] = "AZAEOI";
        coins[1] = "BTC";

        String[] coins2 = new String[1];
        coins2[0] = "ETH";

        TradingClient testClient = new TradingClient("Test", "AZAEOI, BTC", "StrategyA");
        TradingClient testClient2 = new TradingClient("Test", "ETH, DOGE", "StrategyB");

        ArrayList<TradingClient> clients = new ArrayList<TradingClient>();
        //clients.add(testClient);
        //clients.add(testClient2);

        Trader.performTrades(clients);
    }

}
