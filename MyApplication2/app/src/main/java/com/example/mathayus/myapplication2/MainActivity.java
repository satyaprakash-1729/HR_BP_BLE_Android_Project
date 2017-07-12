package com.example.mathayus.myapplication2;

import android.Manifest;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.ActionMenuItemView;
import android.util.Log;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.PointsGraphSeries;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import java.util.UUID;

public class MainActivity extends AppCompatActivity /*implements BluetoothAdapter.LeScanCallback*/ {
    private static final String TAG = "BluetoothGattActivity";
    public ArrayList<Double> signalData;
    public ArrayList<Double> signalData2;
    public ArrayList<Double> times;
    public double t11,t12,t13,t14,t15,t16,t17,t18,t19,t110;
    public double t21,t22,t23,t24,t25,t26,t27,t28,t29,t210;
    public boolean paused = false;
    private final Handler mHandler2 = new Handler();
    private Runnable mTimer1;
    private PointsGraphSeries<DataPoint> mSeries1;
    private PointsGraphSeries<DataPoint> mSeries2;
    public long startTime = 0;
    /* Data Service */
    private static final UUID DATA_SERVICE = UUID.fromString("0000FFF0-0000-1000-8000-00805F9B34FB");
    private static final UUID SIGNAL_DATA_CHAR = UUID.fromString("0000FFF4-0000-1000-8000-00805F9B34FB");
    //private static final UUID SIGNAL_DATA_CHAR2 = UUID.fromString("0000FFF6-0000-1000-8000-00805F9B34FB");
    private static final UUID SIGNAL_CONFIG_CHAR = UUID.fromString("00002902-0000-1000-8000-00805F9B34FB");
    /* Client Configuration Descriptor */
    private static final UUID CONFIG_DESCRIPTOR = UUID.fromString("00002901-0000-1000-8000-00805F9B34FB");

    private BluetoothAdapter mBluetoothAdapter;
    private SparseArray<BluetoothDevice> mDevices;
    private boolean isBP;
    private boolean isHR;
    private BluetoothGatt mConnectedGatt;
    private PointsGraphSeries<DataPoint> mSeries3;
    private GraphView graph1;
    private GraphView graph2, graph3;

