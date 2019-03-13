package com.example.illuminate_me;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class logoScreen extends AppCompatActivity {

    private MediaPlayer instruction ;
    SharedPreferences prefs = null;
    Editor editor;

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


        // to not go back to this screen
        logoLauncher logo = new logoLauncher();
       logo.start();


    }// end onCreate


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


/*
    @Override
    protected void onResume() {
        super.onResume();


        if (prefs.getBoolean("firstrun", true)) {
            instruction = MediaPlayer.create(logoScreen.this, R.raw.instruction1);
            instruction.start();
            // Do first run stuff here then set 'firstrun' as false
            // using the following line to edit/commit prefs
            prefs.edit().putBoolean("firstrun", false).commit();
        }
    }

*/

    // this class purpose is to make the logo screen appears for a number of seconds before showing the main screen

    private class logoLauncher extends Thread {


        public void run (){
            try {
                // to make the logo appear for 2 seconds
                sleep(2000);
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
