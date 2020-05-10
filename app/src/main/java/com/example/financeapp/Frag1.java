package com.example.financeapp;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Scatter;
import com.anychart.core.scatter.series.Marker;
import com.anychart.enums.HoverMode;
import com.anychart.scales.DateTime;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class Frag1 extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.frag1, container, false);
        final EditText stockname = v.findViewById(R.id.stock_edit);
        final Button enterbutton = v.findViewById(R.id.enterbutton);


        enterbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sn = stockname.getText().toString();
                stockname.getText().clear();


                scraper sc = new scraper(sn);
                Thread t = new Thread(sc);
                t.start();
                try {
                    t.join();
                    try {
                        String[] info = sc.getName();
                        ArrayList<DataEntry> dataEntries = sc.getData();
                        updateUI(info[0], info[1], info[2], dataEntries);
                        System.out.println(info[3]);
                    } catch (Exception e) {
                        Toast.makeText(getActivity(), "NO STOCK FOUND", Toast.LENGTH_LONG).show();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


                //System.out.println(Arrays.toString(sc.getName()));
            }
        });

        return v;
    }


    private void updateUI(String stockname, String value, String change, ArrayList<DataEntry> dataEntryArrayList) {
        TextView stock_name = findViewById(R.id.stock_name);
        TextView stock_price = findViewById(R.id.stock_price);
        TextView stock_change = findViewById(R.id.stock_change);
        boolean fall = change.contains("-");
        stock_name.setText(stockname);
        stock_price.setText(value);
        stock_change.setText(change);
        if (fall) {
            stock_change.setTextColor(Color.RED);
        } else {
            stock_change.setTextColor(Color.BLUE);
        }

        Scatter chart = AnyChart.scatter();
        AnyChartView acv = findViewById(R.id.any_chart_view);
        chart.xGrid(true);
        chart.yGrid(true);
        chart.xMinorGrid(true);
        chart.yMinorGrid(true);
        chart.xAxis(0).title("Date");
        chart.yAxis(0).title("Stock Closing Price");
        chart.xScale(DateTime.instantiate());
        DateTime dateTime = ((DateTime) chart.xScale(DateTime.class));
        dateTime.ticks().interval("d", 1);
        Marker series = chart.marker(dataEntryArrayList);
        series.size(1);
        chart.interactivity(HoverMode.BY_X);

        acv.setChart(chart);


    }
    //private void updateChart(ArrayList<>)



    public class scraper implements Runnable {
        private String[] arr = new String[4];
        private String stockname;
        private ArrayList<DataEntry> arrData = new ArrayList<>();

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
                //Elements e = doc.select("span[data-reactid='51']");
                /*for (int i = 0; i < e.size(); i++) {
                    System.out.printf("%s: %s\n", i, e.get(i).text());
                }*/

                //System.out.println(name.text() + " " + price.text() + " " + change.text());
                arr[0] = name.text();
                arr[1] = price.text();
                arr[2] = change.text();


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
                    System.out.println("AAAHAHAAHHAAHxxxxxx   " + date);
                    String closingprice = x[6];
                    closingprice.replace("\\s+", "");
                    System.out.println(closingprice);
                    arrData.add(new ValueDataEntry(date, Double.parseDouble(closingprice)));
                    // Data d = new Data( Double.parseDouble(closingprice), date);
                    //arrData.add(d);
                    //System.out.println("DATA: " + d);

                }
            } catch (Exception e) {
                MainActivity.this.showToast("NO STOCK FOUND WITH THAT NAME");
                e.printStackTrace();
            }
        }

        public String[] getName() {
            return arr;
        }

        public ArrayList<DataEntry> getData() {
            return arrData;
        }


    }
}
