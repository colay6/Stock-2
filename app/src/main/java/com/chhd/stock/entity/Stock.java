package com.chhd.stock.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by CWQ on 2016/7/10.
 */
public class Stock implements Parcelable {

    private String name;

    private String code;

    private String date;

    private String time;

    private double OpenningPrice;

    private double closingPrice;

    private double currentPrice;

    private double hPrice;

    private double lPrice;

    private double competitivePrice;

    private double auctionPrice;

    private int totalNumber;

    private long turnover;

    private double increase;

    private int buyOne;

    private double buyOnePrice;

    private int buyTwo;

    private double buyTwoPrice;

    private int buyThree;

    private double buyThreePrice;

    private int buyFour;

    private double buyFourPrice;

    private int buyFive;

    private double buyFivePrice;

    private int sellOne;

    private double sellOnePrice;

    private int sellTwo;

    private double sellTwoPrice;

    private int sellThree;

    private double sellThreePrice;

    private int sellFour;

    private double sellFourPrice;

    private int sellFive;

    private double sellFivePrice;

    private String minurl;

    private String dayurl;

    private String weekurl;

    private String monthurl;

    public Stock() {
    }

    public Stock(String name, double currentPrice, double increase) {
        this.name = name;
        this.currentPrice = currentPrice;
        this.increase = increase;
    }

