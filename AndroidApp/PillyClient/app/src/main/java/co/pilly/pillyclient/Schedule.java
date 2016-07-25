package co.pilly.pillyclient;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AlertDialog;
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

import java.util.ArrayList;

public class Schedule extends AppCompatActivity implements ActionMode.Callback, AdapterView.OnItemLongClickListener {
    public static final String EXTRA_PILLALERT = "co.pilly.pillyclient.EXTRA_PILLALERT";
    public static final String EXTRA_ADD = "co.pilli.pilliclient.EXTRA_ADD";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        myToolbar.setTitle("Schedule");
        setSupportActionBar(myToolbar);


        PillAlert[] alerts = new PillAlert[2];
        alerts[0] = new PillAlert(8, 30, 1, new int[]{1, 3});
        alerts[1] = new PillAlert(21, 30, 2, new int[]{1, 3, 5, 6, 7});
        aList = new ArrayList<>(2);
        for (int i = 0; i < alerts.length; i++)
            aList.add(alerts[i]);

        listView = (ListView) findViewById(R.id.schedule_list);
        scheduleAdapter = new ScheduleAdapter(this, R.layout.list_element, aList, Typeface.createFromAsset(getAssets(), "Roboto-Thin.ttf"));
        listView.setAdapter(scheduleAdapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listView.setOnItemLongClickListener(this);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
        view.setSelected(true);
        listView.setItemChecked(i, true);
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
                startActivityForResult(intent, NewAlert.ACTION_ADD);
                if(mActionMode != null)
                    mActionMode.finish();
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
                PillAlert toEdit = (PillAlert) listView.getItemAtPosition(listView.getCheckedItemPosition());
                Intent intent = new Intent(this, NewAlert.class);
                intent.putExtra(EXTRA_ADD, false);
                intent.putExtra(EXTRA_PILLALERT, toEdit);
                startActivityForResult(intent, NewAlert.ACTION_EDIT);
                if(mActionMode != null)
                    mActionMode.finish();
                return true;
            case R.id.info:
                return true;
            case R.id.delete:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder
                        .setMessage(getResources().getString(R.string.alert_delete_confirm))
                        .setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                scheduleAdapter.remove((PillAlert) listView.getItemAtPosition(listView.getCheckedItemPosition()));
                            }
                        })
                        .setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        })
                        .show();
                if(mActionMode != null)
                    mActionMode.finish();
                return true;
            default:
                return false;
        }
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == NewAlert.RESULT_SAVE) {
            Toast.makeText(this, getResources().getString(R.string.alert_saved), Toast.LENGTH_SHORT).show();
            switch (requestCode) {
                case NewAlert.ACTION_EDIT:
                    aList.set(listView.getCheckedItemPosition(), (PillAlert) data.getParcelableExtra(EXTRA_PILLALERT));
                    break;
                case NewAlert.ACTION_ADD:
                    aList.add((PillAlert) data.getParcelableExtra(EXTRA_PILLALERT));
                    break;
            }
            scheduleAdapter.notifyDataSetChanged();
        }
    }

    ActionMode mActionMode;
    ListView listView;
    ScheduleAdapter scheduleAdapter;
    ArrayList<PillAlert> aList;
}
