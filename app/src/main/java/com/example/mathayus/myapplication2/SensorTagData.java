package com.example.mathayus.myapplication2;

import android.bluetooth.BluetoothGattCharacteristic;

import java.util.ArrayList;

class SensorTagData {
    static ArrayList<Double> extractSignal(BluetoothGattCharacteristic c) {
        Integer temp1 = c.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT16, 0);
        Integer temp2 = c.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT16, 2);
        Integer temp3 = c.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT16, 4);
        Integer temp4 = c.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT16, 6);
        Integer temp5 = c.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT16, 8);
        Integer temp6 = c.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT16, 10);
        Integer temp7 = c.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT16, 12);
        Integer temp8 = c.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT16, 14);
        Integer temp9 = c.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT16, 16);
        Integer temp10 = c.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT16, 18);
        ArrayList<Double> ans1 = new ArrayList<>();
        ans1.add((double)temp1);
        ans1.add((double)temp2);
        ans1.add((double)temp3);
        ans1.add((double)temp4);
        ans1.add((double)temp5);
        ans1.add((double)temp6);
        ans1.add((double)temp7);
        ans1.add((double)temp8);
        ans1.add((double)temp9);
        ans1.add((double)temp10);
        return ans1;
    }
}
