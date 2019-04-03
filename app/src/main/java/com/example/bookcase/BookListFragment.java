package com.example.bookcase;


import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class BookListFragment extends Fragment { //this has a list view and returns selected book to main with interface

    ListView listView;
    Context parentContext;
    public static final String BOOK_LIST = "Book_list";
    ArrayList<BookClass> bookList;

    public BookListFragment() {
        // Required empty public constructor
    }

    public static BookListFragment newInstance(ArrayList<? extends Parcelable> bookList){
        //stores the start index for the view pager and bundles it so it can be used in the other methods
        BookListFragment blf = new BookListFragment();

        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(BOOK_LIST, bookList);
        blf.setArguments(bundle);

        return blf;
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        this.parentContext = context;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null)
            bookList = getArguments().getParcelableArrayList(BOOK_LIST); //sets the start index from the previously created instance

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_book_list, container, false);
        ArrayList<String> bookNames= new ArrayList<String>();
        listView = v.findViewById(R.id.bookListView);


        for(int i=0; i<bookList.size();i++){
            bookNames.add(bookList.get(i).getTitle());
        }

        listView.setAdapter(new ArrayAdapter<String>(parentContext, android.R.layout.simple_list_item_1, bookNames));
        //attaches list view to an array adapter and fills it with the book array


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parentView, View view, int position, long id) {
                int bookIndex = position;

                ((BookListInterface) parentContext).bookselected(bookIndex);
            }
        }); //after a book is selected transfers the index to main by using the interface

        return v;
    }


    interface BookListInterface{
        void bookselected(int bookIndex); //forces main to implement this function
    }

}
