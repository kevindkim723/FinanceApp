package com.example.financeapp;

import android.content.Context;

//import com.example.financeapp.Fragments.Frag1;

import com.example.financeapp.Fragments.details_fragment;
import com.example.financeapp.Fragments.portfolio_fragment;
import com.example.financeapp.Fragments.summary_fragment;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class SectionsPagerAdapter extends FragmentPagerAdapter {
    private static final int[] TAB_TITLES = new int[]{R.string.tab1, R.string.tab2};
    private final Context mContext;
    public SectionsPagerAdapter(Context context, FragmentManager fm){
        super(fm);
        mContext = context;

    }
    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch(position){
            case 0:
                fragment = new portfolio_fragment();
                break;
            case 1:
                fragment = new summary_fragment();
                break;

        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 2;
    }
    public CharSequence getPageTitle(int position)
    {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }
}
