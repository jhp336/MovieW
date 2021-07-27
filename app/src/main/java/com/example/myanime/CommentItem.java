package com.example.myanime;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.RequiresApi;

public class CommentItem implements Parcelable {
    String userId, Date, Comment, Like;
    boolean likeState;
    float Rate;
    public CommentItem(String userId, String date, String comment, String like, float rate, boolean likeState) {
        this.userId = userId;
        this.Date = date;
        this.Comment = comment;
        this.Like = like;
        this.Rate = rate;
        this.likeState = likeState;
    }
    @RequiresApi(api = Build.VERSION_CODES.Q)
    public  CommentItem(Parcel src) {
        userId = src.readString();
        Date = src.readString();
        Comment = src.readString();
        Like = src.readString();
        Rate = src.readFloat();
        likeState = src.readBoolean();
    }
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator(){
        @RequiresApi(api = Build.VERSION_CODES.Q)
        public CommentItem createFromParcel(Parcel src){
            return new CommentItem(src);
        }

        public CommentItem[] newArray(int size){
            return new CommentItem[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userId);
        dest.writeString(Date);
        dest.writeString(Comment);
        dest.writeString(Like);
        dest.writeFloat(Rate);
        dest.writeBoolean(likeState);
    }



    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getComment() {
        return Comment;
    }

    public void setComment(String comment) {
        Comment = comment;
    }

    public String getLike() {
        return Like;
    }

    public void setLike(String like) {
        Like = like;
    }

    public float getRate() {
        return Rate;
    }

    public void setRate(float rate) {
        Rate = rate;
    }

    public boolean isLikeState() {
        return likeState;
    }
    public void setLikeState(boolean likeState) {
        this.likeState = likeState;
    }
}
