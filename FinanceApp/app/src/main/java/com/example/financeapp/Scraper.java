package com.example.financeapp;

import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

public class Scraper {


    public static String[] getData(String stockname)
    {
        scraper s = new scraper(stockname);
        Thread thread = new Thread(s);
        thread.start();
        try
        {
            thread.join();

        }
        catch (Exception e)
        {
           System.out.println("Interrupted") ;
        }
        return s.getName();

    }


    private static class scraper implements Runnable{
        private static String stockname;
        private String[] arr = new String[4];
        public scraper(String stockname) {

            this.stockname = stockname;
        }

        @Override
        public void run() {
            try {
                String url = "https://finance.yahoo.com/quote/" + stockname + "/history?period1=345427200&period2=1585785600&interval=1d&filter=history&frequency=1d";
                Document doc = Jsoup.connect(url).get();
                Element name = doc.select("h1[data-reactid='7']").first();
                Element price = doc.select("span[data-reactid='50']").first();
                Element change = doc.select("span[data-reactid='51']").first();
                System.out.println(change.text()+ "RABBY");
                //Elements e = doc.select("span[data-reactid='51']");


                System.out.println(name.text() + " " + price.text() + "SPANSPANSPAN " + change.text());
                arr[0] = name.text();
                arr[1] = price.text();
                arr[2] = change.text();



            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }



        public String[] getName() {
            return arr;
        }



    } public static ArrayList<DataEntry> getEntries(final String stockname) {

        final ArrayList<DataEntry> arrData = new ArrayList<>();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    String url = "https://finance.yahoo.com/quote/" + stockname + "/history?period1=345427200&period2=1585785600&interval=1d&filter=history&frequency=1d";
                    Document doc = Jsoup.connect(url).get();
                    doc = Jsoup.connect(url).get();
                    Element table = doc.select("table").get(0);
                    //System.out.println(table.text());
                    Elements rows = table.select("tr");

                    for (int i = 1; i < rows.size(); i++) { //first row is the col names so skip it.

                        Element row = rows.get(i);
                        Elements cols = row.select("span[data-reactid]");
                        //arr[3] += cols.text() + "\n";
                        String[] x = cols.text().split(" ");
                        if (x.length < 5 || x.length > 9) {
                            continue;
                        }
                        //System.out.println(x.length);
                        String date = x[2] + "-" + Data.getMonth(x[0]) + "-" + x[1].substring(0, x[1].length() - 1);
                        //System.out.println("AAAHAHAAHHAAHxxxxxx   " + date);
                        String closingprice = x[6];
                        closingprice.replace("\\s+", "");
                        // System.out.println(closingprice);
                        arrData.add(new ValueDataEntry(date, Double.parseDouble(closingprice)));

                    }
                }
                catch (Exception e){
                    System.out.println("IO Exception");
                }
            }
        };
        Thread t = new Thread(runnable);
        t.start();
        try {
            t.join();
        }
        catch(Exception e)
        {
            System.out.println("oooh");
        }
        return arrData;
    }
}

