package com.example.wifitutorial;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.List;
import java.util.stream.Collectors;

public class WiFiReceiver extends BroadcastReceiver {

    private WiFiScannerActivity wiFiScannerActivity;

    public WiFiReceiver(WiFiScannerActivity wiFiScannerActivity) {
        this.wiFiScannerActivity = wiFiScannerActivity;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onReceive(Context context, Intent intent) {
        List<ScanResult> scanResults = this.wiFiScannerActivity.wifiManager.getScanResults().stream().sorted((o1, o2) -> Integer.compare(o2.level, o1.level)).collect(Collectors.toList());
        this.wiFiScannerActivity.unregisterReceiver(this);

        for (ScanResult scanResult : scanResults) {
            this.wiFiScannerActivity.arrayList.add("SSID: " + scanResult.SSID + "\nLevel: " + scanResult.level + "dBm");
            this.wiFiScannerActivity.arrayAdapter.notifyDataSetChanged();
        }
    }
}
