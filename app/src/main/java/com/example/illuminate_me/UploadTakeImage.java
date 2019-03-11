package com.example.illuminate_me;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;
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
import android.media.AudioManager;
import android.media.MediaPlayer;
import java.util.ArrayList.*;

public class UploadTakeImage extends AppCompatActivity {
    private float x1, x2, y1, y2;
    private ImageView image;
    private TextView txtView;
    private Uri selectedImage;
    private Uri photoUri;
    private  static Bitmap takenPicture;
    private String pathToFile;
    private Recognizer recognizer = new Recognizer();
    private Translator translator = new Translator();
    private ImageDescription imageDescription = new ImageDescription();
    private  String TranslatedText;
    private EnglishToTagalog ett;
    private byte[] photo ;
    private String imageFileName ;
    private SwipeDetector sd ;
    private MediaPlayer tone;
    // we should replace the selected and taken with only one attribute
    UserImage userImage = new UserImage();
    private static String currentPhotoPath;
    boolean  isPlayingAudio = true;

//startttt
    private final String LOG_TAG = "MainActivity";
    private String [] excludeTextLabels = {"text","line","font","calligraphy","word","clip art","handwriting","witting","number","ink"};
    // private String[] receivedLabels = new String[20] ;
    private ArrayList<String> receivedLabels = new ArrayList<String>();
    private String[] maleLabels = { "male","beard","facialhair","moustache","macho"};
    //"feminine" is a female label
    //cowboy//"king","prince",
    private String [] manLabel = {"man","gentleman","businessman","men", "macho man","father","guy","grandfather","old man"};
    //"queen","princess",
    private String[] womanLabel = {"woman" , "lady" , "mam","businesswoman","gentlewoman","mother","old woman", "women","grandmother"};
    private String[] childrenLabels = { "child" , "baby", "girl" , "boy" ,"baby laughing"};
    private String[] genderLabels = {"male","female"};
    // private String[] jobLabels = {"physician","pharmacy technician","chemist","scientist"};
    private String[] emotionLabels = {"smile","laugh","laughing"};
    private String[] wearingsLabels = {"white coat","coat","stethoscope","cowboy hat","sun hat","hat","dress","t-shirt","jeans","headgear","fashion accessory"};
    private String[] describeHairLabels={"blond","long hair","brown hair","black hair"};
    private String[] onLabels ={"table","desk","shelf","side table","coffee table","sofa tables","outdoor table","dresser","night stand","writing desk","computer desk","drawer","chest of drawers"};
    private String[] natureLabels = {"grassland","grass","hill","mountain","natural landscape"};
    private String [] lookingatLabels ={"sky","cloud"};
    private String[] inLabels = {"water","lake","pond","waterfowl","swimming pool","sea","raver"};
    private String [] stateLabels ={"sitting","standing","swimming","running"};
    //if "nature" private String[] timeLabels={"sunlight","evening","sunrise","sunset","morning"};
    private String[] textLabels={"street sign","sign","traffic sign","signage","book","notebook","diary","paper product","paper","product","document"};
    private String[] placesLabels={"restaurant","hospital"};
    //baharat , Indian Cuisine , Baked Goods, Dessert,"vegetarian food"
    private String[] ExcludefoodLabels ={"food","meat","dish","plate","natural foods","indian cuisine ","dessert","baked goods","superfood","plant","gluten","vegan nutrition","cruciferous vegetables","recipe","cuisine","brunch","breakfast","dinner","lunch","cooking","snack","produce","kids' meal","junk food","ingredient","sweetness","finger food","fast food","baking"};
    private String[] ExcludeTableLabels={"office","room","wood","furniture","metal","technology","floor","flooring","interior design","building","rectangle","writing office","office writing","marble","hutch","wood stain","interior design","end table","hardwood","living room","chair","solid wood","tile","tiles","iron"};

