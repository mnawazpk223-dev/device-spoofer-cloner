package com.virtual.cloner;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import top.niunaijun.blackbox.BlackBoxCore;
import top.niunaijun.blackbox.entity.device.BDevice;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        Button btn = new Button(this);
        btn.setText("🚀 Launch Cloned App (Spoofed Info)");
        setContentView(btn);

        btn.setOnClickListener(v -> {
            int virtualUserId = 0;

            // 1. Set Fake Device Information inside Sandbox
            BDevice fakeDevice = new BDevice();
            fakeDevice.setAndroidId("88a1b2c3d4e5f678"); // Fake Android ID
            fakeDevice.setBuildModel("Pixel 8 Pro");       // Fake Model Name
            fakeDevice.setBrand("Google");               // Fake Device Brand

            BlackBoxCore.get().getBDeviceManager().setBDevice(virtualUserId, fakeDevice);

            // 2. Launch Target Package (e.g., Chrome) inside sandbox
            String targetPkg = "com.android.chrome";
            if (!BlackBoxCore.get().isInstalled(targetPkg, virtualUserId)) {
                BlackBoxCore.get().installPackageAsUser(targetPkg, virtualUserId);
            }

            BlackBoxCore.get().launchApk(targetPkg, virtualUserId);
            Toast.makeText(this, "App Started in Isolated Virtual Sandbox!", Toast.LENGTH_SHORT).show();
        });
    }
}
