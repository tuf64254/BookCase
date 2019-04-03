package com.example.bookcase;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements BookListFragment.BookListInterface {

    FragmentManager fm = getSupportFragmentManager(); //creates a fragment manager
    boolean smallScreen; //determines if the screen is small
    public static ArrayList<BookClass> library;
    BookClass currentBook;
    String test= "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        smallScreen = findViewById(R.id.container_2) == null; //checks if the layout is a small screen

        URL bookList = null;
        try {
            bookList = new URL("https://kamorris.com/lab/audlib/booksearch.php");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        createList(bookList);

        final EditText editText = findViewById(R.id.editText);
        Button  button = findViewById(R.id.button);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String search=editText.getText().toString();
                try {
                    URL searchURL= new URL("https://kamorris.com/lab/audlib/booksearch.php?search="+search);
                    createList(searchURL);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }

            }
        });




    }

    Handler bookResponseHandler = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            library = new ArrayList<BookClass>();
            try {
                JSONArray JA = (JSONArray) msg.obj;

                JSONObject JO;
                for (int i=0; i<JA.length();i++){
                    JO=JA.getJSONObject(i);
                    library.add(new BookClass(JO));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            if(!smallScreen){
                BookListFragment newFragment = BookListFragment.newInstance(library);

                fm.beginTransaction()
                        .replace(R.id.container_1, newFragment)
                        .addToBackStack(null)
                        .commit();  //adds the book list fragment for book selection also adds it to the back stack
            }
            else {
                fm.beginTransaction()
                        .replace(R.id.container_1, ViewPagerFragment.newInstance(0))
                        .addToBackStack(null)
                        .commit();

            }



            return false;
        }
    });

    public void createList(final URL url){
        Thread t =new Thread() {
            @Override
            public void run() {

                try {



                    BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));

                    String tmpResponse = reader.readLine();
                    String response = "";
                    while (tmpResponse != null) {
                        response += tmpResponse;
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

    }

    @Override
    public void bookselected(int bookIndex) {
        BookDetailsFragment newFragment = BookDetailsFragment.newInstance(library.get(bookIndex));
        //creates a new book detail fragment and passes the selected book index into the fragment


            fm.beginTransaction()
                    .add(R.id.container_2, newFragment)
                    .addToBackStack(null)
                    .commit();


    }




}
