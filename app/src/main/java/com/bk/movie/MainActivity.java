package com.bk.movie;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    EditText etMovieName;
    Button btnSearch;
    TextView tvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etMovieName=findViewById(R.id.etMovieName);
        btnSearch=findViewById(R.id.btnSearch);
        tvResult=findViewById(R.id.tvResult);


        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mn=etMovieName.getText().toString();
                if(mn.length()==0){
                    etMovieName.setError("MOVIE NAME IS EMPTY !");
                    etMovieName.requestFocus();
                    return;
                }

                MeraTask t1=new MeraTask();
                String w1="http://www.omdbapi.com/";
                String w2="?s="+mn;
                String w3="&apikey=7f0ea0da";
                String w=w1+w2+w3;
                t1.execute(w);
            }
        });
    }

    class MeraTask extends AsyncTask<String, Void, String>{
        String jsonStr="";
        String line="";
        String searchResult="";

        @Override
        protected String doInBackground(String... strings) {
            try {
                URL url=new URL(strings[0]);
                HttpURLConnection httpURLConnection=(HttpURLConnection) url.openConnection();
                httpURLConnection.connect();


                InputStream is = httpURLConnection.getInputStream();
                BufferedReader reader=new BufferedReader(new InputStreamReader(is));

                while ((line=reader.readLine())!=null){
                    jsonStr += line+"\n";
                }

                if(jsonStr!=null){
                    JSONObject jsonObject=new JSONObject(jsonStr);
                    JSONArray movieArray=jsonObject.getJSONArray("Search");

                    for (int i=0;i<movieArray.length();i++){
                        JSONObject movie=movieArray.getJSONObject(i);
                        String title=movie.getString("Title");
                        String year=movie.getString("Year");
                        searchResult += title+"    "+year+"\n";
                    }
                }

            }
            catch (Exception e){
                e.printStackTrace();
            }
            return searchResult;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            tvResult.setText(s);
        }
    }
}
