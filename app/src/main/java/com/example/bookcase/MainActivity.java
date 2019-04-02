package com.example.bookcase;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements BookListFragment.BookListInterface {

    FragmentManager fm = getSupportFragmentManager(); //creates a fragment manager
    boolean smallScreen; //determines if the screen is small
    public static ArrayList<BookClass> library = new ArrayList<BookClass>();
    BookClass currentBook;
    String test= "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        smallScreen = findViewById(R.id.container_2) == null; //checks if the layout is a small screen

        Thread t = new Thread() {
            @Override
            public void run() {

                URL bookList;

                try {

                    bookList = new URL("https://kamorris.com/lab/audlib/booksearch.php");

                    BufferedReader reader = new BufferedReader(new InputStreamReader(bookList.openStream()));

                    String tmpResponse = reader.readLine();
                    String response="";
                    while (tmpResponse != null) {
                        response+=tmpResponse;
                        tmpResponse = reader.readLine();
                    }

                    JSONArray JA = new JSONArray(response);
                    Message msg = Message.obtain();
                    msg.obj = JA;
                    bookResponseHandler.sendMessage(msg);





                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        t.start();

        BookListFragment newFragment = BookListFragment.newInstance(library);

        //fm.beginTransaction()
          //      .replace(R.id.container_1, newFragment)
              //  .addToBackStack(null)
            //    .commit();  //adds the book list fragment for book selection also adds it to the back stack

    }

    Handler bookResponseHandler = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {

            try {
                JSONArray JA = (JSONArray) msg.obj;

                JSONObject JO;
                for (int i=0; i<1;i++){
                    JO=JA.getJSONObject(i);
                    library.add(new BookClass(JO));
                    test=(new BookClass(JO)).getTitle();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Toast.makeText(MainActivity.this, test, Toast.LENGTH_LONG).show();



            return false;
        }
    });

    @Override
    public void bookselected(int bookIndex) {
        BookDetailsFragment newFragment = BookDetailsFragment.newInstance(library.get(bookIndex));
        //creates a new book detail fragment and passes the selected book index into the fragment

        if (!smallScreen) { //if screen is big add the book fragment to the second half of the screen
            fm.beginTransaction()
                    .add(R.id.container_2, newFragment)
                    .addToBackStack(null)
                    .commit();
        } else {
            //if screen is small add the view pager fragment and pass the selected book so it can be selected in the view pager
            fm.beginTransaction()
                    .add(R.id.container_1, ViewPagerFragment.newInstance(bookIndex))
                    .addToBackStack(null)
                    .commit();
        }

    }




}
