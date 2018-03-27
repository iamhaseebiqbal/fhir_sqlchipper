package com.demo.haseeb.fhir_sqlchipper_demo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.demo.haseeb.fhir_sqlchipper_demo.Helper.DBHelper;
import com.demo.haseeb.fhir_sqlchipper_demo.Helper.FetchJSON;

public class MainActivity extends AppCompatActivity {

    FetchJSON fetchJSON;
    DBHelper dbHelper;
    TextView patients_label;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        patients_label = (TextView)findViewById(R.id.patients_label);

        dbHelper = new DBHelper(this);
        fetchJSON = new FetchJSON(this, patients_label);

        fetchJSON.displayPatientsNames();

        Button downloadButton = findViewById(R.id.download_button);
        downloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                patients_label.setText("Downloading...");
                fetchJSON.execute();
            }
        });

        Button deleteButton = findViewById(R.id.delete_button);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                patients_label.setText("");
                dbHelper.deleteAllPatients();
            }
        });

    }
}
