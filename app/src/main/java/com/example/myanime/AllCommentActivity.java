package com.example.myanime;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.myanime.data.CommentInfo;
import com.example.myanime.data.ResponseInfo3;
import com.google.gson.Gson;

import java.util.ArrayList;

public class AllCommentActivity extends AppCompatActivity {
    Button commentWrite;
    RatingBar ratingBar;
    TextView rateView, title, rateNum;
    ImageView imageView;
    ListView listView;
    CommentAdapter adapter;
    int grade, id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_comment);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        title = findViewById(R.id.textView26);
        imageView = findViewById(R.id.imageView);
        rateNum = findViewById(R.id.rateNum);
        listView = findViewById(R.id.listView2);
        Intent passedIntent = getIntent();
        processIntent(passedIntent);

        requestCommentList(id);
        listView.setAdapter(adapter);

        commentWrite = findViewById(R.id.commentWrite);
        commentWrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCommentWrite();
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent();

        setResult(RESULT_OK, intent);
        finish();
    }

    public void showCommentWrite() {
        Intent intent = new Intent(this, CommentWriteActivity.class);
        intent.putExtra("where",1);
        intent.putExtra("title", title.getText());
        intent.putExtra("grade", grade);
        intent.putExtra("id", id);
        launcher.launch(intent);
    }

    ActivityResultLauncher<Intent> launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK && result.getData()!=null) {
                        Log.d("TAG", "onActivityResult: "+result.getData());
                        requestCommentList(id);
                    }
                }
            });

    @SuppressLint("SetTextI18n")
    private void processIntent(Intent intent){
        title.setText(intent.getStringExtra("title"));
        id = intent.getIntExtra(("id"),0);
        grade = intent.getIntExtra("grade",0);
        switch (grade) {
            case 12:
                imageView.setImageResource(R.drawable.ic_12);
                break;
            case 15:
                imageView.setImageResource(R.drawable.ic_15);
                break;
            case 19:
                imageView.setImageResource(R.drawable.ic_19);
                break;
            default:
                imageView.setImageResource(R.drawable.ic_all);
                break;
        }

        ratingBar = findViewById(R.id.ratingBar5);
        float rateAvg = intent.getFloatExtra("rateAvg",0);
        ratingBar.setRating(rateAvg);
        rateView = findViewById(R.id.rate);
        rateView.setText(String.valueOf(rateAvg));
    }

    public void requestCommentList(int id){
        String url = "https://" + AppHelper.host + "/home/readCommentList";
        url += "?" + "id=" + id ;
        StringRequest request = new StringRequest(
                Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.d("Response-Error", "gg");
                            processResponse3(response);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Response-Error", "응답 오류");
                    }
                }
        );
        request.setShouldCache(false);
        AppHelper.requestQueue.add(request);

    }
    @SuppressLint("SetTextI18n")
    public void processResponse3(String response){
        Gson gson = new Gson();
        ResponseInfo3 commentInfo = gson.fromJson(response, ResponseInfo3.class);
        adapter = new CommentAdapter();

        if (commentInfo.code == 200) {
            rateNum.setText("("+commentInfo.totalCount+" 명)");
            for(int i=0;i<commentInfo.result.size();i++){
                CommentInfo info = commentInfo.result.get(i);
                CommentItem item = new CommentItem(info.writer,info.time,info.contents,String.valueOf(info.recommend),info.rating,false,info.id, info.movieId);
                adapter.addItem(item);
            }
            listView.setAdapter(adapter);
        }

    }
}