package com.example.illuminate_me;
import com.google.api.services.vision.v1.model.EntityAnnotation;
import com.google.api.services.vision.v1.model.FaceAnnotation;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class Recognizer {

    //variables
    private int numberofpersons=0;
    private String person ;
    private String receivedColor;
    private String ocrtext="";
    String label;
    private ArrayList<String> receivedLabels = new ArrayList<String>();

    //to find gender
    private String[] maleLabels = { "male","beard","facialhair","moustache","macho"};
    private String [] manLabel = {"man","gentleman","businessman","men", "macho man","father","guy","grandfather","old man"};
    private String[] womanLabel = {"woman" , "lady" , "mam","businesswoman","gentlewoman","mother","old woman", "women","grandmother"};
    private String[] childrenLabels = { "child" , "baby", "girl" , "boy" ,"baby laughing"};
    private String[] genderLabels = {"male","female"};

    //to describe people
    private String[] emotionLabels = {"smile","laugh","laughing","crying","smiling"};
    private String[] wearingsLabels = {"white coat","coat","stethoscope","cowboy hat","sun hat","hat","dress","t-shirt","jeans","headgear","fashion accessory"};
    private String[] describeHairLabels={"blond","blonde","short hair","brown hair","black hair","long hair"};

    //table lebls
    private String[] onLabels ={"table","desk","shelf","side table","coffee table","sofa tables","outdoor table","dresser","night stand","writing desk","computer desk","drawer","chest of drawers"};
    private String[] ExcludeTableLabels={"office","room","wood","furniture","metal","technology","floor","flooring","interior design","building","rectangle","writing office","office writing","marble","hutch","wood stain","interior design","end table","hardwood","living room","chair","solid wood","tile","tiles","iron"};

    //text labels
    private String[] textLabels={"street sign","sign","traffic sign","signage","book","notebook","diary","paper product","paper","product","document"};
    private String [] excludeTextLabels = {"text","line","font","calligraphy","word","clip art","handwriting","witting","number","ink"};

    //food labels
    //baharat , Indian Cuisine , Baked Goods, Dessert,"vegetarian food"
    private String[] ExcludefoodLabels ={"food","meat","dish","plate","natural foods","indian cuisine ","dessert","baked goods","superfood","plant","gluten","vegan nutrition","cruciferous vegetables","recipe","cuisine","brunch","breakfast","dinner","lunch","cooking","snack","produce","kids' meal","junk food","ingredient","sweetness","finger food","fast food","baking"};

    //nature arrays
    private String [] excludeNatureLabels = {"blue","natural view","natural views","klippe","spring","moss","rock","vascular plant","terrestrial plant","tributary","arroyo","fluvial landforms of streams","stream bed","riparian forest","riparian zone","mist","yellow","atmospheric phenomenon","plain","field","massif","sunlight","branch","path","dirt road","infrastructure","park","national park","fence","state park","maple leaf","autumn","red","green","house","reservoir","fell","tarn","tourist attraction","elaeis","arecales","leisure","resort","sound","cape","world","drainage basin","headland","terrain","spit","shore","promontory","inlet","vacation","tourism","wildlife","theatrical scenery","adaptation","tropics","walkway","annual plant","rhododendron","maple","deciduous","state park","groundcover","temperate broadleaf and mixed forest","shrub","botany","woody plant","bird's-eye view","terrace","aerial photography","thoroughfare","plant community","road","grass","biome","body of water","water feature","hill station","water resource","water","water resources","stream","watercourse","calm","leaf","reflection","spring ","wilderness"};
    private String [] natureLabel = {"nature","highland","headland","landscaping","natural view","natural views","vegetation", "natural landscape" ,"nature reserve","natural environment","nature landscape", "landscape"};

    //for general labels
    private String [] excludVerbsLabels = {"drink","drinking","eat","eating","sitting","standing","swimming"};



    public String getLabel(ArrayList<String>labels) {

        String firstLabel;
        String wearings;
        String hair ;

        // papers,books etc.
        label = findBestLabel(labels, textLabels);
        if (label != null) {
            if (ocrtext != null)
                return label + " written on it: ";
            else
                return receivedColor + " " + label;
        }

        //natural scenery
        label = getNaturalScenery(labels);
        if (label != null) {
            return label;
        }

        // person
        wearings = getWearings(labels);
        hair = getHair(labels);

        //men
        if (label == null) {
            label = findBestLabel(labels, manLabel);
            if (label != null) {
                if (numberofpersons > 1) {
                    label = " men ";
                    wearings = wearings + "s";

                    if(wearings!=null)
                        return person + " " + label + " " + wearings;
                    return person + " " + label;
                }

                if(wearings!=null)
                    return person + " " + label + " " + wearings;
                else
                if(hair!=null)
                    return "a " + person + " " + label+" "+hair;
                return "a " + person + " " + label;
            }
        }

        label= findBestLabel(labels,maleLabels);
        if (label != null) {
            if (numberofpersons > 1) {
                label = " men ";
                wearings = wearings + "s";

                if(wearings!=null)
                    return person + " " + label + " " + wearings;
                return person + " " + label;
            }

            if(wearings!=null)
                return person + " " + label + " " + wearings;
            else
            if(hair!=null)
                return "a " + person + " " + label+" "+hair;
            return "a " + person + " " + label;
        }

        //women
        label = findBestLabel(labels,womanLabel);
        if (label != null) {
            if (numberofpersons > 1) {
                label = " women ";
                wearings = wearings + "s";

                if(wearings!=null)
                    return person + " " + label + " " + wearings;
                return person + " " + label;
            }

            if(wearings!=null)
                return person + " " + label + " " + wearings;
            else
            if(hair!=null)
                return "a " + person + " " + label+" "+hair;
            return "a " + person + " " + label;
        }


        //children
        label = findBestLabel(labels,childrenLabels);
        if (label != null) {
            if (numberofpersons > 1) {
                label = " children ";
                wearings = wearings + "s";

                if(wearings!=null)
                    return person + " " + label + " " + wearings;
                return person + " " + label;
            }

            if(wearings!=null)
                return " a "+person + " " + label + " " + wearings;
            else
            if(hair!=null)
                return "a " + person + " " + label+" "+hair;
            return "a " + person + " " + label;
        }


        //if it did not recognize the gender but recognized the facial expression , or wearings
        if (label==null&& wearings != null){
            if(numberofpersons>1)
                return " persons  "+wearings;
            else
                return "a person  "+wearings;}

        if (label==null&& person!= null){
            if(numberofpersons>1)
                return person+" people";
           else{
                if(hair!=null){
                  return "a "+person+" person "+hair;
                }
                return "a "+person+" person ";
            }
               }

        // tables
        label= getThingsOnTable(labels);
        if(label!=null)
            return label;

        // food : fruits , vegetables etc.
        label = getFood(labels);
        if (label != null) {
            return label; }

         firstLabel= getBestLabel(labels);
        return firstLabel;

    }//end method getLabels


    //(1) Man, Woman, Child
    public String findBestLabel (ArrayList<String>labels, String [] compare){
        String label ="";
        for(int i =0; i< labels.size(); i++) {
            if (labels.get(i) != null) {
                label = labels.get(i).toLowerCase();
                for (int j = 0; j < compare.length; j++) {
                    if (label.equals(compare[j])) {
                        if (label.equals("text") || label.equals("line") || label.equals("font") || label.equals("calligraphy") || label.equals("word") || label.equals("clip art") || label.equals("handwriting") || label.equals("witting") || label.equals("number")) {
                            label = "written text: ";
                            return label;
                        }
                        if (label.equals("male") || label.equals("facialhair") || label.equals("moustache") || label.equals("macho") || label.equals("beard"))
                            label = " man";
                        if (label.equals("child model"))
                            label = "child";
                        if (label.equals("child") || label.equals("baby") || label.equals("child model"))
                            label = getChildGender(labels, label);

                        return label;
                    }
                }
            }
        }
        return null;
    }

    //(2)Child gender
    public String getChildGender(ArrayList<String>labels , String child){
        String gender="";
        for(int i =0; i< labels.size(); i++) {
            if (labels.get(i) != null) {
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
        }
        return child ;
    }
    //(3) Wearings
    public String getWearings(ArrayList<String>labels  ){
        String wearing="";
        for(int i =0; i< labels.size(); i++) {
            if (labels.get(i) != null) {
                wearing = labels.get(i).toLowerCase();
                for (int j = 0; j < wearingsLabels.length; j++) {
                    if (wearing.equals(wearingsLabels[j])) {
                        if (receivedColor != null)
                            return " wearing  " + receivedColor + " " + wearing;
                        return " wearing  " + wearing;
                    }

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
        for(int i=0;i<labels.size();i++) {
            if (labels.get(i) != null) {
                table = labels.get(i).toLowerCase();
                for (int k = 0; k < onLabels.length; k++) {
                    if (table.equals(onLabels[k])) {  //break if you find the type of table
                        table = onLabels[k];
                        count = 1;
                        break;
                    }
                }
                if (count == 1)
                    break;
            }
        }
        //if a kind of table exists
        if(count==1) {
            //remove table , furniture ......
            for(int i=things.size()-1;i>=0;i--) {
                if (things.get(i) != null) {
                    onTable = things.get(i).toLowerCase();
                    for (int k = 0; k < onLabels.length; k++) {
                        if (onTable.equals(onLabels[k]))
                            if (i < things.size())
                                things.remove(i);
                        //if i<things.size() why is it required ?? above it works without it
                    }
                }
            }
            //need optimization
            for(int i=things.size()-1;i>=0;i--) {
                if (things.get(i) != null) {
                    onTable = things.get(i).toLowerCase();
                    for (int k = 0; k < ExcludeTableLabels.length; k++) {
                        if (onTable.equals(ExcludeTableLabels[k]))
                            if (i < things.size())
                                things.remove(i);
                    }
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

        String food= "";
        ArrayList<String>foods=labels;

        for(int i=foods.size()-1;i>=0;i--) {
            if (foods.get(i) != null) {
                food = foods.get(i).toLowerCase();
                for (int k = 0; k < ExcludefoodLabels.length; k++) {
                    if (food.equals(ExcludefoodLabels[k]))
                        foods.remove(i);
                }
            }
        }
        if(foods.size()!=0)
            return foods.get(0)+" ";
        return null;
    } // end getFood

    //(6) Nature
    public String getNaturalScenery (ArrayList<String>labels){

        boolean isNaturalScenery=false;
        String natureScene="";

        //to search in the received list for any label that represents a natural scenery (natureLabel)
        for(int i =0; i< labels.size(); i++) {
            if (labels.get(i) != null) {
                natureScene = labels.get(i).toLowerCase();
                for (int j = 0; j < natureLabel.length; j++) {
                    if (natureScene.equals(natureLabel[j])) {
                        isNaturalScenery = true;
                    }
                } }
        }

        //to remove excludeNatureLabels from the received array (labels) to get correct results
        if(isNaturalScenery){

            for(int i=labels.size()-1;i>=0;i--) {
                if (labels.get(i) != null) {
                    natureScene = labels.get(i).toLowerCase();
                    for (int k = 0; k < natureLabel.length; k++) {
                        if (natureScene.equals(natureLabel[k]))
                            labels.remove(i);
                    }
                }
            }
            for(int i=labels.size()-1;i>=0;i--) {
                if (labels.get(i) != null) {
                    natureScene = labels.get(i).toLowerCase();
                    for (int k = 0; k < excludeNatureLabels.length; k++) {
                        if (natureScene.equals(excludeNatureLabels[k])) {
                            if(i!=labels.size())
                                labels.remove(i);
                        }
                    }
                }
            }
            if(labels.size()!=0)
                return " a natural view for "+labels.get(0)+"s";
            return " a picture for a natural view ";

        }// end if naturalScenery


        return null;
    }

    //(7) hair
    public String getHair (ArrayList<String>labels) {
        String hair = "";

        //to search in the received list for any label that represents a natural scenery (natureLabel)
        for (int i = 0; i < labels.size(); i++) {
            if (labels.get(i) != null) {
                hair = labels.get(i).toLowerCase();
                for (int j = 0; j < describeHairLabels.length; j++) {
                    if (hair.equals(describeHairLabels[j])) {
                        if (hair.equals("blond")||hair.equals("blonde"))
                            hair = " blond hair";
                        return "has a " +hair;
                    }
                }
            }
        }

        return null ;
    }

    //(8) colors

    public void setColorNameFromRgb(int r, int g, int b) {

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

        //(9) general labels
    public String getBestLabel (ArrayList<String> labels) {

        String firstLabel="";
        String secondLabel="";
        String finalLabel = receivedColor + " ";

        //to remove any verbs or adjectives from the received array
        for (int i = labels.size() - 1; i >= 0; i--) {
            if (labels.get(i) != null) {
                firstLabel = labels.get(i).toLowerCase();
                for (int k = 0; k < excludVerbsLabels.length; k++) {
                    if (firstLabel.equals(excludVerbsLabels[k]))
                        labels.remove(i);
                }
            }
        }

        finalLabel= receivedColor+ " "+labels.get(0);
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
    //setters
    public void setOCR (  List<EntityAnnotation> logos,List<EntityAnnotation> texts){

        StringBuilder TextOCR = new StringBuilder();
        String Logo ="";

        //logos
        if (logos != null) {
            for (EntityAnnotation logo : logos) {
                Logo = String.format(Locale.getDefault(), "%.3f: %s", logos.get(0).getLocale(), logo.getDescription());
                Logo = Logo.substring(4);

            }
        }

        if (texts != null) {
            ocrtext= texts.get(0).getDescription();
            if (logos != null){
                ocrtext = ocrtext + "from" + Logo + " company";
            }
            ocrtext = ocrtext.toLowerCase();
            ocrtext= ocrtext.replaceAll("[\r\n]+", " "); }

        // ocrtext=null;

    }// end setOCR

    public void setFacialExpressions (List<FaceAnnotation> faces ){
        person = "";
        numberofpersons=0;

        if (faces != null) {
            for (FaceAnnotation face : faces) {
                numberofpersons++;

                String joy = String.format(face.getJoyLikelihood());
                if (joy.equals("VERY_LIKELY") || joy.equals("LIKELY") || joy.equals("POSSIBLE"))
                    person = "  happy ";
                String sorrow = String.format(face.getSorrowLikelihood());
                if (sorrow.equals("VERY_LIKELY") || sorrow.equals("LIKELY") || sorrow.equals("POSSIBLE"))
                    person = "  sad ";
                String anger = String.format(face.getAngerLikelihood());
                if (anger.equals("VERY_LIKELY") || anger.equals("LIKELY") || anger.equals("POSSIBLE"))
                    person = "  angry ";
                String surprise = String.format(face.getSurpriseLikelihood());
                if (surprise.equals("VERY_LIKELY") || surprise.equals("LIKELY") || surprise.equals("POSSIBLE"))
                    person = "  surprised ";
               /* if (numberofpersons > 1 ){
                    //person=+person;
                    TextfacialExpressions.append("and "+person);} else

                    TextfacialExpressions.append(person);

            }*/
            }
        }

    }//end setFacialExpressions

    //getters

    public String getOcrtext(){

        return ocrtext;
    }

    public String getFacialExpression(){

        return person;
    }
    public String getColor(){

        return receivedColor;
    }


    public String getLabel(){

        return label;
    }
}//end class
