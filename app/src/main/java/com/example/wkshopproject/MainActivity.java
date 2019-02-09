package com.example.wkshopproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    private Set<String> data = new HashSet<>();
    private String val;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseApp.initializeApp(MainActivity.this);
        EditText edt = findViewById(R.id.editText);
        Button push = findViewById(R.id.push), fetch = findViewById(R.id.fetch);
        push.setOnClickListener(v -> {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference();
            myRef.child("message").push().setValue(edt.getText().toString());
            edt.setText(null);
            Toast.makeText(this, "Data Uploaded !!", Toast.LENGTH_SHORT).show();
        });
        fetch.setOnClickListener(v -> {

            ListAdapter adapter = new ArrayAdapter(this, R.layout.list,new ArrayList( data));
            ListView lv = findViewById(R.id.list);
            lv.setAdapter(adapter);
        });

        DatabaseReference myRef1 = FirebaseDatabase.getInstance().getReference();
        ValueEventListener eventlistner = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                val = map.values().toString();
                String[] str = val.split(",");
                for (String string : str) {
                    if (string.contains("}")) {
                        data.add(string.substring(string.indexOf("=") + 1, (string.length() - 2)));
                    } else {
                        data.add(string.substring(string.indexOf("=") + 1));
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Toast.makeText(MainActivity.this, "Failed to read value." + error.toException(), Toast.LENGTH_SHORT).show();
            }
        };
        myRef1.addValueEventListener(eventlistner);
    }


}
