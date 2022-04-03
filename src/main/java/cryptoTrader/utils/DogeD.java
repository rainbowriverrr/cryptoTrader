package cryptoTrader.utils;

import java.util.Date;
import java.util.Dictionary;

public class DogeD extends Strategy{

    public DogeD(String strategyName) {
        super(strategyName);
    }

    @Override
    public LogItem performTrade(Dictionary requestedCoins) {

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
