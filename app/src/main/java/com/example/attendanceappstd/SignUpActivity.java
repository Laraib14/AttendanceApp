package com.example.attendanceappstd;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpActivity extends AppCompatActivity {
    EditText mETEmail, mETPassword;
    TextView mTVLogin;
    Button mBtnSignUp,mBtnLogin;
    ProgressBar mProgressBar;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        auth=FirebaseAuth.getInstance();

        mETEmail=(EditText)findViewById(R.id.et_email);
        mETPassword=(EditText)findViewById(R.id.et_password);
        mProgressBar=(ProgressBar) findViewById(R.id.progress_bar);

//        mTVLogin=findViewById(R.id.tv_login);
        mBtnSignUp=(Button) findViewById(R.id.btn_signup);
        mBtnLogin=(Button) findViewById(R.id.btn_login);

        mBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
//        mTVLogin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent=new Intent(SignUpActivity.this, LoginActivity.class);
//                startActivity(intent);
//            }
//        });

        mBtnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mStrEmail,mStrPassword;

                mStrEmail=mETEmail.getText().toString().trim();
                mStrPassword=mETPassword.getText().toString().trim();

                if (TextUtils.isEmpty(mStrEmail))
                {
                    Toast.makeText(getApplicationContext(),"Enter Email", Toast.LENGTH_SHORT).show();
                }

                if (TextUtils.isEmpty(mStrPassword)) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (mStrPassword.length() < 6) {
                    Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
                    return;
                }

                mProgressBar.setVisibility(View.VISIBLE);

                auth.createUserWithEmailAndPassword(mStrEmail,mStrPassword)
                .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        mProgressBar.setVisibility(View.GONE);

                        Toast.makeText(SignUpActivity.this, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(SignUpActivity.this, DashboardActivity.class);
                        startActivity(intent);
                        finish();
                        if (!task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Error in SignUp: " + task.getException(), Toast.LENGTH_LONG).show();

                        }
                    }
                });

            }
        });


    }
}
