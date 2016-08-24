package co.pilly.pillyclient;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;

/**
 * Broadcast receiver that resets the next alarm on boot
 */
public class BootCompletedBReceiver extends BroadcastReceiver{

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            ArrayList<PillAlert> aList = Schedule.getSavedAlerts(
                    context.getSharedPreferences(context.getResources().getString(R.string.preferences_file_key),
                            Context.MODE_PRIVATE));
            if (aList.size() > 0) {
                PillAlert nextAlert = Schedule.getEarliestAlert(aList);
                Intent nextAlarmIntent = new Intent(context, AlarmHandler.class);
                intent.putExtra(Schedule.EXTRA_PILLALERT, nextAlert);
                PendingIntent nextPendingIntent = PendingIntent.getService(context, Schedule.ALARM_INTENT_ID, nextAlarmIntent, 0);
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, nextAlert.getNextTrigger(), nextPendingIntent);
            }
        }
    }
}
