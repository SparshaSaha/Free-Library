package com.fourthstatelab.free_library;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;


public class Signup_activity extends AppCompatActivity {
    EditText name,email,password,re_password;
    Button signup;
    private FirebaseAuth firebaseauth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_activity);

        name=(EditText)findViewById(R.id.name);
        email=(EditText)findViewById(R.id.emailid);
        password=(EditText)findViewById(R.id.pass_word);
        signup=(Button)findViewById(R.id.sign_up_button);
        re_password=(EditText)findViewById(R.id.reenterpassword);

        firebaseauth=FirebaseAuth.getInstance();

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean re_password_match_valid=false;
                if(password.getText().toString().equals(re_password.getText().toString()))
                    re_password_match_valid=true;
                if(re_password_match_valid==false)
                    Toast.makeText(Signup_activity.this, "Both the passwords must match", Toast.LENGTH_SHORT).show();
                if(re_password_match_valid==true)
                {
                    create_user();//create user with email and password
                }

            }
        });



    }

    public void create_user()
    {
        firebaseauth.createUserWithEmailAndPassword(email.getText().toString(),password.getText().toString()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    Toast.makeText(Signup_activity.this, "Successfully Registered", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Signup_activity.this,My_Books_and_projects.class));
                }
                else {
                    try {
                        throw task.getException();

                    }catch(FirebaseAuthWeakPasswordException e)
                    {
                        Toast.makeText(Signup_activity.this,"Password used is too weak", Toast.LENGTH_SHORT).show();
                        password.setText("");
                    }
                    catch(FirebaseAuthUserCollisionException e)
                    {
                        Toast.makeText(Signup_activity.this, "Email has already been taken", Toast.LENGTH_SHORT).show();
                        email.setText("");
                        password.setText("");
                    }
                    catch(Exception e)
                    {
                        Log.e("other_exception",e.getMessage());
                    }
                }

            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(Signup_activity.this,Start_Page.class));
        finish();
        super.onBackPressed();
    }
}
