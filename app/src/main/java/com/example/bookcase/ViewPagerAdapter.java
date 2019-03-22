package com.example.bookcase;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class ViewPagerAdapter extends FragmentPagerAdapter {


    private ArrayList<Fragment> fragmentList = new ArrayList<>();
    private Context context;
    BookDetailsFragment fragment;
    public ViewPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {


        return BookDetailsFragment.newInstance(position);
    }

    @Override
    public int getCount() {

        return context.getResources().getStringArray(R.array.books).length;
    }




}
