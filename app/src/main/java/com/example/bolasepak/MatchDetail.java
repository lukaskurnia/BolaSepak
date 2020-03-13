package com.example.bolasepak;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;

import static com.example.bolasepak.MainActivity.EXTRA_ID_MATCH;
import static com.example.bolasepak.MainActivity.EXTRA_DATE;
import static com.example.bolasepak.MainActivity.EXTRA_ID_HOME;
import static com.example.bolasepak.MainActivity.EXTRA_ID_AWAY;
import static com.example.bolasepak.MainActivity.EXTRA_HOME_TEAM;
import static com.example.bolasepak.MainActivity.EXTRA_AWAY_TEAM;
import static com.example.bolasepak.MainActivity.EXTRA_HOME_SCORE;
import static com.example.bolasepak.MainActivity.EXTRA_AWAY_SCORE;
import static com.example.bolasepak.MainActivity.EXTRA_HOME_IMAGE;
import static com.example.bolasepak.MainActivity.EXTRA_AWAY_IMAGE;



public class MatchDetail extends AppCompatActivity {
    public static final String EXTRA_TEAM_ID_D = "idTeam";
    public static final String EXTRA_TEAM_NAME_D = "teamName";
    public static final String EXTRA_TEAM_IMAGE_D = "teamImage";

    private RequestQueue requestQueue;
    private String[] homeGoalsList;
    private String[] awayGoalsList;
    private String homeShots;
    private String awayShots;
    private String idHome;
    private String idAway;
    private String homeTeam;
    private String awayTeam;
    private String homeImage;
    private String awayImage;

//    private OnItemClickListener dListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_detail);
        requestQueue = Volley.newRequestQueue(this);
        homeGoalsList = new String[20];
        awayGoalsList = new String[20];

        Intent intent = getIntent();

        String idMatch = intent.getStringExtra(EXTRA_ID_MATCH);
        String date = intent.getStringExtra(EXTRA_DATE);
        idHome = intent.getStringExtra(EXTRA_ID_HOME);
        idAway = intent.getStringExtra(EXTRA_ID_AWAY);
        homeTeam = intent.getStringExtra(EXTRA_HOME_TEAM);
        awayTeam = intent.getStringExtra(EXTRA_AWAY_TEAM);
        String homeScore = intent.getStringExtra(EXTRA_HOME_SCORE);
        String awayScore = intent.getStringExtra(EXTRA_AWAY_SCORE);
        homeImage = intent.getStringExtra(EXTRA_HOME_IMAGE);
        awayImage = intent.getStringExtra(EXTRA_AWAY_IMAGE);

        TextView tDate = findViewById(R.id.ddate);
        TextView teamHome = findViewById(R.id.dTextHome);
        TextView teamAway = findViewById(R.id.dTextAway);
        TextView scoreHome = findViewById(R.id.dScoreHome);
        TextView scoreAway = findViewById(R.id.dScoreAway);
        ImageView imageHome = findViewById(R.id.dImageHome);
        ImageView imageAway = findViewById(R.id.dImageAway);

        tDate.setText(date);
        teamHome.setText(homeTeam);
        teamAway.setText(awayTeam);
        scoreHome.setText(homeScore);
        scoreAway.setText(awayScore);
        Picasso.get().load(homeImage).fit().centerInside().into(imageHome);
        Picasso.get().load(awayImage).fit().centerInside().into(imageAway);

