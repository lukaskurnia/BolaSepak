package com.example.bolasepak;

public class MatchItem {
    private String id;
    private String date;
    private String homeTeam;
    private String awayTeam;
    private String homeScore;
    private String awayScore;

    public MatchItem(String id, String date, String homeTeam, String awayTeam, String homeScore, String awayScore){
        this.id = id;
        this.date = date;
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.homeScore = homeScore;
        this.awayScore = awayScore;
    }

    public String getId(){
        return this.id;
    }

    public String getDate(){
        return this.date;
    }

    public String getHomeTeam(){
        return this.homeTeam;
    }

    public  String getAwayTeam(){
        return this.awayTeam;
    }

    public String getHomeScore(){
        return this.homeScore;
    }

    public String getAwayScore(){
        return this.awayScore;
    }

}
