package com.stormdzh.utdid;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private TextView tvInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvInfo = findViewById(R.id.tvInfo);

        StringBuilder sb = new StringBuilder();
        sb.append("Utdid值为：");
        sb.append("\n");
        sb.append(getUtdid());
        tvInfo.setText(sb);
    }


    private String getUtdid() {
        String utdid = com.ut.device.UTDevice.getUtdid(this);
        Log.i("utdid", "getUtdid:" + utdid);

        return utdid;
    }

}
