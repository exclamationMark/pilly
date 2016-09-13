package co.pilly.pillyclient;

import android.app.AlarmManager;
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

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;

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
        ArrayList<PillAlert> aList = Schedule.getSavedAlerts(getSharedPreferences(getResources().getString(R.string.preferences_file_key), Context.MODE_PRIVATE));

        String deviceResponse = "";
        JSONArray uncheckedEvents = null;
        try {
            ConnectivityManager connectivityManager =
                    (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) {
                deviceResponse = fetchURL("http://130.237.3.216:5000/getRecentUnchecked/101");
                uncheckedEvents = new JSONArray(deviceResponse);
            } else {
                Log.d("AlarmManager", "No connection available");
            }
        }
        catch (IOException|JSONException e) {
            e.printStackTrace();
        }

        if (uncheckedEvents != null) {
            if (uncheckedEvents.length() == 0) {
                Log.d("AlarmHandler", "No unchecked events, issuing alarm...");
                issueAlarm(pillAlert, pillAlert.getQuantity());
            }
            else if (uncheckedEvents.length() == 1) {
                try {
                    int pillDelta = uncheckedEvents.getJSONObject(0).getInt("pillDelta");
                    if (pillDelta > 0) {
                        deviceResponse = fetchURL("http://130.237.3.216:5000/setEventChecked/101/1/" + 0);
                        if (!deviceResponse.contains("ok")) {
                            Log.e("AlarmHandler", "Error while checking event.");
                        }
                        Log.d("AlarmHandler", "Positive delta in the only unchecked event. Issuing alarm...");
                        issueAlarm(pillAlert, pillAlert.getQuantity());
                    } else if (-pillDelta == pillAlert.getQuantity()) {
                        Log.d("AlarmHandler", "User took pills. Scheduling next alarm...");
                        setEventChecked(pillAlert, 1);
                        scheduleNextAlarm(aList);
                    } else if (-pillDelta > pillAlert.getQuantity()) {
                        Log.d("AlarmHandler", "The user overdosed. DO SOMETHING FOR GOD'S SAKE!");
                        deviceResponse = fetchURL("http://130.237.3.216:5000/setEventChecked/101/1/" + 0);
                        if (!deviceResponse.contains("ok")) {
                            Log.e("AlarmHandler", "Error while checking event.");
                        }
                        fireOverDoseWarning(-pillDelta - pillAlert.getQuantity());
                    }
                    else {
                        Log.d("AlarmHandler", "User didn't take enough pills. Issuing alarm...");
                        issueAlarm(pillAlert, pillAlert.getQuantity() + pillDelta);
                    }
                }
                catch (IOException|JSONException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    int totalDelta = 0;
                    for(int i = 0; i < uncheckedEvents.length(); i++)
                        if (uncheckedEvents.getJSONObject(i).getInt("pillDelta") < 0)
                            totalDelta += uncheckedEvents.getJSONObject(i).getInt("pillDelta");
                    if (-totalDelta == pillAlert.getQuantity()) {
                        Log.d("AlarmHandler", "User took the correct nr of pills in multiple sittings. Scheduling next alarm...");
                        for(int i = 0; i < uncheckedEvents.length(); i++)
                            setEventChecked(pillAlert, i + 1);
                        scheduleNextAlarm(aList);
                    } else if (-totalDelta > pillAlert.getQuantity()) {
                        Log.d("AlarmHandler", "Multiple overdoses! OH MY GAWD!");
                        for(int i = 0; i < uncheckedEvents.length(); i++)
                            setEventChecked(pillAlert, i + 1);
                        fireOverDoseWarning(-totalDelta - pillAlert.getQuantity());
                    } else {
                        Log.d("AlarmHandler", "Still not enough pills, Carl.");
                        issueAlarm(pillAlert, pillAlert.getQuantity() + totalDelta);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void issueAlarm(PillAlert pillAlert, int remaining) {
        Intent alarmIntent = new Intent(getApplicationContext(), AlarmScreen.class);
        alarmIntent.putExtra(Schedule.EXTRA_PILLALERT, pillAlert);
        alarmIntent.putExtra(Schedule.EXTRA_REMAINING, remaining);
        alarmIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getApplicationContext().startActivity(alarmIntent);
    }

    private void fireOverDoseWarning(int odQuantity) {
        Intent alarmIntent = new Intent(getApplicationContext(), OverdoseDetected.class);
        alarmIntent.putExtra(Schedule.EXTRA_REMAINING, odQuantity);
        alarmIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getApplicationContext().startActivity(alarmIntent);
    }

    private void scheduleNextAlarm(ArrayList<PillAlert> aList) {
        Intent nextIntent = new Intent(this, AlarmHandler.class);
        nextIntent.putExtra(Schedule.EXTRA_PILLALERT, Schedule.getEarliestAlert(aList));
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getService(this, Schedule.ALARM_INTENT_ID, nextIntent, 0);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, Schedule.getEarliestAlert(aList).getNextTrigger() , pendingIntent);
        Log.d("AlarmManager", "Next alarm set");
    }

    private void setEventChecked(PillAlert pillAlert, int eventId) {
        try {
            String deviceResponse;
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.set(Calendar.HOUR_OF_DAY, pillAlert.getHours());
            calendar.set(Calendar.MINUTE, pillAlert.getMinutes());
            calendar.set(Calendar.SECOND, 0);
            long currentTimeMillis = System.currentTimeMillis();
            long lastTriggerMillis = calendar.getTimeInMillis();
            int minutes = ((int) (currentTimeMillis - lastTriggerMillis)) / 1000 / 60;
            deviceResponse = fetchURL("http://130.237.3.216:5000/setEventChecked/101/" + eventId + "/" + minutes);
            if (!deviceResponse.contains("ok")) {
                Log.e("AlarmHandler", "Error while checking event.");
            }
        } catch (IOException e) {
            e.printStackTrace();
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
