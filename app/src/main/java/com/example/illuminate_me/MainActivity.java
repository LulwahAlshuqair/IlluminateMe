package com.example.illuminate_me;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    //to be changed
    public static int chosenButton;
    public static final int UPLOAD_PIC =1;
    public static final int TAKE_PIC =2;
   private  Button takePic;
   private Button uploadPic;
    private Button pronouncer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //to remove top bar
       // getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        //initiate buttons
        setbtnviews ();
        //button listeners
        //(1)
        takePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              chosenButton=TAKE_PIC;
                //call user.takePic or write the code directly
                Intent intent = new Intent (MainActivity.this, UploadTakeImage.class);
                startActivity(intent);
            }
        });
        //(2)
        uploadPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chosenButton=UPLOAD_PIC;
                //call user.uploadPic or write the code directly
                Intent intent = new Intent (MainActivity.this, UploadTakeImage.class);
                startActivity(intent);
            }
        });


    ImageDescription img = new ImageDescription();
        pronouncer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent (MainActivity.this, Pronouncer.class);
                startActivity(intent);
            }
        });
    }

    private void setbtnviews (){

        takePic = findViewById(R.id.takePic_btn);
        uploadPic = findViewById(R.id.uploadPic_btn);
        pronouncer = findViewById(R.id.pronouncer);
    }

}
