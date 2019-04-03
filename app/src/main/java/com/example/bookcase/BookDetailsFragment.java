package com.example.bookcase;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;


/**
 * A simple {@link Fragment} subclass.
 */
public class BookDetailsFragment extends Fragment {

    public static final String BOOK_INDEX = "book_index";
    BookClass book;


    public BookDetailsFragment() {
        // Required empty public constructor
    }

    public static BookDetailsFragment newInstance(BookClass book){
        //attaches the book index to a new instance
        BookDetailsFragment bdf = new BookDetailsFragment();

        Bundle bundle = new Bundle();
        bundle.putParcelable(BOOK_INDEX,book);
        bdf.setArguments(bundle);

        return bdf;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null)
            book = getArguments().getParcelable(BOOK_INDEX);
        //sets the book index so it can be used in other methods

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         View view =inflater.inflate(R.layout.fragment_book_details, container, false);

        TextView textView = view.findViewById(R.id.textView);
        TextView textView2 = view.findViewById(R.id.textView2);
        TextView textView3 = view.findViewById(R.id.textView3);
        ImageView imageView= view.findViewById(R.id.imageView);


        textView.setText(book.getTitle());
        textView2.setText("The author is " + book.getAuthor());
        textView3.setText("The year published is "+ book.getPublished());
        Picasso.with(getContext()).load(book.getCoverURL()).into(imageView);




         return view;
    }



}
