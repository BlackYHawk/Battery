package com.hawk.battery;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.hawk.battery.widget.BatteryView;

public class MainActivity extends AppCompatActivity {
    private BatteryChangeReceiver mBatteryReceiver;
    private BatteryView ivBattery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ivBattery = (BatteryView) findViewById(R.id.ivBattery);

        mBatteryReceiver = new BatteryChangeReceiver();
        IntentFilter batteryFilter = new IntentFilter();
        batteryFilter.addAction(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(mBatteryReceiver, batteryFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mBatteryReceiver);
    }

    private class BatteryChangeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (Intent.ACTION_BATTERY_CHANGED.equals(action)) {
                // 是否在充电
                int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
                boolean isCharging = (status == BatteryManager.BATTERY_STATUS_CHARGING ||
                        status == BatteryManager.BATTERY_STATUS_FULL);
                // 获取当前电量
                int level = intent.getIntExtra("level", 0);
                // 电量的总刻度
                int scale = intent.getIntExtra("scale", 0);
                // 把它转换成百分比
                int key = ((level * 100) / scale);
                Log.e("MainActivity", String.format("charge:%s, key:%d", String.valueOf(isCharging), key));
                ivBattery.setPower(key);
            }
        }
    }
}
