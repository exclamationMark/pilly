package co.pilly.pillyclient;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class NetErrorFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mView = inflater.inflate(R.layout.fragment_no_schedule, container, false);
        label = (TextView) mView.findViewById(R.id.no_schedule_label);
        label.setText(getResources().getString(R.string.net_error));
        return mView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        FragmentActivity activity = getActivity();
        if (activity instanceof Status)
            ( (Status)activity).setNetErrorLabel(label);
        else if (activity instanceof AllEvents)
            ( (AllEvents)activity).setNetErrorLabel(label);
    }

    TextView label;
}
