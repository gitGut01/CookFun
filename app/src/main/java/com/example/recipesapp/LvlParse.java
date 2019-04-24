package com.example.recipesapp;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;

public class LvlParse implements Parcelable {

    private String lvlName;
    private int score = 0;
    private int currentLvl = 0;

    private ArrayList<String> arrTitleOfThingsMade;
    private ArrayList<String> arrIdOfThingsMade;
    private ArrayList<String> arrUrlPictureOfThingsMade;

    public ArrayList<String> getArrUrlPictureOfThingsMade() {
        return arrUrlPictureOfThingsMade;
    }

    public void setArrUrlPictureOfThingsMade(ArrayList<String> arrUrlPictureOfThingsMade) {
        this.arrUrlPictureOfThingsMade = arrUrlPictureOfThingsMade;
    }

    private ArrayList<String> arrLvlName;
    private ArrayList<String> arrPicture;
    private ArrayList<Integer> arrLvlRange;

    private double nextLvlScore;
    private double currLvlScore;



    public LvlParse(){
        init();
    }

    public void init(){
        nextLvlScore = 0;
        currLvlScore = 0;
        arrLvlName = new ArrayList<String>(
                Arrays.asList(
                        "Student", "\"Gamer\"", "I like cookies", "Lord of the kitchen", "Vegan",
                        "Gotta Cook 'Em All", "Survival expert", "The big money laundry", "Crook",
                        "That's how mafia works", "Epic Roblox warrior",
                        "Your most favorite cooking show", "Your second most favorite cooking show",
                        "To eat, or not to eat", "McDonalds Employee", "Its undercooked",
                        "Now its overcooked", "Guy Fieri", "Gordon Ramsay", "Kitchen God",
                        "Grandma"));

        arrPicture = new ArrayList<String>(Arrays.asList(
                "lvl_1", "lvl_2", "lvl_3", "lvl_4", "lvl_5",
                "lvl_6", "lvl_7", "lvl_8", "lvl_9", "lvl_10",
                "lvl_11", "lvl_12", "lvl_13", "lvl_14", "lvl_15",
                "lvl_16", "lvl_17", "lvl_18", "lvl_19", "lvl_20",
                "lvl_21"));

        /*
        arrLvlRange = new ArrayList<Integer>(Arrays.asList(
                100, 200, 300, 400,
                500, 600, 700, 800, 900,
                1000, 1100, 1200, 1300, 1450,
                1600, 1800, 2000, 2200, 2400,
                2500));
        */
        arrLvlRange = new ArrayList<Integer>(Arrays.asList(
                100, 200, 300, 400,
                500, 600, 700, 800, 900,
                1000, 1100, 1200, 1300, 1400,
                1500, 1600, 1700, 1800, 1900,
                2000));

        arrTitleOfThingsMade = new ArrayList<String>();
        arrIdOfThingsMade = new ArrayList<String>();
        arrUrlPictureOfThingsMade = new ArrayList<>();
    }

