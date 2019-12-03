package com.example.sharenotes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ChooseOptions extends AppCompatActivity {
    Button shareButton,requestButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_options);
        shareButton = (Button) findViewById(R.id.share_button);
        requestButton = (Button) findViewById(R.id.request_button);
    }

    public void goToShare(View view){
        Intent intent = new Intent(this,ShareActivity.class);
        startActivity(intent);
    }

    public void goToRequest(View view){
        Intent intent = new Intent(this,RequestActivity.class);
        startActivity(intent);
    }
}
