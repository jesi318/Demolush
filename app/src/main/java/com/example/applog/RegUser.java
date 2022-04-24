package com.example.applog;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;

public class RegUser extends AppCompatActivity implements View.OnClickListener {
    private TextView banner, reguserr;
    private EditText editTextFirstName, editTextLastName,editTextemail, editTextpasswd;
    private ProgressBar progressBar;
    private FirebaseAuth nAuth;
    FirebaseDatabase database;
    DatabaseReference myRef;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg_user);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        nAuth = FirebaseAuth.getInstance();

        banner = (TextView) findViewById(R.id.banner);
        banner.setOnClickListener(this);

        reguserr = (Button) findViewById(R.id.regusernew);
        reguserr.setOnClickListener(this);

        editTextFirstName = (EditText) findViewById(R.id.Firstname);
        editTextLastName = (EditText) findViewById(R.id.Lastname);
        editTextemail = (EditText) findViewById(R.id.emailaddr);

        progressBar = (ProgressBar) findViewById(R.id.progbarreg);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Users");

        myRef.setValue("Hello, World!");

    }

    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.regusernew:
                Registeruser();
                break;

            case R.id.banner:
                 startActivity(new Intent(this,MainActivity.class));
                 break;
        }

    }

    public void Registeruser(){
        String email = editTextemail.getText().toString().trim();
        String fname = editTextFirstName.getText().toString().trim();
        String lname = editTextLastName.getText().toString().trim();
        String pwd = editTextpasswd.getText().toString().trim();



        if(fname.isEmpty())
        {
            editTextFirstName.setError("First Name required!");
            editTextFirstName.requestFocus();
            return;
        }

        if(lname.isEmpty())
        {
            editTextLastName.setError("Last name required!");
            editTextLastName.requestFocus();
            return;
        }

        if(email.isEmpty())
        {
            editTextemail.setError("Please enter email id!");
            editTextemail.requestFocus();
            return;
        }

        if(pwd.isEmpty())
        {
            editTextpasswd.setError("Password Required!");
            editTextpasswd.requestFocus();
            return;
        }

        if(pwd.length() < 6)
        {
            editTextpasswd.setError("Password too short!");
            editTextpasswd.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        nAuth.createUserWithEmailAndPassword(email,pwd)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {


                        if(task.isSuccessful())
                        {
                            Toast.makeText(RegUser.this,"suuccessfull",Toast.LENGTH_LONG);

                            User user = new User(fname,lname,email);
                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if(task.isSuccessful())
                                    {

                                        View toastView = toast.getView();
                                        toastView.setBackgroundResource(R.drawable.toast_drawable);
                                        toast.show();
                                        progressBar.setVisibility(View.VISIBLE);

                                    }

                                    else
                                    {
                                        Toast toast = Toast.makeText(getApplicationContext(), "User  not created", Toast.LENGTH_LONG);

                                        View toastView = toast.getView();
                                        toastView.setBackgroundResource(R.drawable.toast_drawable);
                                        toast.show();
                                        progressBar.setVisibility(View.VISIBLE);
                                    }
                                }
                            });


                        }

                        else
                        {
                            Toast toast = Toast.makeText(getApplicationContext(), "Not Registered!", Toast.LENGTH_LONG);

                            View toastView = toast.getView();
                            toastView.setBackgroundResource(R.drawable.toast_drawable);
                            toast.show();
                        }
                    }
                });



    }
}