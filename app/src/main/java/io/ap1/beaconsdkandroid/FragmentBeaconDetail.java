package io.ap1.beaconsdkandroid;

import android.app.DialogFragment;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by admin on 13/10/15.
 */
public class FragmentBeaconDetail extends Fragment{
    TextView tv_uuid;
    TextView tv_major;
    TextView tv_minor;
    TextView tv_url;

    EditText et_uuid;
    EditText et_major;
    EditText et_minor;
    EditText et_url;

    public FragmentBeaconDetail(){
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view =  inflater.inflate(R.layout.fragment_beacon_detail, container, false);

        tv_uuid = (TextView) view.findViewById(R.id.tv_beacon_uuid);
        tv_major = (TextView) view.findViewById(R.id.tv_beacon_major);
        tv_minor = (TextView) view.findViewById(R.id.tv_beacon_minor);
        tv_url = (TextView) view.findViewById(R.id.tv_beacon_url);

        et_uuid = (EditText) view.findViewById(R.id.et_beacon_uuid);
        et_major = (EditText) view.findViewById(R.id.et_beacon_major);
        et_minor = (EditText) view.findViewById(R.id.et_beacon_minor);
        et_url = (EditText) view.findViewById(R.id.et_beacon_url);

        Bundle thisBeaconData = getActivity().getIntent().getExtras();
        et_uuid.setText(thisBeaconData.getString("uuid"));
        et_major.setText(thisBeaconData.getString("major"));
        et_minor.setText(thisBeaconData.getString("minor"));
        et_url.setText(thisBeaconData.getString("url"));


        return view;
    }
}
