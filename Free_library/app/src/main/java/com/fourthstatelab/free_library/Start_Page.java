package com.fourthstatelab.free_library;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;

public class Start_Page extends AppCompatActivity {
Button signin,signup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start__page);

        signin=(Button)findViewById(R.id.sign_in);
        signup=(Button)findViewById(R.id.sign_up);

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Shared_Preferences_class.keep_logged_in_or_not(getApplicationContext()))
                {
                    sign_user_in();
                }
                else {
                    startActivity(new Intent(Start_Page.this, Signin_activity.class));
                    finish();
                }
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Start_Page.this,Signup_activity.class));
                finish();
            }
        });

    }

    public void sign_user_in()
    {
        FirebaseAuth firebaseauth=FirebaseAuth.getInstance();
        Login_credentials login=Shared_Preferences_class.get_login_details(getApplicationContext());
        firebaseauth.signInWithEmailAndPassword(login.email_id,login.password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    Toast.makeText(Start_Page.this, "Sign in successful", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Start_Page.this, My_Books_and_projects.class));
                    finish();
                }
                else
                {
                    try
                    {
                        throw task.getException();
                    }
                    catch(FirebaseNetworkException e)
                    {
                        Toast.makeText(Start_Page.this, "Unable to connect.Please check your Internet Connection", Toast.LENGTH_LONG).show();
                    }
                    catch(Exception e)
                    {

                    }
                }

            }
        });
    }
}
