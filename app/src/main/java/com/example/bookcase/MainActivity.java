package com.example.bookcase;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.Fragment;
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
import java.io.File;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import edu.temple.audiobookplayer.AudiobookService;

public class MainActivity extends AppCompatActivity implements BookListFragment.BookListInterface,
         BookDetailsFragment.BookDetailsInterface{
    //needs interface to communicate with BookList Fragment

    AudiobookService.MediaControlBinder binder;
    Intent intent;
    FragmentManager fm = getSupportFragmentManager(); //creates a fragment manager
    boolean smallScreen; //determines if the screen is small
    boolean isRunning;
    URL bookList;
    static final String SAVED_LIST = "savedProgress";
    public static ArrayList<BookClass> library; //main array list stores book list that is currently set


    @Override
    public void onSaveInstanceState(Bundle outState){
        outState.putString(SAVED_LIST, bookList.toString());
        super.onSaveInstanceState(outState);
    }
    public void onRestoreInstanceState(Bundle saved){
        try {
            bookList=new URL(saved.getString(SAVED_LIST));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        bindService(intent, connection, Context.BIND_AUTO_CREATE);

    }

    @Override
    protected void onStop(){
        super.onStop();
        unbindService(connection);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        smallScreen = findViewById(R.id.container_2) == null; //checks if the layout is a small screen

        if (bookList==null){
            try {
                bookList = new URL("https://kamorris.com/lab/audlib/booksearch.php");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } //sets URL to original book list JSON list
        }


        createList(bookList);
        //calls function which extracts JSON from URL fills book library and then adds desired fragment to view

        final EditText editText = findViewById(R.id.editText);
        Button  button = findViewById(R.id.button);
        //finds search bar text and button

        //button is clicked
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String search=editText.getText().toString(); //read search text
                try {

                    bookList= new URL("https://kamorris.com/lab/audlib/booksearch.php?search="+search);
                    //search is added to search url
                    createList(bookList);//creates new list of books derived from search list and fills with fragments
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }

            }
        });

        intent = new Intent(this, AudiobookService.class);
        startService(intent);





    }

    private ServiceConnection connection = new ServiceConnection(){

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            binder = (AudiobookService.MediaControlBinder) service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };



    public void createList(final URL url){ //reads url JSON data sends JSON array to fragment


        new Thread() { //need thread to access internet runs parallel
            @Override
            public void run() {

                try {

                    BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
                    //opens url reads data input stream reads bytes converts to characters
                    //Buffered reader spaces data so it can be read correctly as code values

                    String tmpResponse = reader.readLine();
                    String response = "";
                    while (tmpResponse != null) {
                        response += tmpResponse;
                        tmpResponse = reader.readLine();
                    } //reads whole file stores in response

                    JSONArray JA = new JSONArray(response);
                    Message msg = Message.obtain();
                    msg.obj = JA;
                    bookResponseHandler.sendMessage(msg);
                    //creates JSONArray and send a message contaning it to handler

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();

    }

    Handler bookResponseHandler = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            library = new ArrayList<BookClass>(); //empties list of books

            try { //attempts to recieve JSON array
                JSONArray JA = (JSONArray) msg.obj;

                JSONObject JO;
                for (int i=0; i<JA.length();i++){
                    JO=JA.getJSONObject(i);
                    library.add(new BookClass(JO));
                } //converts JSON into book objects and adds each to arraylist of books

            } catch (JSONException e) {
                e.printStackTrace();
            }

            if(!smallScreen){ //big screen gets a list fragment filled with book list titles
                BookListFragment newFragment = BookListFragment.newInstance(library);

                fm.beginTransaction()
                        .replace(R.id.container_1, newFragment)
                        .addToBackStack(null)
                        .commit();  //adds the book list fragment for book selection also adds it to the back stack
            }
            else { //small screen gets a viewpager fragment which holds the books
                fm.beginTransaction()
                        .replace(R.id.container_1, ViewPagerFragment.newInstance(0))
                        .addToBackStack(null)
                        .commit();

            }

            return false;
        }
    });



    @Override
    public void bookSelected(int bookIndex) {
        BookDetailsFragment newFragment = BookDetailsFragment.newInstance(library.get(bookIndex));
        //creates a new book detail fragment and passes the selected book object into the fragment

            //replaces fragment with new selected book
            fm.beginTransaction()
                    .replace(R.id.container_2, newFragment)
                    .addToBackStack(null)
                    .commit();

    }

    Handler progressHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            int progress = msg.what;
            BookDetailsFragment.sendProgress(progress);


            return false;
        }
    });

    public void play(int id) {

        binder.play(id);
        binder.setProgressHandler(progressHandler);

    }

    public void play(File file) {

        binder.play(file);
        binder.setProgressHandler(progressHandler);

    }

    public void play(int id, int progress) {

        binder.play(id,progress);

    }

    public void play(File file, int progress) {

        binder.play(file,progress);

    }

    public void pause() {
        binder.pause();

    }

    public void stop() {
        binder.stop();

    }

    public void seek(int position) {
        binder.seekTo(position);
    }
}
