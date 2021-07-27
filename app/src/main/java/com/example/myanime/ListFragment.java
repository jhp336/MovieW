package com.example.myanime;

import android.content.Context;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.myanime.data.MovieInfo;
import com.example.myanime.data.ResponseInfo;

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
        if(main.responseInfo!=null) {
            for (int i = 0; i < main.responseInfo.result.size(); i++) {
                MovieInfo info = main.responseInfo.result.get(i);
                Member_list mem = new Member_list();
                mem.setMemInfo(info);

                adapter.addItem(mem);
            }
        }


        pager.setAdapter(adapter);

        return rootView;
    }

    static class MoviePageAdapter extends FragmentStateAdapter {
        ArrayList<Fragment> items = new ArrayList<Fragment>();

        public MoviePageAdapter(@NonNull @NotNull Fragment fragment) {
            super(fragment);
        }

        public void addItem(Fragment item){
            items.add(item);
        }
        @NonNull
        @NotNull
        @Override
        public Fragment createFragment(int position) {
            return items.get(position);
        }

        @Override
        public int getItemCount() {
            return items.size();
        }
    }
    public void setInfo(ResponseInfo responseInfo){
        Log.d("setinfo", "setInfo: on");
        for(int i=0;i<responseInfo.result.size();i++){
            MovieInfo info = responseInfo.result.get(i);
            Member_list mem = new Member_list();
            mem.setMemInfo(info);

            adapter.addItem(mem);
        }
        pager.setAdapter(adapter);
    }
}
