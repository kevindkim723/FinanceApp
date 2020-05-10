package com.example.financeapp.Fragments;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.financeapp.R;
import com.example.financeapp.Scraper;
import com.example.financeapp.Stock;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

public class stock_add_fragment extends Fragment {
    private stockAddFragmentListener listener;
    private Stock mBuyStock;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.stock_add_frag, container, false);
        final AutoCompleteTextView mAutoCompleteTextView = v.findViewById(R.id.mAutoCompleteTextView);
        final CardView mCardView1 = v.findViewById(R.id.mCardView1);
        final ImageButton mCheckButton = v.findViewById(R.id.mCheckButton);
        final String[] stocklist = getResources().getStringArray(R.array.stocklist);
        final EditText mEditTextShareNumber = v.findViewById(R.id.mEditTextShareNumber);

        Toolbar mtoolbar = (Toolbar) v.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mtoolbar);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("hello");
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mtoolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("YUPU WENT BACK BRO");

                // portfolio_fragment pf = new portfolio_fragment();
                getFragmentManager().popBackStack();
                //getFragmentManager().beginTransaction().replace(R.id.Fragment_Container, pf).commit();
            }
        });

        mCheckButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBuyStock.setShares(Integer.parseInt(mEditTextShareNumber.getText().toString()));

                System.out.println("DONKEY DONGK" + mBuyStock.getShares());
                System.out.println(Integer.parseInt(mEditTextShareNumber.getText().toString()) + " AJFSIDHISDA FHSDAHOFSDFAIOHFAIHDOSIHODFIAHSIHA");
                mBuyStock.updatesShare();
                listener.stockAdd(mBuyStock);
                mCardView1.setVisibility(View.GONE);
                mEditTextShareNumber.setVisibility(View.GONE);
                mCheckButton.setVisibility(View.GONE);
            }
        });

        //TODO make custom list item https://github.com/aosp-mirror/platform_frameworks_base/blob/master/core/res/res/layout/simple_list_item_1.xml
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.list_item, stocklist);

        mAutoCompleteTextView.setAdapter(adapter);

        mAutoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mBuyStock = null;
                String mStockSelected = parent.getItemAtPosition(position).toString();
                String[] mStockArr = Scraper.getData(mStockSelected.substring(0, mStockSelected.indexOf(" ")));
                TextView mTextView1 = v.findViewById(R.id.textView1);
                TextView mTextView2 = v.findViewById(R.id.textView2);
                TextView mTextView3 = v.findViewById(R.id.textView3);
                TextView mTextView4 = v.findViewById(R.id.textView4);
                TextView mTextView5 = v.findViewById(R.id.textView5);
                TextView mTextView6 = v.findViewById(R.id.textView6);
                mTextView1.setText(mStockSelected.split(" ")[0]);
                mTextView2.setText(mStockSelected.substring(mStockSelected.indexOf(" "), mStockSelected.length()));
                mTextView3.setText(mStockArr[1]);
                mTextView4.setText("As of recent");
                mTextView5.setText(mStockArr[2].split(" ")[0]);
                mTextView6.setText(mStockArr[2].split(" ")[1]);

                if (mTextView5.getText().toString().contains("-")) {
                    mTextView5.setTextColor(Color.RED);
                    mTextView6.setTextColor(Color.RED);
                } else {
                    mTextView5.setTextColor(Color.GREEN);
                    mTextView6.setTextColor(Color.GREEN);
                }
                System.out.println("POSISISON" + position);
                //mCardView1.setClickable(true);
                mCardView1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v1) {
                        mCheckButton.setVisibility(View.VISIBLE);
                        mEditTextShareNumber.setVisibility(View.VISIBLE);

                    }
                });
                mCardView1.setVisibility(View.VISIBLE);
                mBuyStock = new Stock(mTextView1.getText().toString(), mTextView2.getText().toString(), mTextView3.getText().toString(), 0);


            }
        });
        mAutoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (mAutoCompleteTextView.getText().toString().trim().length() == 0) {
                    mCardView1.setVisibility(View.GONE);
                    mEditTextShareNumber.setVisibility(View.GONE);
                    mCheckButton.setVisibility(View.GONE);
                }
            }
        });


        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof stockAddFragmentListener) {
            listener = (stockAddFragmentListener) context;
        } else {
            throw new RuntimeException("You didn't implement the methods");
        }
    }

    public interface stockAddFragmentListener {
        public void stockAdd(Stock stockName);
    }
}

