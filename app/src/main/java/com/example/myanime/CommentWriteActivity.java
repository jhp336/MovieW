package com.example.myanime;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;

import java.util.ArrayList;

public class CommentWriteActivity extends AppCompatActivity {
    Button saveButton, cancelButton;
    EditText editText;
    RatingBar ratingBar;
    String contents;
    float rate;
    int where;
    ArrayList<CommentItem> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_write);

        where = getIntent().getIntExtra("where",0);
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
                    returnToAllView(contents,rate);
                    finish();
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

    private void returnToAllView(String contents, float rate){
        if(where==1) { // 모두보기에서 작성 -> 모두보기로 돌아감
            Intent intent = new Intent();
            intent.putExtra("contents", contents);
            intent.putExtra("rate", rate);
            setResult(RESULT_OK, intent);
            finish();
        }
        else{// 상세보기에서 바로 작성 -> 모두보기로
            float rateAvg = getIntent().getFloatExtra("rate",0);
            list = getIntent().getParcelableArrayListExtra("list");
            list.add(new CommentItem("wldbs03", "1분 전", contents, "0", rate,false));
            Intent intent2 = new Intent(this, AllCommentActivity.class);
            intent2.putExtra("rateAvg",rateAvg);
            intent2.putExtra("list",list);
            launcher.launch(intent2);
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