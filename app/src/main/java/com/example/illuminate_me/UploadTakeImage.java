package com.example.illuminate_me;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Typeface;
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
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import static android.os.Environment.getExternalStoragePublicDirectory;
import android.os.AsyncTask;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import android.media.MediaPlayer;

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
    private MediaPlayer tone, instruction, instasound, twittersound, whatssound, mainsound, prevsound,sharesound, savesound, hi ;
    // we should replace the selected and taken with only one attribute
    UserImage userImage = new UserImage();
    private static String currentPhotoPath;
    boolean  isPlayingAudio = true;

    private final String LOG_TAG = "MainActivity";
    private ArrayList<String> receivedLabels = new ArrayList<String>();

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
            Log.d("mylog", "Exception : " + e.toString());
        }

        // Swiping :
          sd = new SwipeDetector(this, new SwipeDetector.OnSwipeListener() {

            @Override
            public void onSwipeUp(float distance, float velocity) {
                // Save
               saveImage();
                savesound = MediaPlayer.create(UploadTakeImage.this, R.raw.savevoice);
                savesound.start();
            }

            @Override
            public void onSwipeRight(float distance, float velocity) {
                // previous
                prevsound = MediaPlayer.create(UploadTakeImage.this, R.raw.prev);
                prevsound.start();
                Intent intent = new Intent(UploadTakeImage.this, MainActivity.class);
                startActivity(intent);
            }

            @Override
            public void onSwipeLeft(float distance, float velocity) {
                // SHARE
                sharesound = MediaPlayer.create(UploadTakeImage.this, R.raw.sharevoice);
                sharesound.start();
                Intent intent = new Intent(UploadTakeImage.this, Share.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onSwipeDown(float distance, float velocity) {
                int count =0;
                // Home page
                mainsound = MediaPlayer.create(UploadTakeImage.this, R.raw.mainvoice);
                mainsound.start();


                Intent intent = new Intent(UploadTakeImage.this, logoScreen.class);
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

            if (selectedImage == null ) {
                txtView.setText("selected Image is empty");
            } else {
                try {

                    userImage.setImageUri(selectedImage);
                    takenPicture = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                    userImage.setImageBit(resizeBitmap(takenPicture));

                   // Problem > image rotation
                    takenPicture = handleSamplingAndRotationBitmap(getApplicationContext() , selectedImage ) ;

                    image.setImageBitmap(takenPicture);



                    callCloudVision(userImage.getImageBit());

                    // To get the uploaded image path :
                    setCurrentPhotoPath(getRealPathFromURI(selectedImage)) ;

                } catch (IOException e) {
                    e.printStackTrace();
                }

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

                    // Problem > image rotation:
                  // selectedImage =  getImageUri(getApplicationContext() , takenPicture) ;
                   // image.setImageBitmap(handleSamplingAndRotationBitmap(getApplicationContext() , selectedImage ));
                    userImage.setImageBit(resizeBitmap(takenPicture));

                    // Rotation problem
                    ExifInterface ei = new ExifInterface(pathToFile);
                    int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                            ExifInterface.ORIENTATION_UNDEFINED);

                    Bitmap rotatedBitmap = null;
                    switch(orientation) {

                        case ExifInterface.ORIENTATION_ROTATE_90:
                            rotatedBitmap = rotateImage(takenPicture, 90);
                            break;

                        case ExifInterface.ORIENTATION_ROTATE_180:
                            rotatedBitmap = rotateImage(takenPicture, 180);
                            break;

                        case ExifInterface.ORIENTATION_ROTATE_270:
                            rotatedBitmap = rotateImage(takenPicture, 270);
                            break;

                        case ExifInterface.ORIENTATION_NORMAL:
                        default:
                            rotatedBitmap = takenPicture;
                    }

                    image.setImageBitmap(rotatedBitmap);


                    // End solution
                    callCloudVision(userImage.getImageBit());

                    String des = "" + txtView.getText();

                } catch (IOException e) {
                    e.printStackTrace();
                }

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

    tone = MediaPlayer.create(UploadTakeImage.this, R.raw.proc123);
    tone.start();
    tone.setLooping(true);


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
                labelDetection.setMaxResults(30);
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
            String Checkedresult="" ;
            int dup=-1;

            String [] resultarr = result.split(" ");
            //String [] resultarr = {"نستله",  "نستله" , "نستله" , "اخضر"};
            String []  resultCh = new String[resultarr.length];
            if (result==""|| result==null){
                result="تعذُر التعرف على الصورة";}
            else {


                int j = 0;
                if (resultarr.length == 1){
                    Checkedresult = resultarr[0];}
                else{
 /*   for( int i=0 ; i <resultarr.length-1; i++) {
        if (i + 1 == resultarr.length)
            break;
        if (resultarr[i] == resultarr[i + 1]) {
            resultCh[j] = resultarr[i];
            j++;
        } else {
            resultCh[j] = resultarr[i];
            j++;
            resultCh[j] = resultarr[i + 1];
            j++;
        }//f

    }*/
                    boolean same= false , brk=false;
                    for(int i =0 ; i < resultarr.length ; i+=1) {
                        same =false;
                        brk=false;
                        if (i + 1 == resultarr.length) {
                            if (resultarr[i-1].equals(resultarr[i]) )
                                brk=true;
                            else
                                resultCh[j++] = resultarr[i];
                            break;}

                        if(brk==true) break;

                        if ( i==dup){
                            same=true;}
                        if (resultarr[i].equals(resultarr[i + 1]) ) {
                            dup =i+1;
                        }

                        if(same==false)
                            resultCh[j++] = resultarr[i];
                        //resultCh[j++] = resultarr[i + 1];




                    }

                }//else if size >1
            }//big else
            for ( int i =0 ; i < resultCh.length;i++)
            {
                if(resultCh[i]!=null)
                    Checkedresult+=resultCh[i]+" ";
            }
            txtView.setText(Checkedresult + "\n" );
            tone.release();


            final Illustrate illustrate=new Illustrate(result,UploadTakeImage.this);
            illustrate.startSynthesize();

            /* To repeat description

            txtView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    illustrate.startSynthesize();
                    return false;
                }
            });

            image.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    illustrate.startSynthesize();
                    return false;
                }
            });*/

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
        Recognizer recognizer = new Recognizer();
        StringBuilder message = new StringBuilder("");
        String ocrtext = "", from = "", Logo = "";

        //OCR
        List<EntityAnnotation> logos = response.getResponses().get(0).getLogoAnnotations();
        List<EntityAnnotation> texts = response.getResponses().get(0).getTextAnnotations();
         recognizer.setOCRText(logos,texts);
        if (logos != null) {
            for (EntityAnnotation logo : logos) {
                Logo = String.format(Locale.getDefault(), "%.3f: %s", logos.get(0).getLocale(), logo.getDescription());
                Logo = Logo.substring(4);

            }
        }

        if (texts != null) {
            from = texts.get(0).getLocale();
            ocrtext = texts.get(0).getDescription();
            if (logos != null) {
                ocrtext = ocrtext + "from" + Logo + " company";
            }
            ocrtext = ocrtext.toLowerCase();
            ocrtext = ocrtext.replaceAll("[\r\n]+", " ");
        }


        //Facial Expression
        List<FaceAnnotation> faces = response.getResponses().get(0).getFaceAnnotations();
        recognizer.setFacialExpressions(faces);

        //Colors
        DominantColorsAnnotation colors  = response.getResponses().get(0).getImagePropertiesAnnotation().getDominantColors();
        for ( ColorInfo color : colors.getColors()) {
            recognizer.setColorNameFromRgb((int) Math.round(color.getColor().getRed()), (int) Math.round(color.getColor().getGreen()), (int) Math.round(color.getColor().getBlue()));
            break;
        }
        //Labels
        List<EntityAnnotation> labels = response.getResponses().get(0).getLabelAnnotations();
        if (labels != null) {
            //this loop will add all labels received from vision API to receivedLabels array
            for (EntityAnnotation label : labels) {
                receivedLabels.add(String.format(label.getDescription())); }
            message.append(recognizer.generateDescreption(receivedLabels)); }


        //translation part
        // ocrtext=recognizer.getOcrtext();
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
        if(!(recognizer.getColor()==null||recognizer.getLabel()==null||recognizer.getFacialExpression()==null)){
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
