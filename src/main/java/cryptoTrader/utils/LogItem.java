package cryptoTrader.utils;

import java.util.Date;

public class LogItem {

    private String trader;
    private String strategy;
    private String coin;
    private String action;
    private int quantity;
    private double price;
    private Date date;

    public LogItem(){
        this.strategy = "";
        this.trader = "";
        this.coin = "";
        this.action = "";
        this.quantity = 0;
        this.price = 0;
        this.date = new Date();
    }

    public LogItem(String strategy, String coin, String action, int quantity, double price, Date date){
    	this.strategy = strategy;
        this.coin = coin;
        this.action = action;
        this.quantity = quantity;
        this.price = price;
        this.date = date;
    }

    public String getTrader() {
        return trader;
    }

    public void setTrader(String trader) {
        this.trader = trader;
    }

    public String getStrategy() {
        return strategy;
    }

    public void setStrategy(String strategy) {
        this.strategy = strategy;
    }

    public String getCoin() {
        return coin;
    }

    public void setCoin(String coin) {
        this.coin = coin;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String toString(){
        String toReturn = "Trader: " + trader + "\n Strategy: " + strategy + "\n" + coin + " " + action + " " + price + " " + date.toString();

        return toReturn;
    }
}
