package com.example.illuminate_me;

import android.app.ActionBar;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class Share extends AppCompatActivity {

    private float x1,x2,y1,y2;
    private ImageButton inst ;
    private ImageButton whats ;
    private ImageButton twitter ;
    private SwipeDetector sd ;
    private Bitmap photoBit ;
    private Uri photoUri ;
    private String photoPath ;
    private UploadTakeImage uti ;
    private MediaPlayer tone, instruction, instasound, twittersound, whatssound, mainsound, prevsound,sharesound, savesound ;

private static int instg =0 , wts=0 , twit=0 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        setbtnviews ();
        photoPath = uti.getCurrentPhotoPath() ;
        photoBit = BitmapFactory.decodeFile(photoPath);
        photoUri = getImageUri(this, photoBit) ;


// Swiping :
        sd = new SwipeDetector(this, new SwipeDetector.OnSwipeListener() {


            @Override
            public void onSwipeUp(float distance, float velocity) {

                // Nothing
            }

            @Override
            public void onSwipeRight(float distance, float velocity) {
                // previous
                prevsound = MediaPlayer.create(Share.this, R.raw.prev);
                prevsound.start();

                Intent intent = new Intent(Share.this, MainActivity.class);
                startActivity(intent);
            }

            @Override
            public void onSwipeLeft(float distance, float velocity) {
                //Nothing
            }

            @Override
            public void onSwipeDown(float distance, float velocity) {
                // Home page
                mainsound = MediaPlayer.create(Share.this, R.raw.mainvoice);
                mainsound.start();

                Intent intent = new Intent(Share.this, MainActivity.class);
                startActivity(intent) ;
            }
        });


        //  btn listeners
        inst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (instg==0) {
                    instg++;
                    instasound = MediaPlayer.create(Share.this, R.raw.instavoice);
                    instasound.start();

                }else{
                // Instagram share
                shareInstagram();}
            }
         });

        whats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(wts==0) {
                    wts++;
                    whatssound = MediaPlayer.create(Share.this, R.raw.whatsvoice);
                    whatssound.start();
              }
                // WhatsApp share
                else{
                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("image/jpeg");
                share.putExtra(Intent.EXTRA_STREAM, photoUri);
                share.setPackage("com.whatsapp");
                startActivity(Intent.createChooser(share, "Share Image"));}
            }
        });


        twitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Twitter share
                if(twit==0){
                    twit++;
                    twittersound = MediaPlayer.create(Share.this, R.raw.twittervoice);
               twittersound.start();}else{

                shareTwitter("Test Share");}
            }

        });
    }




    private void shareInstagram () {
        Intent intent = getPackageManager().getLaunchIntentForPackage("com.instagram.android");
        if (intent != null)
        {
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.setPackage("com.instagram.android");

            try {
                shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(), photoPath, "img", "Identified image")));
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            shareIntent.setType("image/jpeg");

            startActivity(shareIntent);
        }
        else
        {
            // bring user to the market to download the app.
            // or let them choose an app?
            intent = new Intent(Intent.ACTION_VIEW);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setData(Uri.parse("market://details?id="+"com.instagram.android"));
            startActivity(intent);
        }
    }
    private void shareTwitter(String message ) {
        Intent tweetIntent = new Intent(Intent.ACTION_SEND);
      //  tweetIntent.putExtra(Intent.EXTRA_TEXT, "This is a Test.");
      //  tweetIntent.setType("text/plain");
        if (photoUri != null) {
            tweetIntent.putExtra(Intent.EXTRA_STREAM, photoUri);
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
          Intent  intent = new Intent(Intent.ACTION_VIEW);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setData(Uri.parse("market://details?id="+"com.twitter.android"));
            startActivity(intent);}
    }


    public boolean onTouchEvent(MotionEvent event) {
        return sd.onTouch(null, event);
    }


    private void setbtnviews (){
        inst = findViewById(R.id.btn_inst);
        whats = findViewById(R.id.btn_whats);
        twitter = findViewById(R.id.btn_twitter);

    }


    public Uri getImageUri(Context context, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }



    }
