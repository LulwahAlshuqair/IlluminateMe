package com.example.illuminate_me;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.ImageView;

import java.util.Objects;

public class UploadTakeImage extends AppCompatActivity {
    private float x1,x2,y1,y2;
    private ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //to make it cover the entire screen,
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
       // getSupportActionBar().hide();

        setContentView(R.layout.activity_upload_take_image);
        image= findViewById(R.id.imageToUpload);
        // repeatDescription =(Button) findViewById(R.id.repeatDescription);
// TO PEN THE GALLARY
        if(MainActivity.chosenButton==MainActivity.UPLOAD_PIC)
            uploadPicture();
        else
            takePicture();
    }

    //to allow user select image from the gallary
    public void uploadPicture() {

        Intent gallaryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(gallaryIntent,MainActivity.chosenButton);

    }

    public void takePicture() {

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent,MainActivity.chosenButton);

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //to upload picture
        if( requestCode==MainActivity.UPLOAD_PIC&&resultCode==RESULT_OK &&data!=null){
            //how to pass this to class user
            //address of the selected image
            Uri selectedImage = data.getData();
            //to display the selected image in the image view (interface)
            image.setImageURI(selectedImage);
        }
        // allow user to take a picture
        if( requestCode==MainActivity.TAKE_PIC&&resultCode==RESULT_OK &&data!=null){
            Bitmap takenPicture = (Bitmap) Objects.requireNonNull(data.getExtras()).get("data");
            image.setImageBitmap(takenPicture);

        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {


        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                x1=event.getX();
                y1=event.getY();
                break;
            case MotionEvent.ACTION_UP:
                x2=event.getX();
                y2=event.getY();
                if(x1<x2){
                    // go to home screen
                    Intent intent = new Intent (UploadTakeImage.this, MainActivity.class);
                    startActivity(intent);

                }
                break;

        }//end switch
        //if swipe left to right

        return super.onTouchEvent(event);
    }
}
