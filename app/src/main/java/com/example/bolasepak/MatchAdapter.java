package com.example.bolasepak;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class MatchAdapter extends RecyclerView.Adapter <MatchAdapter.MatchViewHolder>{
    private Context context;
    private ArrayList<MatchItem> MatchList;

    public MatchAdapter(Context context, ArrayList<MatchItem> MatchList){
        this.context = context;
        this.MatchList = MatchList;
    }

    @Override
    public MatchViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View v = LayoutInflater.from(context).inflate(R.layout.match_item, parent, false);
        return new MatchViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MatchViewHolder holder, int position) {
        MatchItem currentItem = this.MatchList.get(position);

//        String imageUrl = currentItem.getImageUrl();
//        String creatorName = currentItem.getCreator();
//        int likeCount = currentItem.getLikeCount();

        String idMatch = currentItem.getIdMatch();
        String idHome = currentItem.getIdHome();
        String idAway = currentItem.getIdAway();
        String date = currentItem.getDate();
        String homeTeam = currentItem.getHomeTeam();
        String awayTeam = currentItem.getAwayTeam();
        String homeScore = currentItem.getHomeScore();
        String awayScore = currentItem.getAwayScore();
        String homeImage = currentItem.getHomeImage();
        String awayImage = currentItem.getAwayImage();

        holder.date.setText(date);
        holder.textHome.setText(homeTeam);
        holder.textAway.setText(awayTeam);
        holder.scoreHome.setText(homeScore);
        holder.scoreAway.setText(awayScore);
//        holder.imageHome.setText(homeImage);
//        holder.imageHome.setText(homeImage);
        holder.vs.setText("VS");
        Picasso.with(context).load(homeImage).fit().centerInside().into(holder.imageHome);
        Picasso.with(context).load(awayImage).fit().centerInside().into(holder.imageAway);
    }

    @Override
    public int getItemCount() {
        return MatchList.size();
    }


    public class MatchViewHolder extends RecyclerView.ViewHolder{
        public TextView date;
        public TextView textHome;
        public TextView textAway;
        public TextView scoreHome;
        public TextView scoreAway;
        public TextView vs;
        public ImageView imageHome;
        public ImageView imageAway;
        public MatchViewHolder(View itemView){
            super(itemView);
            date = itemView.findViewById(R.id.date);
            textHome = itemView.findViewById(R.id.textHome);
            textAway = itemView.findViewById(R.id.textAway);
            scoreHome = itemView.findViewById(R.id.scoreHome);
            scoreAway = itemView.findViewById(R.id.scoreAway);
            vs = itemView.findViewById(R.id.vs);
            imageHome = itemView.findViewById(R.id.imageHome);
            imageAway = itemView.findViewById(R.id.imageAway);
        }

    }
}
