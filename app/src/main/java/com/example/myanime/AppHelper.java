package com.example.myanime;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.myanime.data.CommentInfo;
import com.example.myanime.data.DetailInfo;
import com.example.myanime.data.MovieInfo;
import com.example.myanime.data.ResponseInfo3;

import java.util.ArrayList;

public class AppHelper {
    public static RequestQueue requestQueue;
    public static String host = "boostcourse-appapi.connect.or.kr";
    public static int port = 10000;
    public static int TYPE_MOBILE = 1;
    public static final int TYPE_WIFI = 2;
    public static final int TYPE_UNCONNECTED = 3;
    public static SQLiteDatabase database;


    public static int getConnectStatus(Context context){
        ConnectivityManager manager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo info = (NetworkInfo)manager.getActiveNetworkInfo();
        if(info!=null){
            int type = info.getType();
            if(type == ConnectivityManager.TYPE_MOBILE){
                return TYPE_MOBILE;
            }
            else if (type == ConnectivityManager.TYPE_WIFI){
                return TYPE_WIFI;
            }
        }
        return TYPE_UNCONNECTED;
    }

    public static void openDB(Context context, String dbName){
        database = context.openOrCreateDatabase(dbName,Context.MODE_PRIVATE,null);
    }

    public static void createTable(String tableName, int id){
        if(database!=null){
            String sql="";
            if(tableName.equals("outline")){
                sql = "create table if not exists outline(" +
                        "id integer PRIMARY KEY, " +
                        "title text, " +
                        "title_eng text, " +
                        "date text, " +
                        "user_rating float, " +
                        "audience_rating float, " +
                        "reviewer_rating float, " +
                        "reservation_rate float, " +
                        "reservation_grade integer, " +
                        "grade integer, " +
                        "thumb text, " +
                        "image text" +
                        ")";
            }
            else if(tableName.equals("detail")){
                sql = "create table if not exists detail(" +
                        "id integer PRIMARY KEY, " +
                        "title text, " +
                        "date text, " +
                        "user_rating float, " +
                        "audience_rating float, " +
                        "reviewer_rating float, " +
                        "reservation_rate float, " +
                        "reservation_grade integer, " +
                        "grade integer, " +
                        "thumb text, " +
                        "image text, " +
                        "photos text, " +
                        "videos text, " +
                        "outlinks text, " +
                        "genre text, " +
                        "duration integer, " +
                        "audience integer, " +
                        "synopsis text, " +
                        "director text, " +
                        "actor text, " +
                        "likes integer, " +
                        "dislike integer" +
                        ")";
            }
            else if(tableName.equals("comment"+id)){
                sql = "create table if not exists comment"+ id +"(" +
                        "id integer PRIMARY KEY, " +
                        "writer text, " +
                        "movieId integer, " +
                        "writer_image text, " +
                        "time text, " +
                        "timestamp double, " +
                        "rating float, " +
                        "contents text, " +
                        "recommend integer" +
                        ")";
            }
            database.execSQL(sql);
        }
    }

    public static void insertOutline(MovieInfo info){
        if (database!=null){
            String sql = "insert or replace into outline values(?,?,?,?,?,?,?,?,?,?,?,?)";
            Object[] params = {info.id, info.title , info.title_eng, info.date, info.user_rating, info.audience_rating,
            info.reviewer_rating, info.reservation_rate, info.reservation_grade, info.grade, info.thumb, info.image};
            database.execSQL(sql,params);
        }
    }
    public static void insertDetail(DetailInfo info){
        if (database!=null){
            String sql = "insert or replace into detail values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            Object[] params = {info.id, info.title, info.date, info.user_rating, info.audience_rating, info.reviewer_rating,
                    info.reservation_rate, info.reservation_grade, info.grade, info.thumb, info.image, info.photos, info.videos,
            info.outlinks, info.genre, info.duration, info.audience, info.synopsis, info.director, info.actor, info.like, info.dislike};
            database.execSQL(sql,params);
        }
    }
    public static void insertComment(CommentInfo info, int id){
        if (database!=null){
            String sql = "insert or replace into comment"+ id + " values(?,?,?,?,?,?,?,?,?)";
            Object[] params = {info.id, info.writer , info.movieId, info.writer_image, info.time, info.timestamp,
                    info.rating, info.contents, info.recommend};
            database.execSQL(sql,params);
        }
    }

    public static ArrayList<MovieInfo> selectOutline(){
        ArrayList<MovieInfo> list = new ArrayList<MovieInfo>();
        String sql = "select * from outline "   ;

        Cursor cursor = database.rawQuery(sql,null);
        for (int i=0;i<cursor.getCount();i++){
            MovieInfo info = new MovieInfo();
            cursor.moveToNext();
            info.id = cursor.getInt(0);
            info.title = cursor.getString(1);
            info.title_eng = cursor.getString(2);
            info.date = cursor.getString(3);
            info.user_rating = cursor.getFloat(4);
            info.audience_rating = cursor.getFloat(5);
            info.reviewer_rating = cursor.getFloat(6);
            info.reservation_rate = cursor.getFloat(7);
            info.reservation_grade = cursor.getInt(8);
            info.grade = cursor.getInt(9);
            info.thumb = cursor.getString(10);
            info.image = cursor.getString(11);
            list.add(info);
        }
        cursor.close();
        return list;
    }

    public static DetailInfo selectDetail(int index){
        DetailInfo info = new DetailInfo();
        String sql = "select * from Detail ";

        Cursor cursor = database.rawQuery(sql,null);
        for (int i=0;i<cursor.getCount();i++) {
            cursor.moveToNext();
            if (cursor.getInt(0) == index+1){
                info.id = index+1;
                info.title = cursor.getString(1);
                info.date = cursor.getString(2);
                info.user_rating = cursor.getFloat(3);
                info.audience_rating = cursor.getFloat(4);
                info.reviewer_rating = cursor.getFloat(5);
                info.reservation_rate = cursor.getFloat(6);
                info.reservation_grade = cursor.getInt(7);
                info.grade = cursor.getInt(8);
                info.thumb = cursor.getString(9);
                info.image = cursor.getString(10);
                info.photos = cursor.getString(11);
                info.videos = cursor.getString(12);
                info.outlinks = cursor.getString(13);
                info.genre = cursor.getString(14);
                info.duration = cursor.getInt(15);
                info.audience = cursor.getInt(16);
                info.synopsis = cursor.getString(17);
                info.director = cursor.getString(18);
                info.actor = cursor.getString(19);
                info.like = cursor.getInt(20);
                info.dislike = cursor.getInt(21);
                break;
            }
        }
        cursor.close();
        return info;
    }

    public static ResponseInfo3 selectComment(int index){
        ResponseInfo3 Rinfo = new ResponseInfo3();
        String sql = "select * from comment"+(index+1)+ " order by time desc"   ;
        Cursor cursor = database.rawQuery(sql,null);
        for (int i=0;i<cursor.getCount();i++){
            CommentInfo info = new CommentInfo();
            cursor.moveToNext();
            info.id = cursor.getInt(0);
            info.writer = cursor.getString(1);
            info.movieId = cursor.getInt(2);
            info.writer_image = cursor.getString(3);
            info.time = cursor.getString(4);
            info.timestamp = cursor.getDouble(5);
            info.rating = cursor.getFloat(6);
            info.contents = cursor.getString(7);
            info.recommend = cursor.getInt(8);
            Rinfo.result.add(info);
        }
        Rinfo.totalCount = cursor.getCount();
        cursor.close();
        return Rinfo;
    }
}
