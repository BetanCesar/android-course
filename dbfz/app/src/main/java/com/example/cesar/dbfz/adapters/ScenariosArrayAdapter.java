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
import com.example.cesar.dbfz.pojo.DbScenario;

import java.util.List;

/**
 * Created by Cesar on 03/03/18.
 */

public class ScenariosArrayAdapter extends ArrayAdapter<DbScenario> {
    private Context context;
    public ScenariosArrayAdapter(Context context, int resource, List<DbScenario> objects) {
        super(context, resource, objects);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        DbScenario dbScenario = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(context)
                    .inflate(R.layout.scenarios_layout, parent, false);
        }
        TextView textView = (TextView) convertView.findViewById(R.id.nameScenario);
        NetworkImageView networkImageView = (NetworkImageView) convertView.findViewById(R.id.imgScenario);
        textView.setText(dbScenario.name);
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
        networkImageView.setImageUrl("http://www.onrpg.com/wp-content/uploads/2018/01/DBFZ-Main-Image.jpg", imageLoader);

        return convertView;
    }
}
