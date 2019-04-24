package com.example.recipesapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.recipesapp.percistand_lvl.LvlRepository;
import com.example.recipesapp.percistand_lvl.Score;
import com.example.recipesapp.percistand_lvl.ScoreRepository;
import com.example.recipesapp.percistand_lvl.ThingsMade;
import com.example.recipesapp.rv_adapters.PrevRecipeAdapter;

import java.util.List;

public class LvlDetailActivity extends AppCompatActivity implements PrevRecipeAdapter.ItemClickListener{


    private ImageView imgLvl;
    private TextView txtLvl;
    private TextView txtLvlName;

    private SharedPreferences sp;
    private SharedPreferences.Editor editor;

    private PrevRecipeAdapter adapter;
    LvlParse lvlParse;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.lvl_detail);

        sp = getSharedPreferences("RecipeId", 0);

        imgLvl = findViewById(R.id.imgLvl);
        txtLvl = findViewById(R.id.txtLvl);
        txtLvlName = findViewById(R.id.txtLvlName);

        Intent mIntent = getIntent();

        lvlParse = mIntent.getParcelableExtra("lvlObject");

        int id = getResources().getIdentifier(
                "com.example.recipesapp:drawable/" + lvlParse.getCurrentLvlPicture(),
                null, null);

        imgLvl.setImageResource(id);

        txtLvl.setText("Level " + String.valueOf(lvlParse.getCurrentLvl()) + ":");
        txtLvlName.setText(lvlParse.getCurrentLvlDescription());

        initRecyclerView();
    }

    public void initRecyclerView(){
        RecyclerView recyclerView = findViewById(R.id.rvPrev);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));



        adapter = new PrevRecipeAdapter(this,
                lvlParse.getArrTitleOfThingsMade(),
                lvlParse.getArrUrlPictureOfThingsMade());

        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(View view, int position) {

        String id = lvlParse.getIdOfThingsMade(position);
        String title = adapter.getItem(position);

        editor = sp.edit();
        editor.putString("id", id);
        editor.apply();

        this.finish();
    }


    public void onClickReset(View v) {

        //Reset the database
        final LvlRepository lvlRepository = new LvlRepository(getApplicationContext());

        lvlRepository.getTasks().observe(this, new Observer<List<ThingsMade>>() {
            @Override
            public void onChanged(@Nullable List<ThingsMade> thingsmadeList) {
                for(ThingsMade thingsMade : thingsmadeList) {
                    lvlRepository.deleteTask(thingsMade);
                }
            }
        });


        final ScoreRepository scoreRepository = new ScoreRepository(getApplicationContext());

        scoreRepository.getScores().observe(this, new Observer<List<Score>>() {
            @Override
            public void onChanged(@Nullable List<Score> scoreList) {
                for(Score score : scoreList) {
                    scoreRepository.deleteTask(score);
                }
            }
        });


        Intent i = new Intent();
        this.setResult(Activity.RESULT_OK, i);
        this.finish();
    }

    @Override
    public void onBackPressed() {
        this.finish();
    }
}
