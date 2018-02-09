package com.hawk.battery;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.hawk.battery.widget.BatteryView;

public class MainActivity extends AppCompatActivity {
    private BatteryView ivBattery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ivBattery = (BatteryView) findViewById(R.id.ivBattery);
        ivBattery.setPower(80);
    }
}
