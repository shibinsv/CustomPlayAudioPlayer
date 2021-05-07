package com.example.audioplayer.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.audioplayer.R;
import com.example.audioplayer.activities.PlayerActivity;
import com.example.audioplayer.models.MusicFiles;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class SongsAdapter extends RecyclerView.Adapter<SongsAdapter.ViewHolder> {

    private Context context;
    private ArrayList<MusicFiles>musicFiles;
    Activity activity;

    public SongsAdapter(Context context, ArrayList<MusicFiles> musicFiles) {
        this.context = context;
        this.musicFiles = musicFiles;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_music_adapter, parent, false);
        return new ViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.setMusic(musicFiles.get(position));

    }


    @Override
    public int getItemCount() {
        return musicFiles.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView musicImage;
        TextView musicFileName;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            musicImage=itemView.findViewById(R.id.musicImage);
            musicFileName=itemView.findViewById(R.id.musicFileName);

        }

        public void setMusic(MusicFiles musicFiles) {

            musicFileName.setText(musicFiles.getTitle());
            byte[] image =getAlbumArt(musicFiles.getPath());
            if(image != null){
                Glide.with(context)
                        .asBitmap()
                        .load(image)
                        .into(musicImage);
            }else{
                Glide.with(context)
                        .load(R.drawable.music_empty_album)
                        .into(musicImage);
            }
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i=new Intent(context, PlayerActivity.class);
                    i.putExtra("position",getPosition());
                    context.startActivity(i);
                }
            });
        }
    }

    private byte[] getAlbumArt(String uri){
        MediaMetadataRetriever retriever =new MediaMetadataRetriever();
        retriever.setDataSource(uri);
        byte[] art =retriever.getEmbeddedPicture();
        retriever.release();
        return art;
    }
}