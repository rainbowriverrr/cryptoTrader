package cryptoTrader.utils;

import java.util.Date;
import java.util.Dictionary;

public class EthBtcC extends Strategy{

    private String strategyName = "ETH C";

    public EthBtcC(String strategyName) {
        super(strategyName);
    }

    @Override
    public LogItem performTrade(Dictionary requestedCoins) {

        Object ethValue = requestedCoins.get("ETH");
        Object btcValue = requestedCoins.get("BTC");
        Date today = new Date();

        if (ethValue == null) {
            return new LogItem(strategyName, "ETH", "FAIL", 0, 0, today);
        }

        if (btcValue == null){
            return new LogItem(strategyName, "BTC", "FAIL", 0, 0, today);
        }

        double btcPrice = (double) btcValue;
        double ethPrice = (double) ethValue;
        LogItem toReturn;
        if(btcPrice > 45000 && ethPrice < 4000){
            toReturn = new LogItem(strategyName, "ETH", "Buy", 10, ethPrice, today);
        } else {
            toReturn = new LogItem(strategyName, "BTC", "Sell", 1, btcPrice, today);
        }

        return toReturn;
    }
}
