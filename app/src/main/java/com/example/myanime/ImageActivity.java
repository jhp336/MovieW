package com.example.myanime;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ImageActivity extends AppCompatActivity {
    int position;
    ArrayList<GalleryItem>list;
    LinearLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("TAG", "onCreate: 이미지 보기");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        layout = findViewById(R.id.index_layout);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("사진 보기");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        processIntent(getIntent());

        ViewPager2 pager = findViewById(R.id.pager);
        pager.setOffscreenPageLimit(1);
        ImageAdapter adapter = new ImageAdapter(getBaseContext(),list);
        pager.setAdapter(adapter);
        pager.setCurrentItem(position,false);
        pager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                indexChange(position);
            }
        });
        setIndexIndicator(adapter.getItemCount());

    }


    public void setIndexIndicator(int count){
        ImageView[] indicator = new ImageView[count];
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        params.setMargins(16, 0, 16, 0);

        for(int i=0;i<count;i++){
            indicator[i] = new ImageView(this);
            indicator[i].setImageResource(R.drawable.image_index_notselect);
            indicator[i].setLayoutParams(params);
            layout.addView(indicator[i]);
        }
        indexChange(position);
    }

    public void indexChange(int position){
        for(int i=0;i<layout.getChildCount();i++){
            ImageView imageView = (ImageView)layout.getChildAt(i);
            if(position == i){
                imageView.setImageResource(R.drawable.image_index_select);
            }
            else {
                imageView.setImageResource(R.drawable.image_index_notselect);
            }
        }
    }

    public void processIntent(Intent intent){
        position = intent.getIntExtra("position",0);
        list = intent.getParcelableArrayListExtra("list");
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }


}