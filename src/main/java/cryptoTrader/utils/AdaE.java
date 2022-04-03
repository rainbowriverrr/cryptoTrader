package cryptoTrader.utils;

import java.util.Date;
import java.util.Dictionary;

public class AdaE extends Strategy{

    private String strategyName = "ADA D";

    public AdaE(String strategyName) {
        super(strategyName);
    }

    @Override
    public LogItem performTrade(Dictionary requestedCoins) {

        Object adaValue = requestedCoins.get("ADA");
        Date today = new Date();

        if (adaValue == null) {
            return new LogItem(strategyName, "ADA", "FAIL", 0, 0, today);
        }

        double adaPrice = (double) adaValue;
        LogItem toReturn;
        if(adaPrice < 1.5){
            toReturn = new LogItem(strategyName, "ADA", "Buy", 100, adaPrice, today);
        } else if (adaPrice < 1.6) {
            toReturn = new LogItem(strategyName, "ADA", "Buy", 50, adaPrice, today);
        } else {
            toReturn = new LogItem(strategyName, "ADA", "Sell", 50, adaPrice, today);
        }

        return toReturn;
    }
}
