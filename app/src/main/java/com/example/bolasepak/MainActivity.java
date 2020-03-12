package com.example.bolasepak;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.bolasepak.api.ApiRepository;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private MatchAdapter matchAdapter;
    private HashMap<String, String> teamHash;
    private ArrayList<MatchItem> matchList;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ApiRepository repo = new ApiRepository();
        //String str = repo.doRequest("https://www.thesportsdb.com/api/v1/json/1/eventsnextleague.php?id=4328");
        repo.execute("https://www.thesportsdb.com/api/v1/json/1/eventsnextleague.php?id=4328");

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        matchList = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(this);
        parseJSONTeam();

    }

    private void parseJSONMatch() {
        String url = "https://www.thesportsdb.com/api/v1/json/1/eventsnextleague.php?id=4328";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("events");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject hit = jsonArray.getJSONObject(i);

                                String idMatch = hit.getString("idEvent");
                                String idHome = hit.getString("idHomeTeam");
                                String idAway = hit.getString("idAwayTeam");
                                String date = hit.getString("dateEvent");
                                String homeTeam = hit.getString("strHomeTeam");
                                String awayTeam = hit.getString("strAwayTeam");
                                String homeScore = hit.getString("intHomeScore");
                                String awayScore = hit.getString("intAwayScore");
                                String homeImage = teamHash.get(idHome);
                                String awayImage = teamHash.get(idAway);

                                matchList.add(new MatchItem(idMatch, idHome, idAway, date, homeTeam, awayTeam, homeScore, awayScore, homeImage, awayImage));
                            }

                            matchAdapter = new MatchAdapter(MainActivity.this, matchList);
                            recyclerView.setAdapter(matchAdapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        requestQueue.add(request);
    }

    private void parseJSONTeam() {
        String url = "https://www.thesportsdb.com/api/v1/json/1/lookupteam.php?id=133604";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("teams");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject hit = jsonArray.getJSONObject(i);

                                String idTeam = hit.getString("idTeam");
                                String badgeTeam = hit.getString("strTeamBadge");

                                teamHash.put(idTeam, badgeTeam);
                            }
                            parseJSONMatch();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        requestQueue.add(request);
    }
}
