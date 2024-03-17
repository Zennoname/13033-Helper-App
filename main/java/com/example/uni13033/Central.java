package com.example.uni13033;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.telephony.SmsManager;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;
import java.util.HashMap;
import java.util.Objects;

public class Central extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 0 ;
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    DbHelper dbHelper;
    FirebaseAuth auth;
    FirebaseDatabase database;
    DatabaseReference reference;
    EditText name;
    EditText address;
    RadioGroup group;
    LocationManager locationManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_central);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        Button update = (Button) findViewById(R.id.updateInfoBtn);
        update.setOnClickListener(this::onClick);
        Button send = (Button) findViewById(R.id.sendBtn);
        send.setOnClickListener(this::onClick);
        Button langChange = (Button) findViewById(R.id.langBtn);
        langChange.setOnClickListener(this::onClick);
        Button logout = (Button) findViewById(R.id.logoutBtn);
        logout.setOnClickListener(this::onClick);
        Button edit = (Button) findViewById(R.id.editOptionBtn);
        edit.setOnClickListener(this::onClick);

        name = (EditText) findViewById(R.id.editTextTextPersonName);
        address = (EditText) findViewById(R.id.editTextTextPersonName2);

        group = (RadioGroup) findViewById(R.id.radioGroup);
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = auth.getCurrentUser();

        if(currentUser == null){
            Intent intent = new Intent(Central.this, MainActivity.class);
            startActivity(intent);
        }else{
            reference = database.getReference("users").child(currentUser.getUid());
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    User user = new User("","");
                    if(snapshot.getValue(User.class)!=null){
                        user = snapshot.getValue(User.class);
                    }
                    assert user != null;
                    name.setText(user.name);
                    address.setText(user.address);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(Central.this, "Issue Reading Data", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }




    public void updateInfo() {
        if(name.getText().toString().equals("") || address.getText().toString().equals("")){
            Toast.makeText(this, "Fill the sections before updating!", Toast.LENGTH_SHORT).show();
        }else {
            FirebaseUser user = auth.getCurrentUser();
            database = FirebaseDatabase.getInstance();
            assert user != null;
            reference = database.getReference("users").child(user.getUid());

            User n1 = new User(name.getText().toString(), address.getText().toString());
            HashMap hashMap = new HashMap();
            hashMap.put("name", n1.getName());
            hashMap.put("address", n1.getAddress());
            reference.updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener() {
                @Override
                public void onSuccess(Object o) {
                    Toast.makeText(Central.this, "Successful Update", Toast.LENGTH_SHORT).show();
                }
            });
        }

    }


    public void sendSMS(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.SEND_SMS)) {
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, MY_PERMISSIONS_REQUEST_SEND_SMS);
                return;
            }
        }
        SmsManager smsManager = SmsManager.getDefault();
        int id = group.getCheckedRadioButtonId();
        RadioButton btn = (RadioButton) findViewById(id);
        String message1 = btn.getText().toString();
        String message = message1.charAt(0)+" "+name.getText().toString().toUpperCase()+" "+address.getText().toString().toUpperCase();
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        smsManager.sendTextMessage("+306949999999", null, message, null, null);
        Toast.makeText(getApplicationContext(), "SMS sent.", Toast.LENGTH_LONG).show();
        gpsCheck();
    }

    public void gpsCheck(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
            return;
        }
        Gps gps = new Gps(getApplicationContext());
        Location l = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        String location = "";
        Date date = new Date();
        if(l == null){
            location = " null";
        }else{
            location = " x:"+(int)l.getLatitude()+" y:"+(int)l.getLongitude();
        }
        FirebaseUser user = auth.getCurrentUser();
        assert user != null;
        reference = database.getReference("users").child(user.getUid());
        String stamp = String.valueOf(date.getTime())+" "+location;
        Toast.makeText(this, String.valueOf(date.getTime())+" "+location, Toast.LENGTH_SHORT).show();
        reference.child("stamps").setValue(stamp);
    }


    @SuppressLint("NonConstantResourceId")
    public void onClick(View view){
        switch (view.getId()){
            case R.id.updateInfoBtn:
                updateInfo();
                break;
            case R.id.sendBtn:
                if (group.getCheckedRadioButtonId() == -1)
                {
                    Toast.makeText(this, "You must check an option first", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    sendSMS();
                }
                break;
            case R.id.langBtn:
                Toast.makeText(this,"language change", Toast.LENGTH_SHORT).show();
                break;
            case R.id.logoutBtn:
                auth.signOut();
                Intent intent = new Intent(Central.this, MainActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.editOptionBtn:
                Toast.makeText(this,"edit", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }

    public void AddOption(String option){
        boolean insertData = dbHelper.addOption(option);
        if(insertData){
            Toast.makeText(this, "Insertion Complete", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "Insertion Incomplete", Toast.LENGTH_SHORT).show();
        }
    }

    //public void editOptions(){}
    //public void lChange(){}

}