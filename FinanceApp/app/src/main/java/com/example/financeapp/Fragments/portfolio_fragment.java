package com.example.financeapp.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.financeapp.FinanceAdapter;
import com.example.financeapp.R;
import com.example.financeapp.Stock;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import static android.content.Context.MODE_PRIVATE;

public class portfolio_fragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private RecyclerView mRecyclerView;
    private FinanceAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManger;
    private ArrayList<Stock> stockList;
    private SwipeRefreshLayout refresher;
    private stockTouchListener listener;
    private ImageButton mAddButton, mRemoveButton;
    private Button mCancelButton;
    private int selectPos;
    private boolean selectorMode;
    private ArrayList<Integer> selectList = new ArrayList<>();
    public interface stockTouchListener
    {
        public void stockTouch(String stockName);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.portfolio_frag, container, false);
        Toolbar mToolbar = v.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("hello");

        return v;
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadData();
        mCancelButton = view.findViewById(R.id.mCancelButton);
        mAddButton = view.findViewById(R.id.mAddButton);

        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stock_add_fragment saf = new stock_add_fragment();
                FragmentManager manager = getFragmentManager();
                FragmentTransaction fragmentTransaction = manager.beginTransaction();
                fragmentTransaction.add(R.id.Fragment_Container, saf);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();


            }
        });
        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRemoveButton.setVisibility(View.GONE);
                mCancelButton.setVisibility(View.GONE);
                selectList.clear();
                mAdapter.selected = false;
                mAddButton.setVisibility(View.VISIBLE);
                mAdapter.notifyDataSetChanged();
            }
        });
        mRemoveButton = view.findViewById(R.id.mRemoveButton);
        mRemoveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRemoveButton.setVisibility(View.GONE);
                mCancelButton.setVisibility(View.GONE);
                Collections.sort(selectList);

                for (int i = 0 ; i < selectList.size(); i++)

                {
                    int index = selectList.get(i)-i;

                    stockList.remove(index);

                    mAdapter.notifyItemRemoved(index);
                }
                selectList.clear();
                mAdapter.selected = false;
                mAddButton.setVisibility(View.VISIBLE);
                mAdapter.notifyDataSetChanged();
                saveData();
            }
        });
        mRecyclerView = view.findViewById(R.id.mRecyclerView);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManger = new LinearLayoutManager(getContext());
        mAdapter = new FinanceAdapter(stockList);
        mAdapter.setOnItemClickListener(new FinanceAdapter.onItemClickListener() {
            @Override
            public void onItemClick(int position, boolean selected) {
                if (selected){
                    if (selectList.contains(position)){
                        selectList.remove(position);
                    }
                    else{
                        selectList.add(position);
                    }
                }
                else {
                    details_fragment df = new details_fragment();
                    FragmentManager manager = getFragmentManager();
                    FragmentTransaction fragmentTransaction = manager.beginTransaction();
                    fragmentTransaction.add(R.id.Fragment_Container, df, "df");
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                    listener.stockTouch(stockList.get(position).getName());
                }
                mAdapter.notifyDataSetChanged();

            }
        });
        mAdapter.setOnLongItemClickListener(new FinanceAdapter.onLongItemClickListener() {
            @Override
            public void onLongItemClick(int position) {
                selectPos = position;

                selectList.add(position);
                mAdapter.selected = true;
                mAddButton.setVisibility(View.GONE);
                mRemoveButton.setVisibility(View.VISIBLE);
                mCancelButton.setVisibility(View.VISIBLE);
            }
        });

        mRecyclerView.setLayoutManager(mLayoutManger);
        mRecyclerView.setAdapter(mAdapter);


       refresher = view.findViewById(R.id.refreshLayout);
        refresher.setOnRefreshListener(this);
        refresher.post(new Runnable() {

            @Override
            public void run() {

                refresher.setRefreshing(true);

                // Fetching data from server
                updateStocks();
            }
        });

    }

    @Override
    public void onRefresh() {
        updateStocks();
    }
    public void updateStocks()
    {
        refresher.setRefreshing(true);

        for (Stock s : stockList)
        {
            s.updatesShare();
        }
        mAdapter.notifyDataSetChanged();
        refresher.setRefreshing(false);
    }

    public void addStock(Stock mStockAdd)
    {
       System.out.println("ADDDED1");

       stockList.add(0,mStockAdd);
       saveData();
       mAdapter.notifyDataSetChanged();
    }
    /*public void updateStocks()
    {
        for (Stock s : stockList)
        {
            s
        }
    }*/
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof stockTouchListener) {
            listener = (stockTouchListener) context;
        } else {
            throw new RuntimeException("You didn't implement the methods");
        }
    }
    public void saveData()
    {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("shared preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(stockList);
        editor.putString("task list", json);
        editor.apply();
    }
    public void loadData()
    {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("shared preferences", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("task list", null);
        Type type = new TypeToken<ArrayList<Stock>>() {}.getType();
        stockList = gson.fromJson(json, type);

        if (stockList == null) {
            stockList = new ArrayList<>();
        }
    }
}
