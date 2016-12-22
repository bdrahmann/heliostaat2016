package nl.drahmann.mega_sun_v01_controler;

// main screen

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class FragmentTab1  extends Fragment
{
    static final String TAG = "Frag1";
    public TextView Datum;
    public TextView Tijd;
    public TextView SunAlt;
    public TextView SunAz;
    public TextView MachNum;
    public TextView MachAlt;
    public TextView MachAz;
    public TextView TargetAlt;
    public TextView TargetAz;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Get the view from fragment_4
        View myView = inflater.inflate(R.layout.fragment_1, container, false);

        String myTag = getTag();
        ((MainActivity)getActivity()).setTabFragment1(myTag);

        Datum = (TextView)myView.findViewById(R.id.tvDatum);
        Tijd = (TextView)myView.findViewById(R.id.tvTijd);
        SunAlt = (TextView)myView.findViewById(R.id.tvZonalt);
        SunAz = (TextView)myView.findViewById(R.id.tvZonaz);
        MachNum = (TextView)myView.findViewById(R.id.tvMachNum);
        MachAlt = (TextView)myView.findViewById(R.id.tvMachalt);
        MachAz = (TextView)myView.findViewById(R.id.tvMachaz);
        TargetAlt = (TextView)myView.findViewById(R.id.tvTargetalt);
        TargetAz = (TextView)myView.findViewById(R.id.tvTargetaz);

        return myView;
    }





    public void onResume()
    {
        super.onResume();
        Log.d(TAG, " onResume in FragmentTab1 is aangeroepen");
    }
}



