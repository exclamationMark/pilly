package co.pilly.pillyclient;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

public class StatusFragment extends Fragment{
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mView = inflater.inflate(R.layout.fragment_status, container, false);

        try {
            TextView Description = (TextView) mView.findViewById(R.id.description);
            Description.setText(jsonObject.getString("description"));

            TextView PillCount = (TextView) mView.findViewById(R.id.pillnr);
            PillCount.setText(String.valueOf(jsonObject.getInt("pillCount")));
            Typeface light = Typeface.createFromAsset(getActivity().getAssets(), "Roboto-Thin.ttf");
            PillCount.setTypeface(light);

            TextView NextPillTime = (TextView) mView.findViewById(R.id.nextpill);
            int next = jsonObject.getInt("nextPillTime");
            if(next < 30)
                NextPillTime.setText(String.format(getResources().getString(R.string.next_pill_min), next));
            else if (next < 90)
                NextPillTime.setText(getResources().getText(R.string.next_pill_hr));
            else {
                int hrs = (int)Math.round((float)next/60.0);
                NextPillTime.setText(String.format(getResources().getString(R.string.next_pill_hrs), hrs));
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

            Date1.setText(jsonObject.getJSONArray("recent").getJSONObject(0).getString("date"));
            Date2.setText(jsonObject.getJSONArray("recent").getJSONObject(1).getString("date"));
            Date3.setText(jsonObject.getJSONArray("recent").getJSONObject(2).getString("date"));
            Date4.setText(jsonObject.getJSONArray("recent").getJSONObject(3).getString("date"));
            Date5.setText(jsonObject.getJSONArray("recent").getJSONObject(4).getString("date"));
            Event1.setText(jsonObject.getJSONArray("recent").getJSONObject(0).getString("event"));
            Event2.setText(jsonObject.getJSONArray("recent").getJSONObject(1).getString("event"));
            Event3.setText(jsonObject.getJSONArray("recent").getJSONObject(2).getString("event"));
            Event4.setText(jsonObject.getJSONArray("recent").getJSONObject(3).getString("event"));
            Event5.setText(jsonObject.getJSONArray("recent").getJSONObject(4).getString("event"));
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

        return mView;
    }

    public void setArguments(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    JSONObject jsonObject;
}
