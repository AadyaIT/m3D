package com.aadya.mylibrary.model;

import android.os.Parcel;
import android.os.Parcelable;

public class MyVideo implements Parcelable{
    String id, videoUrl, videoLabel;

    public MyVideo() {
        super();
    }

    public MyVideo(String id, String videoUrl, String videoLabel) {
        this.id = id;
        this.videoUrl = videoUrl;
        this.videoLabel = videoLabel;
    }

    protected MyVideo(Parcel in) {
        id = in.readString();
        videoUrl = in.readString();
        videoLabel = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(videoUrl);
        dest.writeString(videoLabel);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MyVideo> CREATOR = new Creator<MyVideo>() {
        @Override
        public MyVideo createFromParcel(Parcel in) {
            return new MyVideo(in);
        }

        @Override
        public MyVideo[] newArray(int size) {
            return new MyVideo[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getVideoLabel() {
        return videoLabel;
    }

    public void setVideoLabel(String videoLabel) {
        this.videoLabel = videoLabel;
    }

    @Override
    public String toString() {
        return "MyVideo{" +
                "id='" + id + '\'' +
                ", videoUrl='" + videoUrl + '\'' +
                ", videoLabel='" + videoLabel + '\'' +
                '}';
    }
}
