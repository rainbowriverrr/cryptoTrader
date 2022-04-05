package cryptoTrader.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Dictionary;
import java.util.Hashtable;

import cryptoTrader.gui.TraderActionLog;

/**
 * This class provides the performTrades() method which initiates the creation of log items.  This utilizes
 * the DataFetcher and AvailableCryptoList to obtain the nessecary information that will be pushed to the
 * TradingClients and Strategies.
 * @author rainbowriverrr
 */

public class Trader {

    private static AvailableCryptoList coinsAvailable;
    private static DataFetcher fetcher;
    private static String pattern = "dd-MM-yyyy";

    /**
     * Returns an ArrayList of LogItems which contains both successful and failed trades.  This is static class
     * that is called by front end components which initiates the creation of trading logs by pushing a dictionary
     * of coin and coin price pairs to TradingClients.  After all TradingClients return their logs, the Trader
     * updates the TraderActionLog with an ArrayList of log items.
     * @param clients an Arraylist of TradingClient objects
     * @see TradingClient
     */
    public static void performTrades(ArrayList<TradingClient> clients){

        //Updates data fetcher and MainUI instances
        if(fetcher == null){
            fetcher = new DataFetcher();
        }


        DateFormat df = new SimpleDateFormat(pattern);
        Date today = new Date();
        String todayString = df.format(today);

        coinsAvailable = AvailableCryptoList.getInstance();
        String[] availableCoinsList = coinsAvailable.getAvailableCryptos();

        ArrayList<LogItem> logs = new ArrayList<>();

        //Iterates through clients and sends requested coin price data to them as a dictionary
        for(TradingClient client : clients) {
            String[] coins = client.getCryptoCoins();
            Dictionary<String, Double> requestedCoins = new Hashtable<String, Double>();

            for (String coin : coins) {
                if (searchList(availableCoinsList, coin)) {

                    double price = fetcher.getPriceForCoin(coinsAvailable.getCryptoID(coin.toLowerCase()), todayString);

                    //Adds a coin price pair to the client's requested coin list
                    requestedCoins.put(coin, price);

                } /* else {
                    LogItem newLog = new LogItem();
                    newLog.setCoin(coin);
                    newLog.setTrader(client.getBrokerName());
                    newLog.setStrategy(client.getTraderStrategy().getName());
                    newLog.setAction("FAIL");
                    newLog.setQuantity(0);
                    newLog.setPrice(0);

                    logs.add(newLog);

                    System.out.println("Invalid Coin " + coin + " for broker " + client.getBrokerName());
                }*/
            }

            logs.add(client.trade(requestedCoins));

        }
        for( LogItem log : logs) {
            System.out.println(log.toString());
        }
        TraderActionLog.getInstance().updateLog(logs);
    }


    /**
     * Returns true if coin is in the list coinList, otherwise, returns false
     * Case does not matter
     * @param coinList List of coins to search validity from
     * @param coin The coin whose validity is being tested
     * @return boolean, true if the coin is valid, false if it is not valid
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
