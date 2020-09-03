package com.example.jobportalapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegistrationActivity extends AppCompatActivity {

    EditText edtEmailReg,edtPassReg;
    Button btnReg,btnLog;

    //Firebase auth
     private FirebaseAuth mAuth;

     //Progress dialog
    ProgressDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        mAuth = FirebaseAuth.getInstance();

        mDialog = new ProgressDialog(this);

        RegistrationFunction();
    }

    private void RegistrationFunction() {
        edtEmailReg = findViewById(R.id.email_registration);
        edtPassReg = findViewById(R.id.password_registration);
        btnReg = findViewById(R.id.btn_registration);
        btnLog = findViewById(R.id.btn_log);

        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = edtEmailReg.getText().toString().trim();
                String password = edtPassReg.getText().toString().trim();

                if(TextUtils.isEmpty(email)){
                    edtEmailReg.setError("Required field..");
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    edtPassReg.setError("Required field..");
                    return;
                }

                mDialog.setMessage("Processing..");
                mDialog.show();

                mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(RegistrationActivity.this, "Successful", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                            mDialog.dismiss();
                        }else {

                            Toast.makeText(RegistrationActivity.this, "Registration Failed..", Toast.LENGTH_SHORT).show();
                            mDialog.dismiss();
                        }
                    }
                });
            }
        });


        btnLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }
        });
    }
}
