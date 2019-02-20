package com.example.illuminate_me;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import java.io.File;

public class Share extends AppCompatActivity {

    private float x1,x2,y1,y2;
    private ImageButton inst ;
    private ImageButton whats ;
    private ImageButton twitter ;
    private UploadTakeImage ut = new UploadTakeImage() ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);

        setbtnviews ();

        //  btn listeners
        inst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Create the new Intent using the 'Send' action.
                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("image/*");
                share.putExtra(Intent.EXTRA_STREAM, ut.getSelectedImage());
                // Broadcast the Intent.
                startActivity(Intent.createChooser(share, "Share to"));
            }
        });

        whats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // WhatsApp share
            }
        });


        twitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Twitter share
            }
        });

    }

    public boolean onTouchEvent(MotionEvent event) {

        // Problem: when it goes back it opens the "gallery" or "camera" not go back to the description
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                x1=event.getX();
                y1=event.getY();
                break;
            case MotionEvent.ACTION_UP:
                x2=event.getX();
                y2=event.getY();
                if(x1<x2){
                    // swipe right
                    // go to previous screen
                    Intent intent = new Intent (Share.this, UploadTakeImage.class);
                    startActivity(intent);
                }

                break;

        }//end switch
        return super.onTouchEvent(event);

    }
        private void setbtnviews (){

        inst = findViewById(R.id.btn_inst);
        whats = findViewById(R.id.btn_whats);
        twitter = findViewById(R.id.btn_twitter);
    }



    }
