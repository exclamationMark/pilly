package co.pilly.pilly;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

public class Status extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Typeface light = Typeface.createFromAsset(this.getAssets(), "Roboto-Thin.ttf");
        ((TextView) findViewById(R.id.pillnr)).setTypeface(light);

        JessicaFetcher fetcher = new JessicaFetcher("123");
        Thread thread = new Thread(fetcher);
        thread.start();
        while (fetcher.Data == null) {} // User info stored in fetcher.Data (JSONObject)
        displayInfo(fetcher.Data);
    }

    void displayInfo(JSONObject info) {
        TextView description = (TextView) findViewById(R.id.description);
        TextView pillNr = (TextView) findViewById(R.id.pillnr);
        TextView nextPill = (TextView) findViewById(R.id.nextpill);
        TextView timestamp1 = (TextView) findViewById(R.id.timestamp1);
        TextView event1 = (TextView) findViewById(R.id.event1);
        TextView timestamp2 = (TextView) findViewById(R.id.timestamp2);
        TextView event2 = (TextView) findViewById(R.id.event2);
        TextView timestamp3 = (TextView) findViewById(R.id.timestamp3);
        TextView event3 = (TextView) findViewById(R.id.event3);
        TextView timestamp4 = (TextView) findViewById(R.id.timestamp4);
        TextView event4 = (TextView) findViewById(R.id.event4);
        TextView timestamp5 = (TextView) findViewById(R.id.timestamp5);
        TextView event5 = (TextView) findViewById(R.id.event5);

        try {
            description.setText(info.getString("description"));

            pillNr.setText(String.valueOf(info.getInt("pillCount")));

            int minutesLeft = info.getInt("nextPillTime");
            if(minutesLeft < 60)
                nextPill.setText(String.format(getString(R.string.next_pill_min), minutesLeft));
            else {
                int hoursLeft = minutesLeft/60;
                if (minutesLeft%60 > 30)
                    hoursLeft++;
                nextPill.setText(String.format(getString(R.string.next_pill_hrs), hoursLeft));
            }

            JSONArray array = info.getJSONArray("recent");
            timestamp1.setText(array.getJSONArray(0).getString(0));
            event1.setText(array.getJSONArray(0).getString(1));
            timestamp2.setText(array.getJSONArray(1).getString(0));
            event2.setText(array.getJSONArray(1).getString(1));
            timestamp3.setText(array.getJSONArray(2).getString(0));
            event3.setText(array.getJSONArray(2).getString(1));
            timestamp4.setText(array.getJSONArray(3).getString(0));
            event4.setText(array.getJSONArray(3).getString(1));
            timestamp5.setText(array.getJSONArray(4).getString(0));
            event5.setText(array.getJSONArray(4).getString(1));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
