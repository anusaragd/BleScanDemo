package com.minewbeacon.blescan.demo;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.minew.beacon.BeaconValueIndex;
import com.minew.beacon.BluetoothState;
import com.minew.beacon.MinewBeacon;
import com.minew.beacon.MinewBeaconManager;
import com.minew.beacon.MinewBeaconManagerListener;
import com.yuliwuli.blescan.demo.R;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private MinewBeaconManager mMinewBeaconManager;
    private RecyclerView mRecycle;
    private BeaconListAdapter mAdapter;
//    private BeaconListAdapter mMinewBeacon;
    private static final int REQUEST_ENABLE_BT = 2;
    private boolean isScanning;

    UserRssi comp = new UserRssi();
    private TextView mStart_scan;
    private TextView mreport;
    private TextView mrequest;
    private TextView mnoti;
    private boolean mIsRefreshing;
    private int state;

    private int CountForSend=0;
    private int count = 0 ;
    private int num = 0 ;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        initView();
        initManager();
        checkBluetooth();
        initListener();



    }

    public void ShowNotification(String Title, String Body, String userName){
        String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
        Notification.Builder notification =
                new Notification.Builder(this) // this is context
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(Title + "  " + Body)
                        .setContentText(userName + "    " + currentDateTimeString);

        Intent resultIntent = new Intent(this, MainActivity.class);
        resultIntent.setAction(Intent.ACTION_MAIN);
        resultIntent.addCategory(Intent.CATEGORY_LAUNCHER);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                resultIntent, 0);

        notification.setContentIntent(pendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotificationManager.notify(1, notification.build());
    }


    public void  CallWebservice(String mac_addr, String requester , String major , String minor , int cnt) {

        String strResponse="";

        String URL =  "http://" + "203.151.213.80/ibecon/WebService1.asmx";
//        String URL =  "http://" + "10.0.0.166:8080/ibecon/WebService1.asmx";
        String NAMESPACE = "http://tempuri.org/";
        String METHOD_NAME = "ibecon_status";
        String SOAP_ACTION = "http://tempuri.org/ibecon_status/";
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

        /**** with parameter *****/
        PropertyInfo pi;

        pi=new PropertyInfo();
        pi.setName("serial");
        pi.setValue(mac_addr);
        pi.setType(String.class);
        request.addProperty(pi);

        pi=new PropertyInfo();
        pi.setName("name");
        pi.setValue(requester);
        pi.setType(String.class);
        request.addProperty(pi);

        pi=new PropertyInfo();
        pi.setName("major");
        pi.setValue(major);
        pi.setType(String.class);
        request.addProperty(pi);

        pi=new PropertyInfo();
        pi.setName("minor");
        pi.setValue(minor);
        pi.setType(String.class);
        request.addProperty(pi);

        pi=new PropertyInfo();
        pi.setName("level");
        pi.setValue(cnt);
        pi.setType(Integer.class);
        request.addProperty(pi);



        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
        androidHttpTransport.debug = true;
        try
        {
            androidHttpTransport.call(SOAP_ACTION, envelope);
            SoapObject response;
            response= (SoapObject) envelope.bodyIn;
            strResponse = response.getProperty(0).toString();
        }
        catch (Exception e)
        {
            //e.printStackTrace();
            strResponse = e.toString();
            Toast.makeText(this, strResponse, Toast.LENGTH_SHORT).show();
        }
    }



    /**
     * check Bluetooth state
     */
    private void checkBluetooth() {
        BluetoothState bluetoothState = mMinewBeaconManager.checkBluetoothState();
        switch (bluetoothState) {
            case BluetoothStateNotSupported:
                Toast.makeText(this, "Not Support BLE", Toast.LENGTH_SHORT).show();
                finish();
                break;
            case BluetoothStatePowerOff:
                showBLEDialog();
                break;
            case BluetoothStatePowerOn:
                break;
        }
    }


    private void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mStart_scan = (TextView) findViewById(R.id.start_scan);
//        mreport = (TextView) findViewById(R.id.report);
        mrequest = (TextView) findViewById(R.id.request);
        mnoti = (TextView)findViewById(R.id.noti);

        mRecycle = (RecyclerView) findViewById(R.id.recyeler);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecycle.setLayoutManager(layoutManager);
        mAdapter = new BeaconListAdapter();
        mRecycle.setAdapter(mAdapter);
        mRecycle.addItemDecoration(new RecycleViewDivider(this, LinearLayoutManager
                .HORIZONTAL));
    }

    private void initManager() {
        mMinewBeaconManager = MinewBeaconManager.getInstance(this);
    }


    private void initListener() {

        mnoti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //++  Minimize App
                String userName = namestaff.name.toString();
                if(userName != null && !userName.isEmpty()){
//                    Intent startMain = new Intent(MainActivity.this,MainActivity.class);
                    Intent startMain = new Intent(Intent.ACTION_MAIN);
                    startMain.addCategory(Intent.CATEGORY_HOME);
                    startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(startMain);
                }else{
                    Toast.makeText(MainActivity.this,"กรุณาป้อนชื่อ!!!",Toast.LENGTH_LONG).show();
                }
                //--
            }
        });

