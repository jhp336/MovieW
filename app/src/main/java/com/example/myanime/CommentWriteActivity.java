package com.example.myanime;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CommentWriteActivity extends AppCompatActivity {
    Button saveButton, cancelButton;
    TextView title;
    ImageView imageView;
    EditText editText;
    RatingBar ratingBar;
    String contents, titleStr;
    float rate, rateAvg;
    int where, grade ,id;
    ArrayList<CommentItem> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_write);

        title = findViewById(R.id.textView24);
        imageView = findViewById(R.id.imageView);
        Intent passedIntent = getIntent();
        processIntent(passedIntent);

        saveButton = findViewById(R.id.button9);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ratingBar = findViewById(R.id.ratingBar4);
                rate = ratingBar.getRating();
                editText = findViewById(R.id.editText1);
                contents = editText.getText().toString();

                if(rate == 0){
                    MakeAlert("평점을 입력해 주세요!\n 0.5 ~ 5.0");
                }
                else if(contents.trim().length()>0) {
                    dataToServer(contents,rate);
                    returnToAllView();
                }
                else {
                    MakeAlert("코멘트를 입력해 주세요");
                }
            }
        });

        cancelButton = findViewById(R.id.button10);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void returnToAllView(){
        if(where==1) { // 모두보기에서 작성 -> 모두보기로 돌아감
            finish();
        }
        else{// 상세보기에서 바로 작성 -> 모두보기로
            Intent intent = new Intent(this, AllCommentActivity.class);
            intent.putExtra("rateAvg",rateAvg);
            intent.putExtra("title",titleStr);
            intent.putExtra("grade",grade);
            intent.putExtra("id",id);
            launcher.launch(intent);
        }
    }

    ActivityResultLauncher<Intent> launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK && result.getData()!=null) {
                        list = result.getData().getParcelableArrayListExtra("list");
                        Intent intent = new Intent();
                        intent.putParcelableArrayListExtra("list",list);
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                }
            });

    public void dataToServer(String contents, float rate){
        String url = "http://" + AppHelper.host + ":" + AppHelper.port + "/movie/createComment";

        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.d("Response-Error", "응답 옴");
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
        ){
            @Nullable
            @org.jetbrains.annotations.Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id",String.valueOf(id));
                params.put("writer","testUser");
                params.put("rating",String.valueOf(rate));
                params.put("contents",contents);

                return params;
            }
        };

        request.setShouldCache(false);
        AppHelper.requestQueue.add(request);
    }

    private void processIntent(Intent intent){
        where = intent.getIntExtra("where",1);
        titleStr = intent.getStringExtra("title");
        title.setText(titleStr);
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

        rateAvg = intent.getFloatExtra("rateAvg",0);
    }

    public void MakeAlert(String Message){
        AlertDialog.Builder builder = new AlertDialog.Builder(CommentWriteActivity.this);
        builder.setTitle("코멘트 작성");
        builder.setMessage(Message);
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

}