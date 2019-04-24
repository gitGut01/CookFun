package com.example.recipesapp.percistand_lvl;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.os.AsyncTask;

import java.util.List;

public class ScoreRepository {

    private String DB_NAME = "db_lvl";

    private LvlDatabase lvlDatabase;

    public ScoreRepository(Context context) {
        lvlDatabase = Room.databaseBuilder(context, LvlDatabase.class, DB_NAME).build();
    }

    public void insertTask() {
        Score score = new Score();
        insertTask(score);
    }



    public void insertTask(final Score score) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {

                lvlDatabase.daoScore().insertTask(score);
                return null;
            }
        }.execute();
    }

    public void updateTask(final Score score) {


        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                lvlDatabase.daoScore().updateTask(score);
                return null;
            }
        }.execute();
    }

    public void deleteTask(final int id) {
        final LiveData<Score> task = getScore(id);
        if(task != null) {
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    lvlDatabase.daoScore().deleteTask(task.getValue());
                    return null;
                }
            }.execute();
        }
    }

    public void deleteTask(final Score score) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                lvlDatabase.daoScore().deleteTask(score);
                return null;
            }
        }.execute();
    }

    public LiveData<Score> getScore(int id) {
        return lvlDatabase.daoScore().getTask(id);
    }

    public LiveData<List<Score>> getScores() {
        return lvlDatabase.daoScore().fetchAllScores();
    }



}