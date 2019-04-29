package com.example.bookcase;


import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;


/**
 * A simple {@link Fragment} subclass.
 */
public class BookDetailsFragment extends Fragment {
    //This Fragment displays all information about the book object

    public static final String BOOK_CLASS = "book_class";
    BookClass book;
    Context parentContext;
    static int bookProgress;
    static final String SAVED_PROGRESS = "savedProgress";
    static final String DOWNLOADED = "downloaded";
    public boolean isRunning;
    public boolean downloaded;
    SeekBar seekBar;
    int duration;
    File file;
    String filename;


    @Override
    public void onSaveInstanceState(Bundle outState){
        outState.putInt(SAVED_PROGRESS,bookProgress);
        outState.putBoolean(DOWNLOADED,downloaded);
        super.onSaveInstanceState(outState);
    }
    public void onRestoreInstanceState(Bundle saved){
        bookProgress=saved.getInt(SAVED_PROGRESS);
        downloaded=saved.getBoolean(DOWNLOADED);
    }

    public BookDetailsFragment() {
        // Required empty public constructor
    }

    public static BookDetailsFragment newInstance(BookClass book){
        //creates the book detail fragment with a book object stored in it
        BookDetailsFragment bdf = new BookDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(BOOK_CLASS,book);
        bdf.setArguments(bundle);

        return bdf;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.parentContext = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null)
            book = getArguments().getParcelable(BOOK_CLASS);
        //sets the book object so it can be used in other methods

    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         View view =inflater.inflate(R.layout.fragment_book_details, container, false);

         //reads all of the displays in the view
        TextView textView = view.findViewById(R.id.textView);
        TextView textView2 = view.findViewById(R.id.textView2);
        TextView textView3 = view.findViewById(R.id.textView3);
        ImageView imageView= view.findViewById(R.id.imageView);
        Button playButton = view.findViewById(R.id.playButton);
        Button pauseButton = view.findViewById(R.id.pauseButton);
        Button stopButton = view.findViewById(R.id.stopButton);
        Button downloadButton = view.findViewById(R.id.downloadButton);
        Button deleteButton = view.findViewById(R.id.deleteButton);
        seekBar = view.findViewById(R.id.seekBar);
        seekBar.setMax(book.getDuration());


        //sets all the displays with values from the book object
        textView.setText(book.getTitle());
        textView2.setText("The author is " + book.getAuthor());
        textView3.setText("The year published is "+ book.getPublished());
        Picasso.with(getContext()).load(book.getCoverURL()).into(imageView);
        duration=book.getDuration();


        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isRunning=true;
                if (bookProgress<=10){
                    if (downloaded){
                        ((BookDetailsInterface) parentContext ).play(file);
                    }
                    else{
                        ((BookDetailsInterface) parentContext ).play(book.getId());
                    }

                }
                else{
                    if(downloaded){
                        ((BookDetailsInterface) parentContext ).play(file,bookProgress-10);
                    }
                    else {
                        ((BookDetailsInterface) parentContext ).play(book.getId(),bookProgress-10);
                    }

                }

            }
        });

        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((BookDetailsInterface) parentContext).pause();

            }
        });

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isRunning=false;
                ((BookDetailsInterface) parentContext).stop();
                bookProgress=0;

            }
        });



        filename="File "+String.valueOf(book.getId());
        downloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(downloaded==false){
                    new Thread() {
                        @Override
                        public void run() {

                            URL downloadURL;

                            try {

                                downloadURL = new URL("https://kamorris.com/lab/audlib/download.php?id=" + book.getId());
                                file = new File(getContext().getFilesDir(), filename);

                                try (BufferedInputStream in = new BufferedInputStream(downloadURL.openStream());
                                     FileOutputStream fileOutputStream = new FileOutputStream(filename)) {
                                    byte dataBuffer[] = new byte[1024];
                                    int bytesRead;
                                    while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                                        fileOutputStream.write(dataBuffer, 0, bytesRead);
                                    }
                                } catch (IOException e) {
                                    // handle exception
                                }




                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }.start();
                }
                else {
                    Toast.makeText(getContext(), "File already downloaded", Toast.LENGTH_LONG).show();
                }
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                file.delete();
            }
        });


        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                if(fromUser){
                    ((BookDetailsInterface) parentContext).seek(progress);
                }
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        new Thread(){
            @Override
            public void run() {

                while (true){

                    try {
                        sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Message msg = Message.obtain();
                    int i = 0;
                    progressHandler.sendEmptyMessage(i);

                }
            }
        }.start();

         return view;
    }

    Handler progressHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            seekBar.setProgress(bookProgress);
            //seekBar.setProgress(bookProgress*100/764);
            Toast.makeText(getContext(),String.valueOf(bookProgress),Toast.LENGTH_LONG).show();


            return false;
        }
    });



    public static void sendProgress(int receivedProgress){
        bookProgress=receivedProgress;
    }

    interface BookDetailsInterface{
        void play(int id);
        void play(File file);
        void play(int id, int position);
        void play(File file, int position);
        void pause();
        void stop();
        void seek(int position);

    }



}
