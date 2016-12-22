package nl.drahmann.mega_sun_v01_controler;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class FragmentTab5  extends Fragment
{
    static final String TAG = "Frag5";

    public String ProviderName;
    public TextView heliorzonP;
    public TextView Long;
    public TextView Lat;
    public TextView Datum;
    public TextView Tijd;
    public TextView ZonLong;
    public TextView ZonLat;
    public CheckBox North;
    public CheckBox Zomer;
    public TextView ZonNorth;
    public TextView ZonZomer;
    public TextView ZonDatum;
    public TextView ZonTijd;
    public TextView Zone;
    public TextView ZonZone;
    public Spinner Machine;

    public ImageButton i_long;
    public ImageButton i_lat;
    public ImageButton i_tijd;
    public CheckBox halfrond;   //TODO nog afvangen
    public CheckBox zomertijd;  //TODO nog afvangen

    Calendar now = Calendar.getInstance();
    List<String> machinelist = new ArrayList<String>();

    String str;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Get the view from fragment_1
        View mijnView = inflater.inflate(R.layout.fragment_5, container, false);

        String myTag = getTag();
        ((MainActivity)getActivity()).setTabFragment5(myTag);

        LocationManager lm = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);
        getActivity().getApplicationContext().getSystemService(Context.LOCATION_SERVICE);

        heliorzonP = (TextView)mijnView.findViewById(R.id.textView5);
        ZonLong = (TextView)mijnView.findViewById(R.id.tvZonLong);
        ZonLat = (TextView)mijnView.findViewById(R.id.tvZonLat);
        Long = (TextView)mijnView.findViewById(R.id.tvGooLong);
        Lat = (TextView)mijnView.findViewById(R.id.tvGooLat);
        North = (CheckBox)mijnView.findViewById(R.id.cbNorth);
        North.setOnClickListener(onckBoxClickedNorth);
        ZonNorth = (TextView)mijnView.findViewById(R.id.tvZonNorth);
        Zomer = (CheckBox)mijnView.findViewById(R.id.cbZomertijd);
        Zomer.setOnClickListener(onckBoxClickedZomer);
        ZonZomer = (TextView)mijnView.findViewById(R.id.tvZonZomertijd);
        Zone = (TextView)mijnView.findViewById(R.id.tvGooZone);
        ZonZone = (TextView)mijnView.findViewById(R.id.tvZonZone);

        Datum = (TextView)mijnView.findViewById(R.id.tvGooDatum);
        Tijd = (TextView)mijnView.findViewById(R.id.tvGooTijd);
        ZonTijd = (TextView)mijnView.findViewById(R.id.tvZonTijd);
        ZonDatum = (TextView)mijnView.findViewById(R.id.tvZonDatum);

        i_long = (ImageButton)mijnView.findViewById(R.id.ImagebtnLong);
        i_long.setOnClickListener(onButtonClickedLong);
        i_lat = (ImageButton)mijnView.findViewById(R.id.ImagebtnLat);
        i_lat.setOnClickListener(onButtonClickedLat);
        i_tijd = (ImageButton)mijnView.findViewById(R.id.ImagebtnDateTime);
        i_tijd.setOnClickListener(onButtonClickedDateTime);



        Machine = (Spinner)mijnView.findViewById(R.id.spmachine);
        machinelist.add("0");

        Datum.setText(String.format("%02d:%02d:%02d", now.get(Calendar.DATE),
                now.get(Calendar.MONTH)+1, now.get(Calendar.YEAR)));
        Tijd.setText(String.format("%02d:%02d:%02d", now.get(Calendar.HOUR_OF_DAY),
                now.get(Calendar.MINUTE), now.get(Calendar.SECOND)));
        int i=now.get(Calendar.ZONE_OFFSET);    // tijdzone
        i = i/(60*60*1000); // maak er uren van
        Zone.setText(Integer.toString(i));
        int j = now.get(Calendar.DST_OFFSET);   // zomertijd= offset =1
        Log.d(TAG, " DST_OFFSET = " + j);
        j = j/(60*60);  // maak er uren van
        Log.d(TAG, " j = " + j);
        if(j != 0) Zomer.setChecked(true);
        else Zomer.setChecked(false);

        LocationProvider provider = lm.getProvider("gps");
        lm.requestLocationUpdates("gps", 60000, 1, locationListener);   //TODO oplossen LocationUpdates
        ProviderName = provider.getName();

        return mijnView;
    }



    OnClickListener onButtonClickedLong = new OnClickListener() {
        @Override
        public void onClick(View v) {
            // vervang de Zon Location door de Google Location
            // lees de waarde uit Long
            str = Long.getText().toString();
            Log.d(TAG, " Long in Tab5 = " + str);
            // TODO test op niet-numeriek anders gebeuren er ongelukken
            str = "02" + str + "#";
            ((MainActivity)getActivity()).sendMessage(str);	// verzend de waarde naar de arduino
        }
    };

    OnClickListener onButtonClickedLat = new OnClickListener() {
        @Override
        public void onClick(View v) {
            // vervang de Zon Location door de Google Location
            // lees de waarde uit Lat
            str = Lat.getText().toString();
            // TODO test op niet-numeriek anders gebeuren er ongelukken
            str = "03" + str+ "#";
            ((MainActivity)getActivity()).sendMessage(str);	// verzend de waarde naar de arduino
        }
    };

    OnClickListener onButtonClickedDateTime = new OnClickListener() {
        @Override
        public void onClick(View v) {
            now = Calendar.getInstance();
            // datum
            Datum.setText(String.format("%02d:%02d:%02d", now.get(Calendar.DATE),
                    now.get(Calendar.MONTH)+1, now.get(Calendar.YEAR)));
            str = Datum.getText().toString();
            // TODO test op nietnumeriek anders gebeuren er ongelukken
            str = "04" + str + "#";
            ((MainActivity)getActivity()).sendMessage(str);	// verzend de waarde naar de arduino
            // tijd
            Tijd.setText(String.format("%02d:%02d:%02d", now.get(Calendar.HOUR_OF_DAY),
                    now.get(Calendar.MINUTE), now.get(Calendar.SECOND)));
            str = Tijd.getText().toString();
            // TODO test op nietnumeriek anders gebeuren er ongelukken
            str = "05" + str + "#";
            ((MainActivity)getActivity()).sendMessage(str);	// verzendt de waarde naar de arduino
            // tijdzone
            str = Zone.getText().toString();
            str = "06" + str + "#";
            ((MainActivity)getActivity()).sendMessage(str);	// verzend de waarde naar de arduino
            // zomer of wintertijd
            if (Zomer.isChecked()) ((MainActivity)getActivity()).sendMessage("07Z#");	// verzend de waarde naar de arduino
            else  ((MainActivity)getActivity()).sendMessage("07W#");	// verzend de waarde naar de arduino
        }
    };

    OnClickListener onckBoxClickedNorth = new OnClickListener() {
        @Override
        public void onClick(View v) {
            //is chkIos checked?
            if (((CheckBox) v).isChecked()) {
                ((MainActivity)getActivity()).sendMessage("090#");	// verzend de waarde naar de arduino
            } else {
                ((MainActivity)getActivity()).sendMessage("091#");	// verzend de waarde naar de arduino
            }
        }
    };

    OnClickListener onckBoxClickedZomer = new OnClickListener() {
        @Override
        public void onClick(View v) {
            //is chkIos checked?
            if (((CheckBox) v).isChecked()) {
                ((MainActivity)getActivity()).sendMessage("07Z#");	// verzend de waarde naar de arduino
            } else {
                ((MainActivity)getActivity()).sendMessage("07W#");	// verzend de waarde naar de arduino
            }
        }
    };

    private final LocationListener locationListener = new LocationListener() {
        public void onLocationChanged(Location l) {
            // setContentView (R.layout.settings);

            double id = l.getLongitude() * 1000; // haal de longitude op
            id = Math.round(id); // trucje om af te ronden op 3 decimalen
            id = id / 1000;
            Long.setText(Double.toString(id)); // toon Longitude op scherm

            id = l.getLatitude() * 1000; // haal de Latitude op
            id = Math.round(id); // trucje om af te ronden op 3 decimalen
            id = id / 1000;
            Lat.setText(Double.toString(id)); // toon Latitude op scherm

        }

        @Override
        public void onProviderDisabled(String provider) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onProviderEnabled(String provider) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            // TODO Auto-generated method stub

        }
    };

    public void onResume()
    {
        super.onResume();
        Log.d(TAG, " onResume in FragmentTab5 is aangeroepen");
    }
}



