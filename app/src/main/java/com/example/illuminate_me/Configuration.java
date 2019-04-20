package com.example.illuminate_me;

import android.net.Uri;

import com.nuance.speechkit.PcmFormat;


public class Configuration {

    //All fields are required.
    //Your credentials can be found in your Nuance Developers portal, under "Manage My Apps".
    public static final String APP_KEY = "ffb983e685596ff1d2bda2a442ce5458f31b97ff12ad5592d516274ea172f3a02b7687b1cff6a2a2b34446f6129a4e05b5e504c792595a687081b611f3a9b21e";
    public static final String APP_ID = "NMDPTRIAL_lulubn1997_gmail_com20190203132217";
    public static final String SERVER_HOST = "sslsandbox-nmdp.nuancemobility.net" ;
    public static final String SERVER_PORT = "443";

    public static final String LANGUAGE = "ara-XWW";

    public static final Uri SERVER_URI = Uri.parse("nmsps://" + APP_ID + "@" + SERVER_HOST + ":" + SERVER_PORT);

    //Only needed if using NLU
    public static final String CONTEXT_TAG = "!NLU_CONTEXT_TAG!";

    public static final PcmFormat PCM_FORMAT = new PcmFormat(PcmFormat.SampleFormat.SignedLinear16, 16000, 1);
    public static final String LANGUAGE_CODE = (Configuration.LANGUAGE.contains("!") ? "ara-XWW" : Configuration.LANGUAGE);

}



