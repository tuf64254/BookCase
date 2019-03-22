package com.example.bookcase;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements BookListFragment.BookListInterface {

    FragmentManager fm=getSupportFragmentManager();
    boolean smallScreen;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        smallScreen = findViewById(R.id.container_2)==null;


        fm.beginTransaction()
                .replace(R.id.container_1, new BookListFragment())
                .addToBackStack(null)
                .commit();

    }

    @Override
    public void bookselected(int bookIndex) {
        BookDetailsFragment newFragment = BookDetailsFragment.newInstance(bookIndex);

        if(!smallScreen){
            fm.beginTransaction()
                    .add(R.id.container_2, BookDetailsFragment.newInstance(bookIndex))
                    .addToBackStack(null)
                    .commit();
        }
        else {
            fm.beginTransaction()
                    .add(R.id.container_1, ViewPagerFragment.newInstance(bookIndex))
                    .addToBackStack(null)
                    .commit();
        }

    }
}
