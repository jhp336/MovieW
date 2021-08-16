package com.example.myanime;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.TransitionOptions;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.example.myanime.data.MovieInfo;

import org.jetbrains.annotations.NotNull;

public class Member_list extends Fragment {
    MainActivity main;
    ImageView movieImage;
    TextView title, reservationRate, rate, grade;
    String titleStr, reservationRateStr, gradeStr, rateStr, urlStr;
    int id;

    @Override
    public void onAttach(@NonNull @NotNull Context context) {
        super.onAttach(context);
        main=(MainActivity)getActivity();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        main=null;
    }
    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.listmember1,container, true);
        Log.d("mem", "onCreateView: 멤버 on");

        movieImage = rootView.findViewById(R.id.image1);
        title = rootView.findViewById(R.id.textView29);
        reservationRate = rootView.findViewById(R.id.textView31);
        grade = rootView.findViewById(R.id.textView32);
        rate = rootView.findViewById(R.id.textView33);

        Glide.with(this).load(urlStr).transition(new DrawableTransitionOptions().crossFade()).into(movieImage);
        title.setText(titleStr);
        reservationRate.setText(reservationRateStr);
        grade.setText(gradeStr);
        rate.setText(rateStr);


        return rootView;
    }

    public void setMemInfo(MovieInfo info, int num){
        id = info.id;
        titleStr = num + ". " + info.title;
        reservationRateStr = "점유율 " + info.reservation_rate + "%";
        gradeStr = "| " + info.grade + "세 이용가 |";
        rateStr = "평점 " + info.user_rating;
        urlStr = info.image;
    }
}
