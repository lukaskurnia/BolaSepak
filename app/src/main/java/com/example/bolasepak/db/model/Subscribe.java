package com.example.bolasepak.db.model;

public class Subscribe {
    private String team_id;
    private int id;


    public Subscribe() {
    }

    public Subscribe(String team_id) {
        this.team_id = team_id;
    }

    public void setId(int id) {
        this.team_id = team_id;
    }

    public void setTeamId(String team_id) {
        this.team_id = team_id;
    }
    public int getId() {
        return this.id;
    }

    public String getTeamId() {
        return this.team_id;
    }
}
