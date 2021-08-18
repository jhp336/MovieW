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
    public static String host2 = "winter-form-323208.du.r.appspot.com";
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
                        "share_rate float, " +
                        "share_grade integer, " +
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
                        "share_rate float, " +
                        "share_grade integer, " +
                        "grade integer, " +
                        "thumb text, " +
                        "image text, " +
                        "photos text, " +
                        "videos text, " +
                        "outlinks text, " +
                        "genre text, " +
                        "duration integer, " +
                        "synopsis text, " +
                        "director text, " +
                        "actor text, " +
                        "likes integer, " +
                        "dislike integer, " +
                        "company text, " +
                        "viewrate float" +
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
            String sql = "insert or replace into outline values(?,?,?,?,?,?,?,?,?,?)";
            Object[] params = {info.id, info.title , info.title_eng, info.date, info.user_rating, info.share_rate,
                    info.share_grade, info.grade, info.thumb, info.image};
            database.execSQL(sql,params);
        }
    }
    public static void insertDetail(DetailInfo info){
        if (database!=null){
            String sql = "insert or replace into detail values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            Object[] params = {info.id, info.title, info.date, info.user_rating, info.share_rate, info.share_grade, info.grade,
                    info.thumb, info.image, info.photos, info.videos, info.outlinks, info.genre, info.duration, info.synopsis,
                    info.director, info.actor, info.like, info.dislike, info.company, info.highestViewRate};
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

    public static ArrayList<MovieInfo> selectOutline(int type){
        ArrayList<MovieInfo> list = new ArrayList<MovieInfo>();
        String sql = "select * from outline "   ;
        switch (type){
            case 2:
                sql += "order by user_rating desc";
                break;
            case 3:
                sql += "order by date desc";
                break;
            default:
                sql += "order by share_rate desc";
                break;
        }

        Cursor cursor = database.rawQuery(sql,null);
        for (int i=0;i<cursor.getCount();i++){
            MovieInfo info = new MovieInfo();
            cursor.moveToNext();
            info.id = cursor.getInt(0);
            info.title = cursor.getString(1);
            info.title_eng = cursor.getString(2);
            info.date = cursor.getString(3);
            info.user_rating = cursor.getFloat(4);
            info.share_rate = cursor.getFloat(5);
            info.share_grade = cursor.getInt(6);
            info.grade = cursor.getInt(7);
            info.thumb = cursor.getString(8);
            info.image = cursor.getString(9);
            list.add(info);
        }
        cursor.close();
        return list;
    }

    public static DetailInfo selectDetail(int id){
        DetailInfo info = new DetailInfo();
        String sql = "select * from Detail ";

        Cursor cursor = database.rawQuery(sql,null);
        for (int i=0;i<cursor.getCount();i++) {
            cursor.moveToNext();
            if (cursor.getInt(0) == id){
                info.id = id;
                info.title = cursor.getString(1);
                info.date = cursor.getString(2);
                info.user_rating = cursor.getFloat(3);
                info.share_rate = cursor.getFloat(4);
                info.share_grade = cursor.getInt(5);
                info.grade = cursor.getInt(6);
                info.thumb = cursor.getString(7);
                info.image = cursor.getString(8);
                info.photos = cursor.getString(9);
                info.videos = cursor.getString(10);
                info.outlinks = cursor.getString(11);
                info.genre = cursor.getString(12);
                info.duration = cursor.getInt(3);
                info.synopsis = cursor.getString(14);
                info.director = cursor.getString(15);
                info.actor = cursor.getString(16);
                info.like = cursor.getInt(17);
                info.dislike = cursor.getInt(18);
                info.company = cursor.getString(19);
                info.highestViewRate = cursor.getFloat(20);
                break;
            }
        }
        cursor.close();
        return info;
    }

    public static ResponseInfo3 selectComment(int id){
        ResponseInfo3 Rinfo = new ResponseInfo3();
        String sql = "select * from comment"+ id + " order by time desc"   ;
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
