package com.example.cesar.dbfz;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.cesar.dbfz.adapters.CharactersArrayAdapter;
import com.example.cesar.dbfz.pojo.Heroe;
import com.example.cesar.dbfz.pojo.DbCharacter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class CharactersActivity extends Activity {

    private CharactersArrayAdapter charactersArrayAdapter;
    private ListView listView;
    private RequestQueue mQueue;

    //TEMPORAL MARVEL
    private int offset = 1190;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_characters);
        listView = (ListView) findViewById(R.id.listCharacters);

        // DBFZ
        /*charactersArrayAdapter = new CharactersArrayAdapter(this, R.layout.character_layout, new ArrayList<DbCharacter>());
        listView.setAdapter(charactersArrayAdapter);
        mQueue = VolleySingleton.getInstance(this).getRequestQueue();
        jsonDbCharacters("https://swapi.co/api/people/?page=1&format=json", charactersArrayAdapter);*/
        charactersArrayAdapter = new CharactersArrayAdapter(this, R.layout.character_layout, new ArrayList<DbCharacter>());
        listView.setAdapter(charactersArrayAdapter);
        mQueue = VolleySingleton.getInstance(this).getRequestQueue();
        jsonMarvel(getMarvelString(), charactersArrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DbCharacter md = charactersArrayAdapter.getItem((int)id);
                Intent intent = new Intent(CharactersActivity.this, CharacterDetailActivity.class);
                intent.putExtra("id", md.id);
                startActivity(intent);
            }
        });
    }
    private void jsonDbCharacters(String url, final CharactersArrayAdapter adapter){
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,
                url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("results");
                    for (int i = 0; i < jsonArray.length(); i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        DbCharacter dbCharacter = new DbCharacter();
                        dbCharacter.name = jsonObject.getString("name");
                        dbCharacter.url = jsonObject.getString("url");
                        dbCharacter.id = jsonObject.getString("mass");
                        dbCharacter.birth_year = jsonObject.getString("birth_year");
                        adapter.add(dbCharacter);
                    }
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        mQueue.add(request);
    }

    //Temporal MARVEL
    private final String LOG_TAG = "MARVEL";

    private static char[] HEXCodes = {'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f'};

    private void jsonMarvel(String url, final CharactersArrayAdapter adapter){
        adapter.clear();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,
                url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject data = response.getJSONObject("data");
                    JSONArray jsonArray = data.getJSONArray("results");
                    for (int i = 0; i < jsonArray.length(); i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        JSONObject thumbnail = jsonObject.getJSONObject("thumbnail");
                        DbCharacter dbCharacter = new DbCharacter();
                        dbCharacter.name = jsonObject.getString("name");
                        dbCharacter.url = thumbnail.getString("path") + "/portrait_small." + thumbnail.getString("extension");
                        dbCharacter.id = jsonObject.getString("id");
                        adapter.add(dbCharacter);
                    }
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        mQueue.add(request);
    }
    private String getMarvelString(){
        String ts = Long.toString(System.currentTimeMillis() / 1000);
        String apikey = "d9c9993a63b316379f898412b03435a8";
        String hash = md5(ts + "9441edf550393ac90d79cc4a5f40308c85ae9580" + "d9c9993a63b316379f898412b03435a8");
        ArrayList<Heroe> arrayList = new ArrayList<>();
        final String CHARACTER_BASE_URL =
                "http://gateway.marvel.com/v1/public/characters";
        String characterJsonStr = null;
        final String TIMESTAMP = "ts";
        final String API_KEY = "apikey";
        final String HASH = "hash";
        final String ORDER = "orderBy";

        Uri builtUri;
        builtUri = Uri.parse(CHARACTER_BASE_URL+"?").buildUpon()
                .appendQueryParameter(TIMESTAMP, ts)
                .appendQueryParameter(API_KEY, apikey)
                .appendQueryParameter(HASH, hash)
                .appendQueryParameter(ORDER, "name")
                .appendQueryParameter("offset", offset + "")
                .appendQueryParameter("limit", "10")
                .build();

        return (builtUri.toString());
    }
    public static String md5(String s) {
        try {
            MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(s.getBytes());
            String hash = new String(hexEncode(digest.digest()));
            return hash;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
    public static String hexEncode(byte[] bytes) {
        char[] result = new char[bytes.length*2];
        int b;
        for (int i = 0, j = 0; i < bytes.length; i++) {
            b = bytes[i] & 0xff;
            result[j++] = HEXCodes[b >> 4];
            result[j++] = HEXCodes[b & 0xf];
        }
        return new String(result);
    }
}
