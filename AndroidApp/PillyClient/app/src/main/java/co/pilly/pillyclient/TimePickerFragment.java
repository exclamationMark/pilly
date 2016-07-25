package co.pilly.pillyclient;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.widget.TextView;
import android.widget.TimePicker;

public class TimePickerFragment extends DialogFragment
        implements TimePickerDialog.OnTimeSetListener {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        timeLabel = (TextView) getActivity().findViewById(R.id.alert_time_label);
        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hourOfDay, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        this.hourOfDay = hourOfDay;
        this.minute = minute;
        timeLabel.setText(NewAlert.formatTime(hourOfDay,minute));

    }

    public void setArguments(Bundle arguments) {
        hourOfDay = arguments.getInt("hourOfDay");
        minute = arguments.getInt("minute");
    }

    int hourOfDay = 0;
    int minute = 0;
    TextView timeLabel;
}