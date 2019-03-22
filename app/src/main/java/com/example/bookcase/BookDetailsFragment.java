package com.example.bookcase;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class BookDetailsFragment extends Fragment {

    public static final String BOOK_INDEX = "book_index";
    int bookIndex;


    public BookDetailsFragment() {
        // Required empty public constructor
    }

    public static BookDetailsFragment newInstance(int bookIndex){
        BookDetailsFragment bdf = new BookDetailsFragment();

        Bundle bundle = new Bundle();
        bundle.putInt(BOOK_INDEX,bookIndex);
        bdf.setArguments(bundle);

        return bdf;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null)
            bookIndex = getArguments().getInt(BOOK_INDEX);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         View view =inflater.inflate(R.layout.fragment_book_details, container, false);

        TextView textView = view.findViewById(R.id.textView);
        String[] bookArray= getResources().getStringArray(R.array.books);
        textView.setText(bookArray[bookIndex]);

         return view;
    }



}
