package com.example.uni13033;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignUp extends AppCompatActivity {

    private FirebaseAuth auth;
    FirebaseDatabase database;
    DatabaseReference reference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        auth = FirebaseAuth.getInstance();

        Button confirm = (Button) findViewById(R.id.confirmSignUpBtn);
        EditText email = (EditText) findViewById(R.id.editTextTextEmailAddress);
        EditText password = (EditText) findViewById(R.id.editTextTextPassword);
        EditText name = (EditText) findViewById(R.id.editTextTextPersonName3);
        EditText address = (EditText) findViewById(R.id.editTextTextPostalAddress);

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailStr, passwordStr, nameStr, addressStr;
                emailStr = email.getText().toString().trim();
                passwordStr = password.getText().toString().trim();
                nameStr = name.getText().toString();
                addressStr = address.getText().toString();


                if(emailStr.equals("") || passwordStr.equals("") || nameStr.equals("") || addressStr.equals("")){
                    Toast.makeText(SignUp.this, "Please fill out all info", Toast.LENGTH_SHORT).show();
                }else{
                    database = FirebaseDatabase.getInstance();
                    signUp(emailStr, passwordStr, nameStr, addressStr);
                }
            }
        });
    }

    public void signUp(String email, String password, String name, String address){
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser user = auth.getCurrentUser();
                    assert user != null;
                    reference = database.getReference("users").child(user.getUid());
                    User user1 = new User(name, address);
                    reference.setValue(user1);
                    Intent intent = new Intent(SignUp.this, Central.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(SignUp.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}