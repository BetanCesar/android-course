package com.example.cesar.dbfz;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.cesar.dbfz.adapters.CharactersArrayAdapter;
import com.example.cesar.dbfz.pojo.DbCharacter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CharactersActivity extends Activity {

    private CharactersArrayAdapter charactersArrayAdapter;
    private ListView listView;
    private RequestQueue mQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_characters);
        listView = (ListView) findViewById(R.id.listCharacters);
        charactersArrayAdapter = new CharactersArrayAdapter(this, R.layout.character_layout, new ArrayList<DbCharacter>());
        listView.setAdapter(charactersArrayAdapter);
        mQueue = VolleySingleton.getInstance(this).getRequestQueue();
        for(int i = 1; i < 10; i++){
            jsonDbCharacters("https://swapi.co/api/people/?page=" + i + "&format=json", charactersArrayAdapter);
        }
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
}
