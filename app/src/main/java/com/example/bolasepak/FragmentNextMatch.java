package com.example.bolasepak;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.bolasepak.db.DatabaseHelper;
import com.example.bolasepak.db.model.DataHome;
import com.example.bolasepak.db.model.DataTeam;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import static com.example.bolasepak.MatchDetail.EXTRA_TEAM_ID_D;
import static com.example.bolasepak.MatchDetail.EXTRA_TEAM_NAME_D;
import static com.example.bolasepak.MatchDetail.EXTRA_TEAM_IMAGE_D;

public class FragmentNextMatch extends Fragment implements MatchAdapter.OnItemClickListener{
    public static final String EXTRA_ID_MATCH_T = "idMatch";
    public static final String EXTRA_ID_HOME_T = "idHome";
    public static final String EXTRA_ID_AWAY_T = "idAway";
    public static final String EXTRA_DATE_T = "date";
    public static final String EXTRA_HOME_TEAM_T = "homeTeam";
    public static final String EXTRA_AWAY_TEAM_T = "awayTeam";
    public static final String EXTRA_HOME_SCORE_T = "homeScore";
    public static final String EXTRA_AWAY_SCORE_T = "awayScore";
    public static final String EXTRA_HOME_IMAGE_T = "homeImage";
    public static final String EXTRA_AWAY_IMAGE_T = "awayImage";

    private RecyclerView recyclerView;
    private MatchAdapter matchAdapter;
    private HashMap<String, String> teamHash;
    private HashMap<String, String> stadiumHash;
    private ArrayList<MatchItem> matchList;
    private RequestQueue requestQueue;

    private String idTeam;
    private boolean status;
    private String mainWeather;
    private String descWeather;
//    private String idMatch;
//    private String idHome;
//    private String idAway;
//    private String date;
//    private String homeTeam;
//    private String awayTeam;
//    private String homeScore;
//    private String awayScore;
//    private String homeImage;
//    private String awayImage;


    DatabaseHelper db;

    View v;

    public FragmentNextMatch(){

    }
    public static FragmentNextMatch fragmentInstance (String idTeam, boolean status){
        Bundle args = new Bundle();
        args.putString("idTeam", idTeam);
        args.putBoolean("status", status);
        FragmentNextMatch f = new FragmentNextMatch();
        f.setArguments(args);
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){

        db = new DatabaseHelper(getActivity().getApplicationContext());
        v = inflater.inflate(R.layout.fragment_next_match, container, false);
        recyclerView = v.findViewById(R.id.recycler_view_next_match);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        matchList = new ArrayList<>();
        requestQueue = Volley.newRequestQueue( getActivity().getApplicationContext());
        teamHash = new HashMap<String, String>();
        stadiumHash = new HashMap<String, String>();
        parseJSONTeam();

        db.closeDB();

        return v;
    }

