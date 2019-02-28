package com.example.illuminate_me;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;
import android.graphics.Typeface;

public class MainActivity extends AppCompatActivity {

    //to be changed
    public static int chosenButton;
    public static final int UPLOAD_PIC =1;
    public static final int TAKE_PIC =2;
   private  Button takePic;
   private Button uploadPic;

    ///private Button pronouncer;
EnglishToTagalog ett ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //to remove top bar
       // getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

      Typeface cusFont = Typeface.createFromAsset(getAssets(),"fonts/coconnextarabic-light.ttf");

        //initiate buttons
        setbtnviews ();

        // Permissions :
        if (Build.VERSION.SDK_INT >= 23)
            requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);

        //Setting fonts
        takePic.setTypeface(cusFont);
        uploadPic.setTypeface(cusFont);
        takePic.setTextSize(50);
        uploadPic.setTextSize(50);



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

        //Toast.makeText(this, msg, 1);

    //ImageDescription img = new ImageDescription();
      /*  pronouncer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent (MainActivity.this, Pronouncer.class);
                startActivity(intent);
            }
        });*/
    }

    private void setbtnviews(){

        takePic = findViewById(R.id.takePic_btn);
        uploadPic = findViewById(R.id.uploadPic_btn);
    }

}
