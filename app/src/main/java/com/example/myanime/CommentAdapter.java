package com.example.myanime;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import org.w3c.dom.Comment;

import java.util.ArrayList;

public class CommentAdapter extends BaseAdapter {
    ArrayList<CommentItem> items = new ArrayList<CommentItem>();
    CommentitemView view;
    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        view = null;
        if (convertView==null) {
            view = new CommentitemView(parent.getContext());
        }
        else {
            view = (CommentitemView) convertView;
        }
        CommentItem item = items.get(position);
        view.setUserId(item.getUserId());
        view.setDate(item.getDate());
        view.setRatingBar(item.getRate());
        view.setComment(item.getComment());
        view.setLikeCount(item.getLike());
        view.setLikeState(item.isLikeState());
        view.clickListen(item);

        return view;
    }

    public void addItem(CommentItem item){
        items.add(item);
    }
}
