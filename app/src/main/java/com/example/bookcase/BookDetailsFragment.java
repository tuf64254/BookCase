package com.example.bookcase;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;


/**
 * A simple {@link Fragment} subclass.
 */
public class BookDetailsFragment extends Fragment {
    //This Fragment displays all information about the book object

    public static final String BOOK_CLASS = "book_class";
    BookClass book;
    Context parentContext;


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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         View view =inflater.inflate(R.layout.fragment_book_details, container, false);

         //reads all of the displays in the view
        TextView textView = view.findViewById(R.id.textView);
        TextView textView2 = view.findViewById(R.id.textView2);
        TextView textView3 = view.findViewById(R.id.textView3);
        ImageView imageView= view.findViewById(R.id.imageView);
        Button playButton = view.findViewById(R.id.playButton);
        final Button pauseButton = view.findViewById(R.id.pauseButton);
        Button stopButton = view.findViewById(R.id.stopButton);


        //sets all the displays with vaules from the book object
        textView.setText(book.getTitle());
        textView2.setText("The author is " + book.getAuthor());
        textView3.setText("The year published is "+ book.getPublished());
        Picasso.with(getContext()).load(book.getCoverURL()).into(imageView);

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                ((BookDetailsInterface) parentContext).stop();
            }
        });




         return view;
    }

    interface BookDetailsInterface{
        void play(int id);
        void pause();
        void stop();
    }


}
