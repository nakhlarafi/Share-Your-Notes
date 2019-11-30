package com.example.sharenotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.lang.reflect.Member;

public class MainActivity extends AppCompatActivity {
    EditText emailText,passText;
    TextView signIn;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    Members member;
    //Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        firebaseAuth = FirebaseAuth.getInstance();
        emailText = (EditText) findViewById(R.id.editText);
        passText = (EditText) findViewById(R.id.editText2);
        signIn = (TextView) findViewById(R.id.sign_in);
        member = new Members();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Members");

    }

    public void signUp(View view){
        String email = emailText.getText().toString();
        String pass = passText.getText().toString();
        if (email.isEmpty()){
            emailText.setError("Please enter an email");
            emailText.requestFocus();
        }
        else if (pass.isEmpty()){
            passText.setError("Please enter your password");
            passText.requestFocus();
        }
        else if(email.isEmpty() && pass.isEmpty()){
            Toast.makeText(getApplicationContext(),"Fields are empty",Toast.LENGTH_SHORT).show();
        }
        else if(!(email.isEmpty() && pass.isEmpty())){
            firebaseAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (!task.isSuccessful()){
                        Toast.makeText(MainActivity.this,"Unsuccessful!",Toast.LENGTH_SHORT).show();
                    }
                    else {
                        /*String email = emailText.getText().toString();
                        member.setEmail(email);
                        databaseReference.push().setValue(member);*/
                        Toast.makeText(getApplicationContext(),"yes",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(MainActivity.this,HomeActivity.class));
                    }
                }
            });
        }
        else {
            Toast.makeText(MainActivity.this,"Error!",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void logIn(View view){
        Intent intent = new Intent(MainActivity.this,LoginActivity.class);
        startActivity(intent);
    }

}
