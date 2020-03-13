package com.example.bolasepak.db.model;

public class DataMatch {
    private String data;
    private int id;
    private String match_id;

    public DataMatch() {
    }

    public DataMatch(String data, String match_id) {
        this.data = data;
        this.match_id = match_id;
//        this.status = status;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setData(String data) {
        this.data = data;
    }

    public  void setMatchId(String match_id) {
        this.match_id = match_id;
    }

    public int getId() {
        return this.id;
    }

    public String getMatchId() {
        return this.match_id;
    }

    public String getData() {
        return this.data;
    }

}
