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
        List<ScanResult> scanResults
                = this.wiFiScannerActivity.wifiManager.getScanResults().stream()
                    .sorted((o1, o2) -> Integer.compare(o2.level, o1.level))
                        .collect(Collectors.toList());
        this.wiFiScannerActivity.unregisterReceiver(this);

        for (ScanResult scanResult : scanResults) {
            this.wiFiScannerActivity.arrayList.add(WiFiReceiver.formatScanResult(scanResult));
            this.wiFiScannerActivity.arrayAdapter.notifyDataSetChanged();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private static String formatScanResult(ScanResult scanResult) {
        StringBuffer stringBuffer = new StringBuffer();

        stringBuffer.append(System.lineSeparator());

        stringBuffer.append("SSID: ");
        stringBuffer.append(scanResult.SSID.length() > 0?scanResult.SSID:"<<Hidden Network>>");
        stringBuffer.append(System.lineSeparator());

        stringBuffer.append("BSSID: ");
        stringBuffer.append(scanResult.BSSID);
        stringBuffer.append(System.lineSeparator());

        stringBuffer.append("Capabilities: ");
        stringBuffer.append(scanResult.capabilities);
        stringBuffer.append(System.lineSeparator());

        stringBuffer.append("Channel Width: ");
        stringBuffer.append(WiFiReceiver.formatChannelWidth(scanResult.channelWidth));
        stringBuffer.append(System.lineSeparator());

        stringBuffer.append("Frequency: ");
        stringBuffer.append(scanResult.frequency);
        stringBuffer.append(" MHz");
        stringBuffer.append(System.lineSeparator());

        stringBuffer.append("Strength Level: ");
        stringBuffer.append(scanResult.level);
        stringBuffer.append(" dBm");
        stringBuffer.append(System.lineSeparator());

        stringBuffer.append("Quality of Connection Here: ");
        stringBuffer.append(WiFiReceiver.friendlyStrengthLevelIndicator(scanResult.level));
        stringBuffer.append(System.lineSeparator());

        return stringBuffer.toString();
    }

    private static String formatChannelWidth(int channelWidth) {
        String string;

        switch (channelWidth) {
            case ScanResult.CHANNEL_WIDTH_20MHZ: string = "20 MHz"; break;
            case ScanResult.CHANNEL_WIDTH_40MHZ: string = "40 MHz"; break;
            case ScanResult.CHANNEL_WIDTH_80MHZ: string = "80 MHz"; break;
            case ScanResult.CHANNEL_WIDTH_80MHZ_PLUS_MHZ: string = "80 MHz + 80 MHz"; break;
            case ScanResult.CHANNEL_WIDTH_160MHZ: string = "160 MHz"; break;
            default: string = "<<Unknown Channel Width>>"; break;
        }

        return string;
    }

    private static String friendlyStrengthLevelIndicator(int level) {
        String string;

        if (level >= -50) {
            string = "Excellent";
        } else if (level >= -60) {
            string = "Good";
        } else if (level >= -70) {
            string = "Fair";
        } else {
            string = "Weak";
        }

        return string;
    }
}
