package com.example.myanime;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.renderscript.Sampler;
import android.util.Log;
import android.view.MenuItem;


import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.myanime.data.MovieInfo;
import com.example.myanime.data.ResponseInfo;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,FragmentCallback {
    ListFragment listFragment;
    MovieFragment movieFragment;
    DrawerLayout drawer;
    NavigationView navigationView;
    boolean isLayoutList;
    ResponseInfo responseInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("영화 목록");
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.layout_drawer);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);
        listFragment = new ListFragment();
        movieFragment = new MovieFragment();
        Log.d("asdaasdadadasd", "onCreate: onzzzzzzzzzzzzz");

        Log.d("ㅋㅋㅋㅋㅋㅋ", "onCreate: frag 끝");
        if(AppHelper.requestQueue == null) {
            AppHelper.requestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        if(responseInfo==null) {
            readMovieList();
        }
        getSupportFragmentManager().beginTransaction().add(R.id.container,listFragment).commit();
        isLayoutList=true;
    }

    @Override
    public void onBackPressed() {
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }
        else if(isLayoutList){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("종료");
            builder.setMessage("앱을 종료하시겠습니까?");
            builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
        else {
            isLayoutList=true;
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav0) {
            item.setIcon(R.drawable.ic_list_selected);
            onFragmentSelected(0, null);
            drawer.closeDrawer(GravityCompat.START);
        }
        else {
            MenuItem item_nav0 = navigationView.getMenu().getItem(0);
            item_nav0.setIcon(R.drawable.ic_list);
        }
        return true;
    }

    @Override
    public void onFragmentSelected(int position, Bundle bundle) {
        Fragment cur;
        if (position ==0){
            cur = listFragment;
            getSupportFragmentManager().beginTransaction().replace(R.id.container,cur).commit();
            isLayoutList=true;
        }
    }

    public void readMovieList(){
        Log.d("Response-Error","응답 ???");
        String url = "http://" + AppHelper.host + ":" + AppHelper.port + "/movie/readMovieList";
        url += "?" + "type=1";
        StringRequest request = new StringRequest(
                Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.d("Response-Error", "응답 옴");
                            processResponse(response);
                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Response-Error","응답 오류");
                    }
                }
        );

        request.setShouldCache(false);
        AppHelper.requestQueue.add(request);

    }

    public void processResponse(String response){
        Log.d("Response-Error","응답 ㅋㅋㅋㅋ");

        Gson gson = new Gson();
        responseInfo = gson.fromJson(response, ResponseInfo.class);
        if(responseInfo.code == 200) {
            listFragment.setInfo(responseInfo);
        }
    }

    public void detailButton(){
        getSupportFragmentManager().beginTransaction().replace(R.id.container,movieFragment).addToBackStack("aa").commit();
        isLayoutList=false;
    }

    //코멘트 작성
    public void showCommentWrite() {
        Intent intent = new Intent(this, CommentWriteActivity.class);
        intent.putExtra("where",0);
        intent.putExtra("rateAvg",movieFragment.rate);
        intent.putExtra("list",movieFragment.adapter.items);
        launcher2.launch(intent);
    }

    ActivityResultLauncher<Intent> launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK && result.getData()!=null) {
                        movieFragment.adapter.items=result.getData().getParcelableArrayListExtra("list");

                        /*
                            String contents = result.getData().getStringExtra("contents");
                            float rate = result.getData().getFloatExtra("rate", 0);
                            movieFragment.adapter.addItem(new CommentItem("wldbs03", "1분 전", contents, "0", rate, false));
                        */
                        movieFragment.listView.setAdapter(movieFragment.adapter);
                    }
                }
            });


    //코멘트 모두보기
    public void showAllComment(){
        Intent intent = new Intent(this, AllCommentActivity.class);
        intent.putExtra("rateAvg",movieFragment.rate);
        intent.putExtra("list",movieFragment.adapter.items);
        launcher2.launch(intent);
    }

    ActivityResultLauncher<Intent> launcher2 = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK && result.getData()!=null) {
                        movieFragment.adapter.items = result.getData().getParcelableArrayListExtra("list");
                        movieFragment.listView.setAdapter(movieFragment.adapter);
                    }
                }
            });

    /*@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && isLayoutList) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("종료");
            builder.setMessage("앱을 종료하시겠습니까?");
            builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();

            return true;
        }

        return false;
    }*/
}