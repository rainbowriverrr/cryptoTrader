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
                if(searchList(availableCoinsList, coin)){

                } else {
                    LogItem newLog = new LogItem();
                    newLog.setCoin(coin);
                    newLog.setTrader(client.getBrokerName());
                }
            }
        }
    }


    /*
    Returns true if coin is in the list coinList, otherwise, returns false
    Case does not matter
     */
    private static boolean searchList(String[] coinList, String coin){

        for(String listItem : coinList){
            if (listItem.toLowerCase().equals(coin.toLowerCase())){
                return true;
            }
        }

        return false;
    }
}
