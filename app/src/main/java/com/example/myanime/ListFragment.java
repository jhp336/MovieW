package com.example.myanime;

import android.annotation.SuppressLint;
import android.content.Context;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.myanime.data.MovieInfo;
import com.example.myanime.data.ResponseInfo;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ListFragment extends Fragment {
    MainActivity main;
    MoviePageAdapter adapter;
    ViewPager2 pager;
    Animation translateDown;
    Animation translateUp;
    Boolean isOpen;
    int currentSort = 1;
    ImageView sortView;
    LinearLayout sortLayout;
    @Override
    public void onAttach(@NonNull @NotNull Context context) {
        super.onAttach(context);
        main=(MainActivity)getActivity();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        main=null;
        adapter=null;
        pager=null;
        Log.d("TAG", "onDetach: list");
    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_list,container,false);
        Log.d("createview", "onCreateView: on");
        isOpen = false;
        sortView = main.findViewById(R.id.sortView);
        sortView.setVisibility(View.VISIBLE);
        sortLayout = rootView.findViewById(R.id.sort_layout);
        translateDown = AnimationUtils.loadAnimation(getContext(),R.anim.translate_down);
        translateUp = AnimationUtils.loadAnimation(getContext(),R.anim.translate_up);
        translateUp.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                sortLayout.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        sortView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TAG", "onClick: sort 클릭");
                if(isOpen){
                    sortLayout.startAnimation(translateUp);
                }
                else{
                    sortLayout.setVisibility(View.VISIBLE);
                    sortLayout.bringToFront();
                    sortLayout.startAnimation(translateDown);
                }
                isOpen = !isOpen;
            }
        });

        if(main.getSupportActionBar()!=null)
            main.getSupportActionBar().setTitle("영화 목록");

        pager = rootView.findViewById(R.id.pager);
        pager.setOffscreenPageLimit(3);
        adapter = new MoviePageAdapter(this);
        int status = AppHelper.getConnectStatus(getContext());
        if (status != AppHelper.TYPE_UNCONNECTED) {
            readMovieList(currentSort);
        } else {
            main.make_Toast("인터넷에 연결이 안 되었음!!");
            ArrayList<MovieInfo> list= AppHelper.selectOutline(currentSort);
            setInfo(list);
        }
        pager.setAdapter(adapter);
        Button button = rootView.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int cur = pager.getCurrentItem();
                main.detailButton(cur,adapter.getItem(cur).id);
            }
        });

        ImageView sort1 = rootView.findViewById(R.id.sortBtn1);
        ImageView sort2 = rootView.findViewById(R.id.sortBtn2);
        ImageView sort3 = rootView.findViewById(R.id.sortBtn3);

        sort1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentSort != 1) {
                    currentSort = 1;
                    if (status != AppHelper.TYPE_UNCONNECTED) {
                        readMovieList(currentSort);
                    }
                    else {
                        main.make_Toast("인터넷에 연결이 안 되었음!!");
                        ArrayList<MovieInfo> list= AppHelper.selectOutline(currentSort);
                        setInfo(list);
                    }
                }
                sortLayout.startAnimation(translateUp);
                isOpen = false;
            }
        });
        sort2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentSort != 2) {
                    currentSort = 2;
                    if (status != AppHelper.TYPE_UNCONNECTED) {
                        readMovieList(currentSort);
                    }
                    else {
                        main.make_Toast("인터넷에 연결이 안 되었음!!");
                        ArrayList<MovieInfo> list= AppHelper.selectOutline(currentSort);
                        setInfo(list);
                    }
                }
                sortLayout.startAnimation(translateUp);
                isOpen = false;
            }
        });
        sort3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentSort != 3) {
                    currentSort = 3;
                    if (status != AppHelper.TYPE_UNCONNECTED) {
                        readMovieList(currentSort);
                    }
                    else {
                        main.make_Toast("인터넷에 연결이 안 되었음!!");
                        ArrayList<MovieInfo> list= AppHelper.selectOutline(currentSort);
                        setInfo(list);
                    }
                }
                sortLayout.startAnimation(translateUp);
                isOpen = false;
            }
        });

        return rootView;
    }

    public void setInfo(ArrayList<MovieInfo> list){
        switch (currentSort) {
            case 2:
                sortView.setImageResource(R.drawable.order22);
                break;
            case 3:
                sortView.setImageResource(R.drawable.order33);
                break;
            default:
                sortView.setImageResource(R.drawable.order11);
                break;
        }
        adapter = null;
        adapter = new MoviePageAdapter(this);
        Log.d("setinfo", "setInfo: on");
        for(int i=0;i<list.size();i++){
            AppHelper.createTable("comment"+(i+1), i+1);
            MovieInfo info = list.get(i);
            AppHelper.insertOutline(info);
            Member_list mem = new Member_list();
            mem.setMemInfo(info,i+1);

            adapter.addItem(mem);
        }
        pager.setAdapter(adapter);
        pager.setCurrentItem(main.listItemNumber,false);
        main.listItemNumber = 0;
    }

    public void readMovieList(int type){
        String url = "http://" + AppHelper.host + ":" + AppHelper.port + "/home/readMovieList";/*"/movie/readMovieList";*/
        url += "?" + "type=" + type;
        StringRequest request = new StringRequest(
                Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.d("Response-Error", "응답1 옴");
                            processResponse(response);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Response-Error", "응답 오류");
                        currentSort=0;
                    }
                }
        );
        request.setShouldCache(false);
        AppHelper.requestQueue.add(request);
    }
    @SuppressLint("SetTextI18n")
    public void processResponse(String response){
        Gson gson = new Gson();
        ResponseInfo responseInfo = gson.fromJson(response, ResponseInfo.class);
        if (responseInfo.code == 200) {
            setInfo(responseInfo.result);
        }
    }
}
