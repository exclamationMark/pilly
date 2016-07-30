package co.pilly.pillyclient;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

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
        try {
            deviceResponse = fetchURL("http://192.168.1.4:5000/mustissuewarning/123");
        }
        catch (IOException e) {
            e.printStackTrace();
            Log.d("AlarmHandler", "IOException caught");
        }

        if(deviceResponse.equals("1")) { // TODO: Extend condition or something
            Uri alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
            if (alert == null) {
                // alert is null, using backup
                alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

                // I can't see this ever being null (as always have a default notification)
                // but just in case
                if (alert == null) {
                    // alert backup is null, using 2nd backup
                    alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
                }
            }
            Ringtone ringtone = RingtoneManager.getRingtone(getApplicationContext(), alert);
            ringtone.play();

            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(this)
                            .setSmallIcon(R.drawable.ic_info_outline_black_24dp)
                            .setContentTitle("Take your pills!")
                            .setContentText(pillAlert.getQuantity() + " of them.")
                            .setAutoCancel(true);
            Intent resultIntent = new Intent(this, Status.class);
            PendingIntent resultPendingIntent =
                    PendingIntent.getActivity(
                            this,
                            0,
                            resultIntent,
                            PendingIntent.FLAG_UPDATE_CURRENT
                    );
            mBuilder.setContentIntent(resultPendingIntent);
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.notify(001, mBuilder.build());

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (ringtone.isPlaying())
                ringtone.stop();
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
