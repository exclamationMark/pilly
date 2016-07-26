package co.pilly.pillyclient;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.Comparator;
import java.util.List;

public class ScheduleAdapter extends ArrayAdapter<PillAlert> {
    public ScheduleAdapter(Context context, int textViewResourceId, List<PillAlert> objects, Typeface tf) {
        super(context, textViewResourceId, objects);
        this.light = tf;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.list_element, null);

        PillAlert alert = getItem(position);
        TextView alertTime = (TextView) convertView.findViewById(R.id.alert_time);
        alertTime.setTypeface(light);
        TextView quantity = (TextView) convertView.findViewById(R.id.quantity);
        TextView days = (TextView) convertView.findViewById(R.id.days);

        alertTime.setText(NewAlert.formatTime(alert.getHours(), alert.getMinutes()));
        quantity.setText(String.format(getContext().getResources().getString(R.string.quantity), alert.getQuantity()));
        days.setText(formatDaysString(alert.getDays()));

        return convertView;
    }

    public static String formatDaysString(int[] alertDays) {
        String displayDays = "";
        for(int day : alertDays) {
            switch(day) {
                case 1:
                    displayDays += "MON, ";
                    break;
                case 2:
                    displayDays += "TUE, ";
                    break;
                case 3:
                    displayDays += "WED, ";
                    break;
                case 4:
                    displayDays += "THU, ";
                    break;
                case 5:
                    displayDays += "FRI, ";
                    break;
                case 6:
                    displayDays += "SAT, ";
                    break;
                case 7:
                    displayDays += "SUN, ";
                    break;
                default:
                    break;
            }
        }
        if (displayDays.equals(""))
            return "";
        displayDays = displayDays.substring(0, displayDays.length()-2);
        return displayDays;
    }

    Typeface light;
}