    private void parseJSONMatch() {
        final String url = "http://134.209.97.218:5050/api/v1/json/1/eventsnext.php?id=" + getArguments().getString("idTeam");

        if(getArguments().getBoolean("status")) {
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                DataTeam data = new DataTeam(response.toString(),getArguments().getString("idTeam"),"detail", "next");
                                db.createDataTeam(data);
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
                                    if (homeScore.equals("null")) {
                                        homeScore = "-";
                                    }
                                    String awayScore = hit.getString("intAwayScore");
                                    if (awayScore.equals("null")) {
                                        awayScore = "-";
                                    }
                                    String homeImage = teamHash.get(idHome);
                                    String awayImage = teamHash.get(idAway);
                                    String homeCity = stadiumHash.get(idHome);

                                    parseJSONWeather(idMatch, idHome, idAway, date, homeTeam, awayTeam, homeScore, awayScore, homeImage, awayImage, homeCity);


                                    //requestQueue.add(request);
                                    Log.i("match", "succeded");
                                    //Log.i("outWeather1", mainWeather);
                                    //Log.i("outWeather2", descWeather);
                                    //matchList.add(new MatchItem(idMatch, idHome, idAway, date, homeTeam, awayTeam, homeScore, awayScore, homeImage, awayImage, mainWeather, descWeather));
                                }

                                matchAdapter = new MatchAdapter(getActivity().getApplicationContext(), matchList);
                                recyclerView.setAdapter(matchAdapter);
                                matchAdapter.setOnItemClickListener(FragmentNextMatch.this);

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
        else {
            try {
                DataTeam data = db.getDataTeam(getArguments().getString("idTeam"), "detail", "next");
                if(data!= null) {
                    JSONObject jsonObject = new JSONObject(data.getData());
                    JSONArray jsonArray = jsonObject.getJSONArray("events");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject hit = jsonArray.getJSONObject(i);

                        String idMatch = hit.getString("idEvent");
                        String idHome = hit.getString("idHomeTeam");
                        String idAway = hit.getString("idAwayTeam");
                        String date = hit.getString("dateEvent");
                        String homeTeam = hit.getString("strHomeTeam");
                        String awayTeam = hit.getString("strAwayTeam");
                        String homeScore = hit.getString("intHomeScore");
                        if (homeScore.equals("null")) {
                            homeScore = "-";
                        }
                        String awayScore = hit.getString("intAwayScore");
                        if (awayScore.equals("null")) {
                            awayScore = "-";
                        }
                        String homeImage = teamHash.get(idHome);
                        String awayImage = teamHash.get(idAway);

                        Log.i("match", "succeded");

                        matchList.add(new MatchItem(idMatch, idHome, idAway, date, homeTeam, awayTeam, homeScore, awayScore, homeImage, awayImage));
                    }

                    matchAdapter = new MatchAdapter(getActivity().getApplicationContext(), matchList);
                    recyclerView.setAdapter(matchAdapter);
                    matchAdapter.setOnItemClickListener(FragmentNextMatch.this);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    private void parseJSONTeam() {
        String url = "http://134.209.97.218:5050/api/v1/json/1/search_all_teams.php?l=English%20Premier%20League";

        if(getArguments().getBoolean("status")) {
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
//                        String resp = response.toString();
//                        System.out.println(resp);
//                        System.out.println(JsonObject.stringify(response));
                            try {
                                DataTeam data = new DataTeam(response.toString(), getArguments().getString("idTeam"), "pic", "next");
                                db.createDataTeam(data);
                                JSONArray jsonArray = response.getJSONArray("teams");
                                System.out.println(jsonArray.get(0));
                                System.out.println(jsonArray.get(1));

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject hit = jsonArray.getJSONObject(i);

                                    String idTeam = hit.getString("idTeam");
                                    String badgeTeam = hit.getString("strTeamBadge");
                                    String[] stadiumLocation = hit.getString("strStadiumLocation").split(", ");
                                    String stadiumCity = stadiumLocation[stadiumLocation.length - 1];
//                                    if ( stadiumLocation.length > 1 ){
//                                        stadiumCity = stadiumLocation[1];
//                                    }
//                                    else {
//                                        stadiumCity = stadiumLocation[0];
//                                    }
                                    Log.i("Team", idTeam);
                                    Log.i("Team badge", badgeTeam);
                                    Log.i("Team City", stadiumCity);

                                    teamHash.put(idTeam, badgeTeam);
                                    stadiumHash.put(idTeam, stadiumCity);
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
        else {
            try{
                DataTeam data = db.getDataTeam(getArguments().getString("idTeam"), "pic", "next");
                if(data != null) {
                    JSONObject jsonobject = new JSONObject(data.getData());
                    JSONArray jsonArray = jsonobject.getJSONArray("teams");
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
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void parseJSONWeather(final String idMatch, final String idHome, final String idAway, final String date, final String homeTeam, final String awayTeam, final String homeScore, final String awayScore, final String homeImage, final String awayImage, final String homeCity){
        String urlCity = "https://api.openweathermap.org/data/2.5/weather?q=" + homeCity + "&appid=127eb580ce79e4f5624152aae7b26fd7";
        Log.i("urlCity", urlCity);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, urlCity, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("test2", "test try 1");
                        try {
                            JSONArray jsonArray = response.getJSONArray("weather");
                            System.out.println(jsonArray.length());
                            Log.i("test1", "test try 1");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject hit = jsonArray.getJSONObject(i);
                                Log.i("hit", "mashook");

                                mainWeather = hit.getString("main");
                                descWeather = hit.getString("description");
                                Log.i("main weather", mainWeather);
                                Log.i("desc weather", descWeather);
                                matchList.add(new MatchItem(idMatch, idHome, idAway, date, homeTeam, awayTeam, homeScore, awayScore, homeImage, awayImage, mainWeather, homeCity));
                            }
                            matchAdapter = new MatchAdapter(getActivity().getApplicationContext(), matchList);
                            recyclerView.setAdapter(matchAdapter);
                            matchAdapter.setOnItemClickListener(FragmentNextMatch.this);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.i("errweather1", "failed");
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Log.i("errweather2", "failed");
            }
        });
        requestQueue.add(request);
    }

    @Override
    public void onItemClick(int position) {
        Intent detailIntent = new Intent(this.getActivity(), MatchDetail.class);
        MatchItem clickedItem = matchList.get(position);

        detailIntent.putExtra(EXTRA_ID_MATCH_T, clickedItem.getIdMatch());
        detailIntent.putExtra(EXTRA_DATE_T, clickedItem.getDate());
        detailIntent.putExtra(EXTRA_ID_HOME_T, clickedItem.getIdHome());
        detailIntent.putExtra(EXTRA_ID_AWAY_T, clickedItem.getIdAway());
        detailIntent.putExtra(EXTRA_HOME_TEAM_T, clickedItem.getHomeTeam());
        detailIntent.putExtra(EXTRA_AWAY_TEAM_T, clickedItem.getAwayTeam());
        detailIntent.putExtra(EXTRA_HOME_SCORE_T, clickedItem.getHomeScore());
        detailIntent.putExtra(EXTRA_AWAY_SCORE_T, clickedItem.getAwayScore());
        detailIntent.putExtra(EXTRA_HOME_IMAGE_T, clickedItem.getHomeImage());
        detailIntent.putExtra(EXTRA_AWAY_IMAGE_T, clickedItem.getAwayImage());

        startActivity(detailIntent);
    }
}
