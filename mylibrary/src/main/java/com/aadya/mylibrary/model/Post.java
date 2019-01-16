package com.aadya.mylibrary.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class Post implements Parcelable{

    private int id;

    private String name;

    public Post() {
        super();
    }

    public Post(int id, String name) {
        this.id = id;
        this.name = name;
    }

    protected Post(Parcel in) {
        id = in.readInt();
        name = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Post> CREATOR = new Creator<Post>() {
        @Override
        public Post createFromParcel(Parcel in) {
            return new Post(in);
        }

        @Override
        public Post[] newArray(int size) {
            return new Post[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    public static List<Post> getAllPost(){

        List<Post> tempList = new ArrayList<Post>();

        tempList.add(new Post(1, "Hindi"));
        tempList.add(new Post(2, "Bengali"));
        tempList.add(new Post(3, "Marathi"));
        tempList.add(new Post(4, "Telugu"));
        tempList.add(new Post(5, "Tamil"));
        tempList.add(new Post(6, "Urdu"));
        tempList.add(new Post(7, "Gujarati"));
        tempList.add(new Post(8, "Kannada"));
        tempList.add(new Post(9, "Malayalam"));
        tempList.add(new Post(10, "Odia"));
        tempList.add(new Post(11, "Punjabi"));
        tempList.add(new Post(12, "Assamese"));
        tempList.add(new Post(13, "Maithili"));
        tempList.add(new Post(14, "English"));

        return tempList;
    }
}
