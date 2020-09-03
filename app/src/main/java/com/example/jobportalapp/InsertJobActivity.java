package com.example.jobportalapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.jobportalapp.Model.Job;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.Date;

public class InsertJobActivity extends AppCompatActivity {
    Toolbar toolbarInsert;
    EditText job_title,job_description,job_skills, job_salary;
    Button btn_post_job;

    //FireBase ..
    FirebaseAuth mAuth;
    DatabaseReference mJobPost;
    DatabaseReference mPublicDatabase;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_job);

        toolbarInsert = findViewById(R.id.insert_job_toolbar);
        setSupportActionBar(toolbarInsert);
        getSupportActionBar().setTitle("Insert job");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mAuth = FirebaseAuth.getInstance();
        FirebaseUser mUser = mAuth.getCurrentUser();
        String uId = mUser.getUid();
        mJobPost = FirebaseDatabase.getInstance().getReference().child("Job Post").child(uId);

        mPublicDatabase = FirebaseDatabase.getInstance().getReference().child("Public database");

        InsertJobFunction();
    }

    private void InsertJobFunction() {
        job_title  = findViewById(R.id.job_title);
        job_description = findViewById(R.id.job_description);
        job_skills = findViewById(R.id.job_skill);
        job_salary = findViewById(R.id.job_salary);
        btn_post_job = findViewById(R.id.btn_job_post);

        btn_post_job.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String title = job_title.getText().toString().trim();
                String description = job_description.getText().toString().trim();
                String skills = job_skills.getText().toString().trim();
                String salary = job_salary.getText().toString().trim();

                if(TextUtils.isEmpty(title)){
                    job_title.setError("Required Field..");
                    return;
                }
                if(TextUtils.isEmpty(description)){
                    job_description.setError("Required Field..");
                    return;
                }
                if(TextUtils.isEmpty(skills)){
                    job_skills.setError("Required Field..");
                    return;
                }
                if(TextUtils.isEmpty(salary)){
                    job_salary.setError("Required Field..");
                    return;
                }

                String id = mJobPost.push().getKey();
                String date = DateFormat.getDateInstance().format(new Date());

                Job job = new Job(title,description,skills,salary,id,date);
                mJobPost.child(id).setValue(job);
                mPublicDatabase.child(id).setValue(job);

                Toast.makeText(InsertJobActivity.this, "Successful", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(),PostJobActivity.class));
            }
        });
    }
}
