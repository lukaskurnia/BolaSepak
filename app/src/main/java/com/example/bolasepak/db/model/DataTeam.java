package com.example.bolasepak.db.model;

public class DataTeam {
    private String data;
    private int id;
    private String team_id;
    private String type;
    private String status;

    public DataTeam() {
    }

    public DataTeam(String data, String team_id, String type, String status) {
        this.data = data;
        this.team_id = team_id;
        this.type = type;
        this.status = status;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setData(String data) {
        this.data = data;
    }

    public  void setTeamId(String team_id) {
        this.team_id = team_id;
    }

    public  void setType(String type) {
        this.type = type;
    }

    public  void setStatus(String status) {
        this.type = status;
    }

    public int getId() {
        return this.id;
    }

    public String getTeamId() {
        return this.team_id;
    }

    public String getData() {
        return this.data;
    }

    public String getType() {
        return this.type;
    }

    public String getStatus() {
        return this.status;
    }

}
