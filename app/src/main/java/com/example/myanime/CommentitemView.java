package com.example.myanime;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class CommentitemView extends LinearLayout {
    TextView userId, Date, Comment, LikeCount, LikeText, ReportView;
    RatingBar ratingBar;
    boolean like_commentState;
    int likeCount_comment;
    int id;


    public CommentitemView(Context context) {
        super(context);
        Init(context);
    }

    public CommentitemView(Context context, @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs) {
        super(context, attrs);
        Init(context);
    }

    private void Init(Context context){
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.comment_item_view, this,true);

        userId = findViewById(R.id.userId);
        Date = findViewById(R.id.Date);
        ratingBar = findViewById((R.id.ratingBar2));
        Comment = findViewById(R.id.Comment);
        LikeCount = findViewById(R.id.LikeCount);
        LikeText = findViewById(R.id.Like);
    }

    public void setUserId(String userid){
        userId.setText(userid);
    }

    public void setDate(String date){
        Date.setText(date);
    }

    public void setComment(String comment){
        Comment.setText(comment);
    }

    public void setLikeCount(String likeCount){
        LikeCount.setText(likeCount);
    }

    public void setRatingBar(float rate){
        ratingBar.setRating(rate);
    }
    public void setLikeState(boolean state){
        like_commentState = state;
        if (state) {
            LikeText.setTextColor(Color.parseColor("#06A5A0"));
            LikeCount.setTextColor(Color.parseColor("#06A5A0"));
            LikeText.setText("추천됨");
        }
        else{
            LikeText.setTextColor(Color.parseColor("#706B6B"));
            LikeCount.setTextColor(Color.parseColor("#706B6B"));
            LikeText.setText("추천");
        }
    }
    public void setCommentId(int id){
        this.id=id;
    }

    public void CommentLikeData(int id){
        String url = "http://" + AppHelper.host + ":" + AppHelper.port + "/movie/increaseRecommend";

        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.d("Response-Error", "응답3 보냄" +response);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Response-Error", "응답 오류" );
                    }
                }
        ){
            @Nullable
            @org.jetbrains.annotations.Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("review_id",String.valueOf(id));

                return params;
            }
        };

        request.setShouldCache(false);
        AppHelper.requestQueue.add(request);
    }

    public void clickListen(CommentItem item){
        LikeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                likeCount_comment = Integer.parseInt(LikeCount.getText().toString());
                /*if(like_commentState){
                    LikeText.setTextColor(Color.parseColor("#706B6B"));
                    LikeCount.setTextColor(Color.parseColor("#706B6B"));
                    LikeText.setText("추천");
                    likeCount_comment -= 1;
                }
                else*/if(!like_commentState) {
                    CommentLikeData(id);
                    LikeText.setTextColor(Color.parseColor("#06A5A0"));
                    LikeCount.setTextColor(Color.parseColor("#06A5A0"));
                    LikeText.setText("추천됨");
                    likeCount_comment += 1;
                }
                LikeCount.setText(String.valueOf(likeCount_comment));
                item.setLike(String.valueOf(likeCount_comment));
                like_commentState = /*!like_commentState;*/true;
                item.setLikeState(true);

            }
        });

        ReportView = findViewById(R.id.Report);
        ReportView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("신고");
                builder.setMessage("해당 코멘트를 신고하시겠습니까?");
                builder.setIcon(android.R.drawable.ic_dialog_alert);

                builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        View layout = inflater.inflate(R.layout.toast_custom,findViewById(R.id.toast_layout));
                        TextView text = layout.findViewById(R.id.textView23);
                        text.setText("신고가 완료되었습니다.");

                        Toast toast = new Toast(getContext());
                        toast.setDuration(Toast.LENGTH_SHORT);
                        toast.setView(layout);
                        toast.show();
                    }
                });
                builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create().show();

            }
        });

    }


}
