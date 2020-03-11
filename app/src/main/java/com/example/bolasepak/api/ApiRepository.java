package com.example.bolasepak.api;
import java.net.*;
import java.io.*;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ApiRepository extends AsyncTask<String, Void, String> {
    @Override
    protected String doInBackground(String... url) {
        StringBuilder content = new StringBuilder();

        // many of these calls can throw exceptions, so i've just
        // wrapped them all in one try/catch statement.
        try {
            // create a url object
            URL theURL = new URL(url[0]);

            // create a urlconnection object
            HttpURLConnection urlConnection = (HttpURLConnection)theURL.openConnection();
            Log.i("URL Conn", "succeeded 1");
//            InputStream in = urlConnection.getInputStream();
//            Log.i("URL Conn", "succeeded4");
//            InputStreamReader reader = new InputStreamReader(in);
//            Log.i("URL Conn", "succeeded4");

            // wrap the urlconnection in a bufferedreader
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            Log.i("URL Conn", "succeeded3");
            String line;

            // read from the urlconnection via the bufferedreader
            while ((line = bufferedReader.readLine()) != null) {
                content.append(line);
            }
            Log.i("URL Conn", "succeeded4");
            bufferedReader.close();
            Log.i("URL Conn", "succeeded2");
            return content.toString();
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("URL Conn", "failed");
        }
        return null;

    }

    @Override
    protected void onPostExecute(String res){
        super.onPostExecute(res);

        try{
            JSONObject jsonObject = new JSONObject(res);
            String event = jsonObject.getString("events");
            Log.i("Event json", "succeeded");

            JSONArray jsonArray = new JSONArray(event);

        }
        catch (JSONException e){
            e.printStackTrace();
        }
    }
}
