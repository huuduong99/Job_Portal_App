package com.example.jobportalapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    EditText edtEmail,edtPassword;
    Button btnLogin,btnRegistration;

    //Firebase mAuth;
    FirebaseAuth mAuth;

    //Progress dialog
    ProgressDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(),HomeActivity.class));
        }

        mDialog=new ProgressDialog(this);
        LoginFunction();
    }

    private void LoginFunction() {
        edtEmail = findViewById(R.id.email_login);
        edtPassword = findViewById(R.id.password_login);
        btnLogin = findViewById(R.id.btn_login);
        btnRegistration = findViewById(R.id.btn_reg);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String mEmail = edtEmail.getText().toString().trim();
                String password = edtPassword.getText().toString().trim();

                if(TextUtils.isEmpty(mEmail)){
                    edtEmail.setError("Require Field..");
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    edtPassword.setError("Require Field..");
                    return;
                }

                mDialog.setMessage("Processing");
                mDialog.show();

                mAuth.signInWithEmailAndPassword(mEmail,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){

                            Toast.makeText(MainActivity.this, "Successful", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                            mDialog.dismiss();
                        }else {

                            Toast.makeText(MainActivity.this, "Login Failed..", Toast.LENGTH_SHORT).show();
                            mDialog.dismiss();
                        }
                    }
                });

            }
        });
        btnRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getApplicationContext(),RegistrationActivity.class));
            }
        });
    }


}
