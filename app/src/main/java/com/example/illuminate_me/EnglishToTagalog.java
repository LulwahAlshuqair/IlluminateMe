package com.example.illuminate_me;


import  android.os.AsyncTask;
import  android.app.ProgressDialog;
import android.widget.Toast;


public class EnglishToTagalog extends AsyncTask<Void, Void, Void> {
    private ProgressDialog progress = null;
    GoogleTranslate translator;
    public static String API_KEY = " AIzaSyCyRnfRWsPhBvKLILxsF4A4X7Nat-vUipI";

    String from , msg ;
    public  EnglishToTagalog (String from , String msg){
        this.from = from ;
        this.msg= msg;
    }
    protected void onError(Exception ex) {

    }
    @Override
    public Void doInBackground(Void... params) {

        try {
            translator = new GoogleTranslate(API_KEY);

            Thread.sleep(1000);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;

    }
    @Override
    protected void onCancelled() {
        super.onCancelled();
    }

    @Override
    protected void onPreExecute() {
        //start the progress dialog
        //  progress = ProgressDialog.show(MainActivity.this, null, "Translating...");
        //   progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        // progress.setIndeterminate(true);
        //  super.onPreExecute();
    }
    @Override
    protected void onPostExecute(Void result) {
        // progress.dismiss();

        super.onPostExecute(result);
        translated();
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }
    public void translated(){
        String translatetotagalog = "Love you";//get the value of text
        String text = translator.translte(msg, from ,"ar");
        setMsg(text);
        //  Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
        //  Toast.makeText(getApplicationContext(), text2, Toast.LENGTH_LONG).show();

    }
    public String getMsg (){return msg;}
    public void setMsg(String m){msg=m;}
    public void setFrom (String fr){from=fr;}

}
