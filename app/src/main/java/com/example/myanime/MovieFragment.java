package com.example.myanime;

import android.annotation.SuppressLint;
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
import android.widget.ImageView;
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

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.example.myanime.data.CommentInfo;
import com.example.myanime.data.DetailInfo;
import com.example.myanime.data.MovieInfo;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;


public class MovieFragment extends Fragment {
    RatingBar ratingbar;
    TextView textview, likeView, hateView, title, date, duration, genre, reservation, audience, synopsis, director, actor;
    Button button, button2, commentWrite, allComment;
    ListView listView;
    ImageView imageView, gradeImage;
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

        button = rootView.findViewById(R.id.button);
        likeView = rootView.findViewById(R.id.textView);
        button2 = rootView.findViewById(R.id.button2);
        hateView = rootView.findViewById(R.id.textView3);
        imageView = rootView.findViewById(R.id.imageView4);
        title = rootView.findViewById(R.id.textView2);
        date = rootView.findViewById(R.id.textView5);
        duration = rootView.findViewById(R.id.textView7);
        genre = rootView.findViewById(R.id.textView9);
        reservation = rootView.findViewById(R.id.textView10);
        audience = rootView.findViewById(R.id.textView14);
        synopsis = rootView.findViewById(R.id.textView16);
        director = rootView.findViewById(R.id.textView19);
        actor = rootView.findViewById(R.id.textView21);
        gradeImage = rootView.findViewById(R.id.imageView5);
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
        /*adapter.addItem(new CommentItem("Jung1098", "5달 전", "벌여 놓은 건 많은데 수습할 수 있을까?\n가능하다면" +
                " 역대 한국 블록버스터 올타임 넘버원", "1024", 5, false));
        adapter.addItem(new CommentItem("어울림", "3달 전", "특유의 만화스러운 연출과 독백이 가끔 거슬리지만 CG 진짜 레전드" +
                "\n + 에렌 소리좀 그만질러!!", "788", 4, false));
        adapter.addItem(new CommentItem("브레인스워즈", "3달 전", "절망, 공포, 충격, 전율 그 자체.\n반전과 폭력에 대한" +
                " 아이러니를 '집단'과 '위협'으로 능수능란하게 메타포화시키다."
                , "755", 5, false));*/
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

    @SuppressLint("SetTextI18n")
    public void setDetail(DetailInfo info){
        Glide.with(this).load(info.thumb).into(imageView);
        title.setText(info.title);
        date.setText(info.date);
        duration.setText(info.duration+" 분");
        genre.setText(info.genre);
        likeView.setText(String.valueOf(info.like));
        hateView.setText(String.valueOf(info.dislike));
        reservation.setText(info.reservation_grade+"위  "+info.reservation_rate+"%");
        ratingbar.setRating(info.user_rating);
        rate=ratingbar.getRating();
        textview.setText(String.valueOf(info.user_rating));
        audience.setText(info.audience + " 명");
        synopsis.setText(info.synopsis);
        director.setText(info.director);
        actor.setText(info.actor);
        switch (info.grade) {
            case 12:
                gradeImage.setImageResource(R.drawable.ic_12);
                break;
            case 15:
                gradeImage.setImageResource(R.drawable.ic_15);
                break;
            case 19:
                gradeImage.setImageResource(R.drawable.ic_19);
                break;
            default:
                gradeImage.setImageResource(R.drawable.ic_all);
                break;
        }


    }

    public void setComment(ArrayList<CommentInfo> list){
        for(int i=0;i<list.size();i++){
            CommentInfo info = list.get(i);
            CommentItem item = new CommentItem(info.writer,info.time,info.contents,String.valueOf(info.recommend),info.rating,false);
            adapter.addItem(item);
        }
        listView.setAdapter(adapter);
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