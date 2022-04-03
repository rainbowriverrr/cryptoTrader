package cryptoTrader.utils;

import java.util.ArrayList;

public class testTrader {

    public static void main(String[] args){
        System.out.println("Testing Trader...");


        TradingClient testClient = new TradingClient("Test", "AZAEOI, BTC", "StrategyA");
        TradingClient testClient2 = new TradingClient("Test2", "ADA, BTC", "StrategyB");

        ArrayList<TradingClient> clients = new ArrayList<TradingClient>();
        clients.add(testClient);
        clients.add(testClient2);

        Trader.performTrades(clients);
    }

}