    private static ArrayList<ColorName> initColorList() {
        ArrayList<ColorName> colorList = new ArrayList<ColorName>();
        colorList.add(new ColorName("Black", 0x00, 0x00, 0x00));/////
        colorList.add(new ColorName("Blue", 0x00, 0x00, 0xFF));///
        colorList.add(new ColorName("Brown", 0xA5, 0x2A, 0x2A));///
        colorList.add(new ColorName("Dark Blue", 0x00, 0x00, 0x8B));//
        colorList.add(new ColorName("Dark Gray", 0xA9, 0xA9, 0xA9));///
        colorList.add(new ColorName("Dark Green", 0x00, 0x64, 0x00));//
        colorList.add(new ColorName("Dark Orange", 0xFF, 0x8C, 0x00));//
        colorList.add(new ColorName("Dark Red", 0x8B, 0x00, 0x00));//
        colorList.add(new ColorName("Gold", 0xFF, 0xD7, 0x00));///
        colorList.add(new ColorName("Gray", 0x80, 0x80, 0x80));///
        colorList.add(new ColorName("Green", 0x00, 0x80, 0x00));///
        colorList.add(new ColorName("Green Yellowish", 0xAD, 0xFF, 0x2F));///
        colorList.add(new ColorName("Ivory", 0xFF, 0xFF, 0xF0));////
        colorList.add(new ColorName("Light Blue", 0xAD, 0xD8, 0xE6));///
        colorList.add(new ColorName("Light Gray", 0xD3, 0xD3, 0xD3));//
        colorList.add(new ColorName("Light Green", 0x90, 0xEE, 0x90));//
        colorList.add(new ColorName("Light Yellow", 0xFF, 0xFF, 0xE0));//
        colorList.add(new ColorName("Maroon", 0x80, 0x00, 0x00));///
        colorList.add(new ColorName("Navy", 0x00, 0x00, 0x80));//
        colorList.add(new ColorName("Orange", 0xFF, 0xA5, 0x00));//
        colorList.add(new ColorName("Orange Reddish", 0xFF, 0x45, 0x00));//
        colorList.add(new ColorName("Pink", 0xFF, 0xC0, 0xCB));///
        colorList.add(new ColorName("Purple", 0x80, 0x00, 0x80));///
        colorList.add(new ColorName("Red", 0xFF, 0x00, 0x00));///
        colorList.add(new ColorName("Silver", 0xC0, 0xC0, 0xC0));///
        colorList.add(new ColorName("Violet", 0xEE, 0x82, 0xEE));///
        colorList.add(new ColorName("White", 0xFF, 0xFF, 0xFF));///
        colorList.add(new ColorName("Yellow", 0xFF, 0xFF, 0x00));///
        return colorList; }

    private  int numberofpersons=0;
    private String person ;
    private String receivedColor;
    String ocrtext="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //to make it cover the entire screen,
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // getSupportActionBar().hide();

        try {

            setContentView(R.layout.activity_upload_take_image);
            image = findViewById(R.id.imageToUpload);
            txtView = findViewById(R.id.txtview1);
            txtView.setMovementMethod(new ScrollingMovementMethod());

            Typeface cusFont = Typeface.createFromAsset(getAssets(),"fonts/coconnextarabic-light.ttf");
                //Setting fonts
            txtView.setTypeface(cusFont);

        } catch (Exception e){
            Log.d("mylog", "ExcCCCeption : " + e.toString());
        }

        // Swiping :
          sd = new SwipeDetector(this, new SwipeDetector.OnSwipeListener() {

            @Override
            public void onSwipeUp(float distance, float velocity) {
                // Save
               saveImage();
            }

            @Override
            public void onSwipeRight(float distance, float velocity) {
                // previous
                Intent intent = new Intent(UploadTakeImage.this, MainActivity.class);
                startActivity(intent);
            }

            @Override
            public void onSwipeLeft(float distance, float velocity) {
                // SHARE
                Intent intent = new Intent(UploadTakeImage.this, Share.class);
                startActivity(intent);
                finish() ;
            }

            @Override
            public void onSwipeDown(float distance, float velocity) {
                // Home page
                Intent intent = new Intent(UploadTakeImage.this, MainActivity.class);
                startActivity(intent) ;
            }
        });




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
            File photoFile = createImageFile();

