package co.pilly.pilly;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

public class Status extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String username = "IDT16";
        int pillNumber = 12;
        int hoursLeft = 2;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Typeface light = Typeface.createFromAsset(this.getAssets(), "Roboto-Thin.ttf");
        Intent intent = getIntent();
        TextView topLabel = (TextView) findViewById(R.id.topLabel);
        TextView pillnr = (TextView) findViewById(R.id.pillnr);
        TextView nextPill = (TextView) findViewById(R.id.nextpill);

        topLabel.setText(username + getString(R.string.s_medications));
        pillnr.setText(String.valueOf(pillNumber));
        pillnr.setTypeface(light);
        nextPill.setText(getString(R.string.next_pill_in) + " " + hoursLeft + " " + getString(R.string.hours));
    }
}
