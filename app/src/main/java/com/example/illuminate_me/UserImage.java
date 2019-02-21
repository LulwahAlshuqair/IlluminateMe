package com.example.illuminate_me;

import android.graphics.Bitmap;
import android.net.Uri;

public class UserImage {


   private ImageDescription description ;
private Bitmap imageBit;
private Uri imageUri;

//(1)
    public ImageDescription getDescription (){

        return description;
    }

//(2)
    public void setDescription(ImageDescription description){

        this.description=description;
    }

    public Bitmap getImageBit() {
        return imageBit;
    }

    public Uri getImageUri() {
        return imageUri;
    }

    public void setImageBit(Bitmap imageBit) {
        this.imageBit = imageBit;
    }

    public void setImageUri(Uri imageUri) {
        this.imageUri = imageUri;
    }
}