    protected LvlParse(Parcel in) {
        nextLvlScore = in.readDouble();
        currLvlScore = in.readDouble();
        lvlName = in.readString();
        score = in.readInt();
        currentLvl = in.readInt();
        arrTitleOfThingsMade = in.createStringArrayList();
        arrIdOfThingsMade = in.createStringArrayList();
        arrUrlPictureOfThingsMade = in.createStringArrayList();
        arrLvlName = in.createStringArrayList();
        arrPicture = in.createStringArrayList();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(nextLvlScore);
        dest.writeDouble(currLvlScore);
        dest.writeString(lvlName);
        dest.writeInt(score);
        dest.writeInt(currentLvl);
        dest.writeStringList(arrTitleOfThingsMade);
        dest.writeStringList(arrIdOfThingsMade);
        dest.writeStringList(arrUrlPictureOfThingsMade);
        dest.writeStringList(arrLvlName);
        dest.writeStringList(arrPicture);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<LvlParse> CREATOR = new Creator<LvlParse>() {
        @Override
        public LvlParse createFromParcel(Parcel in) {
            return new LvlParse(in);
        }

        @Override
        public LvlParse[] newArray(int size) {
            return new LvlParse[size];
        }
    };

    public int getLvlByScore(int score) {

        if(arrLvlRange == null){
            Log.v("DODO", String.valueOf(score));
        }


        for (int i = 0; i < arrLvlRange.size(); i++) {
            if (arrLvlRange.get(i) > score) {
                return i;
            }
        }

        return arrLvlRange.size();

    }

    public void increaseScore(int points) {
        this.score += points;
        currentLvl = getLvlByScore(score);
    }

    public String getCurrentLvlDescription(){
        return arrLvlName.get(currentLvl);
    }

    public String getCurrentLvlPicture(){
        return arrPicture.get(currentLvl);
    }


    public void addArrTitleOfThingsMade(String s) {
        this.arrTitleOfThingsMade.add(s);
    }

    public void addArrIdOfThingsMade(String s) {
        this.arrIdOfThingsMade.add(s);
    }

    public void addArrUrlPictureOfThingsMade(String s) {

        this.arrUrlPictureOfThingsMade.add(makeURLHttps(s));
    }


    public String getIdOfThingsMade(int pos){
        return arrIdOfThingsMade.get(pos);
    }

    public String getUrlPictureOfTHingsMade(int pos){
        return arrUrlPictureOfThingsMade.get(pos);
    }



    public String getLvlName() {
        return lvlName;
    }

    public void setLvlName(String lvlName) {
        this.lvlName = lvlName;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
        currentLvl = getLvlByScore(score);
    }

    public int getCurrentLvl() {
        return currentLvl;
    }

    public void setCurrentLvl(int currentLvl) {
        this.currentLvl = currentLvl;
    }

    public ArrayList<String> getArrTitleOfThingsMade() {
        return arrTitleOfThingsMade;
    }

    public void setArrTitleOfThingsMade(ArrayList<String> arrTitleOfThingsMade) {
        this.arrTitleOfThingsMade = arrTitleOfThingsMade;
    }

    public ArrayList<String> getArrIdOfThingsMade() {
        return arrIdOfThingsMade;
    }

    public void setArrIdOfThingsMade(ArrayList<String> arrIdOfThingsMade) {
        this.arrIdOfThingsMade = arrIdOfThingsMade;
    }

    public ArrayList<String> getArrLvlName() {
        return arrLvlName;
    }

    public void setArrLvlName(ArrayList<String> arrLvlName) {
        this.arrLvlName = arrLvlName;
    }

    public ArrayList<String> getArrPicture() {
        return arrPicture;
    }

    public void setArrPicture(ArrayList<String> arrPicture) {
        this.arrPicture = arrPicture;
    }

    public ArrayList<Integer> getArrLvlRange() {
        return arrLvlRange;
    }

    public void setArrLvlRange(ArrayList<Integer> arrLvlRange) {
        this.arrLvlRange = arrLvlRange;
    }


    //The returned URL from food2fork is faulty and should be a https instead of a http
    //This fixes that
    public String makeURLHttps(String s){
        return s.substring(0, 4) + "s" + s.substring(4);
    }

    public void resetLvlParse(){
        currentLvl = 0;
        score = 0;
        arrTitleOfThingsMade = new ArrayList<String>();
        arrIdOfThingsMade = new ArrayList<String>();
        arrUrlPictureOfThingsMade = new ArrayList<String>();

    }

    public int getProgressToNxtLvl(){


        // 30, 60, 90, 120, 150
        //10


        //10/30 * 100


        //40/60 * 100

        //

        // (x - [nextLvl - curLvl]/ [nextLvl - curLvl]) * 100

        //double nextLvlScore = (double) arrLvlRange.get(getLvlByScore(score));
        //double currentLvlScore = (double) arrLvlRange.get(getLvlByScore(score));

        //int k = (int) ((((double) score - currentLvlScore)/(nextLvlScore - currentLvlScore)) * 100);

        int currLvl = getLvlByScore(score);
        int nextLvl = currLvl + 1;

        nextLvlScore = (double) arrLvlRange.get(currLvl);

        if(currLvl != 0){
            currLvlScore = (double) arrLvlRange.get(currLvl - 1);
        }else{
            currLvlScore = 0;
        }

        int k = (int)((((double) score - currLvlScore)/(nextLvlScore - currLvlScore))*100);
        System.out.println(" - -  - - - - - - - - - - ");

        System.out.println(nextLvlScore);
        System.out.println(currLvlScore);


        System.out.println(nextLvl);
        System.out.println(currLvl);
        System.out.println(" - -  - - - - - - - - - - " + k );
        return k;

    }

    public int getCurrLvlScore(){
        return (int) currLvlScore;
    }

    public int getNxtLvlScore(){
        return (int) nextLvlScore;
    }
}
