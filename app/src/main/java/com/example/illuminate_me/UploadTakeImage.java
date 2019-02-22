package com.example.illuminate_me;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import static android.os.Environment.getExternalStoragePublicDirectory;
//startttt
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

public class UploadTakeImage extends AppCompatActivity {
    private float x1, x2, y1, y2;
    private ImageView image;
    private TextView txtView;
    private Uri selectedImage;
    private Uri photoUri;
    private Bitmap takenPicture;
    private String pathToFile;
    private Recognizer recognizer = new Recognizer();
    private Translator translator = new Translator();
    private Pronouncer pronouncer = new Pronouncer();
    private ImageDescription imageDescription = new ImageDescription();
    private EnglishToTagalog ett;
    UserImage userImage = new UserImage();
    // we should replace the selected and taken with only one attribute
//startttt
    private final String LOG_TAG = "MainActivity";
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //to make it cover the entire screen,
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // getSupportActionBar().hide();

        setContentView(R.layout.activity_upload_take_image);
        image = findViewById(R.id.imageToUpload);
        txtView = findViewById(R.id.txtview1);


        // Permissions :
        if (Build.VERSION.SDK_INT >= 23)
            requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);


        // repeatDescription =(Button) findViewById(R.id.repeatDescription);
        // TO PEN THE GALLARY
        if (MainActivity.chosenButton == MainActivity.UPLOAD_PIC) {
            uploadPicture();
        } else {
            takePicture();
        }
    }
    //to allow user select image from the gallary
    public void uploadPicture() {

        Intent gallaryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(gallaryIntent, MainActivity.chosenButton);

    }

    public void takePicture() {

        // intent to open camera
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;

            photoFile = createImageFile();


            if (photoFile != null) {
                pathToFile = photoFile.getAbsolutePath();

                photoUri = FileProvider.getUriForFile(this, "com.example.android.fileprovider", photoFile);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);

                startActivityForResult(cameraIntent, MainActivity.chosenButton);

            }
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override


    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //to upload picture

        if (requestCode == MainActivity.UPLOAD_PIC && resultCode == RESULT_OK && data != null) {
            selectedImage = data.getData(); // This is URI need to convert to Bitmap

            if (selectedImage == null) {
                txtView.setText("selected Image is empty");
            } else {
                try {
                    // userImage.setImageUri(selectedImage);
                    // userImage.setImageBit(recognizer.resizeBitmap(userImage.getImageBit()));
                    // recognizer.callCloudVision(userImage.getImageBit());
                    userImage.setImageUri(selectedImage);
                    takenPicture = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                    userImage.setImageBit(recognizer.resizeBitmap(takenPicture));
                    image.setImageBitmap(takenPicture);
                    // recognizer.callCloudVision(userImage.getImageBit());
                    callCloudVision(userImage.getImageBit());
                } catch (IOException e) {
                    e.printStackTrace();
                }
           /* imageDescription.setDescription(recognizer.getDescription());
            ett =  new EnglishToTagalog("en",imageDescription.getDescription());
            ett.doInBackground();
            ett.translated();
            imageDescription.setTranslatedDescription(ett.getMsg());
            txtView.setText(imageDescription.getTranslatedDescription());
            pronouncer.synthesize(imageDescription.getTranslatedDescription());*/
            }
        }

        // allow user to take a picture
        if (requestCode == MainActivity.TAKE_PIC && resultCode == RESULT_OK) {
            takenPicture = BitmapFactory.decodeFile(pathToFile); // This is Bitmap

            if (takenPicture == null) {
                txtView.setText("taken picture is empty");
                // image.setImageBitmap(takenPicture);
            } else {
                try {
                    //   userImage.setImageBit(recognizer.resizeBitmap(userImage.getImageBit()));
                    //recognizer.callCloudVision(userImage.getImageBit());
                    image.setImageBitmap(takenPicture);
                    userImage.setImageBit(resizeBitmap(takenPicture));
                    // recognizer.callCloudVision(userImage.getImageBit());
                    callCloudVision(userImage.getImageBit());
                    //    imageDescription.setDescription(recognizer.getDescription());
                    String des = (String) txtView.getText();

                } catch (IOException e) {
                    e.printStackTrace();
                }
                //imageDescription.setDescription(recognizer.getDescription());
           /* ett =  new EnglishToTagalog("en",imageDescription.getDescription());
            ett.doInBackground();
            ett.translated();
            imageDescription.setTranslatedDescription(ett.getMsg());*/
                //txtView.setText(imageDescription.getDescription());
                //pronouncer.synthesize(imageDescription.getTranslatedDescription());

            }

        }

    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {


        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x1 = event.getX();
                y1 = event.getY();
                break;
            case MotionEvent.ACTION_UP:
                x2 = event.getX();
                y2 = event.getY();
                if (x1 < x2) {
                    // swipe right
                    // go to previous screen
                    Intent intent = new Intent(UploadTakeImage.this, MainActivity.class);
                    startActivity(intent);
                }


                if (x1 > x2) {
                    // swipe left
                    // share menu
                    Intent intent = new Intent(UploadTakeImage.this, Share.class);
                    startActivity(intent);
                }
                break;

        }//end switch
        //if swipe left to right

        return super.onTouchEvent(event);
    }

    public Uri getSelectedImage() {
        return selectedImage;
    }

    public Bitmap getTakenPicture() {
        return takenPicture;
    }

    public Drawable getImageView() {
        return image.getDrawable();

    }

    private File createImageFile() {
        // Create an image file name
        @SuppressLint("SimpleDateFormat") String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";

        // getExternalFilesDir(Environment.DIRECTORY_PICTURES); < this will make the img available in our app only
        File storageDir = getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

        File Fimage = null;

        try {
            Fimage = File.createTempFile(imageFileName,  /* prefix */
                    ".jpg",         /* suffix */
                    storageDir      /* directory */);

        } catch (Exception e) {
            Log.d("mylog", "ExceptionF : " + e.toString());
        }
        return Fimage;
    }

