package com.example.myanime;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.example.myanime.data.CommentInfo;
import com.example.myanime.data.DetailInfo;
import com.example.myanime.data.MovieInfo;
import com.example.myanime.data.ResponseInfo3;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class MovieFragment extends Fragment {
    RatingBar ratingbar;
    TextView textview, likeView, hateView, title, date, duration, genre, reservation, audience, synopsis, director, actor, commentNum;
    Button button, button2, commentWrite, allComment;
    ListView listView;
    ImageView imageView, gradeImage;
    CommentAdapter adapter;
    ViewGroup rootView;
    MainActivity main;
    int grade, id;

    boolean likeState = false, hateState = false;
    int likeCount, hateCount;
    float rate;

    @Override
    public void onAttach(@NonNull @NotNull Context context) {
        super.onAttach(context);
        main = (MainActivity)getActivity();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        main = null;
    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        Log.d("TAG", "onCreateView: 생성");
        rootView = (ViewGroup)inflater.inflate(R.layout.fragment_movie,container,false);
        ratingbar = rootView.findViewById(R.id.ratingBar);
        textview = rootView.findViewById(R.id.textView12);

        button = rootView.findViewById(R.id.button);
        likeView = rootView.findViewById(R.id.textView);
        button2 = rootView.findViewById(R.id.button2);
        hateView = rootView.findViewById(R.id.textView3);
        imageView = rootView.findViewById(R.id.imageView4);
        title = rootView.findViewById(R.id.textView2);
        date = rootView.findViewById(R.id.textView5);
        duration = rootView.findViewById(R.id.textView7);
        genre = rootView.findViewById(R.id.textView9);
        reservation = rootView.findViewById(R.id.textView10);
        audience = rootView.findViewById(R.id.textView14);
        synopsis = rootView.findViewById(R.id.textView16);
        director = rootView.findViewById(R.id.textView19);
        actor = rootView.findViewById(R.id.textView21);
        gradeImage = rootView.findViewById(R.id.imageView5);
        commentNum = rootView.findViewById(R.id.textView28);
        if(likeState){
            button.setBackgroundResource(R.drawable.ic_thumb_up_selected);
        }
        else{
            button.setBackgroundResource(R.drawable.ic_thumb_up);
        }
        if(hateState){
            button2.setBackgroundResource(R.drawable.ic_thumb_down_selected);
        }
        else{
            button2.setBackgroundResource(R.drawable.ic_thumb_down);
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hateState) {
                    main.make_Toast("이미 '싫어요'를 누르셨습니다.");
                } else {
                    if (likeState) {
                        LikeHateData("N","");
                        decrLikeCount();
                    } else {
                        LikeHateData("Y","");
                        incrLikeCount();
                    }
                    likeState = !likeState;
                }

            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (likeState) {
                    main.make_Toast("이미 '좋아요'를 누르셨습니다.");
                } else {
                    if (hateState) {
                        LikeHateData("","N");
                        decrHateCount();
                    } else {
                        LikeHateData("","Y");
                        incrHateCount();
                    }
                    hateState = !hateState;
                }
            }
        });

        listView = rootView.findViewById(R.id.listView);
        adapter = new CommentAdapter();
        listView.setAdapter(adapter);

        commentWrite = rootView.findViewById(R.id.button4);
        commentWrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                main.showCommentWrite();
            }
        });

        allComment = rootView.findViewById(R.id.button5);
        allComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                main.showAllComment();
            }
        });
        return  rootView;
    }

    public void LikeHateData(String like,String hate){
        String url = "http://" + AppHelper.host + ":" + AppHelper.port + "/movie/increaseLikeDisLike";

        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.d("Response-Error", "응답2 보냄" +response);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Response-Error", "응답 오류" + error);
                    }
                }
        ){
            @Nullable
            @org.jetbrains.annotations.Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id",String.valueOf(id));
                if (like != "") {
                    params.put("likeyn",like);
                }
                if (hate != "") {
                    params.put("dislikeyn", hate);
                }
                return params;
            }
        };

        request.setShouldCache(false);
        AppHelper.requestQueue.add(request);
    }

    public void incrLikeCount(){
        likeCount = Integer.parseInt(likeView.getText().toString());
        likeCount += 1;
        likeView.setText(String.valueOf(likeCount));
        button.setBackgroundResource(R.drawable.ic_thumb_up_selected);
    }
    public void decrLikeCount(){
        likeCount = Integer.parseInt(likeView.getText().toString());
        likeCount -= 1;
        likeView.setText(String.valueOf(likeCount));
        button.setBackgroundResource(R.drawable.thumb_up);
    }
    public void incrHateCount(){
        hateCount = Integer.parseInt(hateView.getText().toString());
        hateCount += 1;
        hateView.setText(String.valueOf(hateCount));
        button2.setBackgroundResource(R.drawable.ic_thumb_down_selected);
    }
    public void decrHateCount(){
        hateCount = Integer.parseInt(hateView.getText().toString());
        hateCount -= 1;
        hateView.setText(String.valueOf(hateCount));
        button2.setBackgroundResource(R.drawable.thumb_down);
    }

    @SuppressLint("SetTextI18n")
    public void setDetail(DetailInfo info){
        id = info.id;
        Glide.with(this).load(info.thumb).into(imageView);
        title.setText(info.title);
        date.setText(info.date);
        duration.setText(info.duration+" 분");
        genre.setText(info.genre);
        likeView.setText(String.valueOf(info.like));
        hateView.setText(String.valueOf(info.dislike));
        reservation.setText(info.reservation_grade+"위  "+info.reservation_rate+"%");
        ratingbar.setRating(info.user_rating);
        rate=ratingbar.getRating();
        textview.setText(String.valueOf(info.user_rating));
        audience.setText(info.audience + " 명");
        synopsis.setText(info.synopsis);
        director.setText(info.director);
        actor.setText(info.actor);
        grade = info.grade;
        switch (grade) {
            case 12:
                gradeImage.setImageResource(R.drawable.ic_12);
                break;
            case 15:
                gradeImage.setImageResource(R.drawable.ic_15);
                break;
            case 19:
                gradeImage.setImageResource(R.drawable.ic_19);
                break;
            default:
                gradeImage.setImageResource(R.drawable.ic_all);
                break;
        }


    }

    @SuppressLint("SetTextI18n")
    public void setComment(ResponseInfo3 resinfo){
        adapter = new CommentAdapter();
        Log.d("TAG", "setComment: 설정");
        commentNum.setText(resinfo.totalCount+" 명");
        for(int i=0;i<resinfo.result.size();i++){
            CommentInfo info = resinfo.result.get(i);
            CommentItem item = new CommentItem(info.writer,info.time,info.contents,String.valueOf(info.recommend),info.rating,false,info.id);
            adapter.addItem(item);
        }
        listView.setAdapter(adapter);
        int totalHeight=0;
        for(int i=0;i<3;i++){
            View listItem = adapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * 2);
        listView.setLayoutParams(params);
        listView.requestLayout();
    }


}