    private TextView mHeartRate;
    private TextView mSBP;
    private TextView mDBP;
    private TextView mFrequency;
    private long currentTime;
    private ProgressDialog mProgress;
    private ProgressBar progressBar;
    private int count1 = 0;
    private boolean flagTheStart = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestPermission();
        isBP = false;
        isHR = true;
        t11 = t12 = t13 = t14= t15=t16=t17=t18=t19=t110 =0;
        t21 = t22 = t23 = t24= t25=t26=t27=t28=t29=t210 =0;
        graph1 = (GraphView) findViewById(R.id.graph1);
        graph2 = (GraphView) findViewById(R.id.graph2);
        graph3 = (GraphView) findViewById(R.id.graph3);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setVisibility(View.VISIBLE);
        progressBar = (ProgressBar) findViewById(R.id.progress_spinner);
        progressBar.setVisibility(View.INVISIBLE);
        mHeartRate = (TextView) findViewById(R.id.hr);
        mSBP = (TextView) findViewById(R.id.sbp);
        mDBP = (TextView) findViewById(R.id.dbp);
        mFrequency = (TextView) findViewById(R.id.frequency);
        BluetoothManager manager = (BluetoothManager) getSystemService(BLUETOOTH_SERVICE);
        mBluetoothAdapter = manager.getAdapter();
        mDevices = new SparseArray<>();
        mProgress = new ProgressDialog(this);
        mProgress.setIndeterminate(true);
        mProgress.setCancelable(false);
        signalData = new ArrayList<>();
        times = new ArrayList<>();
        signalData2 = new ArrayList<>();
        mSeries1 = new PointsGraphSeries<>(new DataPoint[]{new DataPoint(0,0)});
        mSeries1.setSize(2);
        mSeries1.setTitle("1st Data");
        mSeries1.setColor(Color.BLUE);
        graph1.addSeries(mSeries1);
        graph1.getLegendRenderer().setVisible(true);
        graph1.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);
        graph1.getViewport().setXAxisBoundsManual(true);
        graph1.getViewport().setMinX(0);
        graph1.getViewport().setMaxX(7);
        mSeries2 = new PointsGraphSeries<>(new DataPoint[]{new DataPoint(0,0)});
        mSeries2.setSize(3);
        mSeries2.setTitle("2nd Data");
        mSeries2.setColor(Color.GREEN);
        mSeries3 = new PointsGraphSeries<>(new DataPoint[]{new DataPoint(0,0)});
        mSeries3.setSize(5);
        mSeries3.setTitle("R Peaks");
        mSeries3.setColor(Color.RED);
        graph1.addSeries(mSeries3);
        graph1.getViewport().setScalable(true);
        graph1.getViewport().setScalableY(true);
    }

    public void requestPermission(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_DENIED)
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 0);
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED)
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH) == PackageManager.PERMISSION_DENIED)
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH}, 0);
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_ADMIN) == PackageManager.PERMISSION_DENIED)
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH_ADMIN}, 0);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
            //Bluetooth is disabled
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivity(enableBtIntent);
            finish();
            return;
        }
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, "No LE Support.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        if(paused){
            paused = false;
            beginDataStreaming();
        }
        mTimer1 = new Runnable() {
            @Override
            public void run() {
                try {
                    mSeries1.resetData(generateData1());
                    mSeries2.resetData(generateData2());
                    graph1.getViewport().setXAxisBoundsManual(true);
                    graph1.getViewport().setMinX((currentTime-6000)/1000f);
                    graph1.getViewport().setMaxX((currentTime+1000)/1000f);
                    graph2.getViewport().setXAxisBoundsManual(true);
                    graph2.getViewport().setMinX((currentTime-6000)/1000f);
                    graph2.getViewport().setMaxX((currentTime+1000)/1000f);
                    graph3.getViewport().setXAxisBoundsManual(true);
                    graph3.getViewport().setMinX((currentTime-6000)/1000f);
                    graph3.getViewport().setMaxX((currentTime+1000)/1000f);
                    if(isHR) {
                        graph1.getViewport().setYAxisBoundsManual(true);
                        graph1.getViewport().setMaxY(5000);
                        graph1.getViewport().setMinY(0);
                    }else{
                        graph1.getViewport().setYAxisBoundsManual(true);
                        graph1.getViewport().setMaxY(2000);
                        graph1.getViewport().setMinY(0);
                    }
                    if(signalData.size()>100 && signalData2.size() > 100) {
                        if(isHR){
                            DataPoint[] dataPoints = generateData3();
                            mSeries3.resetData(dataPoints);
                        }else{
                            mHeartRate.setText(" - ");
                        }
                        if(isBP){
                            Double[] xx = signalData.toArray(new Double[signalData.size()]);
                            double[] data = new double[signalData.size()];
                            for (int i = 0; i < signalData.size(); i++) {
                                data[i] = xx[i];
                            }
                            ArrayList<Double> BP = extractFeatureVectors(data);
                            double DBP = BP.get(0);
                            double SBP = BP.get(1);
                            if (!Double.isInfinite(DBP) && !Double.isNaN(DBP))
                                mDBP.setText(String.format(Locale.US, "%.1f mm", DBP));
                            if (!Double.isInfinite(SBP) && !Double.isNaN(SBP))
                                mSBP.setText(String.format(Locale.US, "%.1f mm", SBP));
                        }else{
                            mDBP.setText(" - ");
                            mSBP.setText(" - ");
                        }
                    }
                }catch (NullPointerException e){
                    Log.d(TAG, "Happens!");
                }
                mHandler2.postDelayed(this, 80);
            }
        };
        mHandler2.postDelayed(mTimer1, 300);
        clearDisplayValues();
    }

    @Override
    protected void onPause() {
        super.onPause();
        paused = true;
        mProgress.dismiss();
        mHandler.removeCallbacks(mStopRunnable);
        mHandler.removeCallbacks(mStartRunnable);
        mHandler2.removeCallbacks(mTimer1);
    }

    @Override
    protected void onStop() {
        super.onStop();
        //Disconnect from any active tag connection
        if (mConnectedGatt != null) {
            mConnectedGatt.disconnect();
            mConnectedGatt = null;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Add the "scan" option to the menu
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public void beginDataStreaming(){
        for(int i=0; i<mDevices.size(); i++) {
            BluetoothDevice device = mDevices.valueAt(i);
            if(device==null){
                continue;
            }
            if(device.getName()==null){
                continue;
            }
            if (device.getName().equals("SimpleBLEPeripheral")) {
                Log.i(TAG, "Connecting to " + device.getName());
                mConnectedGatt = device.connectGatt(this, false, mGattCallback);
                //Display progress UI
                mHandler.sendMessage(Message.obtain(null, MSG_PROGRESS, "Connecting to " + device.getName() + "..."));
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(this, HelpActivity.class);
        switch (item.getItemId()) {
            case R.id.action_scan:
                mDevices.clear();
                startScan();
                return true;
            case R.id.action_start:
                beginDataStreaming();
                return true;
            case R.id.action_humid:
                isBP = false;
                isHR = true;
                graph1.setVisibility(View.VISIBLE);
                graph2.setVisibility(View.INVISIBLE);
                graph3.setVisibility(View.INVISIBLE);
                graph1.removeAllSeries();
                graph1.addSeries(mSeries1);
                graph1.addSeries(mSeries3);
                return true;
            case R.id.action_temp:
                isBP = true;
                isHR = false;
                graph1.setVisibility(View.VISIBLE);
                graph2.setVisibility(View.INVISIBLE);
                graph3.setVisibility(View.INVISIBLE);
                graph1.removeAllSeries();
                graph1.addSeries(mSeries2);
                return true;
            case R.id.action_both:
                graph1.setVisibility(View.INVISIBLE);
                graph2.setVisibility(View.VISIBLE);
                graph3.setVisibility(View.VISIBLE);
                graph2.removeAllSeries();
                graph3.removeAllSeries();
                graph2.addSeries(mSeries1);
                graph3.addSeries(mSeries2);
                isBP = true;
                isHR = true;
                graph2.addSeries(mSeries3);
                return true;
            case R.id.help:
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void clearDisplayValues() {
        mHeartRate.setText("");
        mSBP.setText("");
        mDBP.setText("");
    }


    private Runnable mStopRunnable = new Runnable() {
        @Override
        public void run() {
            stopScan();
        }
    };
    private Runnable mStartRunnable = new Runnable() {
        @Override
        public void run() {
            startScan();
        }
    };


    private void startScan() {
        BluetoothLeScanner scanner = mBluetoothAdapter.getBluetoothLeScanner();
        scanner.startScan(new ScanCallback() {
            @Override
            public void onScanResult(int callbackType, ScanResult result) {
                BluetoothDevice device = result.getDevice();
                super.onScanResult(callbackType,result);
                mDevices.put(device.hashCode(), device);
                if(device.getName()!=null) {
                    if (device.getName().equals("SimpleBLEPeripheral")) {
                        ActionMenuItemView mi = (ActionMenuItemView) findViewById(R.id.action_scan);
                        mi.setTitle("Found");
                        mi.setEnabled(false);
                    }
                }
            }
        });
        progressBar.setVisibility(View.VISIBLE);
        mHandler.postDelayed(mStopRunnable, 3000);
    }

    private void stopScan() {
        progressBar.setVisibility(View.INVISIBLE);
    }

    private BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {

        /* State Machine Tracking */
        private int mState = 0;
        private void reset() { mState = 0; }
        private void advance() { mState++; }

        private void startReading(BluetoothGatt gatt){
            flagTheStart = true;
            BluetoothGattCharacteristic characteristic1 = gatt.getService(DATA_SERVICE)
                    .getCharacteristic(SIGNAL_DATA_CHAR);
            try {
                gatt.setCharacteristicNotification(characteristic1, true);
                BluetoothGattDescriptor desc = characteristic1.getDescriptor(UUID.fromString("00002902-0000-1000-8000-00805f9b34fb"));
                if(desc!=null) {
                    desc.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                    gatt.writeDescriptor(desc);
                }
            }catch (NullPointerException e){
                e.printStackTrace();
            }
        }

        private void enableNextSensor(BluetoothGatt gatt) {
            final BluetoothGattCharacteristic notifyCharacteristic = new BluetoothGattCharacteristic(
                    SIGNAL_CONFIG_CHAR,
                    BluetoothGattCharacteristic.PROPERTY_NOTIFY,
                    BluetoothGattCharacteristic.PERMISSION_READ
            );
            switch (mState) {
                case 0:
                    Log.d(TAG, "Enabling signal");
                    notifyCharacteristic.setValue(new byte[] {0x01});
                    gatt.getService(DATA_SERVICE).addCharacteristic(notifyCharacteristic);
                    startReading(gatt);
                    break;
                default:
                    mHandler.sendEmptyMessage(MSG_DISMISS);
                    Log.i(TAG, "All Sensors Enabled");
            }
        }

        /*
         * Read the data characteristic's value for each sensor explicitly
         */
        private void readNextSensor(BluetoothGatt gatt) {
            BluetoothGattCharacteristic characteristic;
            switch (mState) {
                case 0:
                    Log.d(TAG, "Reading signal");
                    characteristic = gatt.getService(DATA_SERVICE)
                            .getCharacteristic(SIGNAL_DATA_CHAR);
                    break;
                default:
                    mHandler.sendEmptyMessage(MSG_DISMISS);
                    Log.i(TAG, "All Sensors Enabled");
                    return;
            }
            if(characteristic!=null)
                gatt.readCharacteristic(characteristic);
            else
                mHandler.sendEmptyMessage(MSG_DISMISS);
        }
        /*
         * Enable notification of changes on the data characteristic for each sensor
         * by writing the ENABLE_NOTIFICATION_VALUE flag to that characteristic's
         * configuration descriptor.
         */


        private void setNotifyNextSensor(BluetoothGatt gatt) {
            BluetoothGattCharacteristic characteristic;
            switch (mState) {
                case 0:
                    Log.d(TAG, "Set notify signal");
                    characteristic = gatt.getService(DATA_SERVICE)
                            .getCharacteristic(SIGNAL_DATA_CHAR);
                    break;
                default:
                    mHandler.sendEmptyMessage(MSG_DISMISS);
                    Log.i(TAG, "All Sensors Enabled");
                    return;
            }

            //Enable local notifications
            try {
                gatt.setCharacteristicNotification(characteristic, true);
                //Enabled remote notifications
                BluetoothGattDescriptor desc = characteristic.getDescriptor(CONFIG_DESCRIPTOR);
                if(desc == null){
                    mHandler.sendEmptyMessage(MSG_DISMISS);
                    Log.i(TAG, "All Sensors Enabled");
                    return;
                }
                desc.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                gatt.writeDescriptor(desc);
            }catch (NullPointerException e){
                e.printStackTrace();
            }
        }

        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            Log.d(TAG, "Connection State Change: "+status+" -> "+connectionState(newState));
            if (status == BluetoothGatt.GATT_SUCCESS && newState == BluetoothProfile.STATE_CONNECTED) {
                /*
                 * Once successfully connected, we must next discover all the services on the
                 * device before we can read and write their characteristics.
                 */
                gatt.discoverServices();
                mHandler.sendMessage(Message.obtain(null, MSG_PROGRESS, "Discovering Services..."));
            } else if (status == BluetoothGatt.GATT_SUCCESS && newState == BluetoothProfile.STATE_DISCONNECTED) {
                /*
                 * If at any point we disconnect, send a message to clear the weather values
                 * out of the UI
                 */
                mHandler.sendEmptyMessage(MSG_CLEAR);
            } else if (status != BluetoothGatt.GATT_SUCCESS) {
                /*
                 * If there is a failure at any stage, simply disconnect
                 */
                gatt.disconnect();
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            Log.d(TAG, "Services Discovered: "+status);
            mHandler.sendMessage(Message.obtain(null, MSG_PROGRESS, "Enabling Sensors..."));
            /*
             * With services discovered, we are going to reset our state machine and start
             * working through the sensors we need to enable
             */
            reset();
            enableNextSensor(gatt);
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            //For each read, pass the data up to the UI thread to update the display
            if (SIGNAL_DATA_CHAR.equals(characteristic.getUuid())) {
                mHandler.sendMessage(Message.obtain(null, MSG_HUMIDITY, characteristic));
            }

            //After reading the initial value, next we enable notifications
            if(!flagTheStart) {
                setNotifyNextSensor(gatt);
            }
        }

        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            //After writing the enable flag, next we read the initial value
            readNextSensor(gatt);
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            /*
             * After notifications are enabled, all updates from the device on characteristic
             * value changes will be posted here.  Similar to read, we hand these up to the
             * UI thread to update the display.
             */
            if(startTime == 0){
                startTime = java.lang.System.currentTimeMillis();
            }
            if (SIGNAL_DATA_CHAR.equals(characteristic.getUuid())) {
                count1+=5;
                mHandler.sendMessage(Message.obtain(null, MSG_HUMIDITY, characteristic));
            }
        }

        @Override
        public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            //Once notifications are enabled, we move to the next sensor and start over with enable
            advance();
            enableNextSensor(gatt);
        }

        @Override
        public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
            Log.d(TAG, "Remote RSSI: "+rssi);
        }

        private String connectionState(int status) {
            switch (status) {
                case BluetoothProfile.STATE_CONNECTED:
                    return "Connected";
                case BluetoothProfile.STATE_DISCONNECTED:
                    return "Disconnected";
                case BluetoothProfile.STATE_CONNECTING:
                    return "Connecting";
                case BluetoothProfile.STATE_DISCONNECTING:
                    return "Disconnecting";
                default:
                    return String.valueOf(status);
            }
        }
    };

    /*
     * We have a Handler to process event results on the main thread
     */
    private static final int MSG_HUMIDITY = 101;
    private static final int MSG_PRESSURE = 102;
    private static final int MSG_PRESSURE_CAL = 103;
    private static final int MSG_PROGRESS = 201;
    private static final int MSG_DISMISS = 202;
    private static final int MSG_CLEAR = 301;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            BluetoothGattCharacteristic characteristic;
            switch (msg.what) {
                case MSG_HUMIDITY:
                    characteristic = (BluetoothGattCharacteristic) msg.obj;
                    if (characteristic.getValue() == null) {
                        Log.w(TAG, "Error obtaining signal value");
                        return;
                    }
                    updateSignalValues(characteristic);
                    break;
                case MSG_PRESSURE:
                    characteristic = (BluetoothGattCharacteristic) msg.obj;
                    if (characteristic.getValue() == null) {
                        Log.w(TAG, "Error obtaining pressure value");
                        return;
                    }
                    break;
                case MSG_PRESSURE_CAL:
                    characteristic = (BluetoothGattCharacteristic) msg.obj;
                    if (characteristic.getValue() == null) {
                        Log.w(TAG, "Error obtaining cal value");
                        return;
                    }
                    break;
                case MSG_PROGRESS:
                    mProgress.setMessage((String) msg.obj);
                    if (!mProgress.isShowing()) {
                        mProgress.show();
                    }
                    break;
                case MSG_DISMISS:
                    mProgress.hide();
                    break;
                case MSG_CLEAR:
                    clearDisplayValues();
                    break;
                default:
                    break;
            }
        }
    };

    /* Methods to extract sensor data and update the UI */

    private void updateSignalValues(BluetoothGattCharacteristic characteristic) {
        ArrayList<Double> signal = SensorTagData.extractSignal(characteristic);
        currentTime = (java.lang.System.currentTimeMillis() - startTime);
        for(int i=0; i<5; i++) {
            double signal1 = signal.get(2*i);
            double signal2 = signal.get(2*i+1);
            if(times.size()>0) {
                while (times.size()>0 && times.get(0) * 1000 < currentTime - 6000) {
                    signalData.remove(0);
                    signalData2.remove(0);
                    times.remove(0);
                }
            }
            t11 = t12;
            t12 = t13;
            t13 = t14;
            t14 = t15;
            t15 = t16;
            t16 = t17;
            t17 = t18;
            t18 = t19;
            t19 = t110;
            t110 = (t11+t12+t13+t14+t15+t16+t17+t18+t19+signal1)/10;
            signalData.add(t110);
            t21 = t22;
            t22 = t23;
            t23 = t24;
            t24 = t25;
            t25 = t26;
            t26 = t27;
            t27 = t28;
            t28 = t29;
            t29 = t210;
            t210 = (t21+t22+t23+t24+t25+t26+t27+t28+t29+signal2)/10;
            signalData2.add(t210);
            times.add((double) (currentTime / 1000f));
        }
        mFrequency.setText(String.format(Locale.US,"%.2f Hz",(count1*1000f)/currentTime));
    }

    private DataPoint[] generateData1(){
        DataPoint[] values1 = new DataPoint[signalData.size()];
        for(int i=0; i<signalData.size(); i++){
            if(i+1>signalData.size()){
                values1[i] = new DataPoint(0,0);
            }else {
                values1[i] = new DataPoint(times.get(i), signalData.get(i));
            }
        }
        return values1;
    }

    private DataPoint[] generateData2(){
        DataPoint[] values1 = new DataPoint[signalData2.size()];
        for(int i=0; i<signalData2.size(); i++){
            if(i+1>signalData2.size()){
                values1[i] = new DataPoint(0,0);
            }else {
                values1[i] = new DataPoint(times.get(i), signalData2.get(i));
            }
        }
        return values1;
    }

    private DataPoint[] generateData3(){
        Object[] obj = RRExtract();
        ArrayList<Integer> rLocationsTemp = (ArrayList<Integer>)obj[0];
        ArrayList<Double> rValuesTemp = (ArrayList<Double>) obj[1];
        Integer[] rLocations = rLocationsTemp.toArray(new Integer[rLocationsTemp.size()]);
        Double[] rValues = rValuesTemp.toArray(new Double[rValuesTemp.size()]);
        DataPoint[] values1 = new DataPoint[rLocations.length];
        double ans=-1;
        if(rLocations.length>1) {
            ans = (times.get(rLocations[rLocations.length-1]) - times.get(rLocations[0])) / (rLocations.length-1);
            ans = (1 / ans) * 60;
        }
        if(!Double.isInfinite(ans) && !Double.isNaN(ans) && ans!=-1)
            mHeartRate.setText(String.format(Locale.US, "%.1f", ans));
        Arrays.sort(rLocations);
        DataPoint[] values2;
        for(int i=0; i<rLocations.length; i++){
            if(rLocations[i]<times.size()-1 && rLocations[i]>0){
                values1[i] = new DataPoint(times.get(rLocations[i]), rValues[i]);
            }else{
                values2 = new DataPoint[i];
                System.arraycopy(values1,0,values2,0,i);
                return values2;
            }
        }
        return values1;
    }

    public Object[] RRExtract(){
        ArrayList<Double> readArray;
        readArray = signalData;
        Double[] input = readArray.toArray(new Double[readArray.size()]);
        double[] x1 = new double[readArray.size()];
        double[] tempX1 = x1;
        for(int i=0;i<readArray.size();i++){
            x1[i] = input[i];
        }
        //DC cancelling & Normalization
        x1 = Library.arraySubtractor(x1, Library.mean(x1));
        x1 = Library.arrayDivider(x1,Library.max(Library.abs(x1)));

        //Low Pass Filter
        double[] temp = {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        double[] b = {1,0,0,0,0,0,-2,0,0,0,0,0,1};
        double[] a = {1,-2,1};
        double[] h_LP = Library.filter13_3(b,a,temp);
        double[] x2 = Library.conv(x1, h_LP);
        x2 = Library.arrayDivider(x2, Library.max(Library.abs(x2)));

        double[] x7 = new double[x2.length];
        System.arraycopy(x2, 0, x7, 0, x2.length);
        Arrays.sort(x2);
        double max_h = x2[x2.length - 101];
        double thresh = Library.mean(x7);
        double thresVal = 0.4;
        double[] poss_reg = Library.arrayComparer(x7, thresh+thresVal*max_h);
        double[] zeroArray = {0};
        ArrayList<Integer> left = Library.find(Library.diff(Library.arrayMerger(zeroArray, poss_reg)), 1);
        ArrayList<Integer> right = Library.find(Library.diff(Library.arrayMerger(poss_reg, zeroArray)), -1);
        int leftSize = left.size();
        int rightSize = right.size();
        ArrayList<Double> rValues = new ArrayList<>();
        ArrayList<Integer> rLocations = new ArrayList<>();
        int lastAns = 0;
        for(int i=0;i<Math.min(leftSize, rightSize);i++){
            if(left.get(i)<tempX1.length) {
                double[] maxAns = Library.arrayMaxAndIndex(tempX1, left.get(i), right.get(i));
                if ((int) maxAns[1] != 0 && times.get((int) maxAns[1]) - times.get(lastAns) > 0.05) {
                    rValues.add(maxAns[0]);
                    rLocations.add((int) maxAns[1]);
                    lastAns = (int) maxAns[1];
                }
            }
        }
        return new Object[]{rLocations, rValues};
    }

    public ArrayList<ArrayList<Integer>> getPeaks(double[] x1){
        //Normalization & DC Cancellation
        x1 = Library.arraySubtractor(x1, Library.mean(x1));
        x1 = Library.arrayDivider(x1,Library.max(Library.abs(x1)));

        //Low Pass Filtering
        double[] temp = {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        double[] b = {1,0,0,0,0,0,-2,0,0,0,0,0,1};
        double[] a = {1,-2,1};
        double[] h_LP = Library.filter13_3(b,a,temp);
        double[] x2 = Library.conv(x1, h_LP);
        x2 = Library.arrayDivider(x2, Library.max(Library.abs(x2)));

        //Thresholding
        double[] x6 = new double[x2.length];
        System.arraycopy(x2, 0, x6, 0, x2.length);
        Arrays.sort(x2);
        double max_h = x2[x2.length - 101];
        double thresh = Library.mean(x6);
        double thresVal = 0.2;
        double[] poss_reg = Library.arrayComparer(x6, thresh+thresVal*max_h);

        double[] zeroArray = {0};
        ArrayList<Integer> left = Library.find(Library.diff(Library.arrayMerger(zeroArray, poss_reg)), 1);
        ArrayList<Integer> right = Library.find(Library.diff(Library.arrayMerger(poss_reg, zeroArray)), -1);

        int leftSize = left.size();
        int rightSize = right.size();

        ArrayList<Integer> rlocs = new ArrayList<>();
        ArrayList<Integer> qlocs = new ArrayList<>();
        rlocs.add(0);
        qlocs.add(0);
        for(int i=1;i<Math.min(leftSize, rightSize);i++){
            if(left.get(i) < x1.length) {
                double[] maxAns = Library.arrayMaxAndIndex(x1, left.get(i), right.get(i));
                rlocs.add((int) maxAns[1]);
                double[] minAns = Library.arrayMinAndIndex(x1, rlocs.get(i - 1), left.get(i));
                qlocs.add((int) minAns[1]);
            }
        }
        ArrayList<ArrayList<Integer>> ans = new ArrayList<>();
        ans.add(rlocs);
        ans.add(qlocs);
        return ans;
    }

    public ArrayList<ArrayList<Integer>> getPoints(double[] temper){
        double[] integ = Library.arrayMaker(31, 1);
        integ = Library.arrayDivider(integ, 31);

        double[] dtemper = Library.arrayMultiplier(Library.diff(temper), 100);
        dtemper = Library.arrayDivider(dtemper,Library.max(Library.abs(dtemper)));
        dtemper = Library.conv(dtemper, integ);

        double[] ddtemper = Library.arrayMultiplier(Library.diff(dtemper), 100);
        ddtemper = Library.arrayDivider(ddtemper,Library.max(Library.abs(ddtemper)));
        ddtemper = Library.conv(ddtemper, integ);

        ArrayList<ArrayList<Integer>> rlocsandqlocs = getPeaks(temper);
        ArrayList<Integer> qloc1 = rlocsandqlocs.get(1);
        ArrayList<Integer> rloc1 = rlocsandqlocs.get(0);
        ArrayList<Integer> zeross2 = new ArrayList<>(rloc1.size());
        for(int i=0;i<rloc1.size();i++){
            zeross2.add(0);
        }
        for(int i=0;i<rloc1.size()-1;i++){
            int step = 1;
            int start = rloc1.get(i);
            int end = qloc1.get(i+1);
            int loc = -1;
            while(loc==-1){
                if(step+start >end-step -1){
                    break;
                }
                loc = Library.getInflectionPoint(ddtemper, start + step, end - step -1,step);
                if(loc!=-1){
                    zeross2.set(i,loc);
                    break;
                }else{
                    step+=1;
                }

            }
        }
        ArrayList<Integer> rloc = new ArrayList<>();
        ArrayList<Integer> qloc = new ArrayList<>();
        ArrayList<Integer> sloc = new ArrayList<>();
        for(int i=0;i<rloc1.size();i++){
            if(zeross2.get(i)!=0){
                sloc.add(zeross2.get(i));
                rloc.add(rloc1.get(i));
                qloc.add(qloc1.get(i));
            }
        }
       ArrayList<ArrayList<Integer>> ans = new ArrayList<>();
        ans.add(rloc);
        ans.add(qloc);
        ans.add(sloc);
        return ans;
    }

    public ArrayList<Double> extractFeatureVectors(double[] x){
        x = Library.arraySubtractor(x, Library.mean(x));
        x = Library.arrayDivider(x,Library.max(Library.abs(x)));
        ArrayList<ArrayList<Integer>> points = getPoints(x);
        ArrayList<Integer> rloc = points.get(0);
        ArrayList<Integer> qloc = points.get(1);
        ArrayList<Integer> sloc = points.get(2);
        double[] Asys1 = Library.getSubArray(x, rloc);
        double[] Aval1 = Library.getSubArray(x, qloc);
        double[] Adn1 = Library.getSubArray(x, sloc);
        double DBP, SBP;
        if(Asys1.length >= 2) {
            double[] Asys = new double[Asys1.length-1];
            double[] Adn = new double[Adn1.length-1];
            double[] Aval = new double[Aval1.length-1];

            System.arraycopy(Asys1, 0, Asys, 0, Asys1.length-1);
            System.arraycopy(Aval1, 0, Aval, 0, Aval1.length-1);
            System.arraycopy(Adn1, 0, Adn, 0, Adn1.length-1);
            double[] SysArea = new double[Asys.length];
            for(int i=0; i<Asys.length; i++){
                SysArea[i] = Library.sumArray(x, rloc.get(i), sloc.get(i));
            }
            double[] DNArea = new double[Adn.length];
            for(int i=0; i<Adn.length; i++){
                DNArea[i] = Library.sumArray(x, sloc.get(i), qloc.get(i+1));
            }
            double[] totArea = Library.twoArrayAdder(SysArea, DNArea);
            double[] peakInt = Library.diff(Library.getSubArray2(times, rloc));
            double[] pulseHt = Library.twoArraySubtractor(Asys, Aval);
            double[] pulseInt = Library.diff(Library.getSubArray2(times, qloc));
            double[] augIndex = Library.twoArrayDivider(Library.twoArraySubtractor(Adn, Aval),Library.twoArraySubtractor(Asys, Aval));
            DBP = 73.9188 + Asys[Asys.length - 1] * 0.564005 + Adn[Asys.length - 1] * 1.77362 + Aval[Asys.length - 1] * -2.19562 + SysArea[Asys.length - 1] * -0.280499 + DNArea[Asys.length - 1] * 0.181496 + totArea[Asys.length - 1] * -0.0990031 + peakInt[Asys.length - 1] * 0.928643 + pulseHt[Asys.length - 1] * 2.75962 + pulseInt[Asys.length - 1] * -0.140988 + augIndex[Asys.length - 1] * -0.787559;
            SBP = 119.726 + Asys[Asys.length - 1] * 1.38196 + Adn[Asys.length - 1] * 9.53411 + Aval[Asys.length - 1] * -5.07123 + SysArea[Asys.length - 1] * -0.983105 + DNArea[Asys.length - 1] * 0.585099 + totArea[Asys.length - 1] * -0.398006 + peakInt[Asys.length - 1] * 1.20928 + pulseHt[Asys.length - 1] * 6.4532 + pulseInt[Asys.length - 1] * -1.16069 + augIndex[Asys.length - 1] * -0.681099;
        }else{
            DBP = 0;
            SBP = 0;
        }
        ArrayList<Double> answer = new ArrayList<>();
        answer.add(DBP);
        answer.add(SBP);
        return answer;
    }
}
