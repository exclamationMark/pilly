package co.pilly.pillyclient;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

public class EventListAdapter extends ArrayAdapter<PillyEvent> {
    public EventListAdapter(Context context, int textViewResourceId, List<PillyEvent> objects) {
        super(context, textViewResourceId, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.event_list_element, null);

        PillyEvent event = getItem(position);

        if (position%2 == 1) {
            RelativeLayout relativeLayout = (RelativeLayout) convertView.findViewById(R.id.element_layout);
            relativeLayout.setBackgroundColor(getContext().getResources().getColor(R.color.light));
        }

        TextView timestampLabel = (TextView) convertView.findViewById(R.id.timestamp_label);
        TextView pillDeltaLabel = (TextView) convertView.findViewById(R.id.pillDelta_label);
        TextView minutesFromScheduleLabel = (TextView) convertView.findViewById(R.id.minutesFromSchedule_label);

        timestampLabel.setText(StatusFragment.formatTimestamp(event.getTimestamp()));
        pillDeltaLabel.setText(StatusFragment.formatPillDelta(event.getPillDelta()));
        if (event.getPillDelta() < 0)
            minutesFromScheduleLabel.setText(formatMinutesFromSchedule(event.minutesFromSchedule));

        return convertView;
    }

    public static String formatMinutesFromSchedule(int minutesFromSchedule) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(Math.abs(minutesFromSchedule));
        stringBuilder.append(" minutes ");
        if (minutesFromSchedule < 0)
            stringBuilder.append("ahead of schedule.");
        else
            stringBuilder.append("late.");

        return stringBuilder.toString();
    }
}
