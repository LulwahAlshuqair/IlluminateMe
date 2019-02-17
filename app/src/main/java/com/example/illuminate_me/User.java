package com.example.illuminate_me;

import android.media.Image;

public class User {

    private Image userImage;

    //might change

//(1)
public Image takePicture(){

    //code
    return userImage;
}

//(2)
public Image uploadPicture(){

    //code
    return userImage;
}
//(3)
public void repeatVerbalDescription(){

    //code
}

//(4)
public void sharePicture(String imagePath){

    //code
}

//(5)
public boolean savePicture (Image image){
    boolean isSaved =false;
    //code
    return isSaved;
}

public Image getUserImage(){

    return userImage;
}

public void setUserImage(Image userImage){

    this.userImage=userImage;
}
}// end class
