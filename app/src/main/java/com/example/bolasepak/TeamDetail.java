package com.example.bolasepak;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.bolasepak.db.DatabaseHelper;
import com.example.bolasepak.db.model.Subscribe;
import com.google.android.material.tabs.TabLayout;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import static com.example.bolasepak.MatchDetail.EXTRA_TEAM_ID_D;
import static com.example.bolasepak.MatchDetail.EXTRA_TEAM_NAME_D;
import static com.example.bolasepak.MatchDetail.EXTRA_TEAM_IMAGE_D;

public class TeamDetail extends AppCompatActivity{

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;

    DatabaseHelper db;
    int but;
//    private RecyclerView recyclerView;
//    private MatchAdapter matchAdapter;
//    private HashMap<String, String> teamHash;
//    private ArrayList<MatchItem> matchList;
//    private RequestQueue requestQueue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_detail);

        db = new DatabaseHelper(getApplicationContext());

        Intent intent = getIntent();
        but = 0;
        final String idTeam = intent.getStringExtra(EXTRA_TEAM_ID_D);
        String teamName = intent.getStringExtra(EXTRA_TEAM_NAME_D);
        String teamImage = intent.getStringExtra(EXTRA_TEAM_IMAGE_D);

        final Subscribe mySubscribe = db.getSubscribe(idTeam);

        TextView tvTeamName = findViewById(R.id.teamName);
        ImageView tvTeamImage = findViewById(R.id.teamImage);
        final Button button = findViewById(R.id.subscribe);

        if(mySubscribe != null) {
            button.setText("Subscribed");
            button.setBackgroundColor(Color.rgb(223,223,223));
            button.setTextColor(Color.rgb(159,159,159));
            but = 1;
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(but == 1) {
                    db.deleteSubscribe(idTeam);
                    button.setText("Subscribe");
                    button.setBackgroundColor(Color.rgb(207,0,0));
                    button.setTextColor(Color.rgb(255,255,255));
                    but = 0;
                }
                else{
                    Subscribe subscribe = new Subscribe(idTeam);
                    db.createSubscribe(subscribe);
                    button.setText("Subscribed");
                    button.setBackgroundColor(Color.rgb(223,223,223));
                    button.setTextColor(Color.rgb(159,159,159));
                    but = 1;
                }
            }
        });

        tvTeamName.setText(teamName);
        Picasso.get().load(teamImage).fit().centerInside().into(tvTeamImage);

        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        viewPagerAdapter.AddFragment(FragmentNextMatch.fragmentInstance(idTeam, isConnected()), "Next Matches");
        viewPagerAdapter.AddFragment(FragmentPastMatch.fragmentInstance(idTeam, isConnected()), "Past Matches");
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

//        recyclerView = findViewById(R.id.recycler_view_next_match);
//        recyclerView.setHasFixedSize(true);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        matchList = new ArrayList<>();
//        requestQueue = Volley.newRequestQueue(this);
//        teamHash = new HashMap<String, String>();
//
//        parseJSONTeam();
    }

    public boolean isConnected() {
//        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            return true;
        }
        else
            return false;
    }

//    private void parseJSONMatch() {
//        String url = "https://www.thesportsdb.com/api/v1/json/1/eventspastleague.php?id=4328";
//
//        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
//                new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        try {
//                            JSONArray jsonArray = response.getJSONArray("events");
//                            System.out.println(jsonArray.length());
//
//                            for (int i = 0; i < jsonArray.length(); i++) {
//                                JSONObject hit = jsonArray.getJSONObject(i);
//
//                                String idMatch = hit.getString("idEvent");
//                                String idHome = hit.getString("idHomeTeam");
//                                String idAway = hit.getString("idAwayTeam");
//                                String date = hit.getString("dateEvent");
//                                String homeTeam = hit.getString("strHomeTeam");
//                                String awayTeam = hit.getString("strAwayTeam");
//                                String homeScore = hit.getString("intHomeScore");
//                                if(homeScore.equals("null")) {
//                                    homeScore = "-";
//                                }
//                                String awayScore = hit.getString("intAwayScore");
//                                if(awayScore.equals("null")) {
//                                    awayScore = "-";
//                                }
//                                String homeImage = teamHash.get(idHome);
//                                String awayImage = teamHash.get(idAway);
//
//                                Log.i("match", "succeded");
//
//                                matchList.add(new MatchItem(idMatch, idHome, idAway, date, homeTeam, awayTeam, homeScore, awayScore, homeImage, awayImage));
//                            }
//
//                            matchAdapter = new MatchAdapter(TeamDetail.this, matchList);
//                            recyclerView.setAdapter(matchAdapter);
//                            matchAdapter.setOnItemClickListener(TeamDetail.this);
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                            Log.i("match", "failed");
//                        }
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                error.printStackTrace();
//            }
//        });
//
//        requestQueue.add(request);
//    }
//
//    private void parseJSONTeam() {
//        String url = "https://www.thesportsdb.com/api/v1/json/1/search_all_teams.php?l=English%20Premier%20League";
//
//        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
//                new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
////                        String resp = response.toString();
////                        System.out.println(resp);
////                        System.out.println(JsonObject.stringify(response));
//                        try {
//                            JSONArray jsonArray = response.getJSONArray("teams");
//                            System.out.println(jsonArray.get(0));
//                            System.out.println(jsonArray.get(1));
//
//                            for (int i = 0; i < jsonArray.length(); i++) {
//                                JSONObject hit = jsonArray.getJSONObject(i);
//
//                                String idTeam = hit.getString("idTeam");
//                                String badgeTeam = hit.getString("strTeamBadge");
//                                Log.i("Team", idTeam);
//                                Log.i("Team", badgeTeam);
////                                System.out.println(idTeam);
////                                System.out.println(badgeTeam);
//
//                                teamHash.put(idTeam, badgeTeam);
//                            }
//                            int size = teamHash.size();
//                            System.out.println(size);
//                            parseJSONMatch();
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                            Log.i("Team", "failed");
//                        }
//                    }
//                }, new Response.ErrorListener() {
//
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                error.printStackTrace();
//            }
//        });
//
//        requestQueue.add(request);
//    }
//
//    @Override
//    public void onItemClick(int position){
//        Intent detailIntent = new Intent(this, MatchDetail.class);
//        MatchItem clickedItem = matchList.get(position);
//
//        detailIntent.putExtra(EXTRA_ID_MATCH_T, clickedItem.getIdMatch());
//        detailIntent.putExtra(EXTRA_DATE_T, clickedItem.getDate());
//        detailIntent.putExtra(EXTRA_ID_HOME_T, clickedItem.getIdHome());
//        detailIntent.putExtra(EXTRA_ID_AWAY_T, clickedItem.getIdAway());
//        detailIntent.putExtra(EXTRA_HOME_TEAM_T, clickedItem.getHomeTeam());
//        detailIntent.putExtra(EXTRA_AWAY_TEAM_T, clickedItem.getAwayTeam());
//        detailIntent.putExtra(EXTRA_HOME_SCORE_T, clickedItem.getHomeScore());
//        detailIntent.putExtra(EXTRA_AWAY_SCORE_T, clickedItem.getAwayScore());
//        detailIntent.putExtra(EXTRA_HOME_IMAGE_T, clickedItem.getHomeImage());
//        detailIntent.putExtra(EXTRA_AWAY_IMAGE_T, clickedItem.getAwayImage());
//
//        startActivity(detailIntent);
//    }


}
