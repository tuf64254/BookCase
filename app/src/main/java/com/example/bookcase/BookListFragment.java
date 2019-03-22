package com.example.bookcase;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class BookListFragment extends Fragment {

    ListView listView;
    Context parentContext;
    public BookListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        this.parentContext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_book_list, container, false);

        listView = v.findViewById(R.id.bookListView);

        listView.setAdapter(new ArrayAdapter<>(parentContext, android.R.layout.simple_list_item_1,
                parentContext.getResources().getStringArray(R.array.books)));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parentView, View view, int position, long id) {
                int bookIndex = position;

                ((BookListInterface) parentContext).bookselected(bookIndex);
            }
        });


        return v;
    }

    interface BookListInterface{
        void bookselected(int bookIndex);
    }

}
