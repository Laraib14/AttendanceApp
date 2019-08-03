package com.example.attendanceappstd;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
EditText mETEmail, mETPassword;
TextView mTVSignup,mTVForgetPassword;
Button mBtnLogin;
FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        auth=FirebaseAuth.getInstance();

        if (auth.getCurrentUser()!=null){
            startActivity(new Intent(MainActivity.this,SignUpActivity.class));
            finish();
        }
        mETEmail=findViewById(R.id.et_email);
        mETPassword=findViewById(R.id.et_password);
        mTVSignup=findViewById(R.id.tv_signup);
        mTVForgetPassword=findViewById(R.id.tv_forget_password);
        mBtnLogin=findViewById(R.id.btn_login);



        mBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               String mStrEmail=mETEmail.getText().toString();
               String mStrPassword=mETPassword.getText().toString();

                if (TextUtils.isEmpty(mStrEmail)) {
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(mStrPassword)) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }

                auth.signInWithEmailAndPassword(mStrEmail, mStrPassword)
                        .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {

                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful())
                                {
                                    Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
                                    startActivity(intent);
                                    finish();

                                    Toast.makeText(MainActivity.this,"Login Successful",Toast.LENGTH_LONG).show();
                                }

                                else {
                                    Toast.makeText(MainActivity.this, "Login Failed: "+task.getException(), Toast.LENGTH_LONG).show();
                                }
                            }
                        });


            }
        });
    }
}
