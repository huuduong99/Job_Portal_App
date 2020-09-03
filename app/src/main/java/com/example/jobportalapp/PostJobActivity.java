package com.example.jobportalapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.jobportalapp.Model.Job;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PostJobActivity extends AppCompatActivity {

    FloatingActionButton fabBtn;
    RecyclerView recyclerView ;
    Toolbar toolbar;

    //FireBase
    FirebaseAuth mAuth;
    DatabaseReference JobPostDatabase;
    FirebaseRecyclerAdapter<Job,MyViewHolder> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_job);

        toolbar = findViewById(R.id.toolbar_post_job);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Post Job");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        fabBtn = findViewById(R.id.fab_add);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser mUser = mAuth.getCurrentUser();
        String uId = mUser.getUid();

        JobPostDatabase = FirebaseDatabase.getInstance().getReference().child("Job Post").child(uId);

        recyclerView = findViewById(R.id.recycler_job_post_id);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        fetch();

        fabBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),InsertJobActivity.class));
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    private void fetch(){
        FirebaseRecyclerOptions<Job> options =
                new FirebaseRecyclerOptions.Builder<Job>()
                        .setQuery(JobPostDatabase, new SnapshotParser<Job>() {
                            @NonNull
                            @Override
                            public Job parseSnapshot(@NonNull DataSnapshot snapshot) {
                                return new Job(
                                        snapshot.child("title").getValue().toString(),
                                        snapshot.child("description").getValue().toString(),
                                        snapshot.child("skills").getValue().toString(),
                                        snapshot.child("salary").getValue().toString(),
                                        snapshot.child("id").getValue().toString(),
                                        snapshot.child("date").getValue().toString());
                            }
                        }).build();

        adapter = new FirebaseRecyclerAdapter<Job, MyViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i, @NonNull Job job) {
                myViewHolder.setJobTitle(job.getTitle());
                myViewHolder.setJobDate(job.getDate());
                myViewHolder.setJobDescription(job.getDescription());
                myViewHolder.setJobSkills(job.getSkills());
                myViewHolder.setJobSalary(job.getSalary());
            }

            @NonNull
            @Override
            public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                LayoutInflater inflater = LayoutInflater.from(parent.getContext());
                View view = inflater.inflate(R.layout.job_post_item,parent,false);
                return new MyViewHolder(view);
            }
        };
        recyclerView.setAdapter(adapter);

    }

    public  static class MyViewHolder extends RecyclerView.ViewHolder{
        View myView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            myView = itemView;
        }
        public  void setJobTitle(String title){
            TextView mTitle = myView.findViewById(R.id.job_tile_post_item);
            mTitle.setText(title);

        }

        public  void setJobDate(String date){
            TextView mDate = myView.findViewById(R.id.job_post_date_item);
            mDate.setText(date);

        }

        public  void setJobDescription(String description){
            TextView mDescription = myView.findViewById(R.id.job_description_post_item);
            mDescription.setText(description);

        }

        public  void setJobSkills(String skills){
            TextView mSkills = myView.findViewById(R.id.job_skill_post_item);
            mSkills.setText(skills);

        }

        public  void setJobSalary(String salary){
            TextView mSalary = myView.findViewById(R.id.job_salary_post_item);
            mSalary.setText(salary);

        }
    }
}
