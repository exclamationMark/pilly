package co.pilly.pillyclient;

import android.content.Context;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Status extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);

        Description = (TextView) findViewById(R.id.description);
        PillCount = (TextView) findViewById(R.id.pillnr);
        NextPillTime = (TextView) findViewById(R.id.nextpill);
        Typeface light = Typeface.createFromAsset(this.getAssets(), "Roboto-Thin.ttf");
        PillCount.setTypeface(light);

        ConnectivityManager connectivityManager =   // Start fetching user data
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            new JessicaFetcher().execute("http://192.168.1.6:5000/getinfo/123");
        }
        else {
            System.out.println("No connection available");
        }
    }

    private class JessicaFetcher extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            try {
                return fetchURL(urls[0]);
            }
            catch (IOException e) {
                e.printStackTrace();
                return "Error";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            if(result.equals("Error")) {
                System.out.println("Something wrong");
            }
            else {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    // Set text
                    PillCount.setText(String.valueOf(jsonObject.getInt("pillCount")));
                    Description.setText(jsonObject.getString("description"));
                    int next = jsonObject.getInt("nextPillTime");
                    if(next < 30)
                        NextPillTime.setText(String.format(getResources().getString(R.string.next_pill_min), next));
                    else if (next < 90)
                        NextPillTime.setText(getResources().getText(R.string.next_pill_hr));
                    else {
                        int hrs = (int)Math.round((float)next/60.0);
                        NextPillTime.setText(String.format(getResources().getString(R.string.next_pill_hrs), hrs));
                    }
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        private String fetchURL(String destination) throws IOException{
            InputStream is = null;

            try {
                URL url = new URL(destination);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                // Start query
                conn.connect();
                int response = conn.getResponseCode();
                Log.d("JessicaFetcher", "The response is " + response);
                is = new BufferedInputStream(conn.getInputStream());

                // Convert input stream to string

                ByteArrayOutputStream bo = new ByteArrayOutputStream();
                int i = is.read();
                while (i != -1) {
                    bo.write(i);
                    i = is.read();
                }
                return bo.toString();
            }
            finally {
                if (is != null) {
                    is.close();
                }
            }
        }
    }

    TextView PillCount, Description, NextPillTime;
}
