package com.example.illuminate_me;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

public class logoScreen extends AppCompatActivity {

    private MediaPlayer instruction , nohi , hi ;
    SharedPreferences prefs = null;
    Editor editor;
    private ImageView imageView;
    private static int count =0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        prefs = getSharedPreferences("com.example.illuminate_me", MODE_PRIVATE);

        //to remove top bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //to make it cover the entire screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //this is part of the onCreate method
        setContentView(R.layout.activity_logo_screen);
        // to hide the action bar
        //getSupportActionBar().hide();

        //To Start Hi for the first time open
        if (count <1){
            hi = MediaPlayer.create(logoScreen.this, R.raw.hi);
            hi.start();
            count++;
        }

        //To repeat Instruction
        imageView = findViewById(R.id.imageView);
        imageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                nohi = MediaPlayer.create(logoScreen.this, R.raw.nohi);
                nohi.start();
                return false;
            }
        });

        //To not go back to this screen
        logoLauncher logo = new logoLauncher();
        logo.start();


    }// end onCreate

    //To start instruction for the first time use

    @Override
    protected void onResume() {
        super.onResume();

        if (prefs.getBoolean("firstrun", true)) {
            //You can perform anything over here. This will call only first time
            instruction = MediaPlayer.create(logoScreen.this, R.raw.instruction1);
            instruction.start();
            editor = prefs.edit();
            editor.putBoolean("firstrun", false);
            editor.commit();

        }
    }


    // this class purpose is to make the logo screen appears for a number of seconds before showing the main screen

    private class logoLauncher extends Thread {


        public void run (){
            try {
                // to make the logo appear for 3 seconds
                sleep(3000);
            }
            catch (InterruptedException e){
                e.printStackTrace();
            }
            // to move from this activity to the main activity (main screen)
            Intent intent = new Intent (logoScreen.this, MainActivity.class);
            startActivity(intent);
            // to not go back to this screen
            logoScreen.this.finish();
        }// end run method

    } // end private class

}
