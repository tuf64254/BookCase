package com.example.bookcase;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class ViewPagerFragment extends Fragment { //view pager used to hold multiple fragments

    public static final String START_INDEX = "start_index";
    int startIndex;

    public ViewPagerFragment() {
        // Required empty public constructor
    }

    private ViewPager viewPager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null)
            startIndex = getArguments().getInt(START_INDEX); //sets the start index from the previously created instance

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_view_pager, container, false);
        viewPager = view.findViewById(R.id.viewPager);
        viewPager.setAdapter(new ViewPagerAdapter(getChildFragmentManager(), getContext()));
        //attaches the view pager and sets the view pager adapter
        viewPager.setCurrentItem(startIndex); //sets the view pager to selected book

        return view;
    }

    public static ViewPagerFragment newInstance(int startIndex){
        //stores the start index for the view pager and bundles it so it can be used in the other methods
        ViewPagerFragment vpf = new ViewPagerFragment();

        Bundle bundle = new Bundle();
        bundle.putInt(START_INDEX, startIndex);
        vpf.setArguments(bundle);

        return vpf;
    }



}


