package com.example.recipesapp.percistand_lvl;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.os.AsyncTask;


import java.util.List;

public class LvlRepository {

    private String DB_NAME = "db_lvl";

    private LvlDatabase lvlDatabase;

    public LvlRepository(Context context) {
        lvlDatabase = Room.databaseBuilder(context, LvlDatabase.class, DB_NAME).build();
    }

    public void insertTask(Boolean tmp, String titleOfThing, String idOfThing, String urlOfThing) {


        ThingsMade thingsMade = new ThingsMade();

        thingsMade.setTitleOfThing(titleOfThing);
        thingsMade.setIdOfThing(idOfThing);
        thingsMade.setUrlOfThing(urlOfThing);

        thingsMade.setForignKey(1);


        insertTask(thingsMade);
    }



    public void insertTask(final ThingsMade thingsMade) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {

                lvlDatabase.daoLvl().insertTask(thingsMade);
                return null;
            }
        }.execute();
    }

    public void updateTask(final ThingsMade thingsMade) {


        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                lvlDatabase.daoLvl().updateTask(thingsMade);
                return null;
            }
        }.execute();
    }

    public void deleteTask(final int id) {
        final LiveData<ThingsMade> task = getTask(id);
        if(task != null) {
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    lvlDatabase.daoLvl().deleteTask(task.getValue());
                    return null;
                }
            }.execute();
        }
    }

    public void deleteTask(final ThingsMade thingsMade) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                lvlDatabase.daoLvl().deleteTask(thingsMade);
                return null;
            }
        }.execute();
    }

    public LiveData<ThingsMade> getTask(int id) {
        return lvlDatabase.daoLvl().getTask(id);
    }

    public LiveData<List<ThingsMade>> getTasks() {
        return lvlDatabase.daoLvl().fetchAllTasks();
    }

}