package cryptoTrader.utils;

import java.util.Date;
import java.util.Dictionary;

public class DogeD extends Strategy{

    public DogeD(String strategyName) {
        super(strategyName);
    }

    @Override
    /**
     * Takes the input of a dictionary of coin and coin price pairs in the form of String and double respectively.
     * Returns a LogItem with the result of the strategy computation.  The brokerName field is not filled out.  This
     * is to be filled out by the TradingClient.  Returns an error log if the strategy does not have the required coins.
     * The error log is depicted by "FAIL" in the action field.
     *
     * This strategy requires DOGE as a valid symbol within the dictionary
     *
     * @param requestedCoins Dictionary of String, double pairs.  The String being the coin symbol, and the double
     *                       being the coin price.
     * @return LogItem with result of strategy logic.
     */
    protected LogItem performTrade(Dictionary<String, Double> requestedCoins) {

        Object dogeValue = requestedCoins.get("DOGE");
        Date today = new Date();

        if (dogeValue == null) {
            return new LogItem(super.getName(), "DOGE", "FAIL", 0, 0, today);
        }

        double dogePrice = (double) dogeValue;
        LogItem toReturn;
        if(dogePrice < 0.2){
            toReturn = new LogItem(super.getName(), "DOGE", "Buy", 100, dogePrice, today);
        } else if (dogePrice < 0.4) {
            toReturn = new LogItem(super.getName(), "DOGE", "Sell", 50, dogePrice, today);
        } else {
            toReturn = new LogItem(super.getName(), "DOGE", "Sell", 100, dogePrice, today);
        }

        return toReturn;
    }
}
