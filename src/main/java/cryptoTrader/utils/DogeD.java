package cryptoTrader.utils;

import java.util.Date;
import java.util.Dictionary;

public class DogeD extends Strategy{

    private String strategyName = "Doge D";

    public DogeD(String strategyName) {
        super(strategyName);
    }

    @Override
    public LogItem performTrade(Dictionary requestedCoins) {

        Object dogeValue = requestedCoins.get("DOGE");
        Date today = new Date();

        if (dogeValue == null) {
            return new LogItem(strategyName, "DOGE", "FAIL", 0, 0, today);
        }

        double dogePrice = (double) dogeValue;
        LogItem toReturn;
        if(dogePrice < 0.2){
            toReturn = new LogItem(strategyName, "DOGE", "Buy", 100, dogePrice, today);
        } else if (dogePrice < 0.4) {
            toReturn = new LogItem(strategyName, "DOGE", "Sell", 50, dogePrice, today);
        } else {
            toReturn = new LogItem(strategyName, "DOGE", "Sell", 100, dogePrice, today);
        }

        return toReturn;
    }
}
