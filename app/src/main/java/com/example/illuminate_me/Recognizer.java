package com.example.illuminate_me;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.services.vision.v1.Vision;
import com.google.api.services.vision.v1.VisionRequestInitializer;
import com.google.api.services.vision.v1.model.AnnotateImageRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesResponse;
import com.google.api.services.vision.v1.model.ColorInfo;
import com.google.api.services.vision.v1.model.DominantColorsAnnotation;
import com.google.api.services.vision.v1.model.EntityAnnotation;
import com.google.api.services.vision.v1.model.FaceAnnotation;
import com.google.api.services.vision.v1.model.Feature;
import com.google.api.services.vision.v1.model.Image;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Recognizer {

    //needed attributes
    static final int REQUEST_GALLERY_IMAGE = 10;
    static final int REQUEST_PERMISSIONS = 13;
    private final String LOG_TAG = "MainActivity";

    private String finalResult;

    private static ArrayList<ColorName> initColorList() {
        ArrayList<ColorName> colorList = new ArrayList<ColorName>();
        colorList.add(new ColorName("Black", 0x00, 0x00, 0x00));/////
        colorList.add(new ColorName("Blue", 0x00, 0x00, 0xFF));///
        colorList.add(new ColorName("Brown", 0xA5, 0x2A, 0x2A));///
        colorList.add(new ColorName("DarkBlue", 0x00, 0x00, 0x8B));//
        colorList.add(new ColorName("DarkGray", 0xA9, 0xA9, 0xA9));///
        colorList.add(new ColorName("DarkGreen", 0x00, 0x64, 0x00));//
        colorList.add(new ColorName("DarkOrange", 0xFF, 0x8C, 0x00));//
        colorList.add(new ColorName("DarkRed", 0x8B, 0x00, 0x00));//
        colorList.add(new ColorName("Gold", 0xFF, 0xD7, 0x00));///
        colorList.add(new ColorName("Gray", 0x80, 0x80, 0x80));///
        colorList.add(new ColorName("Green", 0x00, 0x80, 0x00));///
        colorList.add(new ColorName("GreenYellow", 0xAD, 0xFF, 0x2F));///
        colorList.add(new ColorName("Ivory", 0xFF, 0xFF, 0xF0));////
        colorList.add(new ColorName("LightBlue", 0xAD, 0xD8, 0xE6));///
        colorList.add(new ColorName("LightGray", 0xD3, 0xD3, 0xD3));//
        colorList.add(new ColorName("LightGreen", 0x90, 0xEE, 0x90));//
        colorList.add(new ColorName("LightYellow", 0xFF, 0xFF, 0xE0));//
        colorList.add(new ColorName("Maroon", 0x80, 0x00, 0x00));///
        colorList.add(new ColorName("Navy", 0x00, 0x00, 0x80));//
        colorList.add(new ColorName("Orange", 0xFF, 0xA5, 0x00));//
        colorList.add(new ColorName("OrangeRed", 0xFF, 0x45, 0x00));//
        colorList.add(new ColorName("Pink", 0xFF, 0xC0, 0xCB));///
        colorList.add(new ColorName("Purple", 0x80, 0x00, 0x80));///
        colorList.add(new ColorName("Red", 0xFF, 0x00, 0x00));///
        colorList.add(new ColorName("Silver", 0xC0, 0xC0, 0xC0));///
        colorList.add(new ColorName("Violet", 0xEE, 0x82, 0xEE));///
        colorList.add(new ColorName("White", 0xFF, 0xFF, 0xFF));///
        colorList.add(new ColorName("Yellow", 0xFF, 0xFF, 0x00));///
        return colorList; }
    //"paper"
    private String [] excludeTextLabels = {"text","line","font","calligraphy","word","clip art","handwriting","witting","document","number"};
    private String[] receivedLabels = new String[5] ;


//(1)

    @SuppressLint("StaticFieldLeak")
    public void callCloudVision(final Bitmap bitmap) throws IOException {
       // resultTextView.setText("Retrieving results from cloud");


     new AsyncTask<Object, Void, String>() {
            @Override
            protected String doInBackground(Object... params) {

                try {
                    Vision.Builder visionBuilder = new Vision.Builder(new NetHttpTransport(),
                            new AndroidJsonFactory(), null);
                    visionBuilder.setVisionRequestInitializer( new VisionRequestInitializer(
                            "AIzaSyB7O3vQ1cNtJ_NAeVPW7S08OtzD6wiOWzk"));
                    Vision vision = visionBuilder.build();

                    List<Feature> featureList = new ArrayList<>();
                    Feature labelDetection = new Feature();
                    labelDetection.setType("LABEL_DETECTION");
                    labelDetection.setMaxResults(5);
                    featureList.add(labelDetection);

                    Feature textDetection = new Feature();
                    textDetection.setType("DOCUMENT_TEXT_DETECTION");
                    textDetection.setMaxResults(10);
                    featureList.add(textDetection);

                    Feature facesDetection = new Feature();
                    facesDetection.setType("FACE_DETECTION");
                    facesDetection.setMaxResults(5);
                    featureList.add(facesDetection);

                    Feature colorDetection = new Feature();
                    colorDetection.setType("IMAGE_PROPERTIES");
                    colorDetection.setMaxResults(1);
                    featureList.add(colorDetection);

                    List<AnnotateImageRequest> imageList = new ArrayList<>();
                    AnnotateImageRequest annotateImageRequest = new AnnotateImageRequest();
                    Image base64EncodedImage = getBase64EncodedJpeg(bitmap);
                    annotateImageRequest.setImage(base64EncodedImage);
                    annotateImageRequest.setFeatures(featureList);
                    imageList.add(annotateImageRequest);

                    BatchAnnotateImagesRequest batchAnnotateImagesRequest =
                            new BatchAnnotateImagesRequest();
                    batchAnnotateImagesRequest.setRequests(imageList);

                    Vision.Images.Annotate annotateRequest =
                            vision.images().annotate(batchAnnotateImagesRequest);
                    // Due to a bug: requests to Vision API containing large images fail when GZipped.
                    annotateRequest.setDisableGZipContent(true);
                    Log.d(LOG_TAG, "sending request");

                    BatchAnnotateImagesResponse response = annotateRequest.execute();
                    return convertResponseToString(response);
                }

                catch (GoogleJsonResponseException e) {
                    Log.e(LOG_TAG, "Request failed: " + e.getContent());
                } catch (IOException e) {
                    Log.d(LOG_TAG, "Request failed: " + e.getMessage());
                }

                return "Cloud Vision API request failed.";
            }

            protected void onPostExecute(String result) {
              finalResult =result;
               // resultTextView.setText(result+"\n");
            }
        }.execute();
    }//end callcloudvision

    private String convertResponseToString(BatchAnnotateImagesResponse response) {
        StringBuilder message = new StringBuilder("Results:\n");
        StringBuilder TextOCR = new StringBuilder("");
        StringBuilder Textcolors = new StringBuilder("");
        StringBuilder TextfacialExpressions = new StringBuilder("");
        StringBuilder Textlabel = new StringBuilder("");


        //detect Text (OCR)
        List<EntityAnnotation> texts = response.getResponses().get(0).getTextAnnotations();
        List<EntityAnnotation> logos = response.getResponses().get(0).getLogoAnnotations();
        TextOCR = detectText(texts,logos);

        //detect Facial Expressions
        List<FaceAnnotation> faces = response.getResponses().get(0).getFaceAnnotations();
        TextfacialExpressions = detectFacialExpression(faces);

        // get Labels
        List<EntityAnnotation> labels = response.getResponses().get(0).getLabelAnnotations();
        Textlabel= detectLabels(labels);

        //colors
      DominantColorsAnnotation colors  = response.getResponses().get(0).getImagePropertiesAnnotation().getDominantColors();
      Textcolors = detectColors(colors);

        //HERE COMBINED RECEIVED STRING
        if(!(Textlabel.toString().equals("Written Text:")))
        message.append(Textcolors+" ");
        message.append(Textlabel+" ");
        message.append(TextfacialExpressions+" ");
        message.append(TextOCR+" ");
        return message.toString();
    }// end tostring

    public Bitmap resizeBitmap(Bitmap bitmap) {
        int maxDimension = 1024;
        int originalWidth = bitmap.getWidth();
        int originalHeight = bitmap.getHeight();
        int resizedWidth = maxDimension;
        int resizedHeight = maxDimension;

        if (originalHeight > originalWidth) {
            resizedHeight = maxDimension;
            resizedWidth = (int) (resizedHeight * (float) originalWidth / (float) originalHeight);
        } else if (originalWidth > originalHeight) {
            resizedWidth = maxDimension;
            resizedHeight = (int) (resizedWidth * (float) originalHeight / (float) originalWidth);
        } else if (originalHeight == originalWidth) {
            resizedHeight = maxDimension;
            resizedWidth = maxDimension;
        }
        return Bitmap.createScaledBitmap(bitmap, resizedWidth, resizedHeight, false);
    }

    public Image getBase64EncodedJpeg(Bitmap bitmap) {
        Image image = new Image();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, byteArrayOutputStream);
        byte[] imageBytes = byteArrayOutputStream.toByteArray();
        image.encodeContent(imageBytes);
        return image;
    }

    public static String getColorNameFromRgb(int r, int g, int b) {
        ArrayList<ColorName> colorList = initColorList();
        ColorName closestMatch = null;
        int minMSE = Integer.MAX_VALUE;
        int mse;
        for (ColorName c : colorList) {
            mse = c.computeMSE(r, g, b);
            if (mse < minMSE) {
                minMSE = mse;
                closestMatch = c;
            }
        }
        if (closestMatch != null) {
            return closestMatch.getName();
        } else {
            return "No matched color name.";
        }
    }// end getcolor
    public static String getColorNameFromHex(int hexColor) {
        int r = (hexColor & 0xFF0000) >> 16;
        int g = (hexColor & 0xFF00) >> 8;
        int b = (hexColor & 0xFF);
        return getColorNameFromRgb(r, g, b);
    }// end getcolorhex

    //(2) detect Text
    public StringBuilder detectText( List<EntityAnnotation>texts,List<EntityAnnotation> logos ){
        StringBuilder detectedText = new StringBuilder("");
        StringBuilder logo1 = new StringBuilder();
        String ocrtext="",from="",Logo="";
        if (logos != null) {
            for (EntityAnnotation logo : logos) {
                Logo = String.format(Locale.getDefault(), "%.3f: %s", logos.get(0).getLocale(), logo.getDescription());
                Logo = Logo.substring(4);
                logo1.append(Logo); } }
                else { logo1.append(""); }
        if (texts != null) {
            ocrtext= texts.get(0).getDescription();
            from=  texts.get(0).getLocale();
            if (logos != null){
                ocrtext = ocrtext + "from" + Logo + " company"; }
            ocrtext.toLowerCase();
            ocrtext=ocrtext.replaceAll("[\r\n]+", " ");
            detectedText.append(ocrtext); }
        else {
            detectedText.append(""); }
            return detectedText;  }// end detectText

    //(3) detect facial Expressions
    public StringBuilder detectFacialExpression( List<FaceAnnotation> faces){
        StringBuilder facialExpressions = new StringBuilder("");
        String person = "";
        int numberofpersons=0;
        if (faces != null) {
        for (FaceAnnotation face: faces) {
         numberofpersons++;
         String joy=  String.format(face.getJoyLikelihood());
         if (joy.equals("VERY_LIKELY")||joy.equals("LIKELY")||joy.equals("POSSIBLE"))
         person="a happy person";
         String sorrow=  String.format(face.getSorrowLikelihood());
         if (sorrow.equals("VERY_LIKELY")||sorrow.equals("LIKELY")||sorrow.equals("POSSIBLE"))
             person="a sad person";
             String anger =String.format(face.getAngerLikelihood());
         if (anger.equals("VERY_LIKELY")||anger.equals("LIKELY")||anger.equals("POSSIBLE"))
               person="a sad person";
               String surprise =String.format(face.getSurpriseLikelihood());
         if (surprise.equals("VERY_LIKELY")||surprise.equals("LIKELY")||surprise.equals("POSSIBLE"))
                person="a surprised person";
         if (numberofpersons > 1 )
               facialExpressions.append(" and \n"+person);
         else facialExpressions.append(person); } }

         else { facialExpressions.append(""); }
         return facialExpressions; }// end detectFacialExpressions

    //(4) detectLabels
    public StringBuilder detectLabels(List<EntityAnnotation> labels){

    StringBuilder Textlabel = new StringBuilder("");
        if (labels != null) {
       //this loop will add all labels received from vision API to receivedLabels array
        int i=0;
         for (EntityAnnotation label : labels){
         receivedLabels[i]=(String.format( label.getDescription()));
         i++;}}
        else { Textlabel.append(""); }
        return Textlabel; }// end detectLabels

    //(4.A) detectLabels
    public String getLabel(String []labels){
        String label="";
        String firstLabel="";
        firstLabel= labels[0].toLowerCase();
        for (int i=0;i<3;i++) {
        label= labels[i].toLowerCase();
        for (int k = 0; k < excludeTextLabels.length; k++) {
        if (label.equals(excludeTextLabels[k])) {
            label="Written Text:";
       return label; }}// end for exclude
        }// end for labels
     return firstLabel; }//end method getLabels

    //(5) detectColors
    public StringBuilder detectColors(DominantColorsAnnotation colors ){
   StringBuilder Textcolors = new StringBuilder ("");
   for (ColorInfo color : colors.getColors()) {
       Textcolors.append("" +getColorNameFromRgb((int)Math.round(color.getColor().getRed()),(int)Math.round(color.getColor().getGreen()),(int)Math.round(color.getColor().getBlue())));
        break;}
        return Textcolors; }

    //(6) generate Description
    public String getDescription (){
       return finalResult;
    }

}//end class


class ColorName {
    public int r, g, b;
    public String name;

    public ColorName(String name, int r, int g, int b) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.name = name;
    }

    public int computeMSE(int pixR, int pixG, int pixB) {
        return (int) (((pixR - r) * (pixR - r) + (pixG - g) * (pixG - g) + (pixB - b)
                * (pixB - b)) / 3);
    }

    public int getR() {
        return r;
    }

    public int getG() {
        return g;
    }

    public int getB() {
        return b;
    }

    public String getName() {
        return name;
    }
}// end class color


