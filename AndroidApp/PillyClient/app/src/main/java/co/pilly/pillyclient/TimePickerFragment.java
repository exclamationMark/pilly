package co.pilly.pillyclient;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;

public class TimePickerFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), (TimePickerDialog.OnTimeSetListener) getActivity(), hourOfDay, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    public void setArguments(Bundle arguments) {
        hourOfDay = arguments.getInt("hourOfDay");
        minute = arguments.getInt("minute");
    }

    int hourOfDay = 0;
    int minute = 0;
}