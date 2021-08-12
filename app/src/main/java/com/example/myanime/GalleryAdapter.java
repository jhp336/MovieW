package com.example.myanime;

import android.content.Context;
import android.media.Image;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.target.Target;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.zip.Inflater;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ViewHolder> {
    Context context;
    ArrayList<GalleryItem> items = new ArrayList<GalleryItem>();
    OnItemClickListener listener;
    public GalleryAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @NotNull
    @Override
    public GalleryAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.gallery_item_view,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull GalleryAdapter.ViewHolder holder, int position) {
        GalleryItem item = items.get(position);
        holder.setImg(item);
        holder.setOnItemClickListener(listener);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(GalleryItem item){
        items.add(item);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public GalleryItem getItem(int position){
        return items.get(position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener=listener;
    }

    public interface OnItemClickListener{
        public void onItemClick(ViewHolder holder, View v, int position);
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        ImageView videoIcon;
        OnItemClickListener listener;
        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            videoIcon = itemView.findViewById(R.id.videoIcon);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(ViewHolder.this,v,getAdapterPosition());
                }
            });
        }
        public void setImg(GalleryItem item){
            Glide.with(itemView.getContext()).load(Uri.parse(item.getUrl())).override(480,320).centerCrop().into(imageView);
            if(item.isVideo()) {
                videoIcon.setImageResource(R.drawable.video_icon);
            }
            else {
                videoIcon.setVisibility(View.INVISIBLE);
            }
        }
        public void setOnItemClickListener(OnItemClickListener listener){
            this.listener=listener;
        }
    }
}
