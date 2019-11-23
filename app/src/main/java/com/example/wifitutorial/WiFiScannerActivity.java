package com.example.wifitutorial;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.util.ArrayList;
import java.util.List;

public class WiFiScannerActivity extends AppCompatActivity {

    protected WifiManager wifiManager;

    private BroadcastReceiver wifiReceiver = new WiFiReceiver(this);

    private ListView listView;
    private Button scanButton;

    protected ArrayList<String> arrayList = new ArrayList<>();
    protected ArrayAdapter arrayAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Request permission required for scanning WiFi.
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION }, 1);

        this.setContentView(R.layout.activity_main);

        this.wifiManager = (WifiManager) this.getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        if (!wifiManager.isWifiEnabled()) {
            Toast.makeText(this, "WiFi is disabled. Now enabling.", Toast.LENGTH_LONG).show();
            wifiManager.setWifiEnabled(true);
        }

        Toast.makeText(this, "Ready to scan WiFi.", Toast.LENGTH_LONG).show();

        listView = findViewById(R.id.wifi_list);

        this.scanButton = findViewById(R.id.scanBtn);
        this.scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scanWifi();
            }
        });

        this.arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arrayList);
        this.listView.setAdapter(arrayAdapter);

    }

    private void scanWifi() {
        this.arrayList.clear();
        this.arrayAdapter.notifyDataSetChanged();

        this.registerReceiver(this.wifiReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        this.wifiManager.startScan();
        Toast.makeText(this, "Now Scanning WiFi ...", Toast.LENGTH_SHORT).show();

    }

}
