package com.example.myanime;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;

public class AllCommentActivity extends AppCompatActivity {
    Button commentWrite;
    RatingBar ratingBar;
    TextView rateView;
    ArrayList<CommentItem> list;
    ListView listView;
    CommentAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_comment);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


        Intent passedIntent = getIntent();
        processIntent(passedIntent);
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
        intent.putParcelableArrayListExtra("list", adapter.items);

        setResult(RESULT_OK, intent);
        finish();
    }

    public void showCommentWrite() {
        Intent intent = new Intent(this, CommentWriteActivity.class);
        intent.putExtra("where",1);
        launcher.launch(intent);
    }

    ActivityResultLauncher<Intent> launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK && result.getData()!=null) {
                        String contents = result.getData().getStringExtra("contents");
                        float rate = result.getData().getFloatExtra("rate", 0);
                        adapter.addItem(new CommentItem("wldbs03", "1분 전", contents, "0", rate,false));
                        listView.setAdapter(adapter);
                    }
                }
            });

    private void processIntent(Intent intent){
        ratingBar = findViewById(R.id.ratingBar5);
        float rateAvg = intent.getFloatExtra("rateAvg",0);
        ratingBar.setRating(rateAvg);
        rateView = findViewById(R.id.rate);
        rateView.setText(String.valueOf(rateAvg));

        list = intent.getParcelableArrayListExtra("list");
        listView = findViewById(R.id.listView2);
        adapter = new CommentAdapter();
        adapter.items = list;

        listView.setAdapter(adapter);

    }
}