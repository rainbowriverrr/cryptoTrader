package cryptoTrader.utils;

import java.util.ArrayList;

public class Trader {

    private static AvailableCryptoList coinsAvailable;

    public static void performTrades(ArrayList<TradingClient> clients){

        coinsAvailable = AvailableCryptoList.getInstance();
        coinsAvailable.call();

        String[] availableCoinsList = coinsAvailable.getAvailableCryptos();

        ArrayList<LogItem> logs;

        for(TradingClient client : clients) {
            String[] coins = client.getCryptoCoins();
            for(String coin : coins){

            }
        }
    }
}