//        mreport.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(MainActivity.this,ReportActivity.class);
//                startActivity(intent);
//                Toast.makeText(getApplicationContext(), namestaff.name , Toast.LENGTH_SHORT).show();
//            }
//        });

        mrequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,RequestActivity.class);
                startActivity(intent);
                Toast.makeText(getApplicationContext(), namestaff.name , Toast.LENGTH_SHORT).show();
            }
        });

        mStart_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mMinewBeaconManager != null) {
                    BluetoothState bluetoothState = mMinewBeaconManager.checkBluetoothState();
                    switch (bluetoothState) {
                        case BluetoothStateNotSupported:
                            Toast.makeText(MainActivity.this, "Not Support BLE", Toast.LENGTH_SHORT).show();
                            finish();
                            break;
                        case BluetoothStatePowerOff:
                            showBLEDialog();
                            return;
                        case BluetoothStatePowerOn:
                            break;
                    }
                }
                if (isScanning) {
                    isScanning = false;
                    mStart_scan.setText("Start");
                    if (mMinewBeaconManager != null) {
                        mMinewBeaconManager.stopScan();
                    }
                } else {
                    isScanning = true;
                    mStart_scan.setText("Stop");
                    try {
                        mMinewBeaconManager.startScan();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        mRecycle.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                state = newState;
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });

        mMinewBeaconManager.setDeviceManagerDelegateListener(new MinewBeaconManagerListener() {
            /**
             *   if the manager find some new beacon, it will call back this method.
             *
             *  @param minewBeacons  new beacons the manager scanned
             */
            @Override
            public void onAppearBeacons(List<MinewBeacon> minewBeacons) {

            }

            /**
             *  if a beacon didn't update data in 10 seconds, we think this beacon is out of rang, the manager will call back this method.
             *
             *  @param minewBeacons beacons out of range
             */
            @Override
            public void onDisappearBeacons(List<MinewBeacon> minewBeacons) {
                /*for (MinewBeacon minewBeacon : minewBeacons) {
                    String deviceName = minewBeacon.getBeaconValue(BeaconValueIndex.MinewBeaconValueIndex_Name).getStringValue();
                    Toast.makeText(getApplicationContext(), deviceName + "  out range", Toast.LENGTH_SHORT).show();
                }*/
            }

            /**
             *  the manager calls back this method every 1 seconds, you can get all scanned beacons.
             *
             *  @param minewBeacons all scanned beacons
             */
            @Override
            public void onRangeBeacons(final List<MinewBeacon> minewBeacons) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Collections.sort(minewBeacons, comp);
                        Log.e("tag", state + "");
                        if (state == 1 || state == 2) {
                        } else {
                            mAdapter.setItems(minewBeacons);



                            //+++
                            CountForSend += 1;
                            if (CountForSend == 20) {

                                int cnt = 1;
                                int beaconFound=0;
                                String major = "";
                                String minor = "";
                                int rssi=-1;
                                String strNearBeacon = "";
                                String userName = "";
                                userName = namestaff.name.toString();
                                if(userName != null && !userName.isEmpty()) {/**/}
                                else{ userName = "unknown"; }

                                try {

                                    beaconFound = minewBeacons.size();  // amount of beacon was found
                                    Toast.makeText(MainActivity.this, String.valueOf(beaconFound),Toast.LENGTH_LONG).show();
                                    if(beaconFound > 0)
                                    {
                                        for (int l = 0; l < beaconFound; l++) {
                                            major = minewBeacons.get(l).getBeaconValue(BeaconValueIndex.MinewBeaconValueIndex_Major).getStringValue();
                                            if (major.equals("1")) {  //
                                                if (cnt <= 3) { // ** send top 3

                                                    minor = minewBeacons.get(l).getBeaconValue(BeaconValueIndex.MinewBeaconValueIndex_Minor).getStringValue();
                                                    strNearBeacon = minewBeacons.get(l).getBeaconValue(BeaconValueIndex.MinewBeaconValueIndex_MAC).getStringValue();
                                                    rssi = minewBeacons.get(l).getBeaconValue(BeaconValueIndex.MinewBeaconValueIndex_RSSI).getIntValue();



                                                    //++
                                                    ShowNotification("Beacon",strNearBeacon ,userName);
                                                    CallWebservice(strNearBeacon, userName ,major ,minor, cnt);
                                                    //--
                                                    cnt = cnt + 1;
                                                }
                                            }
                                        }

                                    }

                                    if(beaconFound == 0)
                                    {
                                        ShowNotification("Beacon","" ,userName);
                                        CallWebservice("", userName ,"" ,"", 0);
                                    }

                                }
                                catch(Exception ex)
                                {
                                    //strNearBeacon = "";
                                    //ShowNotification("Beacon",strNearBeacon ,userName);
                                    //CallWebservice(strNearBeacon, userName);
                                }

                                CountForSend = 0;

                                //ShowNotification("Beacon",strNearBeacon ,userName);
                                //CallWebservice(strNearBeacon, userName);
                            }
                            //---
                        }

                    }
                });
            }

            /**
             *  the manager calls back this method when BluetoothStateChanged.
             *
             *  @param state BluetoothState
             */
            @Override
            public void onUpdateState(BluetoothState state) {
                switch (state) {
                    case BluetoothStatePowerOn:
                        Toast.makeText(getApplicationContext(), "BluetoothStatePowerOn", Toast.LENGTH_SHORT).show();
                        break;
                    case BluetoothStatePowerOff:
                        Toast.makeText(getApplicationContext(), "BluetoothStatePowerOff", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //stop scan
        if (isScanning) {
            mMinewBeaconManager.stopScan();
        }
    }

    private void showBLEDialog() {
        Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_ENABLE_BT:
                break;
        }
    }
}
