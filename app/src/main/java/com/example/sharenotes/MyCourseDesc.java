package com.example.sharenotes;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class MyCourseDesc extends AppCompatActivity {

    TextView courseId,courseFac,courseDesc,uploaderName;
    ImageButton downloadButton;
    String cID,cDesc,cFac,url,uploader;
    String filename;
    String destinationDirectory;

    private FirebaseStorage mStorage;
    private DatabaseReference mDatabaseRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_course_desc);

        courseId = (TextView) findViewById(R.id.course_id);
        courseDesc = (TextView) findViewById(R.id.course_desc);
        courseFac = (TextView) findViewById(R.id.course_fac);


        Intent intent = getIntent();
        cID = intent.getStringExtra("id");
        cDesc = intent.getStringExtra("name");
        cFac = intent.getStringExtra("faculty");
        url = intent.getStringExtra("url");
        uploader = FirebaseAuth.getInstance().getCurrentUser().getUid();
        destinationDirectory = "/storage/emulated/0/Download";

        courseId.setText(cID);
        courseDesc.setText(cDesc);
        courseFac.setText(cFac);

        mStorage = FirebaseStorage.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Members/"+uploader+"/Courses/"+cID);
    }

    public void delete(View view){
        StorageReference imageRef = mStorage.getReferenceFromUrl(url);

        imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                mDatabaseRef.removeValue();
                Toast.makeText(getApplicationContext(), "Item deleted", Toast.LENGTH_SHORT).show();

            }
        });
        Intent intent = new Intent(this,MainHomeScreen.class);
        startActivity(intent);
    }

    public void downloadPdf(View view){
        filename = System.currentTimeMillis()+"";
        downloadFile(getApplicationContext(),filename,url);
    }

    public void downloadFile(Context context, String fileName, String url) {
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));

        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI |
                DownloadManager.Request.NETWORK_MOBILE);
        request.setTitle("Download");
        request.setDescription("Downloading file...");
        request.allowScanningByMediaScanner();

        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,""+System.currentTimeMillis()+".pdf");

        DownloadManager manager =(DownloadManager)getSystemService(Context.DOWNLOAD_SERVICE);
        manager.enqueue(request);
    }

}
