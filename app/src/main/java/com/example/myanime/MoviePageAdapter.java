package com.example.myanime;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class MoviePageAdapter extends FragmentStateAdapter {
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

    public Member_list getItem(int position) {
        return (Member_list)items.get(position);
    }
}
