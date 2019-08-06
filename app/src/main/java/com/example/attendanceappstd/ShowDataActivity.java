package com.example.attendanceappstd;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.data.ListingAdapter;
import com.example.data.Student;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ShowDataActivity extends AppCompatActivity {
    ListView allusers;
    ProgressDialog mProgressDialog;
FirebaseAuth auth;
FirebaseDatabase database;
DatabaseReference myRef;
    ListingAdapter adapter;
    ArrayList<Student> users=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showusers);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Attendance");
        allusers=(ListView)findViewById(R.id.allusers);
        adapter=new ListingAdapter(ShowDataActivity.this,users);
        allusers.setAdapter(adapter);
        getDataFromServer();
    }
    private void getDataFromServer() {
        showProgressDialog();
        myRef.child(auth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                Log.d(">>>a",dataSnapshot+"");
//                Log.d(">>>b",dataSnapshot.toString());
                for(DataSnapshot datas: dataSnapshot.getChildren()){
                    Student user=datas.getValue(Student.class);
                    users.add(user);
                    adapter.notifyDataSetChanged();
                }
                hideProgressDialog();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(ShowDataActivity.this);
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(true);
        }
        mProgressDialog.show();
    }
    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }
}