//start
@SuppressLint("StaticFieldLeak")
private void callCloudVision(final Bitmap bitmap) throws IOException {
    txtView.setText("Retrieving results from cloud");

    new AsyncTask<Object, Void, String>() {
        @Override
        protected String doInBackground(Object... params) {

            try {
                Vision.Builder visionBuilder = new Vision.Builder(new NetHttpTransport(),
                        new AndroidJsonFactory(), null);
                visionBuilder.setVisionRequestInitializer(new VisionRequestInitializer(
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
            } catch (GoogleJsonResponseException e) {
                Log.e(LOG_TAG, "Request failed: " + e.getContent());
            } catch (IOException e) {
                Log.d(LOG_TAG, "Request failed: " + e.getMessage());
            }

            return "Cloud Vision API request failed.";
        }

        protected void onPostExecute(String result) {

            txtView.setText(result + "\n");

        }
    }.execute();
} //end callcloudvision
    private String convertResponseToString(BatchAnnotateImagesResponse response) {
        StringBuilder message = new StringBuilder("Results:\n");
        StringBuilder TextOCR = new StringBuilder("");
        StringBuilder TextfacialExpressions = new StringBuilder("");
        StringBuilder Textlabel = new StringBuilder("");
        StringBuilder Textcolors = new StringBuilder("");
        StringBuilder logo1 = new StringBuilder();
        String Logo ="",ocrtext="", from="";

        String l1 ="";
        List<EntityAnnotation> logos = response.getResponses().get(0)
                .getLogoAnnotations();
        if (logos != null) {
            for (EntityAnnotation logo : logos) {
                Logo = String.format(Locale.getDefault(), "%.3f: %s", logos.get(0).getLocale(), logo.getDescription());
                Logo = Logo.substring(4);
                logo1.append(Logo);
            }
        } else {
            logo1.append("");
        }
        // "Text"
        List<EntityAnnotation> texts = response.getResponses().get(0).getTextAnnotations();
        if (texts != null) {
            ocrtext= texts.get(0).getDescription();
            from=  texts.get(0).getLocale();
            if (logos != null){
                ocrtext = ocrtext + "from" + Logo + " company";
            }
            ocrtext.toLowerCase();
            ocrtext=ocrtext.replaceAll("[\r\n]+", " ");

            TextOCR.append(ocrtext); }
        else {
            TextOCR.append(""); }

        //FACE START
        String person = "";
        int numberofpersons=0;
        List<FaceAnnotation> faces = response.getResponses().get(0)
                .getFaceAnnotations();
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
                    TextfacialExpressions.append(" and \n"+person);
                else TextfacialExpressions.append(person);
            }

        } else {
            message.append("");
        }
        List<EntityAnnotation> labels = response.getResponses().get(0).getLabelAnnotations();
        if (labels != null) {
            //this loop will add all labels received from vision API to receivedLabels array
            int i=0;
            for (EntityAnnotation label : labels){
                receivedLabels[i]=(String.format( label.getDescription()));
                i++; }
            //send receivedLabels to getLabel method that will exclude some labels inorder to get accurate result
            if(faces==null)
            { Textlabel.append(getLabel(receivedLabels));}
        } else {
            Textlabel.append(""); }

        //colors
        // message.append("\n"+"COLORS:\n");
        DominantColorsAnnotation colors  = response.getResponses().get(0).getImagePropertiesAnnotation().getDominantColors();
        for (ColorInfo color : colors.getColors()) {
            Textcolors.append(
                    "" +getColorNameFromRgb((int)Math.round(color.getColor().getRed()),(int)Math.round(color.getColor().getGreen()),(int)Math.round(color.getColor().getBlue())));
            break;}

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
        return firstLabel;
    }//end method getLabels
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

}//end callcloudvision
