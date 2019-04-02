package com.example.bookcase;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class ViewPagerAdapter extends FragmentPagerAdapter {



    private Context context;

    public ViewPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context; //sets context
    }

    @Override
    public Fragment getItem(int position) {


        return BookDetailsFragment.newInstance(MainActivity.library.get(position)); // returns the fragment at the selected position
    }

    @Override
    public int getCount() {

        return MainActivity.library.size();
        //needs context to access the resource then returns the length of the array
        //this is done so I can change the array and not need to change any code in this class
    }







}
