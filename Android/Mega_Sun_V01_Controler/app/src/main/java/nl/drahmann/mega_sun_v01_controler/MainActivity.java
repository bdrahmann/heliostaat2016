/*
    Standaard applicatie om een Android device via Bluetooth te verbinden met een Arduino.
    Gebasserd op BluetoothChatService voorbeeld van Android.
    Alle noodzakelijke classes en res files staan in een library.
    De afhandeling van het BT verkeer gebeurt in BluetoothChatFragment.
    Het aboutscherm wordt opgeroepen via een aboutlibrary.
    Er zijn drie tabs toegevoegd die met swipe bestuurd worden.

    Gemaakt door: BHJ Drahmann
    Datum: 31-1-2016
*/

/*
* Copyright 2013 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*     http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/


package nl.drahmann.mega_sun_v01_controler;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import nl.drahmann.about.About;
import nl.drahmann.mybtlibrary.chat.BluetoothChatService;
import nl.drahmann.mybtlibrary.chat.Constants;

/**
 * In deze class wordt de methode ProcesInput uitgevoerd.

 */
public class MainActivity extends FragmentActivity {

    public static final String TAG = "Main";

    // Intent request codes
    private static final int REQUEST_CONNECT_DEVICE_SECURE = 1;
    private static final int REQUEST_CONNECT_DEVICE_INSECURE = 2;
    private static final int REQUEST_ENABLE_BT = 3;

    /**
     * Name of the connected device
     */
    private String mConnectedDeviceName = null;

    /**
     * String buffer for outgoing messages
     */
    private StringBuffer mOutStringBuffer;

    /**
     * Local Bluetooth adapter
     */
    private BluetoothAdapter mBluetoothAdapter = null;

    /**
     * Member object for the chat services
     */
    private BluetoothChatService mChatService = null;

    String TabFragment1;
    String TabFragment2;
    String TabFragment3;
    String TabFragment4;
    String TabFragment5;


    public void setTabFragment1(String t) {TabFragment1 = t; }
    public String getTabFragment1() {return TabFragment1; }
    public void setTabFragment2(String t) {TabFragment2 = t; }
    public String getTabFragment2() {return TabFragment2; }
    public void setTabFragment3(String t) {TabFragment3 = t; }
    public String getTabFragment3() {return TabFragment3; }
    public void setTabFragment4(String t) {TabFragment4 = t; }
    public String getTabFragment4() {return TabFragment4; }
    public void setTabFragment5(String t) {TabFragment5 = t; }
    public String getTabFragment5() {return TabFragment5; }

