package com.example.bolasepak.db.model;

public class DataHome {

    private String data;
    private int id;
    private String type;

    public DataHome() {
    }

    public DataHome(String data, String type) {
        this.data = data;
        this.type = type;
//        this.status = status;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setData(String data) {
        this.data = data;
    }

    public  void setType(String type) {
        this.type = type;
    }

    public int getId() {
        return this.id;
    }

    public String getType() {
        return this.type;
    }

    public String getData() {
        return this.data;
    }



}
