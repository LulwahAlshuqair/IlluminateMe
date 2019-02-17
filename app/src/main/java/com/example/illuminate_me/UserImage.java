package com.example.illuminate_me;

public class UserImage {


   private ImageDescription description ;

//(1)
    public ImageDescription getDescription (){

        return description;
    }

//(2)
    public void setDescription(ImageDescription description){

        this.description=description;
    }
}