    public boolean firstrun = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get local Bluetooth adapter
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        // viewpager om de fragments te besturen.
        // Locate the viewpager in activity_main.xml
        ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        // Set the ViewPagerAdapter into ViewPager
        viewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager()));
        viewPager.setOffscreenPageLimit(5);  // zo blijven er 5 fragments in het werkgeheugen

        // If the adapter is null, then Bluetooth is not supported
        if (mBluetoothAdapter == null) {
           Toast.makeText(this, "Bluetooth is not available", Toast.LENGTH_LONG).show();
            //activity.finish();
        }
    }

    public void ProcesInput(String data) { // verwerkt de input uit de Arduino
		 /*
		 * verwerkt de input uit de Arduino De ingelezen informatie bestaat uit
		 * een code van twee posities met daarna direct de informatie de codes:
		 *
		    * 1: Android send "OK" (meaning I am ready to receive data)
            * 2: Arduino send Longitude 02
            * 3: Arduino send Latitude 03
            * 4: Arduino send his date 04
            * 5 Arduino send his Time 05
            * 6 Arduino send his timezone 06
            * 7 Arduino send summer or wintertime 07
            * 8 Arduino send Heliostat or sunfolger 08
            * 9 Arduino send north/south 09
            * 10 Arduino send update interval (sec) 10
            * 11 Arduino send moveAwayFromLimitSwitch 11
            * 12 Arduino send Hour reset (hour) 12
            * 13 Arduino send Target Alt 13
            * 14 Arduino send Target Az 14
            * 15 Arduino send MachineTargetTabel in één keer 15
            * 16 Arduino send Manual on/off
            * 17 Arduino send Windprotection on/off
            * 18 Arduino send Sun's alt&az (alt en az gescheiden door '&'
            * 19 Arduino send Machine number
            * 20 Arduino send Machines alt&az


		 */

        // opzoeken van fragment1
        String TabOfFragment1 = (this).getTabFragment1();
        FragmentTab1 fragment1 = (FragmentTab1)this
                .getSupportFragmentManager()
                .findFragmentByTag(TabOfFragment1);
        // opzoeken van fragment2
        String TabOfFragment2 = (this).getTabFragment2();
        FragmentTab2 fragment2 = (FragmentTab2)this
                .getSupportFragmentManager()
                .findFragmentByTag(TabOfFragment2);
        // opzoeken van fragment3
        String TabOfFragment3 = (this).getTabFragment3();
        FragmentTab3 fragment3 = (FragmentTab3)this
                .getSupportFragmentManager()
                .findFragmentByTag(TabOfFragment3);
        // opzoeken van fragment4
        String TabOfFragment4 = (this).getTabFragment4();
        FragmentTab4 fragment4 = (FragmentTab4)this
                .getSupportFragmentManager()
                .findFragmentByTag(TabOfFragment4);
        // opzoeken van fragment5
        String TabOfFragment5 = (this).getTabFragment5();
        FragmentTab5 fragment5 = (FragmentTab5)this
                .getSupportFragmentManager()
                .findFragmentByTag(TabOfFragment5);



        if (firstrun){  // alleen de eerste keer. Vraag om Arduino instellingen
            sendMessage("01#"); // "OK"
            sendMessage("02#");
            sendMessage("03#");
            sendMessage("04#");
            sendMessage("05#");
            sendMessage("06#");
            sendMessage("07#");
            sendMessage("08#");
            sendMessage("09#");

            Log.d(TAG, " M188 firstrun = " + firstrun);
            firstrun = false;
        }

        // eerst het ingelezen record opdelen in code en informatie
        String s = "";  // de kode in string vorm
        int kode = 0;   // de kode numeriek
        if (data.length() < 2) {
            GeefFoutboodschap(data.substring(0)); // dit komt soms bij de start van een lopende Arduino
            Log.d(TAG, " M183 datastring is te kort = " + data);
            Log.d(TAG, " M184 data.length() = " + data.length());
            return;
        }

        try {
            s = data.substring(0, 2); // eerste 2 posities bevat de kode
        } catch (StringIndexOutOfBoundsException siobe) { // omzetten is niet gelukt
            Log.d(TAG, " M199 omzetten kodestring niet gelukt. data = " + data);
            return;
        }
        String info = data.substring(2); // de recordinformatie
        Log.d(TAG, " M203 info = " + info);
        try {
            kode = Integer.parseInt(s); // omzetten naar integer
        } catch (NumberFormatException ex) { // omzetten is niet gelukt
            GeefFoutboodschap(s);
        }

        switch (kode) {
            case 1:
                //connected = true;
                break;
            case 2:
                fragment5.ZonLong.setText(info);
                break;
            case 3:
                fragment5.ZonLat.setText(info);
                break;
            case 4:
                fragment1.Datum.setText(info);
                fragment5.ZonDatum.setText(info);
                break;
            case 5:
                fragment1.Tijd.setText(String.format(info, "%1$02d"));
                fragment5.ZonTijd.setText(info);
                break;
            case 6:
                fragment5.ZonZone.setText(info);
                break;
            case 7:     // Arduino send summer or wintertime
                if (info.equals("Z")) info = "Summer"; else info = "Winter";
                fragment5.ZonZomer.setText(info);
                break;
            case 8:     // Heliostat or Sun
                if (info.equals("H")){
                    fragment4.HelioSun.setText(R.string.heliostaat);
                    fragment5.heliorzonP.setText(R.string.heliostaat);
                } else {
                    fragment4.HelioSun.setText(R.string.zonnevolger);
                    fragment5.heliorzonP.setText(R.string.zonnevolger);
                }
                break;
            case 9:     // Arduino send north/south
                if (info.equals("0")) info = "North"; else info = "South";
                fragment5.ZonNorth.setText(info);
                break;
            case 10:
                break;
            case 11:
                break;
            case 12:
                break;
            case 13:
                break;
            case 14:
                break;
            case 15:
                break;
            case 16:        // Manual on/off
                if (info.equals("A")) fragment4.ManualOnOf.setText("Manual off"); else fragment4.ManualOnOf.setText("Manual On");
                break;
            case 17:        // windprotection on/off
                if (info.equals("O")) fragment4.WindProtection.setText("Windprotection off"); else fragment4.WindProtection.setText("Windprotection On");
                break;
            case 18:        // Arduino send Sun's alt&az (alt en az gescheiden door '&'
                fragment1.SunAlt.setText(info.substring(0,info.indexOf('&')));  // haal alt op tot '&
                fragment1.SunAz.setText(info.substring(info.indexOf('&')+1));     // haal az op
                break;
            case 19:        // Arduino send Machine Number
                fragment1.MachNum.setText(info);
                break;
            case 20:        // Arduino send Machines alt&az
                fragment1.MachAlt.setText(info.substring(0,info.indexOf('&')));  // haal alt op tot '&
                fragment1.MachAz.setText(info.substring(info.indexOf('&')+1));     // haal az op
                break;
            case 21:        // Arduino send Target alt&az
                fragment1.TargetAlt.setText(info.substring(0,info.indexOf('&')));  // haal alt op tot '&
                fragment1.TargetAz.setText(info.substring(info.indexOf('&')+1));     // haal az op
                break;


            // volgende case
            //
            default:
                // de rest is een onbekende kode
                GeefFoutboodschap(s);
        }

    }

    private void GeefFoutboodschap(String s) { // afvangen van verkeerde Arduino kode
        new AlertDialog.Builder(this)
                .setTitle("Onbekende kode")
                .setMessage(
                        "Arduino heeft een onbekende kode " + s
                                + " opgestuurd. Het programma kan wel doorgaan. ")
                .setPositiveButton(android.R.string.yes,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                // finish(); // of andere actie mogelijk
                            }
                        }).show();
    }

    /*----------------------------------------------------------------------------------------------------------
    hier  een copie van BluetoothChat
    This controls Bluetooth to communicate with other devices.
    */

    @Override
    public void onStart() {
        super.onStart();
        // If BT is not on, request that it be enabled.
        // setupChat() will then be called during onActivityResult
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
            // Otherwise, setup the chat session
        } else if (mChatService == null) {
            setupChat();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mChatService != null) {
            mChatService.stop();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        // Performing this check in onResume() covers the case in which BT was
        // not enabled during onStart(), so we were paused to enable it...
        // onResume() will be called when ACTION_REQUEST_ENABLE activity returns.
        if (mChatService != null) {
            // Only if the state is STATE_NONE, do we know that we haven't started already
            if (mChatService.getState() == BluetoothChatService.STATE_NONE) {
                // Start the Bluetooth chat services
                mChatService.start();
            }
        }
    }

    /**
     * Set up the UI and background operations for chat.
     */
    private void setupChat() {
        Log.d(TAG, "setupChat()");

        // Initialize the BluetoothChatService to perform bluetooth connections
        mChatService = new BluetoothChatService(this, mHandler);

        // Initialize the buffer for outgoing messages
        mOutStringBuffer = new StringBuffer("");
    }

    /**
     * Makes this device discoverable.
     */
    public void  ensureDiscoverable() {
        if (mBluetoothAdapter.getScanMode() !=
                BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
            Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
            startActivity(discoverableIntent);
        }
    }

    /**
     * Sends a message.
     *
     * @param message A string of text to send.
     */
    public void sendMessage(String message) {
        // Check that we're actually connected before trying anything
        // Log.d(TAG, "message ontvangen in sendMessage = " + message);
        if (mChatService.getState() != BluetoothChatService.STATE_CONNECTED) {
            Toast.makeText(this, R.string.not_connected, Toast.LENGTH_SHORT).show();
            return;
        }

        // Check that there's actually something to send
        if (message.length() > 0) {
            // Get the message bytes and tell the BluetoothChatService to write
            byte[] send = message.getBytes();
            mChatService.write(send);
            Log.d(TAG, "message send  = " + message);

            // Reset out string buffer to zero and clear the edit text field
            mOutStringBuffer.setLength(0);
        }
    }

    /**
     * Updates the status on the action bar.
     *
     * @param resId a string resource ID
     */
    private void setStatus(int resId) {
        final ActionBar actionBar = getActionBar();
        if (null == actionBar) {
            return;
        }
        actionBar.setSubtitle(resId);
    }

    /**
     * Updates the status on the action bar.
     *
     * @param subTitle status
     */
    private void setStatus(CharSequence subTitle) {
        final ActionBar actionBar = getActionBar();
        if (null == actionBar) {
            return;
        }
        actionBar.setSubtitle(subTitle);
    }

    /**
     * The Handler that gets information back from the BluetoothChatService
     */
    private final Handler mHandler = new Handler() {
        public String totaalMessage = "";
        @Override
        public void handleMessage(Message msg) {
            // FragmentActivity activity = getActivity();
            switch (msg.what) {
                case Constants.MESSAGE_STATE_CHANGE:
                    switch (msg.arg1) {
                        case BluetoothChatService.STATE_CONNECTED:
                            setStatus(getString(R.string.title_connected_to, mConnectedDeviceName));
                            break;
                        case BluetoothChatService.STATE_CONNECTING:
                            setStatus(R.string.title_connecting);
                            break;
                        case BluetoothChatService.STATE_LISTEN:
                        case BluetoothChatService.STATE_NONE:
                            setStatus(R.string.title_not_connected);
                            break;
                    }
                    break;
                case Constants.MESSAGE_WRITE:
                    byte[] writeBuf = (byte[]) msg.obj;
                    // construct a string from the buffer
                    String writeMessage = new String(writeBuf);
                    break;
                case Constants.MESSAGE_READ:
                    String readMessage = (String) msg.obj;
                    // Make string totaalMessage from the ReadMessage until delimiter "#"
                    // Log.d(TAG, "ReadMessage = " + readMessage);
                    totaalMessage = totaalMessage + readMessage;
                    if(totaalMessage.contains("#")) { // end of information from Arduino
                        int i = totaalMessage.indexOf("#");
                        String Result = totaalMessage.substring(0,i);
                        ProcesInput(Result);    // hier result doorgeven aan Main
                        // Log.d(TAG, "Result = " + Result);
                        totaalMessage = totaalMessage.substring(i+1);   // met de rest van de string beginnen
                    }
                    break;
                case Constants.MESSAGE_DEVICE_NAME:
                    // save the connected device's name
                    mConnectedDeviceName = msg.getData().getString(Constants.DEVICE_NAME);
                    Toast.makeText(getApplicationContext(), "Connected to " + mConnectedDeviceName, Toast.LENGTH_SHORT).show();
                    break;
                case Constants.MESSAGE_TOAST:
                    Toast.makeText(getApplicationContext(), msg.getData().getString(Constants.TOAST),
                                Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CONNECT_DEVICE_SECURE:
                // When DeviceListActivity returns with a device to connect
                if (resultCode == Activity.RESULT_OK) {
                    connectDevice(data, true);
                }
                break;
            case REQUEST_CONNECT_DEVICE_INSECURE:
                // When DeviceListActivity returns with a device to connect
                if (resultCode == Activity.RESULT_OK) {
                    connectDevice(data, false);
                }
                break;
            case REQUEST_ENABLE_BT:
                // When the request to enable Bluetooth returns
                if (resultCode == Activity.RESULT_OK) {
                    // Bluetooth is now enabled, so set up a chat session
                    setupChat();
                } else {
                    // User did not enable Bluetooth or an error occurred
                    Log.d(TAG, "BT not enabled");
                    Toast.makeText(this, R.string.bt_not_enabled_leaving,
                            Toast.LENGTH_SHORT).show();
                    finish();
                }
        }
    }

    /**
     * Establish connection with other device
     *
     * @param data   An {@link Intent} with {@link DeviceListActivity#EXTRA_DEVICE_ADDRESS} extra.
     * @param secure Socket Security type - Secure (true) , Insecure (false)
     */
    private void connectDevice(Intent data, boolean secure) {
        // Get the device MAC address
        String address = data.getExtras()
                .getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
        // Get the BluetoothDevice object
        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        // Attempt to connect to the device
        mChatService.connect(device, secure);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.secure_connect_scan: {
                // Launch the DeviceListActivity to see devices and do scan
                Intent serverIntent = new Intent(this, DeviceListActivity.class);
                startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE_SECURE);
                return true;
            }
            case R.id.insecure_connect_scan: {
                // Launch the DeviceListActivity to see devices and do scan
                Intent serverIntent = new Intent(this, DeviceListActivity.class);
                startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE_INSECURE);
                return true;
            }
            case R.id.discoverable: {
                // Ensure this device is discoverable by others
                ensureDiscoverable();
                return true;
            }

            /*
            case R.id.action_settings: // toon instelscherm
                Intent i = new Intent(this, PreferenceActivity.class);
                startActivity(i);
                return true;

             */

            case R.id.menu_info: {
                About.show((this), getString(R.string.about),
                        getString(R.string.close));
                return true;
            }


        }
        return false;
    }


}
