package com.example.illuminate_me;

import android.content.ClipData;

import android.content.ClipData;
//import android.support.v7.app.ActionBarActivity;
import android.content.res.Resources;
import android.icu.text.UnicodeSetIterator;
import android.view.View;
//import com.microsoft.speech.tts.Synthesizer;
//import com.microsoft.speech.tts.Voice;


public class ImageDescription {

    private String description;
    public String translatedDescription = "";
  //  private Clip  verbalDescription;


    public void setDescription(String description){

        description=description;
    }

    public void setTranslatedDescription(String description){

        translatedDescription=description;
    }

    public String getTranslatedDescription(){

       return  translatedDescription;
    }

    public String getDescription(){

        return  description;
    }


}
