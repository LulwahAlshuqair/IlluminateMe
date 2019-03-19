package com.example.illuminate_me;
import com.google.api.services.vision.v1.model.EntityAnnotation;
import com.google.api.services.vision.v1.model.FaceAnnotation;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class Recognizer {

    //variables
    private int numberofpersons=0;
    private String facialExpression ;
    private String receivedColor;
    private String ocrtext="";
    String label;
    private ArrayList<String> receivedLabels = new ArrayList<String>();

    //to find gender
   // private String[] maleLabels = {"male","beard","facialhair","moustache"};
    private String [] manLabel = {"man","gentleman","businessman","men", "macho man","male","beard","facialhair","guy","moustache","father","grandfather","old man"};
    private String[] womanLabel = {"woman" , "lady" ,"gentlewoman","mother","old woman", "women","grandmother"};
    private String[] childrenLabels = { "child" , "baby", "girl" , "boy" ,"baby laughing"};
    private String[] genderLabels = {"male","female"};

    //to describe people
    private String[] emotionLabels = {"smile","laugh","laughing","crying","smiling"};
    private String[] wearingsLabels = {"white coat","coat","stethoscope","cowboy hat","sun hat","hat","dress","t-shirt","jeans","headgear","fashion accessory"};
    private String[] describeHairLabels={"blond","blonde","short hair","brown hair","black hair","long hair"};

    //table lebls
    private String[] surfaceLabels ={"table","desk","shelf","side table","coffee table","sofa tables","outdoor table","dresser","night stand","writing desk","computer desk","drawer","chest of drawers"};
    private String[] ExcludeSurfaceLabels={"office","room","wood","furniture","metal","technology","floor","flooring","interior design","building","rectangle","writing office","office writing","marble","hutch","wood stain","interior design","end table","hardwood","living room","chair","solid wood","tile","tiles","iron"};

    //text labels
    private String[] textLabels={"street sign","sign","traffic sign","signage","book","notebook","diary","paper product","paper","product","document"};
   // private String [] excludeTextLabels = {"text","line","font","calligraphy","word","clip art","handwriting","witting","number","ink"};

    //food labels
    private String[] ExcludefoodLabels ={"food","meat","dish","plate","natural foods","indian cuisine ","dessert","baked goods","superfood","plant","gluten","vegan nutrition","cruciferous vegetables","recipe","cuisine","brunch","breakfast","dinner","lunch","cooking","snack","produce","kids' meal","junk food","ingredient","sweetness","finger food","fast food","baking"};

    //nature scene labels
    private String [] excludeNatureLabels = {"blue","natural view","natural views","klippe","spring","moss","rock","vascular plant","terrestrial plant","tributary","arroyo","fluvial landforms of streams","stream bed","riparian forest","riparian zone","mist","yellow","atmospheric phenomenon","plain","field","massif","sunlight","branch","path","dirt road","infrastructure","park","national park","fence","state park","maple leaf","autumn","red","green","house","reservoir","fell","tarn","tourist attraction","elaeis","arecales","leisure","resort","sound","cape","world","drainage basin","headland","terrain","spit","shore","promontory","inlet","vacation","tourism","wildlife","theatrical scenery","adaptation","tropics","walkway","annual plant","rhododendron","maple","deciduous","state park","groundcover","temperate broadleaf and mixed forest","shrub","botany","woody plant","bird's-eye view","terrace","aerial photography","thoroughfare","plant community","road","grass","biome","body of water","water feature","hill station","water resource","water","water resources","stream","watercourse","calm","leaf","reflection","spring ","wilderness"};
    private String [] natureLabels= {"nature","highland","headland","landscaping","natural view","natural views","vegetation", "natural landscape" ,"nature reserve","natural environment","nature landscape", "landscape"};

    //for general labels
    private String [] excludVerbsLabels = {"drink","drinking","eat","swings","product","swing","eating","sitting","standing","swimming"};


//(1) This method shows how a final description of the photo is generated.
    public String generateDescreption(ArrayList<String>labels) {
        String wearings;
        String hair ;
        //natural scenes
        label = getNaturalScenery(labels);
        if(label!=null){
            return label; }
        // signs,papers,books etc.
        label = findTextLabel(labels, textLabels);
        if (label != null) {
            if(label.equals("handwriting"))
                return "handwritten  ";
            if (ocrtext != null)
                return label + " written on it: ";
            else
                return receivedColor + " " + label; }
        // tables
        label= getThingsOn(labels);
        if(label!=null){
            return label;}
        //food
        label = getFood(labels);
        if (label != null) {
            return label; }

        // find the wearings and hair type to describe people in the picture
        wearings = getWearings(labels);
        hair = getHair(labels);
        // to find gender of a person
         label = findGender(labels, manLabel);
         label = findGender(labels,womanLabel);
         label = findGender(labels,childrenLabels);
         //return full description about person
            if (label != null) {
                //if more than one person in the picture
                if (numberofpersons > 1) {
              if(label.contains("man"))
                 label= label.replaceAll("woman","women");
              if(label.equals("father")||label.equals("grandfather"))
                  label = label+"s";
              if(label.contains("woman"))
               label=label.replaceAll("woman","women");
              if(label.equals("child"))
                  label ="children";
              if(label.equals("mother")||label.equals("grandmother"))
                   label = label+"s";
              if(label.equals("boy")||label.equals("girl"))
                  label = label+"s";
                    if(wearings!=null){
                  wearings = wearings + "s";
                  return facialExpression + " " + label + " " + wearings;}
                    return facialExpression + " " + label; }
                // only one person
                if(wearings!=null)
                    return facialExpression + " " + label + " " + wearings;
                else
                if(hair!=null)
                    return "a " + facialExpression + " " + label+" "+hair;
                return "a " + facialExpression + " " + label; }

        //if it did not recognize the gender but recognized the facial expression , or wearings.
        if ( facialExpression != null) {
            if (numberofpersons > 1)
                return facialExpression + " people";
            else {
                if(hair!=null)
                  return "a" + facialExpression + " person "+hair;
                else
                return "a" + facialExpression+" person"; } }
        if ( wearings != null){
            if(numberofpersons>1)
                return " persons  "+wearings;
            else
                return "a person  "+wearings; }

        if(labels.contains("face")) {
            return "a person "; }
        //if gender is not recognized and no "face" label
        if(labels.contains("nose")||labels.contains("mouth")) {
            return "a close picture of a face  ";
        }

     // General object case,"getBestLabel" method removes verbs and adjective from the received labels to get the "name" of the object
          return receivedColor+" "+getBestLabel(labels); }//end method getLabels

    //(1) This method shows how description of any picture that contains text is generated
    public String findTextLabel (ArrayList<String>labels, String [] textLabels) {
        String Label = "";
        for (int i = 0; i < labels.size(); i++) {
            if (labels.get(i) != null) {
                Label = labels.get(i).toLowerCase();
                for (int j = 0; j < textLabels.length; j++) {
                    if ( Label.equals(textLabels[j])) {
                       if(Label.equals("handwriting"))
                           return Label;
          //replace
          if ( Label.equals("text") ||  Label.equals("line") ||  Label.equals("font") ||  Label.equals("calligraphy") ||
                  Label.equals("word") ||  Label.equals("clip art") ||  Label.equals("witting") ||  Label.equals("number"))
                        { Label= "written text: ";
                            return  Label; } } } } }

            // if no sign ,book ,text is found.
            return null;
    }

    //(2) This method shows how a person's gender could be found.
    public String findGender(ArrayList<String>labels, String [] targetGender){
        String Label = "";
        //These loops go through the two received lists of labels.
        //labels list represents the actual labels received from the Vision API.
        //targetGender list represents the array that contains labels that indicate certain gender.
        for(int i =0; i< labels.size(); i++) {
            if (labels.get(i) != null) {
                Label = labels.get(i).toLowerCase();
                for (int j = 0; j < targetGender.length; j++) {
                    if (Label.equals(targetGender[j])) {
         //the labels in the if statement indicate that gender is male therefore, they are replaced with "man".
              if (Label.equals("male") || Label.equals("facialhair") ||Label.equals("guy")||Label.equals("moustache")|| Label.equals("beard"))
                   Label = " man";
              if (Label.equals("lady") )
                   Label = " woman";
        //the label "child model" is not translated meaningfully in arabic therefore, it's replaced with "child" only.
              if (Label.equals("child model")||Label.equals("baby"))
                  Label = "child";
        // it is possible to find more accurate labels that indicate the child's gender
         //method "getChildGender" will search for these labels
             if (Label.equals("child"))
               Label = getChildGender(labels, Label);

               return Label; } } } }

        //if no person in the picture
        return null; }


    //(3)This method shows how a child's gender can be found.
    public String getChildGender(ArrayList<String>labels , String child){
        String gender="";
        //"genderLabels" contains only two labels "male" and "female"
        for(int i =0; i< labels.size(); i++) {
            if (labels.get(i) != null) {
                gender = labels.get(i).toLowerCase();
                for (int j = 0; j < genderLabels.length; j++) {
                    if (gender.equals(genderLabels[j])) {
                        if (gender.equals("male"))
                            child = " a boy ";
                        else
                            child = " a girl ";
                        return child; }
                } } }
        // if child gender could not be found return the label received in parameter "child".
        return child ;
    }

    //(4) This method shows how to find labels describe what a person is wearing
    public String getWearings(ArrayList<String>labels ){
        String wearing="";
        //"wearingsLabels" contains labels of wearings such as "dress" , "coat" , etc.
        for(int i =0; i< labels.size(); i++) {
            if (labels.get(i) != null) {
                wearing = labels.get(i).toLowerCase();
                for (int j = 0; j < wearingsLabels.length; j++) {
                    if (wearing.equals(wearingsLabels[j])) {
                        if (receivedColor != null)
                            return " wearing  " + receivedColor + " " + wearing;
                        return " wearing  " + wearing; } } } }
        // if no wearings is found.
        return null ; }

    //(5) This method shows how the facial expression of a person can be categorised.
    public void setFacialExpressions (List<FaceAnnotation> faces ){
        facialExpression= "";
        numberofpersons=0;
        if (faces != null) {
            for (FaceAnnotation face : faces) {
                numberofpersons++;
                String joy = String.format(face.getJoyLikelihood());
                if (joy.equals("VERY_LIKELY") || joy.equals("LIKELY") || joy.equals("POSSIBLE"))
                    facialExpression = "  happy ";
                String sorrow = String.format(face.getSorrowLikelihood());
                if (sorrow.equals("VERY_LIKELY") || sorrow.equals("LIKELY") || sorrow.equals("POSSIBLE"))
                    facialExpression = "  sad ";
                String anger = String.format(face.getAngerLikelihood());
                if (anger.equals("VERY_LIKELY") || anger.equals("LIKELY") || anger.equals("POSSIBLE"))
                    facialExpression = "  angry ";
                String surprise = String.format(face.getSurpriseLikelihood());
                if (surprise.equals("VERY_LIKELY") || surprise.equals("LIKELY") || surprise.equals("POSSIBLE"))
                    facialExpression = "  surprised ";
               /* if (numberofpersons > 1 ){
                    //person=+person;

            }*/
            } }
    }//end setFacialExpressions

    //(6) This method shows how the type or color of a person's hair can be found
    public String getHair (ArrayList<String>labels) {
        String hair = "";
        //to search in the received list for any label that represents a hair type or color
        //"describeHairLabels" include labels such as "brown hair", "long hair".
        for (int i = 0; i < labels.size(); i++) {
            if (labels.get(i) != null) {
                hair = labels.get(i).toLowerCase();
                for (int j = 0; j < describeHairLabels.length; j++) {
                    if (hair.equals(describeHairLabels[j])) {
                        if (hair.equals("blond")||hair.equals("blonde"))
                            hair = " blond hair";
                        return "has a " +hair; } } } }
        // if no hair type
        return null ; }


    //(7) This method shows how a description about surfaces such as tables can be generated.
    public String  getThingsOn(ArrayList<String>labels) {
        String surface= "";
        String onSurface="";
        int count=0;
        ArrayList<String>things=labels;
        //To get the surface type "desk", "coffee table", "table" etc.
        // "surfaceLabels" contains several types of surfaces.
        for(int i=0;i<labels.size();i++) {
            if (labels.get(i) != null) {
                surface = labels.get(i).toLowerCase();
                for (int k = 0; k < surfaceLabels.length; k++) {
                    if (surface.equals(surfaceLabels[k])) {
                        surface = surfaceLabels[k];
                        count = 1;
             //break if you find the type of surface , to shorten response time
                        break; } }
                if (count == 1)
                    break; } }
        //if any "surface related" label exists
        if(count==1) {
            //Remove any "surface" labels from received labels.
            for(int i=things.size()-1;i>=0;i--) {
                if (things.get(i) != null) {
                    onSurface = things.get(i).toLowerCase();
                    for (int k = 0; k < surfaceLabels.length; k++) {
                        if (onSurface.equals(surfaceLabels[k]))
                            if (i < things.size())
                                things.remove(i); } } }
            //Remove any unneeded labels ,material of the table or place e.g. "room", "wood", "metal".
            //"ExcludeTableLabels" contains these kind of labels
            for(int i=things.size()-1;i>=0;i--) {
                if (things.get(i) != null) {
                    onSurface = things.get(i).toLowerCase();
                    for (int k = 0; k < ExcludeSurfaceLabels.length; k++) {
                        if (onSurface.equals(ExcludeSurfaceLabels[k]))
                            if (i < things.size())
                                things.remove(i); } } }
         // After removing unneeded labels from the received labels list things on the surface can be identified.
            if(things.size()==0)
                onSurface=null;
            if(things.size()>1)
                onSurface= " a "+things.get(0)+" and a "+things.get(1);
            else
            if(things.size()==1)
                onSurface= " a "+things.get(0);

            //returned result
            if (onSurface != null)
                return  onSurface + " on a " +" "+ surface;
            //if onTable (Nothing on the table) return the table type and its color.
            return receivedColor +"  " + surface ; }

       // if no table is found
         return null; }


    //(8) This method shows how a food type can be identified correctly.
    public String getFood(ArrayList<String>labels){
    String food= "";
    ArrayList<String>foods=labels;
    // "ExcludefoodLabels" contains labels that represents a food photo such as "food","natural foods","junk food".
     for(int i=foods.size()-1;i>=0;i--) {
     if (foods.get(i) != null) {
         food = foods.get(i).toLowerCase();
         for (int k = 0; k < ExcludefoodLabels.length; k++) {
            if (food.equals(ExcludefoodLabels[k]))
                 foods.remove(i); }
            } }
       // after removing "ExcludefoodLabels" labels from the received list
        // type of the food will remain in the list and will be returned.
        if(foods.size()!=0)
            return foods.get(0)+" ";
        //if no food found
        return null;
    } // end getFood

    //(9) This method shows how a description of natural scenery is generated.
    public String getNaturalScenery (ArrayList<String>labels){
        boolean isNaturalScenery=false;
        String natureScene="";
        //To search in the received list for any label that represents a natural scenery (natureLabel).
        for(int i =0; i< labels.size(); i++) {
            if (labels.get(i) != null) {
                natureScene = labels.get(i).toLowerCase();
                for (int j = 0; j < natureLabels.length; j++) {
                    if (natureScene.equals(natureLabels[j])) {
                        isNaturalScenery = true;
                        break; } } } }

        //To remove excludeNatureLabels and natureLabel from the received list to get Accurate results
        if(isNaturalScenery){
            for(int i=labels.size()-1;i>=0;i--) {
                if (labels.get(i) != null) {
                    natureScene = labels.get(i).toLowerCase();
                    for (int k = 0; k < natureLabels.length; k++) {
                        if (natureScene.equals(natureLabels[k]))
                            labels.remove(i); } } }

            for(int i=labels.size()-1;i>=0;i--) {
                if (labels.get(i) != null) {
                    natureScene = labels.get(i).toLowerCase();
                    for (int k = 0; k < excludeNatureLabels.length; k++) {
                        if (natureScene.equals(excludeNatureLabels[k])) {
                            if(i!=labels.size())
                                labels.remove(i); } } } }

            if(labels.size()!=0)
                return " a natural view for "+labels.get(0)+"s";
            return " a picture of a natural view ";

        }// end if naturalScenery

    // if it's not a natural view
        return null; }

    //(10) general labels
    public String getBestLabel (ArrayList<String> labels) {

        String firstLabel="";
        String secondLabel="";
        String finalLabel ;

        //to remove any verbs or adjectives from the received labels.
        for (int i = labels.size() - 1; i >= 0; i--) {
            if (labels.get(i) != null) {
                firstLabel = labels.get(i).toLowerCase();
                for (int k = 0; k < excludVerbsLabels.length; k++) {
                    if (firstLabel.equals(excludVerbsLabels[k]))
                        labels.remove(i); } } }


        finalLabel= labels.get(0);
        return finalLabel;
        //do not delete additional
        //sometimes first and second label are the same , this loop is to solve this problem
      /*  if (labels.size() != 0) {
            for (int i = 0; i < labels.size(); i++) {
                if (labels.get(i) != null) {
                    firstLabel = labels.get(i).toLowerCase();
                    if (!(i + 1 >= labels.size()))
                        secondLabel = labels.get(i + 1).toLowerCase();
                    else {
                        finalLabel = firstLabel + firstLabel;
                        return finalLabel;
                    }
                    if (firstLabel.equals(secondLabel)) {
                        secondLabel = labels.get(i + 2);
                    }
                }
            }
            finalLabel += firstLabel + " and " + secondLabel;
            return firstLabel;
        }
        return null;*/
    }
    //(11) These methods shows how colors are identified.
    public void setColorNameFromRgb(int r, int g, int b) {
//initiate color list
        ArrayList<ColorName> colorList = initColorList();
        ColorName closestMatch = null;
        int minMSE = Integer.MAX_VALUE;
        int mse;
//send RGB values to get the name of the color
        for (ColorName c : colorList) {
            mse = c.computeMSE(r, g, b);
            if (mse < minMSE) {
                minMSE = mse;
                closestMatch = c;}}
        if (closestMatch != null) {
//set color name
            receivedColor= closestMatch.getName();
        } else {
            receivedColor=null;
        }
    }// end getcolor

    public void getColorNameFromHex(int hexColor) {
        int r = (hexColor & 0xFF0000) >> 16;
        int g = (hexColor & 0xFF00) >> 8;
        int b = (hexColor & 0xFF);
        setColorNameFromRgb(r, g, b);
    } // end getcolorhex


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


    //setters
    public void setOCRText (  List<EntityAnnotation> logos,List<EntityAnnotation> texts){
        StringBuilder TextOCR = new StringBuilder();
        String Logo ="";
        //logos
        if (logos != null) {
            for (EntityAnnotation logo : logos) {
                Logo = String.format(Locale.getDefault(), "%.3f: %s", logos.get(0).getLocale(), logo.getDescription());
                Logo = Logo.substring(4); } }
         if (texts != null) {
            ocrtext= texts.get(0).getDescription();
            if (logos != null){
                ocrtext = ocrtext + "from" + Logo + " company"; }
            ocrtext = ocrtext.toLowerCase();
            ocrtext= ocrtext.replaceAll("[\r\n]+", " "); } }// end setOCR



    //getters

    public String getOcrtext(){

        return ocrtext;
    }

    public String getFacialExpression(){

        return facialExpression;
    }
    public String getColor(){

        return receivedColor;
    }


    public String getLabel(){

        return label;
    }
}//end class
