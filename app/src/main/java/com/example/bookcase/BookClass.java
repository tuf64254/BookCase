package com.example.bookcase;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

public class BookClass implements Parcelable {
    private int id;
    private String title;
    private String author;
    private int published;
    private String coverURL;

    public BookClass(int id, String title, String author, int published, String coverURL){
        this.id = id;
        this.title = title;
        this.author = author;
        this.published = published;
        this.coverURL = coverURL;

    }



    public BookClass (JSONObject bookObject) throws JSONException{
        this(bookObject.getInt("id"), bookObject.getString("title"), bookObject.getString("author"),
                bookObject.getInt("published"), bookObject.getString("coverURL"));
    }

    public String getTitle() {
        return title;
    }

    protected BookClass(Parcel in) {
        id = in.readInt();
        title = in.readString();
        author = in.readString();
        published = in.readInt();
        coverURL = in.readString();
    }

    public static final Creator<BookClass> CREATOR = new Creator<BookClass>() {
        @Override
        public BookClass createFromParcel(Parcel in) {
            return new BookClass(in);
        }

        @Override
        public BookClass[] newArray(int size) {
            return new BookClass[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(author);
        dest.writeInt(published);
        dest.writeString(coverURL);
    }
}
