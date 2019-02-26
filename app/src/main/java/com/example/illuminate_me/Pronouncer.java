package com.example.illuminate_me;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.nuance.speechkit.AudioPlayer;
import com.nuance.speechkit.Session;
import com.nuance.speechkit.Transaction;


import com.nuance.speechkit.Audio;
import com.nuance.speechkit.Language;
import com.nuance.speechkit.TransactionException;
import com.nuance.speechkit.Voice;

public class Pronouncer extends AppCompatActivity implements View.OnClickListener, AudioPlayer.Listener {
    private Session speechSession;
    private Transaction ttsTransaction;
    private State state = State.IDLE;
    private String voice ="Tarik";
    private Button toggleTTS;
    private EditText ttsText;
private  String tts;

//(1)
    /*   public Clip provideVerbalDescription(String text){

}*/
public Pronouncer(String tts){
    this.tts=tts;

toggleTTS();
    setState(State.IDLE);
}
    public void setTts(String tts) {
        this.tts = tts;
    }

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_tts);

       ttsText = (EditText)findViewById(R.id.txtview1);
       // language = (EditText)findViewById(R.id.language);
       // language.setText(Configuration.LANGUAGE_CODE);

      //  logs = (TextView)findViewById(R.id.logs);
      //  clearLogs = (Button)findViewById(R.id.clear_logs);
      //  clearLogs.setOnClickListener(this);

        toggleTTS = (Button)findViewById(R.id.toggletts);
        toggleTTS.setOnClickListener(this);

        //Create a session
        speechSession = Session.Factory.session(this, Configuration.SERVER_URI, Configuration.APP_KEY);
        speechSession.getAudioPlayer().setListener(this);

        setState(State.IDLE);
    }
    @Override
    public void onClick(View v) {
        if(v == toggleTTS) {
            toggleTTS();
        }
    }
    /* TTS transactions */

    private void toggleTTS() {
        switch (state) {
            case IDLE:
                //If we are not loading TTS from the server, then we should do so.
                if(ttsTransaction == null) {
               //     toggleTTS.setText(getResources().getString(R.string.cancel));
                   // synthesize();
                }
                //Otherwise lets attempt to cancel that transaction
                else {
                    cancel();
                }
                break;
            case PLAYING:
                speechSession.getAudioPlayer().pause();
                setState(State.PAUSED);
                break;
            case PAUSED:
                speechSession.getAudioPlayer().play();
                setState(State.PLAYING);
                break;
        }
    }

    public void synthesize() {
        //Setup our TTS transaction options.
        Transaction.Options options = new Transaction.Options();
        options.setLanguage(new Language(Configuration.LANGUAGE));

        options.setVoice(new Voice(voice)); //optionally change the Voice of the speaker, but will use the default if omitted.
//System.out.print(ttsText.getText().toString());
        //Start a TTS transaction
        ttsTransaction = speechSession.speakString(tts, options, new Transaction.Listener() {
        //ttsTransaction = speechSession.speakString("hello my name is lulu", options, new Transaction.Listener() {
            @Override
            public void onAudio(Transaction transaction, Audio audio) {
             //   logs.append("\nonAudio");

                //The TTS audio has returned from the server, and has begun auto-playing.
                ttsTransaction = null;
            //    toggleTTS.setText(getResources().getString(R.string.speak_string));
            }

            @Override
            public void onSuccess(Transaction transaction, String s) {
           //     logs.append("\nonSuccess");

                //Notification of a successful transaction. Nothing to do here.
            }

            @Override
            public void onError(Transaction transaction, String s, TransactionException e) {
             //   logs.append("\nonError: " + e.getMessage() + ". " + s);

                //Something went wrong. Check Configuration.java to ensure that your settings are correct.
                //The user could also be offline, so be sure to handle this case appropriately.

                ttsTransaction = null;
            }
        });
    }

    /**
     * Cancel the TTS transaction.
     * This will only cancel if we have not received the audio from the server yet.
     */
    private void cancel() {
        ttsTransaction.cancel();
    }

    @Override
    public void onBeginPlaying(AudioPlayer audioPlayer, Audio audio) {
    //    logs.append("\nonBeginPlaying");

        //The TTS Audio will begin playing.

        setState(State.PLAYING);
    }

    @Override
    public void onFinishedPlaying(AudioPlayer audioPlayer, Audio audio) {
      //  logs.append("\nonFinishedPlaying");

        //The TTS Audio has finished playing

        setState(State.IDLE);
    }


    private enum State {
        IDLE,
        PLAYING,
        PAUSED
    }

    /**
     * Set the state and update the button text.
     */
    private void setState(State newState) {
        state = newState;
        switch (newState) {
            case IDLE:
                // Next possible action is speaking
             //   toggleTTS.setText(getResources().getString(R.string.speak_string));
                break;
            case PLAYING:
                // Next possible action is pausing
               // toggleTTS.setText(getResources().getString(R.string.pause));
                break;
            case PAUSED:
                // Next possible action is resuming the speech
               // toggleTTS.setText(getResources().getString(R.string.speak_string));
                break;
        }
    }


}
