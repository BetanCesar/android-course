package com.example.cesar.dbfz.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.example.cesar.dbfz.R;
import com.example.cesar.dbfz.VolleySingleton;
import com.example.cesar.dbfz.pojo.DbCharacter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Cesar on 02/03/18.
 */

public class CharactersArrayAdapter extends ArrayAdapter<DbCharacter> {
    private Context context;
    public CharactersArrayAdapter(Context context, int resource, List<DbCharacter> objects) {
        super(context, resource, objects);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        DbCharacter dbCharacter = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(context)
                    .inflate(R.layout.character_layout, parent, false);
        }

        TextView textView = (TextView) convertView.findViewById(R.id.txtNameCharacter);
        TextView textView2 = (TextView) convertView.findViewById(R.id.txtDifficultyCharacter);
        NetworkImageView networkImageView = (NetworkImageView) convertView.findViewById(R.id.imgCharacter);
        textView.setText(dbCharacter.name);
        textView2.setText(dbCharacter.birth_year);
        RequestQueue requestQueue = VolleySingleton.getInstance(context).getRequestQueue();
        ImageLoader imageLoader = new ImageLoader(requestQueue,
                new ImageLoader.ImageCache() {

                    private final LruCache<String, Bitmap> cache = new LruCache<String, Bitmap>(10);
                    @Override
                    public Bitmap getBitmap(String url) {
                        return cache.get(url);
                    }

                    @Override
                    public void putBitmap(String url, Bitmap bitmap) {
                        cache.put(url, bitmap);

                    }
                });
        networkImageView.setImageUrl(dbCharacter.url, imageLoader);
        //networkImageView.setImageUrl("https://upload.wikimedia.org/wikipedia/commons/thumb/6/6c/Star_Wars_Logo.svg/1200px-Star_Wars_Logo.svg.png", imageLoader);


        return convertView;
    }
}
