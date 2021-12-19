package com.example.freespotify;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.w3c.dom.Text;

import java.util.ArrayList;


public class MusicListAdapter extends ArrayAdapter<music> {

    private static final String TAG = "MusicListAdapter";
    public static Context mContext;
    private static int mResource;

    public MusicListAdapter(Context context, int resource, ArrayList<music> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }



    @SuppressLint("ViewHolder")
    @NonNull
    @Override
    public View getView(int position, View convertView,ViewGroup parent) {

        String artistName = getItem(position).getArtistName();
        String musicName = getItem(position).getMusicName();

        music Music = new music(musicName, artistName);

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        TextView musicNameDisplay = (TextView) convertView.findViewById(R.id.music_name);
        TextView artistNameDisplay = (TextView) convertView.findViewById(R.id.artist_name);

        musicNameDisplay.setText(musicName);
        artistNameDisplay.setText(artistName);

        return convertView;
    }
}