            if (photoFile != null) {
                pathToFile = photoFile.getAbsolutePath();
                photoUri = FileProvider.getUriForFile(this, "com.example.android.fileprovider", photoFile);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(cameraIntent, MainActivity.chosenButton);
            }
        }

    }

    private File createImageFile() {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        imageFileName = "JPEG_" + timeStamp + "_";

        File storageDir = getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File Fimage = null;

        try {
            Fimage = File.createTempFile(imageFileName,".jpg",storageDir);
        } catch (Exception e) {
            Log.d("mylog", "Exception : " + e.toString());
        }

        // Save a file: path for use with ACTION_VIEW intents
        setCurrentPhotoPath(Fimage.getAbsolutePath());
        return Fimage;
    }


    private void saveImage() {

        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(currentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
        Toast.makeText(getApplicationContext(), "تم حفظ الصورة بنجاح", Toast.LENGTH_LONG).show();

    }



    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override


    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //to upload picture

        if (requestCode == MainActivity.UPLOAD_PIC && resultCode == RESULT_OK && data != null) {
            selectedImage = data.getData();

            if (selectedImage == null) {
                txtView.setText("selected Image is empty");
            } else {
                try {
                    // userImage.setImageBit(recognizer.resizeBitmap(userImage.getImageBit()));
                    // recognizer.callCloudVision(userImage.getImageBit());
                    userImage.setImageUri(selectedImage);
                    takenPicture = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                    userImage.setImageBit(recognizer.resizeBitmap(takenPicture));

                   // Problem > image rotation
                    takenPicture = handleSamplingAndRotationBitmap(getApplicationContext() , selectedImage ) ;

                    image.setImageBitmap(takenPicture);
                   // image.setImageBitmap( handleSamplingAndRotationBitmap(this, selectedImage)) ;
                    // recognizer.callCloudVision(userImage.getImageBit());


                    callCloudVision(userImage.getImageBit());

                    // To get the uploaded image path :

                    setCurrentPhotoPath(getRealPathFromURI(selectedImage)) ;

                } catch (IOException e) {
                    e.printStackTrace();
                }
           /* imageDescription.setDescription(recognizer.getDescription());
            ett =  new EnglishToTagalog("en",imageDescription.getDescription());
            ett.doInBackground();
            ett.translated();
            imageDescription.setTranslatedDescription(ett.getMsg());
            txtView.setText(imageDescription.getTranslatedDescription());
             pronouncer.synthesize(imageDescription.getTranslatedDescription());
            */

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

                    // Problem > image rotation:
                   selectedImage =  getImageUri(getApplicationContext() , takenPicture) ;
                   // takenPicture = handleSamplingAndRotationBitmap(getApplicationContext() , selectedImage ) ;
                    image.setImageBitmap(handleSamplingAndRotationBitmap(getApplicationContext() , selectedImage ));
                    userImage.setImageBit(resizeBitmap(takenPicture));
                    // recognizer.callCloudVision(userImage.getImageBit());


                    callCloudVision(userImage.getImageBit());

                    //
                    //    imageDescription.setDescription(recognizer.getDescription());
                    String des = "" + txtView.getText();

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
        return sd.onTouch(null, event);
    }




//start
@SuppressLint("StaticFieldLeak")
private void callCloudVision(final Bitmap bitmap) throws IOException {

    tone = MediaPlayer.create(UploadTakeImage.this, R.raw.uploading1);
    tone.start();
    tone.setLooping(true);


    // txtView.setText("Uploading...");

 //   if (tone.isPlaying()) {

  //      isPlayingAudio = false;
 //   }

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
                labelDetection.setMaxResults(20);
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
            txtView.setText(result + "\n" );
            tone.release();


            final Illustrate illustrate=new Illustrate(result,UploadTakeImage.this);
            illustrate.startSynthesize();



            // To repeat description
            txtView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    illustrate.startSynthesize();
                }
            });


        }
    }.execute();



} //end callcloudvision

    private String convertResponseToString(BatchAnnotateImagesResponse response) {
        StringBuilder message = new StringBuilder("");
        StringBuilder TextOCR = new StringBuilder("");
        StringBuilder TextfacialExpressions = new StringBuilder("");
        StringBuilder Textlabel = new StringBuilder("");
        StringBuilder Textcolors = new StringBuilder("");
        StringBuilder logo1 = new StringBuilder();
        String Logo ="",from="";

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
            ocrtext = ocrtext.toLowerCase();
            ocrtext= ocrtext.replaceAll("[\r\n]+", " ");

            TextOCR.append("Written on it: "+ocrtext); }
        else {
            TextOCR.append(""); }

        //FACE START
         person = "";
         numberofpersons=0;
        List<FaceAnnotation> faces = response.getResponses().get(0)
                .getFaceAnnotations();
        if (faces != null) {
            for (FaceAnnotation face: faces) {
                numberofpersons++;

                String joy=  String.format(face.getJoyLikelihood());
                if (joy.equals("VERY_LIKELY")||joy.equals("LIKELY")||joy.equals("POSSIBLE"))
                    person="  happy ";
                String sorrow=  String.format(face.getSorrowLikelihood());
                if (sorrow.equals("VERY_LIKELY")||sorrow.equals("LIKELY")||sorrow.equals("POSSIBLE"))
                    person="  sad ";
                String anger =String.format(face.getAngerLikelihood());
                if (anger.equals("VERY_LIKELY")||anger.equals("LIKELY")||anger.equals("POSSIBLE"))
                    person="  angry ";
                String surprise =String.format(face.getSurpriseLikelihood());
                if (surprise.equals("VERY_LIKELY")||surprise.equals("LIKELY")||surprise.equals("POSSIBLE"))
                    person="  surprised ";
                if (numberofpersons > 1 ){
                    //person=+person;
                TextfacialExpressions.append("and "+person);} else

                TextfacialExpressions.append(person);

            }

        } else {
            message.append("");
        }

        //colors
        DominantColorsAnnotation colors  = response.getResponses().get(0).getImagePropertiesAnnotation().getDominantColors();
        if(faces==null) {
            for (ColorInfo color : colors.getColors()) {
                Textcolors.append(
                        "" + getColorNameFromRgb((int) Math.round(color.getColor().getRed()), (int) Math.round(color.getColor().getGreen()), (int) Math.round(color.getColor().getBlue())));
                receivedColor=Textcolors.toString();
                break;
            }
        }
        else {
            Textcolors.append(""); }

            //labels
        List<EntityAnnotation> labels = response.getResponses().get(0).getLabelAnnotations();
        if (labels != null) {
            //this loop will add all labels received from vision API to receivedLabels array
            int i = 0;
            for (EntityAnnotation label : labels) {
                receivedLabels.add(String.format(label.getDescription()));
                i++;
            }
            Textlabel.append(getLabel(receivedLabels));
            message.append(Textlabel + " ");
        }// end if labels !=null



        String arabicText="";
        if ((from.equals("ar"))) {

            arabicText = " "+ocrtext;
            ocrtext="";}
        else  {
            ett = new EnglishToTagalog(from, ocrtext);
            if (texts != null) {
                ett.doInBackground();
                ett.translated();
                ocrtext = " "+ett.getMsg();
            }
        }
        if(!(Textcolors.equals("")||Textlabel.equals("")||TextfacialExpressions.equals(""))){
            ett = new EnglishToTagalog("", "");

            ett.setFrom("en");
            ett.setMsg(message.toString());
            ett.doInBackground();
            ett.translated();
            TranslatedText=ett.getMsg();}
            String arabicFullMsg="";
        arabicFullMsg= textContainsArabic(TranslatedText + ocrtext  + arabicText);
        imageDescription.setTranslatedDescription(ett.getMsg()+ocrtext);

        return arabicFullMsg;
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
    public String getLabel(ArrayList<String>labels) {
        String label = null;
        String firstLabel = labels.get(0).toLowerCase();
        String wearings = "";
        String food=" ";
        //String table= labels.get(0).toLowerCase()+" "+labels.get(1).toLowerCase()+" "+labels.get(2).toLowerCase()+" ";
        String table=null;

        // papers,books etc.
        label = findBestLabel(labels, textLabels);
        if (label != null) {
            if (ocrtext != null)
                return label + " written on it: ";
            else
                return receivedColor + " " + label;
        }


        // person
        wearings = getWearings(labels);
        // hair = getHair;
        //men
        if(label==null) {
            label = findBestLabel(labels, manLabel);
            if (label != null) {
                label = "a " + label;
                if (wearings != null) {
                    if (numberofpersons > 1) {
                        label = " men ";
                        wearings = wearings + "s";
                    }
                    return person + " " + label + " " + wearings;
                } else {
                    if (numberofpersons > 1) {
                        label = " men ";
                        return person + " " + label;
                    }
                }
                return "a " + person + " " + label;
            }
        }
        if(label==null){
            // if(label.equals("male")||label.equals("facialhair")||label.equals("moustache")||label.equals("macho")||label.equals("beard"))
           /* if (labels.contains("male") || labels.contains("facialhair") || labels.contains("moustache") || labels.contains("macho") || labels.contains("beard"))
                label = "a man ";*/
            label= findBestLabel(labels,maleLabels);
            if (label != null) {
                if (wearings != null && person != null) {
                    if (numberofpersons > 1) {
                        label = " men ";
                        wearings = wearings + "s";
                    }
                    return person + " " + label + " " + wearings;
                } else {
                    if (numberofpersons > 1) {
                        label = " men ";
                        return person + " " + label;
                    }
                    return "a " + person + " " + label;
                }
            }
        }
        //women
        if(label==null){
            label = findBestLabel(labels,womanLabel);

            if(label!=null) {
                if (wearings != null){
                    if( numberofpersons>1){
                        label=" women ";
                        wearings=wearings+"s";}
                    return person+" "+label+" "+wearings;}
                else{
                    if( numberofpersons>1){
                        label=" women ";
                        return person+" "+label;
                    }

                    return "a "+person+" "+label;}
            }}

        //children
        if(label==null){
            label = findBestLabel(labels,childrenLabels);
            if(label!=null) {
                if (wearings != null){
                    if( numberofpersons>1){
                        label=" children ";
                        wearings=wearings+"s";}
                    return person+" "+label+" "+ wearings;}
                else{
                    if( numberofpersons>1){
                        label=" children ";

                        return person+" "+label;}
                }
                return "a "+person + " " + label;
            }}

        //if it did not recognize the gender but recognized it's a person or recognized the person's job
        if (label==null&& wearings != null){
            if(numberofpersons>1)
                return " persons  "+wearings;
            else
                return "a person  "+wearings;}

        // tables
        //supposed to be  if(labels.contains("table"))
        // if(table.contains("table"))
        table= getThingsOnTable(labels);
        if(table!=null)
            return table;

        // food : fruits , vegetables etc.
        //why did not work?
        // if(labels.contains("food")||labels.contains("dish")||labels.contains("cuisine")||labels.contains("recipe")||labels.contains("produce"))
        //  if(firstLabel.equals("food")||firstLabel.equals("dish")||firstLabel.equals("cuisine")||firstLabel.equals("recipe")||firstLabel.equals("produce"))
        food = getFood(labels);
        if (food != null) {
            return food; }

        return receivedColor+" "+firstLabel;


    }//end method getLabels


    //(1) Man, Woman, Child
    public String findBestLabel (ArrayList<String>labels, String [] compare){
        String label ="";
        for(int i =0; i< labels.size(); i++) {
            if(labels.get(i)!=null)
                label=labels.get(i).toLowerCase();
            for (int j = 0; j < compare.length; j++) {
                if (label.equals(compare[j])){
                    if(label.equals("text")||label.equals("line")||label.equals("font")||label.equals("calligraphy")||label.equals("word")||label.equals("clip art")||label.equals("handwriting")||label.equals("witting")||label.equals("number"))
                    {label ="written text: ";
                        return label;}
                    if(label.equals("male")||label.equals("facialhair")||label.equals("moustache")||label.equals("macho")||label.equals("beard"))
                        label=" man";
                    if(label.equals("child model"))
                        label="child";
                    if(label.equals("child")||label.equals("baby")||label.equals("child model"))
                        label = getChildGender(labels,label);

                    return label;
                }
            }
        }
        return null;
    }

    //(2)Child gender
    public String getChildGender(ArrayList<String>labels , String child){
        String gender="";
        for(int i =0; i< labels.size(); i++) {
            if(labels.get(i)!=null)
                gender = labels.get(i).toLowerCase();
            for (int j = 0; j < genderLabels.length; j++) {
                if (gender.equals(genderLabels[j])) {
                    if (gender.equals("male"))
                        child = " a baby boy ";
                    else
                        child = " a baby girl ";
                    return child;
                }

            }
        }
        return child ;
    }
    //(3) Wearings
    public String getWearings(ArrayList<String>labels  ){
        String wearing="";
        for(int i =0; i< labels.size(); i++) {
            if(labels.get(i)!=null)
                wearing = labels.get(i).toLowerCase();
            for (int j = 0; j < wearingsLabels.length; j++) {
                if (wearing.equals(wearingsLabels[j])) {
                    if(receivedColor!=null)
                        return " wearing  " +receivedColor+" "+wearing;
                    return " wearing  " +wearing;
                }

            }
        }
        return null ;
    }

    //(4) Tables
    public String  getThingsOnTable(ArrayList<String>labels) {
        String table= "";
        String onTable="";
        int count=0;
        ArrayList<String>things=labels;

        //problem when to return null? to optimize search time for other categories , i solution

        //to get the table type
        for(int i=0;i<labels.size();i++){
            table=labels.get(i).toLowerCase();
            for(int k=0;k<onLabels.length;k++){
                if(table.equals(onLabels[k]))
                {  //break if you find the type of table
                    table=onLabels[k];
                    count=1;
                    break;}
            }
            if(count==1)
                break;
        }

        //if a kind of table exists
        if(count==1) {
            //remove table , furniture ......
            for(int i=things.size()-1;i>=0;i--) {
                onTable = things.get(i).toLowerCase();
                for (int k = 0; k < onLabels.length; k++) {
                    if (onTable.equals(onLabels[k]))
                        if(i<things.size())
                            things.remove(i);
                    //if i<things.size() why is it required ?? above it works without it
                }
            }
            //need optimization
            for(int i=things.size()-1;i>=0;i--) {
                onTable = things.get(i).toLowerCase();
                for (int k = 0; k < ExcludeTableLabels.length; k++) {
                    if (onTable.equals(ExcludeTableLabels[k]))
                        if(i<things.size())
                            things.remove(i);
                }
            }
            if(things.size()==0)
                onTable=null;
            if(things.size()>1)
                onTable= " a "+things.get(0)+" and a "+things.get(1);
            else
            if(things.size()==1)
                onTable= " a "+things.get(0);
            // return onTable+" on a "+table;
            if (onTable != null)
                return  onTable + " on a " +" "+ table;

            return receivedColor +"  " + table ;
        }

        return null;
    }

    //(5)food
    public String getFood(ArrayList<String>labels){

        String food="";
        ArrayList<String>foods=labels;

        for(int i=foods.size()-1;i>=0;i--){
            food=foods.get(i).toLowerCase();
            for(int k=0;k<ExcludefoodLabels.length;k++){
                if(food.equals(ExcludefoodLabels[k]))
                    foods.remove(i);
            }
        }
        return foods.get(0)+" ";
    } // end getFood

   //(6) colors
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




    // to know the exact name of the image.
    public String getImageFileName () {
        return imageFileName ;
    }
    public static String getCurrentPhotoPath () { return currentPhotoPath; }
    public void setCurrentPhotoPath (String currentPhotoPath) {
        this.currentPhotoPath = currentPhotoPath ;
    }



