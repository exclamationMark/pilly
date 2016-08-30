package co.pilly.pillyclient;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class AllEvents extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        myToolbar.setTitle(getResources().getString(R.string.all_events));
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
            jessicaFetcher = new JessicaFetcher();
            jessicaFetcher.execute("http://130.237.3.216:5000/getRecentEvents/123/0");
        }
        else {
            Log.d("Status", "WARNING: No connection available");
            Toast.makeText(this, getResources().getString(R.string.no_connection), Toast.LENGTH_SHORT).show();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new NetErrorFragment()).commit();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        jessicaFetcher.cancel(true);
    }

    @Override
    public void onClick(View view) {
        ConnectivityManager connectivityManager =   // Start fetching user data
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            new JessicaFetcher().execute("http://130.237.3.216:5000/getRecentEvents/123/0");
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new LoadingFragment()).commit();
        }
        else {
            Log.d("AllEvents", "WARNING: No connection available");
            Toast.makeText(this, getResources().getString(R.string.no_connection), Toast.LENGTH_SHORT).show();
        }
    }

    public void setNetErrorLabel(TextView label) {
        label.setOnClickListener(this);
    }

    private class JessicaFetcher extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            try {
                return fetchURL(urls[0]);
            } catch (IOException e) {
                e.printStackTrace();
                Log.d("JessicaFetcher", "Caught IOException");
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new NetErrorFragment()).commit();
                return "Error";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            if (result.equals("Error")) {
                Log.d("JessicaFetcher", "ERROR: Could not create JSONObject");
            } else {
                AllEventsFragment allEventsFragment = new AllEventsFragment();
                String[] events = result.substring(1, result.length() - 1).split("(?<=\\}),\\s");
                eList = new ArrayList<>();
                for (String event : events)
                    eList.add(PillyEvent.fromString(event));
                eventListAdapter = new EventListAdapter(getApplicationContext(), R.layout.event_list_element, eList);
                allEventsFragment.setArgs(eventListAdapter);
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, allEventsFragment);
                fragmentTransaction.commit();
            }
        }

        private String fetchURL(String destination) throws IOException {
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
            } finally {
                if (is != null) {
                    is.close();
                }
            }
        }
    }

    ArrayList<PillyEvent> eList;
    EventListAdapter eventListAdapter;
    JessicaFetcher jessicaFetcher;
}
