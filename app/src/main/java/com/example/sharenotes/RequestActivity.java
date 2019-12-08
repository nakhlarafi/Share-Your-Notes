package com.example.sharenotes;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

public class RequestActivity extends AppCompatActivity {

    EditText courseIdText,courseDescText;
    String courseAll,cId,cDesc,currentUserId,currentUserEmail;

    FirebaseStorage storage;
    FirebaseDatabase database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);
        courseDescText = (EditText) findViewById(R.id.course_desc_info);
        courseIdText = (EditText) findViewById(R.id.course_id_info);
        System.out.println(courseAll);
        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        currentUserEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();

        // Users Email



    }

    /**
     * Requesting a Course PDF
     * @param view
     */
    public void doRequest(View view){
        cId = courseIdText.getText().toString();
        cDesc = courseDescText.getText().toString();
        courseAll = cId+"\n"+cDesc;
        System.out.println(courseAll);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Requests")
                .child(currentUserId);
        reference.push().setValue(courseAll);
        Toast.makeText(getApplicationContext(),"Added",Toast.LENGTH_SHORT).show();
    }
}
