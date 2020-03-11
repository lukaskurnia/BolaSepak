package com.example.bolasepak;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import com.example.bolasepak.api.ApiRepository;

import android.util.Log;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ApiRepository repo = new ApiRepository();
        //String str = repo.doRequest("https://www.thesportsdb.com/api/v1/json/1/eventsnextleague.php?id=4328");
        repo.execute("https://www.thesportsdb.com/api/v1/json/1/eventsnextleague.php?id=4328");
    }
}
