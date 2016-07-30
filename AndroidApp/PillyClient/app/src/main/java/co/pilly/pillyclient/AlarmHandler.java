package co.pilly.pillyclient;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.view.WindowManager;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class AlarmHandler extends IntentService {

    public AlarmHandler() {
        super("AlarmHandler");
        Log.d("AlarmHandler", "AlarmHandler service created");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        // Handle shit
        Log.d("AlarmHandler", "Handling intent " + intent.toString());
        PillAlert pillAlert = intent.getParcelableExtra(Schedule.EXTRA_PILLALERT);
        // ArrayList<PillAlert> pillAlerts = Schedule.getSavedAlerts(getSharedPreferences(getResources().getString(R.string.preferences_file_key), Context.MODE_PRIVATE));

        String deviceResponse = "";
        /*try {
            ConnectivityManager connectivityManager =
                    (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) {
                deviceResponse = fetchURL("http://192.168.1.4:5000/mustissuewarning/123");
            } else {
                Log.d("AlarmManager", "No connection available");
            }
        }
        catch (IOException e) {
            e.printStackTrace();
            Log.d("AlarmHandler", "IOException caught");
        }*/

        if(deviceResponse.equals("")) { // TODO: Extend condition or something
            Intent alarmIntent = new Intent(getApplicationContext(), AlarmScreen.class);
            alarmIntent.putExtra(Schedule.EXTRA_PILLALERT, pillAlert);
            alarmIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getApplicationContext().startActivity(alarmIntent);
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
            Log.d("fetchULR", "The response is " + response);
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
