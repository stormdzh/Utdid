package com.stormdzh.utdid;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private TextView tvInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestPermissions();
        tvInfo = findViewById(R.id.tvInfo);


        StringBuilder sb = new StringBuilder();
        sb.append("Utdid值为：");
        sb.append("\n");
        sb.append(getUtdid());
        sb.append("\n");

        DeviceInfo.init(this);
        sb.append("\n");
        sb.append("正常获取IMEI值为：");
        sb.append("\n");
        sb.append(DeviceInfo.getSn());


        sb.append("\n");
        sb.append("\n");
        sb.append("反射获取IMEI值为：");
        sb.append("\n");
        sb.append(DeviceInfo.getRefIMEI());


        sb.append("\n");
        sb.append("\n");
        sb.append("遍历卡槽：");
        sb.append("\n");
        sb.append(DeviceInfo.JudgeSIM());



//        sb.append("\n");
//        sb.append("\n");
//        sb.append("测试：");
//        sb.append("\n");
//        sb.append(DeviceInfo.getIMEI());



        tvInfo.setText(sb);

        DeviceInfo.JudgeSIM();
    }


    private void requestPermissions() {
        ArrayList<String> ps = new ArrayList<>();

        int per = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);
        if (per != PackageManager.PERMISSION_GRANTED) {
            ps.add(Manifest.permission.READ_PHONE_STATE);
        }

        if (!ps.isEmpty()) {
            String[] ps3 = new String[ps.size()];
            ps.toArray(ps3);
            ActivityCompat.requestPermissions(this, ps3, 100);
        }
    }


    private String getUtdid() {
        String utdid = com.ut.device.UTDevice.getUtdid(this);
        Log.i("utdid", "getUtdid:" + utdid);

        return utdid;
    }

}
