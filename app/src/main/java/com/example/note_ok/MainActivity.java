package com.example.note_ok;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private EditText mloginemail,mloginpassword;
    private RelativeLayout mlogin,mgotosignup;
    private TextView mgotoforgotpassword;

    ProgressBar mprogressbarofmainactivity;
    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();

        mloginemail=findViewById(R.id.loginemail);
        mloginpassword=findViewById(R.id.loginpassword);
        mlogin=findViewById(R.id.login);
        mgotosignup=findViewById(R.id.gotosignup);
        mgotoforgotpassword=findViewById(R.id.gotoforgotpassword);
        mprogressbarofmainactivity=findViewById(R.id.progressbarofmainactivity);

        firebaseAuth=FirebaseAuth.getInstance();
        FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();
        if(firebaseUser!=null){
            finish();
            startActivity(new Intent(MainActivity.this,notesActivity.class));
        }

        mgotosignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,signup.class));
            }
        });

        mgotoforgotpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,forgotpassword.class));
            }
        });

        mlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mail=mloginemail.getText().toString().trim();
                String password=mloginpassword.getText().toString().trim();
                if(mail.isEmpty()  || password.isEmpty()){
                    Toast.makeText(getApplicationContext(),"All Fields are Required",Toast.LENGTH_SHORT).show();
                }
                else{
                    mprogressbarofmainactivity.setVisibility(View.VISIBLE);
                    firebaseAuth.signInWithEmailAndPassword(mail,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                checkmailverification();
                            }
                            else{
                                Toast.makeText(getApplicationContext(),"Account Doesn't Exist",Toast.LENGTH_SHORT).show();
                                mprogressbarofmainactivity.setVisibility(View.INVISIBLE);
                            }
                        }
                    });

                }
            }
        });
    }
    private void checkmailverification(){
        FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();
        if(firebaseUser.isEmailVerified()==true){
            Toast.makeText(getApplicationContext(),"Log In Successfull",Toast.LENGTH_SHORT).show();
            finish();
            startActivity(new Intent(MainActivity.this,notesActivity.class));
        }
        else{
            mprogressbarofmainactivity.setVisibility(View.INVISIBLE);
            Toast.makeText(getApplicationContext(),"Verify Your Mail First",Toast.LENGTH_SHORT).show();
            firebaseAuth.signOut();
        }
    }
}