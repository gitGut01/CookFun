package com.example.recipesapp.percistand_lvl;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class ThingsMade implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String titleOfThing;
    private String idOfThing;
    private String urlOfThing;

    private int forignKey;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitleOfThing() {
        return titleOfThing;
    }

    public void setTitleOfThing(String titleOfThing) {
        this.titleOfThing = titleOfThing;
    }

    public String getIdOfThing() {
        return idOfThing;
    }

    public void setIdOfThing(String idOfThing) {
        this.idOfThing = idOfThing;
    }

    public String getUrlOfThing() {
        return urlOfThing;
    }

    public void setUrlOfThing(String urlOfThing) {
        this.urlOfThing = urlOfThing;
    }


    public int getForignKey() {
        return forignKey;
    }

    public void setForignKey(int forignKey) {
        this.forignKey = forignKey;
    }


}