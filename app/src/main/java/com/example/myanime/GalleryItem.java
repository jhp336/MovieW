package com.example.myanime;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.RequiresApi;

public class GalleryItem implements Parcelable{
    String url;
    boolean isVideo;

    public GalleryItem(String url, boolean isVideo) {
        this.url = url;
        this.isVideo = isVideo;
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    public GalleryItem(Parcel src) {
        url = src.readString();
        isVideo = src.readBoolean();
    }
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator(){
        @RequiresApi(api = Build.VERSION_CODES.Q)
        public GalleryItem createFromParcel(Parcel src){
            return new GalleryItem(src);
        }

        public GalleryItem[] newArray(int size){
            return new GalleryItem[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(url);
        dest.writeBoolean(isVideo);
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isVideo() {
        return isVideo;
    }

    public void setVideo(boolean video) {
        isVideo = video;
    }
}
