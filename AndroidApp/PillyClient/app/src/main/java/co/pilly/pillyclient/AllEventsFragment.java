package co.pilly.pillyclient;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class AllEventsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_schedule, container, false);
        listView = (ListView) mView.findViewById(R.id.schedule_list);
        listView.setDividerHeight(0);
        listView.setDivider(null);
        listView.setAdapter(eventListAdapter);
        return mView;
    }

    public void setArgs(EventListAdapter eventListAdapter) {
        this.eventListAdapter = eventListAdapter;
    }

    ListView listView;
    EventListAdapter eventListAdapter;
}
