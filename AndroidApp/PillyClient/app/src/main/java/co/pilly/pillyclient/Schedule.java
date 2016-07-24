package co.pilly.pillyclient;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

public class Schedule extends AppCompatActivity implements ActionMode.Callback, AdapterView.OnItemLongClickListener {
    public static final String EXTRA_HOUROFDAY = "co.pilly.pillyclient.EXTRA_HOUROFDAY";
    public static final String EXTRA_MINUTES = "co.pilly.pillyclient.EXTRA_MINUTES";
    public static final String EXTRA_DAYS = "co.pilly.pillyclient.EXTRA_DAYS";
    public static final String EXTRA_ADD = "co.pilli.pilliclient.EXTRA_ADD";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        myToolbar.setTitle("Schedule");
        setSupportActionBar(myToolbar);

        ListView listView = (ListView) findViewById(R.id.schedule_list);
        PillAlert [] alerts = new PillAlert[2];
        alerts[0] = new PillAlert(8, 30, 1, new DAYS[] {DAYS.MON, DAYS.WED});
        alerts[1] = new PillAlert(21, 30, 2, new DAYS[] {DAYS.MON, DAYS.WED, DAYS.FRI, DAYS.SAT, DAYS.SUN});
        ScheduleAdapter scheduleAdapter = new ScheduleAdapter(this, R.layout.list_element, alerts);
        listView.setAdapter(scheduleAdapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listView.setOnItemLongClickListener(this);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
        view.setSelected(true);
        // TODO: Fix the 'unable to stay highlighted' issue
        if (mActionMode != null)
            return false;
        mActionMode = startSupportActionMode(this);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_schedule, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add:
                Intent intent = new Intent(this, NewAlert.class);
                intent.putExtra(EXTRA_ADD, true);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_schedule, menu);
        return true;
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {
        mActionMode = null;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit:
                Intent intent = new Intent(this, NewAlert.class);
                intent.putExtra(EXTRA_ADD, false);
                startActivity(intent);
                return true;
            case R.id.info:
                return true;
            case R.id.delete:
                return true;
            default:
                return false;
        }
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return false;
    }

    ActionMode mActionMode;
}