    public Stock(String name, String code, String date, String time, double openningPrice, double closingPrice, double currentPrice, double hPrice, double lPrice, double competitivePrice, double auctionPrice, int totalNumber, long turnover, double increase, int buyOne, double buyOnePrice, int buyTwo, double buyTwoPrice, int buyThree, double buyThreePrice, int buyFour, double buyFourPrice, int buyFive, double buyFivePrice, int sellOne, double sellOnePrice, int sellTwo, double sellTwoPrice, int sellThree, double sellThreePrice, int sellFour, double sellFourPrice, int sellFive, double sellFivePrice, String minurl, String dayurl, String weekurl, String monthurl) {
        this.name = name;
        this.code = code;
        this.date = date;
        this.time = time;
        OpenningPrice = openningPrice;
        this.closingPrice = closingPrice;
        this.currentPrice = currentPrice;
        this.hPrice = hPrice;
        this.lPrice = lPrice;
        this.competitivePrice = competitivePrice;
        this.auctionPrice = auctionPrice;
        this.totalNumber = totalNumber;
        this.turnover = turnover;
        this.increase = increase;
        this.buyOne = buyOne;
        this.buyOnePrice = buyOnePrice;
        this.buyTwo = buyTwo;
        this.buyTwoPrice = buyTwoPrice;
        this.buyThree = buyThree;
        this.buyThreePrice = buyThreePrice;
        this.buyFour = buyFour;
        this.buyFourPrice = buyFourPrice;
        this.buyFive = buyFive;
        this.buyFivePrice = buyFivePrice;
        this.sellOne = sellOne;
        this.sellOnePrice = sellOnePrice;
        this.sellTwo = sellTwo;
        this.sellTwoPrice = sellTwoPrice;
        this.sellThree = sellThree;
        this.sellThreePrice = sellThreePrice;
        this.sellFour = sellFour;
        this.sellFourPrice = sellFourPrice;
        this.sellFive = sellFive;
        this.sellFivePrice = sellFivePrice;
        this.minurl = minurl;
        this.dayurl = dayurl;
        this.weekurl = weekurl;
        this.monthurl = monthurl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public double getOpenningPrice() {
        return OpenningPrice;
    }

    public void setOpenningPrice(double openningPrice) {
        OpenningPrice = openningPrice;
    }

    public double getClosingPrice() {
        return closingPrice;
    }

    public void setClosingPrice(double closingPrice) {
        this.closingPrice = closingPrice;
    }

    public double getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(double currentPrice) {
        this.currentPrice = currentPrice;
    }

    public double gethPrice() {
        return hPrice;
    }

    public void sethPrice(double hPrice) {
        this.hPrice = hPrice;
    }

    public double getlPrice() {
        return lPrice;
    }

    public void setlPrice(double lPrice) {
        this.lPrice = lPrice;
    }

    public double getCompetitivePrice() {
        return competitivePrice;
    }

    public void setCompetitivePrice(double competitivePrice) {
        this.competitivePrice = competitivePrice;
    }

    public double getAuctionPrice() {
        return auctionPrice;
    }

    public void setAuctionPrice(double auctionPrice) {
        this.auctionPrice = auctionPrice;
    }

    public int getTotalNumber() {
        return totalNumber;
    }

    public void setTotalNumber(int totalNumber) {
        this.totalNumber = totalNumber;
    }

    public long getTurnover() {
        return turnover;
    }

    public void setTurnover(long turnover) {
        this.turnover = turnover;
    }

    public double getIncrease() {
        return increase;
    }

    public void setIncrease(double increase) {
        this.increase = increase;
    }

    public int getBuyOne() {
        return buyOne;
    }

    public void setBuyOne(int buyOne) {
        this.buyOne = buyOne;
    }

    public double getBuyOnePrice() {
        return buyOnePrice;
    }

    public void setBuyOnePrice(double buyOnePrice) {
        this.buyOnePrice = buyOnePrice;
    }

    public int getBuyTwo() {
        return buyTwo;
    }

    public void setBuyTwo(int buyTwo) {
        this.buyTwo = buyTwo;
    }

    public double getBuyTwoPrice() {
        return buyTwoPrice;
    }

    public void setBuyTwoPrice(double buyTwoPrice) {
        this.buyTwoPrice = buyTwoPrice;
    }

    public int getBuyThree() {
        return buyThree;
    }

    public void setBuyThree(int buyThree) {
        this.buyThree = buyThree;
    }

    public double getBuyThreePrice() {
        return buyThreePrice;
    }

    public void setBuyThreePrice(double buyThreePrice) {
        this.buyThreePrice = buyThreePrice;
    }

    public int getBuyFour() {
        return buyFour;
    }

    public void setBuyFour(int buyFour) {
        this.buyFour = buyFour;
    }

    public double getBuyFourPrice() {
        return buyFourPrice;
    }

    public void setBuyFourPrice(double buyFourPrice) {
        this.buyFourPrice = buyFourPrice;
    }

    public int getBuyFive() {
        return buyFive;
    }

    public void setBuyFive(int buyFive) {
        this.buyFive = buyFive;
    }

    public double getBuyFivePrice() {
        return buyFivePrice;
    }

    public void setBuyFivePrice(double buyFivePrice) {
        this.buyFivePrice = buyFivePrice;
    }

    public int getSellOne() {
        return sellOne;
    }

    public void setSellOne(int sellOne) {
        this.sellOne = sellOne;
    }

    public double getSellOnePrice() {
        return sellOnePrice;
    }

    public void setSellOnePrice(double sellOnePrice) {
        this.sellOnePrice = sellOnePrice;
    }

    public int getSellTwo() {
        return sellTwo;
    }

    public void setSellTwo(int sellTwo) {
        this.sellTwo = sellTwo;
    }

    public double getSellTwoPrice() {
        return sellTwoPrice;
    }

    public void setSellTwoPrice(double sellTwoPrice) {
        this.sellTwoPrice = sellTwoPrice;
    }

    public int getSellThree() {
        return sellThree;
    }

    public void setSellThree(int sellThree) {
        this.sellThree = sellThree;
    }

    public double getSellThreePrice() {
        return sellThreePrice;
    }

    public void setSellThreePrice(double sellThreePrice) {
        this.sellThreePrice = sellThreePrice;
    }

    public int getSellFour() {
        return sellFour;
    }

    public void setSellFour(int sellFour) {
        this.sellFour = sellFour;
    }

    public double getSellFourPrice() {
        return sellFourPrice;
    }

    public void setSellFourPrice(double sellFourPrice) {
        this.sellFourPrice = sellFourPrice;
    }

    public int getSellFive() {
        return sellFive;
    }

    public void setSellFive(int sellFive) {
        this.sellFive = sellFive;
    }

    public double getSellFivePrice() {
        return sellFivePrice;
    }

    public void setSellFivePrice(double sellFivePrice) {
        this.sellFivePrice = sellFivePrice;
    }

    public String getMinurl() {
        return minurl;
    }

    public void setMinurl(String minurl) {
        this.minurl = minurl;
    }

    public String getDayurl() {
        return dayurl;
    }

    public void setDayurl(String dayurl) {
        this.dayurl = dayurl;
    }

    public String getWeekurl() {
        return weekurl;
    }

    public void setWeekurl(String weekurl) {
        this.weekurl = weekurl;
    }

    public String getMonthurl() {
        return monthurl;
    }

    public void setMonthurl(String monthurl) {
        this.monthurl = monthurl;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.code);
        dest.writeString(this.date);
        dest.writeString(this.time);
        dest.writeDouble(this.OpenningPrice);
        dest.writeDouble(this.closingPrice);
        dest.writeDouble(this.currentPrice);
        dest.writeDouble(this.hPrice);
        dest.writeDouble(this.lPrice);
        dest.writeDouble(this.competitivePrice);
        dest.writeDouble(this.auctionPrice);
        dest.writeInt(this.totalNumber);
        dest.writeLong(this.turnover);
        dest.writeDouble(this.increase);
        dest.writeInt(this.buyOne);
        dest.writeDouble(this.buyOnePrice);
        dest.writeInt(this.buyTwo);
        dest.writeDouble(this.buyTwoPrice);
        dest.writeInt(this.buyThree);
        dest.writeDouble(this.buyThreePrice);
        dest.writeInt(this.buyFour);
        dest.writeDouble(this.buyFourPrice);
        dest.writeInt(this.buyFive);
        dest.writeDouble(this.buyFivePrice);
        dest.writeInt(this.sellOne);
        dest.writeDouble(this.sellOnePrice);
        dest.writeInt(this.sellTwo);
        dest.writeDouble(this.sellTwoPrice);
        dest.writeInt(this.sellThree);
        dest.writeDouble(this.sellThreePrice);
        dest.writeInt(this.sellFour);
        dest.writeDouble(this.sellFourPrice);
        dest.writeInt(this.sellFive);
        dest.writeDouble(this.sellFivePrice);
        dest.writeString(this.minurl);
        dest.writeString(this.dayurl);
        dest.writeString(this.weekurl);
        dest.writeString(this.monthurl);
    }

