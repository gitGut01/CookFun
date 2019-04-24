package com.example.recipesapp.percistand_lvl;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;


@Database(entities = {ThingsMade.class, Score.class}, version = 1, exportSchema = false)
public abstract class LvlDatabase extends RoomDatabase {

    public abstract DaoLvl daoLvl();

    public abstract DaoScore daoScore();
}