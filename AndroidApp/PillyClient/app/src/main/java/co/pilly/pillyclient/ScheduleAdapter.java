package co.pilly.pillyclient;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ScheduleAdapter extends ArrayAdapter<PillAlert> {
    public ScheduleAdapter(Context context, int textViewResourceId, PillAlert [] objects) {
        super(context, textViewResourceId, objects);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.list_element, null);

        PillAlert alert = getItem(position);
        TextView alertTime = (TextView) convertView.findViewById(R.id.alert_time);
        TextView quantity = (TextView) convertView.findViewById(R.id.quantity);
        TextView days = (TextView) convertView.findViewById(R.id.days);

        alertTime.setText(String.valueOf(alert.getHours()) + ":" + String.valueOf(alert.getMinutes()));
        quantity.setText(String.format(context.getResources().getString(R.string.quantity), alert.getQuantity()));
        String displayDays = "";
        DAYS [] alertDays = alert.getDays();
        for(DAYS day : alertDays) {
            switch(day) {
                case MON:
                    displayDays += "MON, ";
                    break;
                case TUE:
                    displayDays += "TUE, ";
                    break;
                case WED:
                    displayDays += "WED, ";
                    break;
                case THU:
                    displayDays += "THU, ";
                    break;
                case FRI:
                    displayDays += "FRI, ";
                    break;
                case SAT:
                    displayDays += "SAT, ";
                    break;
                case SUN:
                    displayDays += "SUN, ";
                    break;
                default:
                    break;
            }
        }
        displayDays = displayDays.substring(0, displayDays.length()-2);
        days.setText(displayDays);

        return convertView;
    }

    Context context;
}
