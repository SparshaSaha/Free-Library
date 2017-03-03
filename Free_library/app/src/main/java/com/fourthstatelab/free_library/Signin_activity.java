package com.fourthstatelab.free_library;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;

public class Signin_activity extends AppCompatActivity {
    TextView forgotpassword;
    Button signin;
    EditText emailid,password;
    CheckBox keep_logged_in;
    private FirebaseAuth firebaseauth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin_activity);
        forgotpassword=(TextView)findViewById(R.id.forgotpassword);
        signin=(Button)findViewById(R.id.loginbutton);
        emailid=(EditText) findViewById(R.id.emailid);
        password=(EditText)findViewById(R.id.password);
        keep_logged_in=(CheckBox)findViewById(R.id.keep_logged_in);
        firebaseauth=FirebaseAuth.getInstance();




        forgotpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Signin_activity.this, "Forgot Password", Toast.LENGTH_SHORT).show();
            }
        });




        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(emailid.getText().length()!=0 && password.getText().length()!=0)
                {
                    sign_user_in();
                }
            }
        });


    }

    public void sign_user_in() //Used to sign in user
    {
        firebaseauth.signInWithEmailAndPassword(emailid.getText().toString(),password.getText().toString()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    Toast.makeText(Signin_activity.this, "Sign in successful", Toast.LENGTH_SHORT).show();

                    if(keep_logged_in.isChecked())
                    {
                        Shared_Preferences_class.set_keep_logged_in(true,getApplicationContext());
                        Login_credentials login_credentials=new Login_credentials();
                        login_credentials.email_id=emailid.getText().toString();
                        login_credentials.password=password.getText().toString();
                        Shared_Preferences_class.put_login_details(getApplicationContext(),login_credentials);
                    }

                    startActivity(new Intent(Signin_activity.this, My_Books_and_projects.class));
                    finish();
                }
                else
                {
                    try
                    {
                        throw task.getException();
                    }
                    catch(FirebaseAuthInvalidCredentialsException e)
                    {
                        Toast.makeText(Signin_activity.this, "Invalid credentials", Toast.LENGTH_SHORT).show();
                        password.setText("");
                    }
                    catch(Exception e)
                    {
                        Log.e("other_exception_sign_in",e.getMessage());
                    }
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(Signin_activity.this,Start_Page.class));
        finish();
        super.onBackPressed();
    }
}
