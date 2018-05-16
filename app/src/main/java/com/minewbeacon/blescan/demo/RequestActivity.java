package com.minewbeacon.blescan.demo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.yuliwuli.blescan.demo.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RequestActivity extends AppCompatActivity {

    private TextView mback_report;

    ListView reportlist;
    TextView showReport;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);

        reportlist = (ListView)findViewById(R.id.request_user);

        // Initializing a new String Array
        String[] fruits = new String[] {
                "Japanese Persimmon",
                "Kakadu lime",
                "Illawarra Plum",
                "Malay Apple ",
                "Mamoncillo",
                "Persian lime"
        };

        // Create a List from String Array elements
        List<String> fruits_list = new ArrayList<String>(Arrays.asList(fruits));

        // Create a ArrayAdapter from List
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_list_item_1, fruits_list);

        // Populate ListView with items from ArrayAdapter
        reportlist.setAdapter(arrayAdapter);

        // Set an item click listener for ListView
        reportlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Get the selected item text from ListView
                String selectedItem = (String) parent.getItemAtPosition(position);

                // Start an alpha animation for clicked item
                Animation animation1 = new AlphaAnimation(0.3f, 1.0f);
                animation1.setDuration(4000);
                view.startAnimation(animation1);

                AlertDialog.Builder builder = new AlertDialog.Builder(RequestActivity.this);

                // Set a title for alert dialog
                builder.setTitle("Select your answer.");

                // Ask the final question
                builder.setMessage("Want to apply big font size?");

                // Set click listener for alert dialog buttons
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch(which){
                            case DialogInterface.BUTTON_POSITIVE:
                                // User clicked the Yes button
//                                tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 35);
                                Intent intent = new Intent(RequestActivity.this,InformationActivity.class);
                                startActivity(intent);
//                                Toast.makeText(getApplicationContext(), name_edit.getText() , Toast.LENGTH_SHORT).show();
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                // User clicked the No button
                                break;
                        }
                    }
                };

                // Set the alert dialog yes button click listener
                builder.setPositiveButton("Yes", dialogClickListener);

                // Set the alert dialog no button click listener
                builder.setNegativeButton("No",dialogClickListener);

                AlertDialog dialog = builder.create();
                // Display the alert dialog on interface
                dialog.show();




                // Display the selected item text on TextView
//                tv.setText("Your favorite : " + selectedItem);
            }
        });

        initView();
        initListener();

//        showReport = (TextView)findViewById(R.id.Showreport);
//        showReport.setText("คุณ "+ namestaff.name + " อยู่ใกล้ " + namestaff.mac + " นี้มากที่สุด");
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
                Intent intent = new Intent(RequestActivity.this, MainActivity.class);
                startActivity(intent);
                Toast.makeText(getApplicationContext(), namestaff.mac, Toast.LENGTH_SHORT).show();


            }
        });
    }
}
