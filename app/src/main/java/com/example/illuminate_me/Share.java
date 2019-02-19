package com.example.illuminate_me;

import android.app.ActionBar;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import java.io.File;

public class Share extends AppCompatActivity {

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

    private void setbtnviews (){

        inst = findViewById(R.id.btn_inst);
        whats = findViewById(R.id.btn_whats);
        twitter = findViewById(R.id.btn_twitter);
    }



    }