        parseJSONMatchDetail(idMatch);

    }

    private void parseJSONMatchDetail(String idMatch) {
        String url = "https://www.thesportsdb.com/api/v1/json/1/lookupevent.php?id=" + idMatch;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("events");
//                            System.out.println(jsonArray.get(0));
//                            System.out.println(jsonArray.get(1));

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject hit = jsonArray.getJSONObject(i);

                                homeShots = hit.getString("intHomeShots");
                                if (homeShots.equals("null")){
                                    homeShots = "-";
                                }
                                awayShots = hit.getString("intAwayShots");
                                if (awayShots.equals("null")){
                                    awayShots = "-";
                                }
                                String homeGoalsRaw = hit.getString("strHomeGoalDetails");
                                String homeGoalsRep = homeGoalsRaw.replace(":", " ");
                                homeGoalsList = homeGoalsRep.split(";");
                                String awayGoalsRaw = hit.getString("strAwayGoalDetails");
                                String awayGoalsRep = awayGoalsRaw.replace(":", " ");
                                awayGoalsList = awayGoalsRep.split(";");
                                Log.i("Team", homeGoalsRep);
                                Log.i("Team", homeGoalsList[0]);
//                                System.out.println(idTeam);
//                                System.out.println(badgeTeam);
                            }
                            TextView shotsHome = findViewById(R.id.dShotsHome);
                            TextView shotsAway = findViewById(R.id.dShotsAway);
                            TextView goalsHome = findViewById(R.id.dGoalsHome);
                            TextView goalsAway = findViewById(R.id.dGoalsAway);

                            shotsHome.setText(homeShots);
                            shotsAway.setText(awayShots);
                            for (int i = 0; i < homeGoalsList.length; i++) {
                                    goalsHome.append(homeGoalsList[i]);
                                    goalsHome.append("\n");
                            }

                            for (int i = 0; i < awayGoalsList.length; i++) {
                                goalsAway.append(awayGoalsList[i]);
                                goalsAway.append("\n");
                            }
//                            int size = teamHash.size();
//                            System.out.println(size);
//                            parseJSONMatch();

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.i("Match Detail", "failed");
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

    public void onImageHomeClick(View v){
        Intent teamIntent = new Intent(getBaseContext(), TeamDetail.class);

        teamIntent.putExtra(EXTRA_TEAM_ID_D, idHome);
        teamIntent.putExtra(EXTRA_TEAM_NAME_D, homeTeam);
        teamIntent.putExtra(EXTRA_TEAM_IMAGE_D, homeImage);

        startActivity(teamIntent);
    }

    public void onImageAwayClick(View v){
        Intent teamIntent = new Intent(getBaseContext(), TeamDetail.class);

        teamIntent.putExtra(EXTRA_TEAM_ID_D, idAway);
        teamIntent.putExtra(EXTRA_TEAM_NAME_D, awayTeam);
        teamIntent.putExtra(EXTRA_TEAM_IMAGE_D, awayImage);

        startActivity(teamIntent);
    }

//    public void OnItemClick(int position){
//        Intent detailIntent = new Intent(this, MatchDetail.class);
//        MatchItem clickedItem = matchList.get(position);
//
//        detailIntent.putExtra(EXTRA_ID_MATCH, clickedItem.getIdMatch());
//        detailIntent.putExtra(EXTRA_DATE, clickedItem.getDate());
//        detailIntent.putExtra(EXTRA_ID_HOME, clickedItem.getIdHome());
//        detailIntent.putExtra(EXTRA_ID_AWAY, clickedItem.getIdAway());
//        detailIntent.putExtra(EXTRA_HOME_TEAM, clickedItem.getHomeTeam());
//        detailIntent.putExtra(EXTRA_AWAY_TEAM, clickedItem.getAwayTeam());
//        detailIntent.putExtra(EXTRA_HOME_SCORE, clickedItem.getHomeScore());
//        detailIntent.putExtra(EXTRA_AWAY_SCORE, clickedItem.getAwayScore());
//        detailIntent.putExtra(EXTRA_HOME_IMAGE, clickedItem.getHomeImage());
//        detailIntent.putExtra(EXTRA_AWAY_IMAGE, clickedItem.getAwayImage());
//
//        startActivity(detailIntent);
//    }
//
//    public void setOnItemClickListener(OnItemClickListener listener) {
//        dListener = listener;
//    }
}
