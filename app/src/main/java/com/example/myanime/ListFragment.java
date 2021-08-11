package com.example.myanime;

import android.annotation.SuppressLint;
import android.content.Context;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


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
        pager = rootView.findViewById(R.id.pager);
        pager.setOffscreenPageLimit(3);
        adapter = new MoviePageAdapter(this);
        int status = AppHelper.getConnectStatus(getContext());
        if (status != AppHelper.TYPE_UNCONNECTED) {
            readMovieList();
        } else {
            main.make_Toast("인터넷에 연결이 안 되었음!!");
            ArrayList<MovieInfo> list= AppHelper.selectOutline();
            setInfo(list);;
        }
        pager.setAdapter(adapter);
        Button button = rootView.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                main.detailButton(pager.getCurrentItem());
            }
        });

        return rootView;
    }

    public void setInfo(ArrayList<MovieInfo> list){
        Log.d("setinfo", "setInfo: on");
        for(int i=0;i<list.size();i++){
            AppHelper.createTable("comment"+(i+1), i+1);
            MovieInfo info = list.get(i);
            AppHelper.insertOutline(info);
            Member_list mem = new Member_list();
            mem.setMemInfo(info);

            adapter.addItem(mem);
        }
        pager.setAdapter(adapter);
        pager.setCurrentItem(main.listItemNumber,false);
        adapter=null;
    }

    public void readMovieList(){
        String url = "http://" + AppHelper.host + ":" + AppHelper.port + "/movie/readMovieList";
        url += "?" + "type=1";
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
