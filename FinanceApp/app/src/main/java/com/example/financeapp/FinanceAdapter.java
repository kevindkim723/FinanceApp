package com.example.financeapp;


import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class FinanceAdapter extends RecyclerView.Adapter<FinanceAdapter.FinanceViewHolder> {
    private ArrayList<Stock> mPortfolioList;
    private onItemClickListener mListener;
    private onLongItemClickListener mLongListener;
    public static boolean selected;
    public FinanceAdapter(ArrayList<Stock> mPortfolioList) {
        this.mPortfolioList = mPortfolioList;
    }

    public void setOnItemClickListener(onItemClickListener listener) {
        mListener = listener;
    }
    public void setOnLongItemClickListener(onLongItemClickListener mLongListener){

        this.mLongListener = mLongListener;
    }


    @NonNull
    @Override
    public FinanceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview2, parent, false);
        FinanceViewHolder mFinanceViewHolder = new FinanceViewHolder(v, mListener, mLongListener);

        return mFinanceViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull FinanceViewHolder holder, int position) {
        Stock mCurrentStock = mPortfolioList.get(position);


        holder.mName.setText(mCurrentStock.getName());
        holder.mFullName.setText(mCurrentStock.getFullName());
        holder.mDayChange.setText("Day: " + mCurrentStock.getDayChange());
        holder.mCurrPrice.setText(mCurrentStock.getCurrentSharePrice());
        holder.mBoughtPrice.setText(mCurrentStock.getBoughtSharePrice());
        holder.mNetChange.setText(mCurrentStock.getNetChange());
        holder.mQuantity.setText("Quantity: " + mCurrentStock.getShares());
        holder.mMarketValue.setText("Market Value: " + mCurrentStock.getMarketValue());
        if (mCurrentStock.getDayChange().contains("-")) {
            holder.mDayChange.setTextColor(Color.parseColor("#ff0000"));
        } else {

            holder.mDayChange.setTextColor(Color.parseColor("#00ff00"));
        }
        if (!selected)
        {
            holder.itemView.setBackgroundColor(Color.TRANSPARENT);
        }
    }

    @Override
    public int getItemCount() {
        return mPortfolioList.size();
    }

    public interface onItemClickListener {
        void onItemClick(int position, boolean selected);
    }
    public interface onLongItemClickListener{
        void onLongItemClick(int position);
    }
    public void refresh()
    {

    }

    public static class FinanceViewHolder extends RecyclerView.ViewHolder {
        public TextView mName, mFullName, mCurrPrice, mNetChange, mQuantity, mDayChange, mBoughtPrice, mMarketValue;

        public FinanceViewHolder(final View itemView, final onItemClickListener listener, final onLongItemClickListener longlistener) {
            super(itemView);
            mName = itemView.findViewById(R.id.textView1_1);
            mFullName = itemView.findViewById(R.id.textView1_2);
            mBoughtPrice = itemView.findViewById(R.id.textView1_4);
            mCurrPrice = itemView.findViewById(R.id.textView1_3);
            mDayChange = itemView.findViewById(R.id.textView1_6);
            mNetChange = itemView.findViewById(R.id.textView1_5);
            mQuantity = itemView.findViewById(R.id.textView1_7);
            mMarketValue = itemView.findViewById(R.id.textView1_8);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position, selected);
                            if (selected)
                            {
                                if (((ColorDrawable)itemView.getBackground()).getColor()==Color.GRAY)
                                {
                                    itemView.setBackgroundColor(Color.TRANSPARENT);
                                }
                                else
                                {
                                    itemView.setBackgroundColor(Color.GRAY);
                                }
                            }
                        }
                    }
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    itemView.setBackgroundColor(Color.GRAY);
                    if (longlistener != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            longlistener.onLongItemClick(position);
                        }
                    }
                    return true;
                }
            });
        }

    }
}
