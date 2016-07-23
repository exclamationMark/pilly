package co.pilly.pillyclient;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

public class Schedule extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        myToolbar.setTitle("Schedule");
        setSupportActionBar(myToolbar);

        final ListView listView = (ListView) findViewById(R.id.schedule_list);
        PillAlert [] alerts = new PillAlert[2];
        alerts[0] = new PillAlert(8, 30, 1, new DAYS[] {DAYS.MON, DAYS.WED});
        alerts[1] = new PillAlert(21, 30, 2, new DAYS[] {DAYS.MON, DAYS.WED, DAYS.FRI, DAYS.SAT, DAYS.SUN});
        ScheduleAdapter scheduleAdapter = new ScheduleAdapter(this, R.layout.list_element, alerts);
        listView.setAdapter(scheduleAdapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                view.setSelected(true);
                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_schedule, menu);
        return true;
    }
}
