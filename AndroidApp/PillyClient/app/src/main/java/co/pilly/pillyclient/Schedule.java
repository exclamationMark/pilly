package co.pilly.pillyclient;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Set;

public class Schedule extends AppCompatActivity implements ActionMode.Callback, AdapterView.OnItemLongClickListener {
    public static final String EXTRA_PILLALERT = "co.pilly.pillyclient.EXTRA_PILLALERT";
    public static final String EXTRA_ADD = "co.pilli.pilliclient.EXTRA_ADD";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        myToolbar.setTitle(getResources().getString(R.string.schedule));
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        aList = getSavedAlerts(getSharedPreferences(getResources().getString(R.string.preferences_file_key), Context.MODE_PRIVATE));

        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        listView = (ListView) findViewById(R.id.schedule_list);
        scheduleAdapter = new ScheduleAdapter(this, R.layout.list_element, aList, Typeface.createFromAsset(getAssets(), "Roboto-Thin.ttf"));
        listView.setAdapter(scheduleAdapter);
        listView.setOnItemLongClickListener(this);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
    }

    @Override
    public void onPause() {
        super.onPause();
        saveAlertsToPreferences();
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
        listView.setItemChecked(i, true);
        view.setSelected(true);
        // TODO: Fix the 'unable to stay highlighted' issue
        if (mActionMode != null)
            return false;
        mActionMode = startSupportActionMode(this);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_schedule, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add:
                Intent intent = new Intent(this, NewAlert.class);
                intent.putExtra(EXTRA_ADD, true);
                startActivityForResult(intent, NewAlert.ACTION_ADD);
                if (mActionMode != null)
                    mActionMode.finish();
                return true;
            case R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_schedule, menu);
        return true;
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {
        mActionMode = null;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit:
                PillAlert toEdit = (PillAlert) listView.getItemAtPosition(listView.getCheckedItemPosition());
                Intent intent = new Intent(this, NewAlert.class);
                intent.putExtra(EXTRA_ADD, false);
                intent.putExtra(EXTRA_PILLALERT, toEdit);
                startActivityForResult(intent, NewAlert.ACTION_EDIT);
                if (mActionMode != null)
                    mActionMode.finish();
                return true;
            case R.id.info:
                return true;
            case R.id.delete:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder
                        .setMessage(getResources().getString(R.string.alert_delete_confirm))
                        .setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                if (mActionMode != null)
                                    mActionMode.finish();
                                scheduleAdapter.remove((PillAlert) listView.getItemAtPosition(listView.getCheckedItemPosition()));
                            }
                        })
                        .setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        })
                        .show();
                return true;
            default:
                return false;
        }
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == NewAlert.RESULT_SAVE) {
            PillAlert receivedAlert = data.getParcelableExtra(EXTRA_PILLALERT);
            Intent intent = new Intent(this, AlarmHandler.class);
            intent.putExtra(Schedule.EXTRA_PILLALERT, receivedAlert);
            PendingIntent newAlarmPendingIntent = PendingIntent.getService(this, 0, intent, 0);
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis()); // Used to set the current date other than hor and minute
            calendar.set(Calendar.HOUR_OF_DAY, receivedAlert.getHours());
            calendar.set(Calendar.MINUTE, receivedAlert.getMinutes());
            switch (requestCode) {
                case NewAlert.ACTION_EDIT:
                    /* alarmManager.cancel(receivedAlert.getPendingIntent());
                    receivedAlert.setPendingIntent(newAlarmPendingIntent);
                    alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), receivedAlert.getPendingIntent());*/

                    aList.set(listView.getCheckedItemPosition(), receivedAlert);
                    break;
                case NewAlert.ACTION_ADD:
                    //receivedAlert.setPendingIntent(newAlarmPendingIntent);
                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), newAlarmPendingIntent);
                    aList.add(receivedAlert);
                    break;
            }
            Collections.sort(aList);
            scheduleAdapter.notifyDataSetChanged();
            Toast.makeText(this, getResources().getString(R.string.alert_saved), Toast.LENGTH_SHORT).show();
        }
    }

    public static ArrayList<PillAlert> getSavedAlerts(SharedPreferences sharedPreferences) {
        ArrayList<PillAlert> aList = new ArrayList<>();
        int jsonArraySize = sharedPreferences.getInt("alertNumber", 0);
        for (int i = 0; i < jsonArraySize; i++)
            aList.add(PillAlert.fromString(sharedPreferences.getString("alert_" + i, null)));
        Collections.sort(aList);
        return aList;
    }

    private void saveAlertsToPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences(getResources().getString(R.string.preferences_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        int alertNumber = sharedPreferences.getInt("alertNumber", 0);
        for(int i = 0; i < alertNumber; i++)
            editor.remove("alert_" + i);
        editor.remove("alertNumber");

        editor.putInt("alertNumber", aList.size());
        for(int i = 0; i < aList.size(); i++)
            editor.putString("alert_" + i, aList.get(i).toString());

        editor.commit();
    }

    ActionMode mActionMode;
    ListView listView;
    ScheduleAdapter scheduleAdapter;
    ArrayList<PillAlert> aList = null;
    AlarmManager alarmManager;
}
