package co.pilly.pillyclient;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class StatusFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mView = inflater.inflate(R.layout.fragment_status, container, false);
        ArrayList<PillAlert> aList = Schedule.getSavedAlerts(getActivity().getSharedPreferences(getResources().getString(R.string.preferences_file_key), Context.MODE_PRIVATE));

        try {
            TextView Description = (TextView) mView.findViewById(R.id.description);
            Description.setText(jsonObject.getString("description"));

            TextView PillCount = (TextView) mView.findViewById(R.id.pillnr);
            PillCount.setText(String.valueOf(jsonObject.getInt("pillCount")));
            Typeface light = Typeface.createFromAsset(getActivity().getAssets(), "Roboto-Thin.ttf");
            PillCount.setTypeface(light);

            TextView NextPillTime = (TextView) mView.findViewById(R.id.nextpill);
            if (aList.size() > 0) {
                long nextMillis = Schedule.getEarliestAlert(aList).getNextTrigger() - System.currentTimeMillis();
                int next = (int) (nextMillis / (1000 * 60));
                if (next < 30)
                    NextPillTime.setText(String.format(getResources().getString(R.string.next_pill_min), next));
                else if (next < 90)
                    NextPillTime.setText(getResources().getText(R.string.next_pill_hr));
                else if (next < 1440) {
                    int hrs = (int) Math.round((float) next / 60.0);
                    NextPillTime.setText(String.format(getResources().getString(R.string.next_pill_hrs), hrs));
                } else {
                    int days = (int) Math.round((float) next / 1440);
                    NextPillTime.setText(String.format(getResources().getString(R.string.next_pill_days), days));
                }
            } else {
                NextPillTime.setText(getResources().getString(R.string.no_next_pill));
            }

            TextView Date1 = (TextView) mView.findViewById(R.id.timestamp1);
            TextView Date2 = (TextView) mView.findViewById(R.id.timestamp2);
            TextView Date3 = (TextView) mView.findViewById(R.id.timestamp3);
            TextView Date4 = (TextView) mView.findViewById(R.id.timestamp4);
            TextView Date5 = (TextView) mView.findViewById(R.id.timestamp5);
            TextView Event1 = (TextView) mView.findViewById(R.id.event1);
            TextView Event2 = (TextView) mView.findViewById(R.id.event2);
            TextView Event3 = (TextView) mView.findViewById(R.id.event3);
            TextView Event4 = (TextView) mView.findViewById(R.id.event4);
            TextView Event5 = (TextView) mView.findViewById(R.id.event5);

            JSONArray recent = jsonObject.getJSONArray("recent");

            if (recent.length() > 0) {
                Date1.setText(String.valueOf(recent.getJSONObject(0).getString("time")));
                Event1.setText(String.valueOf(recent.getJSONObject(0).getString("pillDelta")));
            }
            if (recent.length() > 1) {
                Date2.setText(String.valueOf(recent.getJSONObject(1).getString("time")));
                Event2.setText(String.valueOf(recent.getJSONObject(1).getString("pillDelta")));
            }
            if (recent.length() > 2) {
                Date3.setText(String.valueOf(recent.getJSONObject(2).getString("time")));
                Event3.setText(String.valueOf(recent.getJSONObject(2).getString("pillDelta")));
            }
            if (recent.length() > 3) {
                Date4.setText(String.valueOf(recent.getJSONObject(3).getString("time")));
                Event4.setText(String.valueOf(recent.getJSONObject(3).getString("pillDelta")));
            }
            if (recent.length() > 4) {
                Date5.setText(String.valueOf(recent.getJSONObject(4).getString("time")));
                Event5.setText(String.valueOf(recent.getJSONObject(4).getString("pillDelta")));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return mView;
    }

    public void setArguments(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    JSONObject jsonObject;
}
