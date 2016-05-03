package co.pilly.pilly;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

public class Status extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Typeface light = Typeface.createFromAsset(this.getAssets(), "Roboto-Thin.ttf");

        JessicaFetcher fetcher = new JessicaFetcher("123");
        Thread thread = new Thread(fetcher);
        thread.start();
        while (fetcher.Data == null) {}

        TextView topLabel = (TextView) findViewById(R.id.topLabel);
        TextView pillnr = (TextView) findViewById(R.id.pillnr);
        TextView nextPill = (TextView) findViewById(R.id.nextpill);
        pillnr.setTypeface(light);

        try {
            topLabel.setText(fetcher.Data.getString("description"));
            pillnr.setText(String.valueOf(fetcher.Data.getInt("pillCount")));
            int minutesLeft = fetcher.Data.getInt("nextPillTime");

            if(minutesLeft < 60)
                nextPill.setText(String.format(getString(R.string.next_pill_min), minutesLeft));
            else {
                int hoursLeft = minutesLeft/60;
                if (minutesLeft%60 > 30)
                    hoursLeft++;
                nextPill.setText(String.format(getString(R.string.next_pill_hrs), hoursLeft));
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