    protected Stock(Parcel in) {
        this.name = in.readString();
        this.code = in.readString();
        this.date = in.readString();
        this.time = in.readString();
        this.OpenningPrice = in.readDouble();
        this.closingPrice = in.readDouble();
        this.currentPrice = in.readDouble();
        this.hPrice = in.readDouble();
        this.lPrice = in.readDouble();
        this.competitivePrice = in.readDouble();
        this.auctionPrice = in.readDouble();
        this.totalNumber = in.readInt();
        this.turnover = in.readLong();
        this.increase = in.readDouble();
        this.buyOne = in.readInt();
        this.buyOnePrice = in.readDouble();
        this.buyTwo = in.readInt();
        this.buyTwoPrice = in.readDouble();
        this.buyThree = in.readInt();
        this.buyThreePrice = in.readDouble();
        this.buyFour = in.readInt();
        this.buyFourPrice = in.readDouble();
        this.buyFive = in.readInt();
        this.buyFivePrice = in.readDouble();
        this.sellOne = in.readInt();
        this.sellOnePrice = in.readDouble();
        this.sellTwo = in.readInt();
        this.sellTwoPrice = in.readDouble();
        this.sellThree = in.readInt();
        this.sellThreePrice = in.readDouble();
        this.sellFour = in.readInt();
        this.sellFourPrice = in.readDouble();
        this.sellFive = in.readInt();
        this.sellFivePrice = in.readDouble();
        this.minurl = in.readString();
        this.dayurl = in.readString();
        this.weekurl = in.readString();
        this.monthurl = in.readString();
    }

    public static final Creator<Stock> CREATOR = new Creator<Stock>() {
        @Override
        public Stock createFromParcel(Parcel source) {
            return new Stock(source);
        }

        @Override
        public Stock[] newArray(int size) {
            return new Stock[size];
        }
    };
}
