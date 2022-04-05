package cryptoTrader.utils;

import java.util.Date;
import java.util.Dictionary;

public class EthBtcC extends Strategy{


    public EthBtcC(String strategyName) {
        super(strategyName);
    }

    @Override
    protected LogItem performTrade(Dictionary<String, Double> requestedCoins) {

        Object ethValue = requestedCoins.get("ETH");
        Object btcValue = requestedCoins.get("BTC");
        Date today = new Date();

        if (ethValue == null) {
            return new LogItem(super.getName(), "ETH", "FAIL", 0, 0, today);
        }

        if (btcValue == null){
            return new LogItem(super.getName(), "BTC", "FAIL", 0, 0, today);
        }

        double btcPrice = (double) btcValue;
        double ethPrice = (double) ethValue;
        LogItem toReturn;
        if(btcPrice > 45000 && ethPrice < 4000){
            toReturn = new LogItem(super.getName(), "ETH", "Buy", 10, ethPrice, today);
        } else {
            toReturn = new LogItem(super.getName(), "BTC", "Sell", 1, btcPrice, today);
        }

        return toReturn;
    }
}
