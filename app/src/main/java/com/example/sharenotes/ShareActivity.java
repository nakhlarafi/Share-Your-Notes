package com.example.sharenotes;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.drm.DrmStore;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageException;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class ShareActivity extends AppCompatActivity {
    public String userId;
    private String directory;
    String currentDateandTime;
    FirebaseStorage storage;
    FirebaseDatabase database;
    ProgressDialog progressDialog;
    Uri pdfUri;

    TextView slectedFiletext;
    EditText courseId,courseName,facultyName;
    //private String directory2 = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/myCamera/";
    private int STORAGE_PERMISSION_CODE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        userId = MainHomeScreen.userId;

        courseId = (EditText) findViewById(R.id.course_id_edit_text);
        courseName = (EditText) findViewById(R.id.course_desc_edit_text);
        facultyName = (EditText) findViewById(R.id.course_faculty_edit_text);

        storage = FirebaseStorage.getInstance(); //used for uploading files
        database = FirebaseDatabase.getInstance();//used to store uploaded files
        slectedFiletext = (TextView) findViewById(R.id.selected_file_name);

        ActivityCompat.requestPermissions(this,new String[]{WRITE_EXTERNAL_STORAGE},
                PackageManager.PERMISSION_GRANTED);

    }
    Document document = new Document();

    //V2
    public void convertButton(){

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
        currentDateandTime = sdf.format(new Date());

        String directoryPath = android.os.Environment.getExternalStorageDirectory().toString();
        String path = new File(directory).getParent();

        try {
            PdfWriter.getInstance(document, new FileOutputStream("/storage/emulated/0/DCIM" + "/"+userId+currentDateandTime +"example.pdf")); //  Change pdf's name.
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        document.open();

        Image image = null;  // Change image's name and extension.
        try {
            image = Image.getInstance(directory);
        } catch (BadElementException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            float scaler = ((document.getPageSize().getWidth() - document.leftMargin()
                    - document.rightMargin() - 0) / image.getWidth()) * 100; // 0 means you have no indentation. If you have any, change it.
            image.scalePercent(scaler);
            image.setAlignment(Image.ALIGN_CENTER|Image.ALIGN_MIDDLE );

        }catch (Exception e){
            Toast.makeText(this,"Something went wrong",Toast.LENGTH_SHORT).show();
        }

        try {
            document.add(image);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        document.close();
    }




    public void pickImage(View view){
        if (ContextCompat.checkSelfPermission(ShareActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(ShareActivity.this, "You have already granted this permission!",
                    Toast.LENGTH_SHORT).show();
        } else {
            requestStoragePermission();
        }
        /*Intent  intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent,120);*/
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
        intent.setType("image/*");
        startActivityForResult(intent,120);
    }


    private void requestStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)) {

            new AlertDialog.Builder(this)
                    .setTitle("Permission needed")
                    .setMessage("This permission is needed because of this and that")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(ShareActivity.this,
                                    new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create().show();

        } else {
            ActivityCompat.requestPermissions(this,
                    new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission GRANTED", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public String getRealPathForOnePhoto(Uri contentURI){
        String result;
        Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }

    private String getRealPathFromURI(Uri contentURI) {
        /*String result;
        Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }

        return result;*/
        String filePath = "";
        String wholeID = DocumentsContract.getDocumentId(contentURI);

        // Split at colon, use second item in the array
        String id = wholeID.split(":")[1];

        String[] column = { MediaStore.Images.Media.DATA };

        // where id is equal to
        String sel = MediaStore.Images.Media._ID + "=?";

        Cursor cursor = getApplicationContext().getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                column, sel, new String[]{ id }, null);

        int columnIndex = cursor.getColumnIndex(column[0]);

        if (cursor.moveToFirst()) {
            filePath = cursor.getString(columnIndex);
        }
        cursor.close();
        return filePath;

    }


    /*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 120 && resultCode == RESULT_OK && data!=null){

            Uri selectedImageURI = data.getData();
            directory = getRealPathFromURI(selectedImageURI);

            //directory = data.getData().getPath();
            convertButton();
            /*File files = new File(uri.getPath());//create path from uri
            final String[] split = files.getPath().split(":");//split the path.
            //filePath = split[1];
            //String[] filepath = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedImageUri,split,null,null,null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(split[0]);
            //directory = cursor.getString(columnIndex);
            cursor.close();
            convertButton();

            /*Bitmap bitmap = BitmapFactory.decodeFile(mypath);

            PdfDocument pdfDocument = new PdfDocument();
            PdfDocument.PageInfo pi =  new PdfDocument.PageInfo
                    .Builder(bitmap.getWidth(),bitmap.getHeight(),1)
                    .create();
            PdfDocument.Page page = pdfDocument.startPage(pi);
            Canvas canvas = page.getCanvas();
            Paint paint = new Paint();
            paint.setColor(Color.parseColor("#FFFFFF"));
            canvas.drawPaint(paint);

            bitmap = Bitmap.createScaledBitmap(bitmap,bitmap.getWidth(),bitmap.getHeight(),true);
            paint.setColor(Color.BLUE);
            canvas.drawBitmap(bitmap,0,0,null);

            pdfDocument.finishPage(page);

            //save the bitmap image

            File root = new File(Environment.getExternalStorageDirectory(),"PDF Folder 12");
            if (!root.exists()){
                root.mkdir();
            }
            File file = new File(root,"picture.pdf");
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                pdfDocument.writeTo(fileOutputStream);
            } catch (IOException e){
                e.printStackTrace();
            }
            pdfDocument.close();*/
        //}
    //}

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 86 && resultCode == RESULT_OK && data!=null){
            System.out.println("************upload dibar aise");
            pdfUri = data.getData();
            slectedFiletext.setText("A file is selected: "+data.getData().getLastPathSegment());
        }

        else if (requestCode == 120 && resultCode == RESULT_OK) {
            Uri selectedImageURI;
            System.out.println("aise edike");
            //If Single image selected then it will fetch from Gallery
            if (data.getData() != null) {

                selectedImageURI = data.getData();
                directory = getRealPathFromURI(selectedImageURI);
                convertButton();
                System.out.println(directory);

            } else {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
                currentDateandTime = sdf.format(new Date());
                List<Bitmap> bitmaps = new ArrayList<>();
                ClipData clipData = data.getClipData();
                if (clipData != null) {
                    try {
                        PdfWriter.getInstance(document, new FileOutputStream("/storage/emulated/0/DCIM" + "/"+userId+currentDateandTime +"example.pdf")); //  Change pdf's name.
                    } catch (DocumentException e) {
                        e.printStackTrace();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                    document.open();
                    Image image = null;

                    for (int i = 0; i < clipData.getItemCount(); ++i) {
                        selectedImageURI = clipData.getItemAt(i).getUri();
                        //Uri selectedImageURI = data.getData();
                        //System.out.println(selectedImageURI);
                        directory = getRealPathFromURI(selectedImageURI);
                        System.out.println(directory);
                        String path = new File(directory).getParent();
                        //convertButton();

                        try {
                            image = Image.getInstance(directory);
                        } catch (BadElementException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        float scaler = ((document.getPageSize().getWidth() - document.leftMargin()
                                - document.rightMargin() - 0) / image.getWidth()) * 100; // 0 means you have no indentation. If you have any, change it.
                        image.scalePercent(scaler);
                        image.setAlignment(Image.ALIGN_CENTER|Image.ALIGN_MIDDLE );

                        try {
                            document.add(image);
                        } catch (DocumentException e) {
                            e.printStackTrace();
                        }







                        try {
                            InputStream is = getContentResolver().openInputStream(selectedImageURI);
                            Bitmap bitmap = BitmapFactory.decodeStream(is);
                            bitmaps.add(bitmap);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    Uri imageUri = data.getData();
                    try {
                        InputStream is = getContentResolver().openInputStream(imageUri);
                        Bitmap bitmap = BitmapFactory.decodeStream(is);
                        bitmaps.add(bitmap);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                document.close();
            }
        }
    }

    /**
     * to offer user to select file
     * Intent will be used
     * @param view
     */
    public void selectingPdf(View view){
        if (ContextCompat.checkSelfPermission(ShareActivity.this,Manifest.permission.READ_EXTERNAL_STORAGE)
                ==PackageManager.PERMISSION_GRANTED){

            Intent intent = new Intent();
            intent.setType("application/pdf");
            intent.setAction(Intent.ACTION_GET_CONTENT); //to fetch files
            startActivityForResult(intent,86);

        }
        else {
            requestStoragePermission();
        }
    }

    /**
     * Uploads the file when the button is pressed.
     * @param view
     */
    public void uploadFile(View view){
        if (pdfUri != null){
            progressDialog = new ProgressDialog(this);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setTitle("Uploading...");
            progressDialog.setProgress(0);
            progressDialog.show();
            final String fileName = System.currentTimeMillis()+"";
            StorageReference storageReference = storage.getReference(); //returns path
            storageReference.child("Upload").child(fileName).putFile(pdfUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            String url = taskSnapshot.getMetadata().getReference().getDownloadUrl().toString(); //returns the url of uploaded file
                            //DatabaseReference reference = database.getReference(); //returns the path of the root
                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Members")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                            final String courseNameStr = courseId.getText().toString();
                            final DatabaseReference table =  reference.child("Courses/"+courseNameStr);
                            table.child("url").setValue(url).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        progressDialog.dismiss();
                                        DatabaseReference courseRef = table;
                                        courseRef.child("course_name").setValue(courseName.getText().toString());
                                        courseRef.child("faculty").setValue(facultyName.getText().toString());
                                        Toast.makeText(getApplicationContext(),"File Uploaded",Toast.LENGTH_SHORT).show();
                                    }
                                    else {
                                        Toast.makeText(getApplicationContext(),"Not Uploaded",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(),"File Not Uploaded",Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    //track the progress of upload
                    int currentProgress = (int) (100*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                    progressDialog.setProgress(currentProgress);
                }
            });
        }
        else {
            Toast.makeText(getApplicationContext(),"Please select a pdf file",Toast.LENGTH_SHORT).show();
        }
    }

}
