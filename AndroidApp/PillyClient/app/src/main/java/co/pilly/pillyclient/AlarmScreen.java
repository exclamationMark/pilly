package co.pilly.pillyclient;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

public class AlarmScreen extends AppCompatActivity { // TODO: Add broadcast receiver to stop ringtone on screen lock

    public static final String EXTRA_PILLQTY = "co.pilly.pillyclient.EXTRA_PILLQTY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_screen);

        hideNavigationBar();

        pillAlert = getIntent().getParcelableExtra(Schedule.EXTRA_PILLALERT);
        TextView alarmPillnr = (TextView) findViewById(R.id.alarm_pillnr);
        Typeface light = Typeface.createFromAsset(getAssets(), "Roboto-Thin.ttf");
        alarmPillnr.setTypeface(light);
        alarmPillnr.setText(String.valueOf(pillAlert.getQuantity()));

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
        ringtone = RingtoneManager.getRingtone(getApplicationContext(), alert);
        ringtone.play();

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                + WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD|
                + WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED|
                + WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
    }

    @Override
    public void onPause() {
        super.onPause();
        if(this.isFinishing())
            if(ringtone.isPlaying())
                ringtone.stop();
    }

    private void hideNavigationBar() {
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    public void snooze(View view) {
        if(ringtone.isPlaying())
            ringtone.stop();
        finish();
    }

    PillAlert pillAlert;
    Ringtone ringtone;
}
