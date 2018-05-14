package com.minewbeacon.blescan.demo;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.minewbeacon.blescan.demo.SQLite.myDBClass;
import com.yuliwuli.blescan.demo.R;

public class EditnameActivity extends AppCompatActivity {

    Button Sub_edit ;
    EditText name_edit ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editname);

        myDBClass myDb = new myDBClass(this);
        myDb.getWritableDatabase(); // First method

        name_edit = (EditText) findViewById(R.id.editText_name);
//        name_edit.setText(Build.MODEL);

        Sub_edit = (Button)findViewById(R.id.button_submit);
        Sub_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                namestaff.name = name_edit.getText().toString(); // save namestaff
                Intent intent = new Intent(EditnameActivity.this,MainActivity.class);
                startActivity(intent);
                Toast.makeText(getApplicationContext(), name_edit.getText() , Toast.LENGTH_SHORT).show();

            }
        });


    }
}
