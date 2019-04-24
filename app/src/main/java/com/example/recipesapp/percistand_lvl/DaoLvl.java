package com.example.recipesapp.percistand_lvl;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;


import java.util.List;

@Dao
public interface DaoLvl {

    @Insert
    Long insertTask(ThingsMade thingsMade);


    @Query("SELECT * FROM ThingsMade")
    LiveData<List<ThingsMade>> fetchAllTasks();


    @Query("SELECT * FROM ThingsMade WHERE id =:taskId")
    LiveData<ThingsMade> getTask(int taskId);

    @Update
    void updateTask(ThingsMade thingsMade);


    @Delete
    void deleteTask(ThingsMade thingsMade);
}