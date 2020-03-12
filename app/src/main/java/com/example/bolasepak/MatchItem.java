package com.example.bolasepak;

public class MatchItem {
    private String idMatch;
    private String idHome;
    private String idAway;
    private String date;
    private String homeTeam;
    private String awayTeam;
    private String homeScore;
    private String awayScore;
    private String homeImage;
    private String awayImage;

    public MatchItem(String idMatch, String idHome, String idAway, String date, String homeTeam, String awayTeam, String homeScore, String awayScore, String homeImage, String awayImage){
        this.idMatch = idMatch;
        this.idHome = idHome;
        this.idAway = idAway;
        this.date = date;
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.homeScore = homeScore;
        this.awayScore = awayScore;
        this.homeImage = homeImage;
        this.awayImage = awayImage;
    }

    public String getIdMatch(){
        return this.idMatch;
    }

    public String getIdHome(){
        return this.idHome;
    }

    public String getIdAway(){
        return this.idAway;
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

    public String getHomeImage(){
        return this.homeImage;
    }

    public String getAwayImage(){
        return this.awayImage;
    }

}
