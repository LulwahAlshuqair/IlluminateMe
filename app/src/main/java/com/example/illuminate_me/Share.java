package com.example.illuminate_me;

import android.app.ActionBar;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Share extends AppCompatActivity {

    private float x1,x2,y1,y2;
    private ImageButton inst ;
    private ImageButton whats ;
    private ImageButton twitter ;
    private UploadTakeImage uti ;
    private SwipeDetector sd ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        setbtnviews ();

        // Swiping :
        sd = new SwipeDetector(this, new SwipeDetector.OnSwipeListener() {


            @Override
            public void onSwipeRight(float distance, float velocity) {
                // previous
                Intent intent = new Intent(Share.this, UploadTakeImage.class);
                startActivity(intent);
            }


            @Override
            public void onSwipeDown(float distance, float velocity) {
                // Home page
                Intent intent = new Intent(Share.this, MainActivity.class);
                startActivity(intent) ;
            }
        });

        //  btn listeners
        inst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Instagram share
/*
                Bitmap img = retriveImg() ;
                Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
                shareIntent.setType("image/*");

                final ContentResolver cr = getContentResolver();
                final String[] p1 = new String[] {
                        MediaStore.Images.ImageColumns._ID, MediaStore.Images.ImageColumns.TITLE, MediaStore.Images.ImageColumns.DATE_TAKEN
                };
                Cursor c1 = cr.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, p1, null, null, p1[1] + " DESC");


                shareIntent.putExtra(Intent.EXTRA_STREAM, img);
                shareIntent.setPackage("com.instagram.android");

                c1.close();

          */  }
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



    private void shareTwitter(String message , Uri Shimg) {
        //UploadTakeImage img = new UploadTakeImage();
       // Uri Shimg =  img.getSelectedImage();

        /*
        if (Shimg==null) {
            Toast.makeText(this, "Twitter app isn't found", Toast.LENGTH_LONG).show();
        }
        Intent tweetIntent = new Intent(Intent.ACTION_SEND);
        tweetIntent.putExtra(Intent.EXTRA_TEXT, "This is a Test.");
        tweetIntent.setType("text/plain");
        if (Shimg != null) {
            tweetIntent.putExtra(Intent.EXTRA_STREAM, Shimg);
            tweetIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            tweetIntent.setType("image/*");
        }
        PackageManager packManager = getPackageManager();
        List<ResolveInfo> resolvedInfoList = packManager.queryIntentActivities(tweetIntent, PackageManager.MATCH_DEFAULT_ONLY);

        boolean resolved = false;
        for (ResolveInfo resolveInfo : resolvedInfoList) {
            if (resolveInfo.activityInfo.packageName.startsWith("com.twitter.android")) {
                tweetIntent.setClassName(
                        resolveInfo.activityInfo.packageName,
                        resolveInfo.activityInfo.name);
                resolved = true;
                break;
            }
        }
        if (resolved) {
            startActivity(tweetIntent);
        } else {
            Intent i = new Intent();
            i.putExtra(Intent.EXTRA_STREAM, Shimg);
            i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            i.setType("image/*");
            i.putExtra(Intent.EXTRA_TEXT, message );
            i.setAction(Intent.ACTION_VIEW);
            i.setData(Uri.parse("https://twitter.com/intent/tweet?text=" + urlEncode(message)));
            startActivity(i);
            Toast.makeText(this, "Twitter app isn't found", Toast.LENGTH_LONG).show();
        }
    */}

    private String urlEncode(String s) {
        try {
            return URLEncoder.encode(s, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            // Log.wtf();
            //TAG, "UTF-8 should always be supported", e);
            return "";
        }}


        public Bitmap retriveImg () {
        // To get the image path:
            String fname = uti.getImageFileName() ;
            String path = "/storage/emulated/0/Pictures/" + fname ;
            Bitmap bitmap = BitmapFactory.decodeFile(path);
            return bitmap ;

    }


    public boolean onTouchEvent(MotionEvent event) {
        return sd.onTouch(null, event);
    }


    private void setbtnviews (){
        inst = findViewById(R.id.btn_inst);
        whats = findViewById(R.id.btn_whats);
        twitter = findViewById(R.id.btn_twitter);

    }
    }
