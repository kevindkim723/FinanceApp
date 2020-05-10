package com.example.financeapp;

import android.os.Bundle;

import com.example.financeapp.Fragments.details_fragment;
import com.example.financeapp.Fragments.portfolio_fragment;
import com.example.financeapp.Fragments.stock_add_fragment;
import com.example.financeapp.Fragments.tab_fragment;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements stock_add_fragment.stockAddFragmentListener, portfolio_fragment.stockTouchListener {
    private stock_add_fragment mFragment_stock_add;
    private portfolio_fragment mPortfolioFragment;
    private details_fragment df;
    private tab_fragment mTabFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("jeffy time");
        mPortfolioFragment = new portfolio_fragment();


        getSupportFragmentManager().beginTransaction()
                .replace(R.id.Fragment_Container, mPortfolioFragment)
                .commit();
     /* mTabFragment = new tab_fragment();
      getSupportFragmentManager().beginTransaction()
              .replace(R.id.Fragment_Container, mTabFragment)
              .commit();*/

    }


    @Override
    public void stockAdd(Stock stockName) {
        mPortfolioFragment.addStock(stockName);
    }

    @Override
    public void stockTouch(String stockName) {
        System.out.println("touched");
        getSupportFragmentManager().executePendingTransactions();
        details_fragment df = (details_fragment) getSupportFragmentManager().findFragmentByTag("df");
        if(df != null && df.isAdded()){

            df.loadUI(stockName);
        }



    }

}
