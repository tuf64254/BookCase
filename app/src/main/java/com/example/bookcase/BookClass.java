package com.example.bookcase;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

public class BookClass implements Parcelable{ //Parcelable interface needed to bundle into fragments
    private int id;
    private String title;
    private String author;
    private int duration;
    private int published;
    private String coverURL;


    public BookClass(int id, String title, String author, int duration, int published, String coverURL){
        //creates Book object
        this.id = id;
        this.title = title;
        this.author = author;
        this.duration = duration;
        this.published = published;
        this.coverURL = coverURL;


    }

    public BookClass (JSONObject bookObject) throws JSONException{
        //creates Book object out of JSON
        this(bookObject.getInt("book_id"), bookObject.getString("title"), bookObject.getString("author"), bookObject.getInt("duration"),
                bookObject.getInt("published"), bookObject.getString("cover_url"));
    }

    //functions returning object variable values
    public int getId(){return id;}
    public String getTitle() {
        return title;
    }
    public String getAuthor(){ return author;}
    public int getDuration() {return duration;}
    public int getPublished(){ return published;}
    public String getCoverURL(){ return coverURL;}


    //below functions all required from interface for storing object in a parcel

    protected BookClass(Parcel in) {
        id = in.readInt();
        title = in.readString();
        author = in.readString();
        duration = in.readInt();
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
        dest.writeInt(duration);
        dest.writeInt(published);
        dest.writeString(coverURL);

    }
}
