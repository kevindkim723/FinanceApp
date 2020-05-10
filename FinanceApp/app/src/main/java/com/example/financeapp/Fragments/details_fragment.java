package com.example.financeapp.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.charts.Scatter;
import com.anychart.core.scatter.series.Marker;
import com.anychart.enums.HoverMode;
import com.anychart.scales.DateTime;
import com.example.financeapp.R;
import com.example.financeapp.Scraper;
import com.example.financeapp.Stock;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

public class details_fragment extends Fragment{
    Scatter chart;
    AnyChartView acv;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View v = inflater.inflate(R.layout.detail_frag, container,false);
       setHasOptionsMenu(true);
       chart = AnyChart.scatter();
       acv = v.findViewById(R.id.any_chart_view);
       ProgressBar mProgressBar = v.findViewById(R.id.progress_bar);
       acv.setProgressBar(mProgressBar);
       acv.setBackgroundColor(getResources().getColor(android.R.color.background_dark));

       Toolbar mToolbar = v.findViewById(R.id.toolbar);
       ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("YUPU WENT BACK BRO");

                // portfolio_fragment pf = new portfolio_fragment();
                getFragmentManager().popBackStack();
                //getFragmentManager().beginTransaction().replace(R.id.Fragment_Container, pf).commit();
            }
        });

       return v;

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.example_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item1:
                Toast.makeText(getContext(), "Item 1 selected", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.item2:
                Toast.makeText(getContext(), "Item 2 selected", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.item3:
                Toast.makeText(getContext(), "Item 3 selected", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.subitem1:
                Toast.makeText(getContext(), "Sub Item 1 selected", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.subitem2:
                Toast.makeText(getContext(), "Sub Item 2 selected", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void loadUI(String stockName){
        chart.xGrid(true);
        chart.yGrid(true);
        chart.xMinorGrid(true);
        chart.yMinorGrid(true);
        chart.xAxis(0).title("Date");
        chart.yAxis(0).title("Stock Closing Price");
        chart.xScale(DateTime.instantiate());
        DateTime dateTime = ((DateTime) chart.xScale(DateTime.class));
        dateTime.ticks().interval("d", 1);
        Marker series = chart.marker(Scraper.getEntries(stockName));
        series.size(1);
        chart.interactivity(HoverMode.BY_X);

        acv.setChart(chart);
    }

}
