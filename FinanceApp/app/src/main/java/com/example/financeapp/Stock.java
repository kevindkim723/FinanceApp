package com.example.financeapp;

import java.text.DecimalFormat;

public class Stock {
    private String mName, mFullName, mBoughtPrice, mChange, mPercentChange, mCurrentPrice, mDayChange, mMarketValue;
    private double dBoughtPrice, dPercentChange, dCurrentPrice, dDayChange, dMarketValue, dOriginalMarket;
    private int shares;

    public Stock(String mName, String mFullName, String mBoughtPrice, int shares) {
        this.mName = mName;
        this.mFullName = mFullName;
        this.shares = shares;
        this.mBoughtPrice = mBoughtPrice;


        dBoughtPrice = Double.parseDouble(mBoughtPrice.replace(",", ""));
        dOriginalMarket = dBoughtPrice * shares;
        dMarketValue = shares * dCurrentPrice;


    }


    public void setShares(int shares) {
        this.shares = shares;
    }

    public void updatesShare() {

        String[] mStockArr = Scraper.getData(mName);
        System.out.println("updated succesfully");
        mCurrentPrice = (mStockArr[1]);

        dCurrentPrice = Double.parseDouble(mCurrentPrice.replace(",", ""));
        if (dOriginalMarket == 0)
        {
            dOriginalMarket = shares * dCurrentPrice;
        }
        dMarketValue = dCurrentPrice * shares;
        mMarketValue = dMarketValue + "";

        DecimalFormat df = new DecimalFormat("#.##");
        mChange = " " + df.format(dMarketValue - dOriginalMarket);
        mPercentChange = (df.format((dMarketValue - dOriginalMarket) / dOriginalMarket)) + "";
        System.out.println("CHANGE" + (dMarketValue-dOriginalMarket));
        System.out.println("origL : " + dOriginalMarket);
        System.out.println( "PErcnetL :" + mPercentChange);
        mDayChange = mStockArr[2];

        System.out.printf("\n%s %s %s %s %s %s %s %s\n", getName(), getFullName(), getCurrentSharePrice(), getBoughtSharePrice(), getDayChange(),getNetChange(),getShares(), getMarketValue());


    }

    public String getName() {
        return mName;
    }

    public String getFullName() {
        return mFullName;
    }

    public String getCurrentSharePrice() {
        DecimalFormat df = new DecimalFormat("#.##");
        return df.format(dCurrentPrice);
        //return mCurrentPrice;
    }

    public String getBoughtSharePrice() {
        return mBoughtPrice;
    }

    public String getDayChange() {
        return mDayChange;
    }

    public String getMarketValue() {
        DecimalFormat df = new DecimalFormat("#,###.00");

        return df.format(dMarketValue);
    }

    public String getNetChange() {
        return mChange + " (" + mPercentChange + "%)";
    }
    public int getShares() {
        return shares;
    }
    public String getPercentChange()
    {
        return mPercentChange;
    }

}
