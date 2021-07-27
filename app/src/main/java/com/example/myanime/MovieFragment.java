package com.example.myanime;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import org.jetbrains.annotations.NotNull;


public class MovieFragment extends Fragment {
    RatingBar ratingbar;
    TextView textview, likeView, hateView;
    Button button, button2, commentWrite, allComment;
    ListView listView;
    CommentAdapter adapter;
    ViewGroup rootView;
    MainActivity main;

    boolean likeState = false, hateState = false;
    int likeCount, hateCount;
    float rate;

    @Override
    public void onAttach(@NonNull @NotNull Context context) {
        super.onAttach(context);
        main = (MainActivity)getActivity();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        main = null;
    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        rootView = (ViewGroup)inflater.inflate(R.layout.fragment_movie,container,false);
        ratingbar = rootView.findViewById(R.id.ratingBar);
        textview = rootView.findViewById(R.id.textView12);
        rate = ratingbar.getRating();
        textview.setText(String.valueOf(rate));

        button = rootView.findViewById(R.id.button);
        likeView = rootView.findViewById(R.id.textView);
        button2 = rootView.findViewById(R.id.button2);
        hateView = rootView.findViewById(R.id.textView3);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hateState) {
                    make_Toast("이미 '싫어요'를 누르셨습니다.");
                } else {
                    if (likeState) {
                        decrLikeCount();
                    } else {
                        incrLikeCount();
                    }
                    likeState = !likeState;
                }

            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (likeState) {
                    make_Toast("이미 '좋아요'를 누르셨습니다.");
                } else {
                    if (hateState) {
                        decrHateCount();
                    } else {
                        incrHateCount();
                    }
                    hateState = !hateState;
                }
            }
        });

        listView = rootView.findViewById(R.id.listView);
        adapter = new CommentAdapter();
        adapter.addItem(new CommentItem("Jung1098", "5달 전", "벌여 놓은 건 많은데 수습할 수 있을까?\n가능하다면" +
                " 역대 한국 블록버스터 올타임 넘버원", "1024", 5, false));
        adapter.addItem(new CommentItem("어울림", "3달 전", "특유의 만화스러운 연출과 독백이 가끔 거슬리지만 CG 진짜 레전드" +
                "\n + 에렌 소리좀 그만질러!!", "788", 4, false));
        adapter.addItem(new CommentItem("브레인스워즈", "3달 전", "절망, 공포, 충격, 전율 그 자체.\n반전과 폭력에 대한" +
                " 아이러니를 '집단'과 '위협'으로 능수능란하게 메타포화시키다."
                , "755", 5, false));
        listView.setAdapter(adapter);

        commentWrite = rootView.findViewById(R.id.button4);
        commentWrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                main.showCommentWrite();
            }
        });

        allComment = rootView.findViewById(R.id.button5);
        allComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                main.showAllComment();
            }
        });
        return  rootView;
    }

    public void incrLikeCount(){
        likeCount = Integer.parseInt(likeView.getText().toString());
        likeCount += 1;
        likeView.setText(String.valueOf(likeCount));
        button.setBackgroundResource(R.drawable.ic_thumb_up_selected);
    }
    public void decrLikeCount(){
        likeCount = Integer.parseInt(likeView.getText().toString());
        likeCount -= 1;
        likeView.setText(String.valueOf(likeCount));
        button.setBackgroundResource(R.drawable.thumb_up);
    }
    public void incrHateCount(){
        hateCount = Integer.parseInt(hateView.getText().toString());
        hateCount += 1;
        hateView.setText(String.valueOf(hateCount));
        button2.setBackgroundResource(R.drawable.ic_thumb_down_selected);
    }
    public void decrHateCount(){
        hateCount = Integer.parseInt(hateView.getText().toString());
        hateCount -= 1;
        hateView.setText(String.valueOf(hateCount));
        button2.setBackgroundResource(R.drawable.thumb_down);
    }


    public void make_Toast(String message){
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.toast_custom,rootView.findViewById(R.id.toast_layout));
        TextView text = layout.findViewById(R.id.textView23);
        text.setText(message);

        Toast toast = new Toast(getContext());
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }
}