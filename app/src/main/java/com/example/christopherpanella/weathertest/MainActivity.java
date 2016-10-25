package com.example.christopherpanella.weathertest;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    String location = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = (Button) findViewById(R.id.weatherButton);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EditText editText = (EditText)findViewById(R.id.location);
                location = editText.getText().toString();

                WeatherDownloader weatherDownloader = new WeatherDownloader();
                weatherDownloader.execute("http://api.openweathermap.org/data/2.5/weather?q=" + location + ",uk&appid=4781217584092d88fa71589346da7404");

            }
        });
    }

    public class WeatherDownloader extends AsyncTask<String, Void, String>{


        @Override
        protected String doInBackground(String... strings) {

            try {

                String result = "";
                URL url = new URL(strings[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.connect();
                InputStream inputStream = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(inputStream);

                int data = reader.read();

                while (data != -1){

                    char current = (char) data;

                    result += current;

                    data = reader.read();

                }

                return result;


            } catch (MalformedURLException e) {

                e.printStackTrace();

            } catch (IOException e) {

                e.printStackTrace();

            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            try {

                JSONObject jsonObject = new JSONObject(result);

                String weatherInfo = jsonObject.getString("weather");

                JSONArray array = new JSONArray(weatherInfo);

                for (int i = 0; i < array.length(); i++){

                    JSONObject jsonObject1 = array.getJSONObject(i);

                    String test1 = jsonObject1.getString("main");

                    String test2 = jsonObject1.getString("description");

                    TextView textView = (TextView) findViewById(R.id.weatherText);

                    textView.setText("Main: " + test1 + "Description: " + test2);

                }

            } catch (JSONException e) {

                e.printStackTrace();

            }

        }
    }
}
