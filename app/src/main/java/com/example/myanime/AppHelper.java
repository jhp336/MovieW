package com.example.myanime;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.myanime.data.MovieInfo;

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

    public static void createTable(String tableName){
        if(database!=null){
            if(tableName.equals("outline")){
                String outlineSql = "create table if not exists outline(" +
                        "_id integer PRIMARY KEY autoincrement, " +
                        "id integer, " +
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
                database.execSQL(outlineSql);
            }
            else if(tableName.equals("detail")){
                String detailSql = "create table if not exists detail(" +
                        "_id integer PRIMARY KEY autoincrement, " +
                        "id integer, " +
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
                database.execSQL(detailSql);
            }
            else if(tableName.equals("comment")){
                String commentSql = "create table if not exists comment(" +
                        "_id integer PRIMARY KEY autoincrement, " +
                        "id integer, " +
                        "writer text, " +
                        "movieId integer, " +
                        "writer_image text, " +
                        "time text, " +
                        "timestamp double, " +
                        "rating float, " +
                        "contents text, " +
                        "recommend integer" +
                        ")";
                database.execSQL(commentSql);
            }
        }
    }

    public static void insertOutline(MovieInfo info){
        if (database!=null){
            String sql = "insert into outline values(?)";
            Object[] params = {info.id, info.title , info.title_eng, info.date, info.user_rating, info.audience_rating,
            info.reviewer_rating, info.reservation_rate, info.reservation_grade, info.grade, info.thumb, info.image};
            database.execSQL(sql,params);
        }

    }

}
