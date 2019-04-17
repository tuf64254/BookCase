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


/**
 * A simple {@link Fragment} subclass.
 */
public class BookDetailsFragment extends Fragment {
    //This Fragment displays all information about the book object

    public static final String BOOK_CLASS = "book_class";
    BookClass book;
    Context parentContext;
    static int bookProgress;
    public boolean isRunning;
    SeekBar seekBar;
    int duration;


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
                ((BookDetailsInterface) parentContext ).play(book.getId());
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

            }
        });


        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {



                if(fromUser){
                    ((BookDetailsInterface) parentContext).seek(progress);
                    //Toast.makeText(getContext(), String.valueOf(duration), Toast.LENGTH_LONG).show();
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
            //Toast.makeText(getContext(),String.valueOf(duration),Toast.LENGTH_LONG).show();


            return false;
        }
    });

    public static void sendProgress(int receivedProgress){
        bookProgress=receivedProgress;
    }

    interface BookDetailsInterface{
        void play(int id);
        void pause();
        void stop();
        void seek(int position);

    }



}
