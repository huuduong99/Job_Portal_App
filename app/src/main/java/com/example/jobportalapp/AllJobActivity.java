package com.example.jobportalapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AllJobActivity extends AppCompatActivity {
    Toolbar toolbar;
    RecyclerView recyclerView;
    //Firebase
    private DatabaseReference mAllJobPost;
    FirebaseRecyclerAdapter<Job,AllJobPostViewHolder> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_job);

        toolbar = findViewById(R.id.toolbar_all_job_post);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("All Job Post");

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Database
        mAllJobPost = FirebaseDatabase.getInstance().getReference().child("Public database");
        mAllJobPost.keepSynced(true);

        recyclerView = findViewById(R.id.recycler_all_job_post);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        fetch();
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

    public  static class AllJobPostViewHolder extends RecyclerView.ViewHolder{
        View myView;

        public AllJobPostViewHolder(@NonNull View itemView) {
            super(itemView);
            myView = itemView;
        }
        public  void setJobTitle(String title){
            TextView mTitle = myView.findViewById(R.id.all_job_post_title);
            mTitle.setText(title);

        }

        public  void setJobDate(String date){
            TextView mDate = myView.findViewById(R.id.all_job_post_date);
            mDate.setText(date);

        }

        public  void setJobDescription(String description){
            TextView mDescription = myView.findViewById(R.id.all_job_post_description);
            mDescription.setText(description);

        }

        public  void setJobSkills(String skills){
            TextView mSkills = myView.findViewById(R.id.all_job_post_skills);
            mSkills.setText(skills);

        }

        public  void setJobSalary(String salary){
            TextView mSalary = myView.findViewById(R.id.all_job_post_salary);
            mSalary.setText(salary);

        }
    }

    private void fetch(){
        FirebaseRecyclerOptions<Job> options =
                new FirebaseRecyclerOptions.Builder<Job>()
                        .setQuery(mAllJobPost, new SnapshotParser<Job>() {
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
        adapter = new FirebaseRecyclerAdapter<Job, AllJobPostViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull AllJobPostViewHolder allJobPostViewHolder, int i, @NonNull final Job job) {
                allJobPostViewHolder.setJobTitle(job.getTitle());
                allJobPostViewHolder.setJobDate(job.getDate());
                allJobPostViewHolder.setJobDescription(job.getDescription());
                allJobPostViewHolder.setJobSkills(job.getSkills());
                allJobPostViewHolder.setJobSalary(job.getSalary());

                allJobPostViewHolder.myView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getApplicationContext(),JobDetailsActivity.class);
                        intent.putExtra("job",job);
                        startActivity(intent);
                    }
                });
            }

            @NonNull
            @Override
            public AllJobPostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                LayoutInflater inflater = LayoutInflater.from(parent.getContext());
                View view = inflater.inflate(R.layout.all_job_post,parent,false);
                return new AllJobPostViewHolder(view);
            }
        };
        recyclerView.setAdapter(adapter);

    }
}
