package co.pilly.pillyclient;

import android.content.Context;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
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

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        myToolbar.setTitle("Status");
        setSupportActionBar(myToolbar);

        if(findViewById(R.id.fragment_container) != null) {
            if(savedInstanceState != null)
                return;

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, new LoadingFragment()).commit();
        }

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
                    StatusFragment statusFragment = new StatusFragment();
                    statusFragment.setArguments(jsonObject);
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_container, statusFragment);
                    fragmentTransaction.commit();
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
}
