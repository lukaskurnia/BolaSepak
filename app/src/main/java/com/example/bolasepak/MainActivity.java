package com.example.bolasepak;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonObject;


import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements MatchAdapter.OnItemClickListener {
    public static final String EXTRA_ID_MATCH = "idMatch";
    public static final String EXTRA_ID_HOME = "idHome";
    public static final String EXTRA_ID_AWAY = "idAway";
    public static final String EXTRA_DATE = "date";
    public static final String EXTRA_HOME_TEAM = "homeTeam";
    public static final String EXTRA_AWAY_TEAM = "awayTeam";
    public static final String EXTRA_HOME_SCORE = "homeScore";
    public static final String EXTRA_AWAY_SCORE = "awayScore";
    public static final String EXTRA_HOME_IMAGE = "homeImage";
    public static final String EXTRA_AWAY_IMAGE = "awayImage";

    private RecyclerView recyclerView;
    private MatchAdapter matchAdapter;
    private HashMap<String, String> teamHash;
    private ArrayList<MatchItem> matchList;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        matchList = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(this);
        teamHash = new HashMap<String, String>();
        parseJSONTeam();

    }

    private void parseJSONMatch() {
        String url = "https://www.thesportsdb.com/api/v1/json/1/eventspastleague.php?id=4328";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("events");
                            System.out.println(jsonArray.length());

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject hit = jsonArray.getJSONObject(i);

                                String idMatch = hit.getString("idEvent");
                                String idHome = hit.getString("idHomeTeam");
                                String idAway = hit.getString("idAwayTeam");
                                String date = hit.getString("dateEvent");
                                String homeTeam = hit.getString("strHomeTeam");
                                String awayTeam = hit.getString("strAwayTeam");
                                String homeScore = hit.getString("intHomeScore");
                                if(homeScore.equals("null")) {
                                    homeScore = "-";
                                }
                                String awayScore = hit.getString("intAwayScore");
                                if(awayScore.equals("null")) {
                                    awayScore = "-";
                                }
                                String homeImage = teamHash.get(idHome);
                                String awayImage = teamHash.get(idAway);

                                Log.i("match", "succeded");

                                matchList.add(new MatchItem(idMatch, idHome, idAway, date, homeTeam, awayTeam, homeScore, awayScore, homeImage, awayImage));
                            }

                            matchAdapter = new MatchAdapter(MainActivity.this, matchList);
                            recyclerView.setAdapter(matchAdapter);
                            matchAdapter.setOnItemClickListener(MainActivity.this);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.i("match", "failed");
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
        String url = "https://www.thesportsdb.com/api/v1/json/1/search_all_teams.php?l=English%20Premier%20League";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
//                        String resp = response.toString();
//                        System.out.println(resp);
//                        System.out.println(JsonObject.stringify(response));
                        try {
                            JSONArray jsonArray = response.getJSONArray("teams");
                            System.out.println(jsonArray.get(0));
                            System.out.println(jsonArray.get(1));

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject hit = jsonArray.getJSONObject(i);

                                String idTeam = hit.getString("idTeam");
                                String badgeTeam = hit.getString("strTeamBadge");
                                Log.i("Team", idTeam);
                                Log.i("Team", badgeTeam);
//                                System.out.println(idTeam);
//                                System.out.println(badgeTeam);

                                teamHash.put(idTeam, badgeTeam);
                            }
                            int size = teamHash.size();
                            System.out.println(size);
                            parseJSONMatch();

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.i("Team", "failed");
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

    @Override
    public void onItemClick(int position){
        Intent detailIntent = new Intent(this, MatchDetail.class);
        MatchItem clickedItem = matchList.get(position);

        detailIntent.putExtra(EXTRA_ID_MATCH, clickedItem.getIdMatch());
        detailIntent.putExtra(EXTRA_DATE, clickedItem.getDate());
        detailIntent.putExtra(EXTRA_ID_HOME, clickedItem.getIdHome());
        detailIntent.putExtra(EXTRA_ID_AWAY, clickedItem.getIdAway());
        detailIntent.putExtra(EXTRA_HOME_TEAM, clickedItem.getHomeTeam());
        detailIntent.putExtra(EXTRA_AWAY_TEAM, clickedItem.getAwayTeam());
        detailIntent.putExtra(EXTRA_HOME_SCORE, clickedItem.getHomeScore());
        detailIntent.putExtra(EXTRA_AWAY_SCORE, clickedItem.getAwayScore());
        detailIntent.putExtra(EXTRA_HOME_IMAGE, clickedItem.getHomeImage());
        detailIntent.putExtra(EXTRA_AWAY_IMAGE, clickedItem.getAwayImage());

        startActivity(detailIntent);
    }
}
