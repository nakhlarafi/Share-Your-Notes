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

public class DownloadPageActivity extends AppCompatActivity {
    TextView courseId,courseFac,courseDesc,uploaderName;
    ImageButton downloadButton;
    String cID,cDesc,cFac,url,uploader;
    String destinationDirectory;
    String filename;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        destinationDirectory = "/storage/emulated/0/Download";
        setContentView(R.layout.activity_download_page);
        courseId = (TextView) findViewById(R.id.course_id);
        courseDesc = (TextView) findViewById(R.id.course_desc);
        courseFac = (TextView) findViewById(R.id.course_fac);
        uploaderName = (TextView)findViewById(R.id.uploader_name);
        Intent intent = getIntent();
        cID = intent.getStringExtra("courseid");
        cDesc = intent.getStringExtra("coursedesc");
        cFac = intent.getStringExtra("coursefac");
        url = intent.getStringExtra("url");
        uploader = intent.getStringExtra("uploader");
        courseId.setText(cID);
        courseDesc.setText(cDesc);
        courseFac.setText(cFac);
        uploaderName.setText(uploader);

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
