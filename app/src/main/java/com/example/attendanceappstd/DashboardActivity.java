package com.example.attendanceappstd;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.data.Student;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DashboardActivity extends AppCompatActivity {
TextView date;
Button buttonPresent, buttonLeave,view_all;
FirebaseAuth auth;
FirebaseDatabase database;
DatabaseReference myRef;
    String date_current;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        auth=FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Attendance");
        date_current = new SimpleDateFormat("MM-dd-yyyy", Locale.getDefault()).format(new Date());

        date=(TextView) findViewById(R.id.date);
        buttonPresent=(Button) findViewById(R.id.buttonPresent);
        buttonLeave=(Button) findViewById(R.id.buttonLeave);
        view_all=(Button) findViewById(R.id.view_all);
        date.setText(date_current);
        updateUI();
        buttonPresent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Student student = new Student();
                //student.setUserID(myRef.push().getKey());
                myRef.child(auth.getUid()).push();
                String newNotificationId = myRef.child(auth.getUid()).push().getKey();
                student.setUserID(newNotificationId);
                student.setStatus("Present");
                student.setCurrentDate(date.getText().toString());
                myRef.child(auth.getUid()).child(newNotificationId).setValue(student);
                buttonPresent.setEnabled(false);
                buttonLeave.setEnabled(false);
            }
        });

        buttonLeave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Student student = new Student();
                //student.setUserID(myRef.push().getKey());
                myRef.child(auth.getUid()).push();
                String newNotificationId = myRef.child(auth.getUid()).push().getKey();
                student.setUserID(newNotificationId);
                student.setStatus("Leave");
                student.setCurrentDate(date.getText().toString());
                myRef.child(auth.getUid()).child(newNotificationId).setValue(student);
                buttonPresent.setEnabled(false);
                buttonLeave.setEnabled(false);
            }
        });
        view_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, ShowDataActivity.class);
                startActivity(intent);
            }
        });
    }

    private void updateUI() {
        myRef.child(auth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                Log.d(">>>a",dataSnapshot+"");
//                Log.d(">>>b",dataSnapshot.toString());
                for(DataSnapshot datas: dataSnapshot.getChildren()){
                    Log.d(">>>a",dataSnapshot+"");
                    String date=datas.child("currentDate").getValue().toString();
                    Log.d(">>>",date);
                    if(date_current.equalsIgnoreCase(date)){
                        Log.d(">>>","yes");
                        buttonPresent.setEnabled(false);
                        buttonLeave.setEnabled(false);
                    }


                   /* try {
                        Date date1=new SimpleDateFormat("MM-dd-yyyy").parse(date);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }*/

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
