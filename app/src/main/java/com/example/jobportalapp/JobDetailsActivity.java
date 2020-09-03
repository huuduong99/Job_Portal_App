package com.example.jobportalapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.jobportalapp.Model.Job;

public class JobDetailsActivity extends AppCompatActivity {

    Toolbar toolbar;
    TextView mTitle,mDate,mDescription,mSkills,mSalary;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_details);

        toolbar = findViewById(R.id.toolbar_job_details);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Job Details");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mTitle = findViewById(R.id.job_details_title);
        mDate = findViewById(R.id.job_details_date);
        mDescription = findViewById(R.id.job_details_description);
        mSkills = findViewById(R.id.job_details_skills);
        mSalary = findViewById(R.id.job_details_salary);

        Intent intent = getIntent();

        if(intent != null){
            if(intent.hasExtra("job")){
                Job jobDetails = (Job) intent.getSerializableExtra("job");

                mTitle.setText(jobDetails.getTitle());
                mDate.setText(jobDetails.getDate());
                mDescription.setText(jobDetails.getDescription());
                mSalary.setText(jobDetails.getSalary());
                mSkills.setText(jobDetails.getSkills());
            }
        }


    }
}
