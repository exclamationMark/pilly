package co.pilly.pillyclient;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.List;

public class NewAlert extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener, NumberPicker.OnValueChangeListener{
    public static final int RESULT_SAVE = 3;
    public static final int RESULT_CANCEL = 666;
    public static final int ACTION_ADD = 42;
    public static final int ACTION_EDIT = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_alert);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        myToolbar.setTitle("New Alert");
        setSupportActionBar(myToolbar);

        Typeface light = Typeface.createFromAsset(getAssets(), "Roboto-Thin.ttf");
        TextView alertTime = (TextView) findViewById(R.id.alert_time_label);
        alertTime.setTypeface(light);

        NumberPicker quantityPicker = (NumberPicker) findViewById(R.id.quantity_picker);
        quantityPicker.setMinValue(1);
        quantityPicker.setMaxValue(99);
        quantityPicker.setWrapSelectorWheel(false);
        quantityPicker.setOnValueChangedListener(this);

        Intent intent = getIntent();
        if(intent.getBooleanExtra(Schedule.EXTRA_ADD, true)) {
            DialogFragment newFragment = new TimePickerFragment();
            newFragment.show(getSupportFragmentManager(), "timePicker");
            alert = new PillAlert(0, 0, 1, new int[]{});
        }
        else {
            alert = intent.getParcelableExtra(Schedule.EXTRA_PILLALERT);
            quantityPicker.setValue(alert.getQuantity());
            alertTime.setText(formatTime(alert.getHours(), alert.getMinutes()));
            for(int day : alert.getDays()) {
                ToggleButton toggleButton;
                switch(day) {
                    case 2:
                        toggleButton = (ToggleButton) findViewById(R.id.toggle_monday);
                        break;
                    case 3:
                        toggleButton = (ToggleButton) findViewById(R.id.toggle_tuesday);
                        break;
                    case 4:
                        toggleButton = (ToggleButton) findViewById(R.id.toggle_wednesday);
                        break;
                    case 5:
                        toggleButton = (ToggleButton) findViewById(R.id.toggle_thursday);
                        break;
                    case 6:
                        toggleButton = (ToggleButton) findViewById(R.id.toggle_friday);
                        break;
                    case 7:
                        toggleButton = (ToggleButton) findViewById(R.id.toggle_saturday);
                        break;
                    case 1:
                        toggleButton = (ToggleButton) findViewById(R.id.toggle_sunday);
                        break;
                    default:
                        toggleButton = null;
                        break;
                }
                if (toggleButton != null)
                    toggleButton.setChecked(true);
            }
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
                Intent intent_cancel = new Intent();
                setResult(RESULT_CANCEL, intent_cancel);
                finish();
                return true;
            case R.id.save:
                updateDays();
                if(alert.getDays().length == 0) {
                    Toast.makeText(this, getResources().getString(R.string.no_days_selected), Toast.LENGTH_SHORT).show();
                    return true;
                }
                Intent intent_save = new Intent();
                intent_save.putExtra(Schedule.EXTRA_PILLALERT, alert);
                setResult(RESULT_SAVE, intent_save);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        TextView timeLabel = (TextView) findViewById(R.id.alert_time_label);
        alert.setHours(hourOfDay);
        alert.setMinutes(minute);
        timeLabel.setText(formatTime(hourOfDay, minute));
    }

    @Override
    public void onValueChange(NumberPicker numberPicker, int oldVal, int newVal) {
        alert.setQuantity(newVal);
    }

    public void changeAlertTime(View view) {
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

    private void updateDays() {
        ArrayList<Integer> daysList = new ArrayList<>();
        ToggleButton toggleButton = (ToggleButton) findViewById(R.id.toggle_monday);
        if(toggleButton.isChecked())
            daysList.add(Integer.valueOf(2));
        toggleButton = (ToggleButton) findViewById(R.id.toggle_tuesday);
        if(toggleButton.isChecked())
            daysList.add(Integer.valueOf(3));
        toggleButton = (ToggleButton) findViewById(R.id.toggle_wednesday);
        if(toggleButton.isChecked())
            daysList.add(Integer.valueOf(4));
        toggleButton = (ToggleButton) findViewById(R.id.toggle_thursday);
        if(toggleButton.isChecked())
            daysList.add(Integer.valueOf(5));
        toggleButton = (ToggleButton) findViewById(R.id.toggle_friday);
        if(toggleButton.isChecked())
            daysList.add(Integer.valueOf(6));
        toggleButton = (ToggleButton) findViewById(R.id.toggle_saturday);
        if(toggleButton.isChecked())
            daysList.add(Integer.valueOf(7));
        toggleButton = (ToggleButton) findViewById(R.id.toggle_sunday);
        if(toggleButton.isChecked())
            daysList.add(Integer.valueOf(1));
        Integer[] daysArray = daysList.toArray(new Integer[daysList.size()]);
        int[] intDaysArray = new int[daysArray.length];
        for(int i = 0; i < daysArray.length; i++)
            intDaysArray[i] = daysArray[i].intValue();

        alert.setDays(intDaysArray);
    }

    PillAlert alert;
}
