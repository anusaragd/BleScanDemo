package com.minewbeacon.blescan.demo;

import android.app.ListActivity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.minew.beacon.BeaconValueIndex;
import com.yuliwuli.blescan.demo.R;

public class ReportActivity extends AppCompatActivity {

    private TextView mback_report;

    ListView reportlist;
    TextView showReport;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        initView();
        initListener();

        showReport = (TextView)findViewById(R.id.Showreport);
        showReport.setText("คุณ "+ namestaff.name + " อยู่ใกล้ " + namestaff.mac + " นี้มากที่สุด");
    }

    private void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mback_report = (TextView) findViewById(R.id.backreport);


    }

    private void initListener() {
        mback_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReportActivity.this, MainActivity.class);
                startActivity(intent);
                Toast.makeText(getApplicationContext(), namestaff.mac, Toast.LENGTH_SHORT).show();


            }
        });
    }
}
