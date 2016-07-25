package co.pilly.pillyclient;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;

public class NewAlert extends AppCompatActivity {
    public static final int RESULT_SAVE = 3;
    public static final int RESULT_CANCEL = 666;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_alert);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        myToolbar.setTitle("New Alert");
        setSupportActionBar(myToolbar);

        Intent intent = getIntent();
        if(intent.getBooleanExtra(Schedule.EXTRA_ADD, true)) {
            DialogFragment newFragment = new TimePickerFragment();
            newFragment.show(getSupportFragmentManager(), "timePicker");
        }
        else {
            alert = intent.getParcelableExtra(Schedule.EXTRA_PILLALERT);
            TextView alertTime = (TextView) findViewById(R.id.alert_time_label);
            alertTime.setText(formatTime(alert.getHours(), alert.getMinutes()));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_new_alert, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.cancel:
                // TODO: Add suitable code here
                Intent intent_cancel = new Intent();
                setResult(RESULT_CANCEL, intent_cancel);
                finish();
                return true;
            case R.id.save:
                Intent intent_save = new Intent();
                setResult(RESULT_SAVE, intent_save);
                finish();
                // TODO: And here
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void changeAlertTime(View view) {
        TextView alertTime = (TextView) findViewById(R.id.alert_time_label);
        String hourText = alertTime.getText().toString();
        String[] hrMin = hourText.split(":");
        alert.setHours(Integer.valueOf(hrMin[0]));
        alert.setMinutes(Integer.valueOf(hrMin[1]));
        DialogFragment newFragment = new TimePickerFragment();
        Bundle args = new Bundle();
        args.putInt("hourOfDay", alert.getHours());
        args.putInt("minute", alert.getMinutes());
        newFragment.setArguments(args);
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }

    public static String formatTime(int hourOfDay, int minute) {
        if(hourOfDay < 10) {
            if (minute < 10) {
                return "0" + hourOfDay + ":0" + minute;
            } else {
                return "0" + hourOfDay + ":" + minute;
            }
        } else {
            if(minute < 10) {
                return hourOfDay + ":0" + minute;
            } else {
                return hourOfDay + ":" + minute;
            }
        }
    }

    PillAlert alert;
}
