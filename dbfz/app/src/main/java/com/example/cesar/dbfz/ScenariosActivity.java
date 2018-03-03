package com.example.cesar.dbfz;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.cesar.dbfz.adapters.ScenariosArrayAdapter;
import com.example.cesar.dbfz.pojo.DbScenario;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ScenariosActivity extends Activity {

    private ScenariosArrayAdapter scenariosArrayAdapter;
    private ListView listView;
    private RequestQueue mQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scenarios);
        listView = (ListView) findViewById(R.id.listScenarios);

        scenariosArrayAdapter = new ScenariosArrayAdapter(this, R.layout.character_layout, new ArrayList<DbScenario>());
        listView.setAdapter(scenariosArrayAdapter);
        mQueue = VolleySingleton.getInstance(this).getRequestQueue();
        jsonDbScenarios("https://swapi.co/api/people/?page=1&format=json", scenariosArrayAdapter);
    }

    private void jsonDbScenarios(String url, final ScenariosArrayAdapter adapter){
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,
                url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("results");
                    for (int i = 0; i < jsonArray.length(); i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        DbScenario dbScenario = new DbScenario();
                        dbScenario.name = jsonObject.getString("name");
                        dbScenario.url = jsonObject.getString("url");
                        adapter.add(dbScenario);
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
