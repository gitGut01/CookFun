package com.example.recipesapp.percistand_lvl;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface DaoScore {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    Long insertTask(Score score);


    @Query("SELECT * FROM Score")
    LiveData<List<Score>> fetchAllScores();


    @Query("SELECT * FROM Score WHERE id =:taskId")
    LiveData<Score> getTask(int taskId);


    @Update
    void updateTask(Score score);


    @Delete
    void deleteTask(Score score);
}