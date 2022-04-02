package cryptoTrader.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Dictionary;
import java.util.Hashtable;

import cryptoTrader.gui.MainUI;

public class Trader {

    private static AvailableCryptoList coinsAvailable;
    private static DataFetcher fetcher;
    private static String pattern = "dd-MM-yyyy";
    private static MainUI ui;

    public static void performTrades(ArrayList<TradingClient> clients){

        //Updates data fetcher and MainUI instances
        if(fetcher == null){
            fetcher = new DataFetcher();
        }
        ui = MainUI.getInstance();


        DateFormat df = new SimpleDateFormat(pattern);
        Date today = new Date();
        String todayString = df.format(today);

        coinsAvailable = AvailableCryptoList.getInstance();
        String[] availableCoinsList = coinsAvailable.getAvailableCryptos();

        ArrayList<LogItem> logs = new ArrayList<>();

        //Iterates through clients and sends requested coin price data to them as a dictionary
        for(TradingClient client : clients) {
            String[] coins = client.getCryptoCoins();
            Dictionary requestedCoins = new Hashtable();

            for (String coin : coins) {
                if (searchList(availableCoinsList, coin)) {

                    double price = fetcher.getPriceForCoin(coinsAvailable.getCryptoID(coin.toLowerCase()), todayString);
                    System.out.println(price);

                    requestedCoins.put(coin, price);

                } else {
                    LogItem newLog = new LogItem();
                    newLog.setCoin(coin);
                    newLog.setTrader(client.getBrokerName());
                    newLog.setStrategy("N/A");
                    newLog.setAction("FAIL");
                    newLog.setQuantity(0);
                    newLog.setPrice(0);

                    logs.add(newLog);

                    System.out.println("Invalid Coin " + coin + " for broker " + client.getBrokerName());
                }
            }
            System.out.println(requestedCoins);
            //TODO: ui.updateLog(logs)
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