// To solve rotation problem:
public static Bitmap handleSamplingAndRotationBitmap(Context context, Uri selectedImage) throws IOException {
    int MAX_HEIGHT = 1024;
    int MAX_WIDTH = 1024;

    // First decode with inJustDecodeBounds=true to check dimensions
    final BitmapFactory.Options options = new BitmapFactory.Options();
    options.inJustDecodeBounds = true;
    InputStream imageStream = context.getContentResolver().openInputStream(selectedImage);
    BitmapFactory.decodeStream(imageStream, null, options);
    imageStream.close();

    // Calculate inSampleSize
    options.inSampleSize = calculateInSampleSize(options, MAX_WIDTH, MAX_HEIGHT);

    // Decode bitmap with inSampleSize set
    options.inJustDecodeBounds = false;
    imageStream = context.getContentResolver().openInputStream(selectedImage);
    Bitmap img = BitmapFactory.decodeStream(imageStream, null, options);

    img = rotateImageIfRequired(context, img, selectedImage);
    return img;
}
    private static int calculateInSampleSize( BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            // Calculate ratios of height and width to requested height and width
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will guarantee a final image
            // with both dimensions larger than or equal to the requested height and width.
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;

            final float totalPixels = width * height;

            // Anything more than 2x the requested pixels we'll sample down further
            final float totalReqPixelsCap = reqWidth * reqHeight * 2;

            while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
                inSampleSize++;
            }
        }
        return inSampleSize;
    }
    private static Bitmap rotateImageIfRequired(Context context, Bitmap img, Uri selectedImage) throws IOException {

        InputStream input = context.getContentResolver().openInputStream(selectedImage);
        ExifInterface ei;
        if (Build.VERSION.SDK_INT > 23)
            ei = new ExifInterface(input);
        else
            ei = new ExifInterface(selectedImage.getPath());

        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                return rotateImage(img, 90);
            case ExifInterface.ORIENTATION_ROTATE_180:
                return rotateImage(img, 180);
            case ExifInterface.ORIENTATION_ROTATE_270:
                return rotateImage(img, 270);
            default:
                return img;
        }
    }
    private static Bitmap rotateImage(Bitmap img, int degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        Bitmap rotatedImg = Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, true);
        img.recycle();
        return rotatedImg;
    }


// To get image for share
    public Uri getImageUri(Context context, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
    public void setPhotoUri (Uri uri) {  uri = photoUri ; }
    public Uri getPhotoUri () { return  photoUri; }
    public Bitmap getTakenPicture () { return takenPicture; }



    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }
    public  String textContainsArabic(String text) {
        String [] arr = text.split(" ");
        String [] newArray = new String [arr.length];
        String ocr="";
int newArrayCount=0;
        for(int i =0 ; i <arr.length;i++){
            if (Character.UnicodeBlock.of(arr[i].charAt(0)) == Character.UnicodeBlock.ARABIC){
                newArray[newArrayCount++]= arr[i];
            }


        }//for

        for (int i =0 ; i < newArrayCount ; i++)
ocr+=newArray[i] + " ";
            return ocr;
    }


}//end class
