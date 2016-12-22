package nl.drahmann.mega_sun_v01_controler;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class FragmentTab4 extends Fragment {
    static final String TAG = "Frag4";
    public TextView HelioSun;
    public TextView ManualOnOf;
    public TextView WindProtection;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Get the view from fragmenttab2.xml
        View view = inflater.inflate(R.layout.fragment_4, container, false);
        String myTag = getTag();
        ((MainActivity)getActivity()).setTabFragment4(myTag);

        HelioSun = (TextView)view.findViewById(R.id.tvHelioOrSun);
        ManualOnOf = (TextView)view.findViewById(R.id.tvManualOnOf);
        WindProtection = (TextView)view.findViewById(R.id.tvWindProtection);




        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, " onResume in fragmenttab4 is aangeroepen");
    }
}
