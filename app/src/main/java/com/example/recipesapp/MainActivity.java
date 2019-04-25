package com.example.recipesapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.recipesapp.api_retrieval.Recipe;
import com.example.recipesapp.api_retrieval.RecipeMetaData;
import com.example.recipesapp.api_retrieval.RecipeService;
import com.example.recipesapp.percistand_lvl.LvlRepository;
import com.example.recipesapp.percistand_lvl.Score;
import com.example.recipesapp.percistand_lvl.ScoreRepository;
import com.example.recipesapp.percistand_lvl.ThingsMade;
import com.example.recipesapp.rv_adapters.IngredientsAdapter;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MainActivity extends AppCompatActivity implements FragmentA.FragmentAListener{
    public static final String baseUrl = "https://www.food2fork.com/";

    private RecyclerView recyclerView;

    private TextView tv_advice;
    private TextView tvScore;

    private ImageView img_food;

    private Button btn_ad;
    private Button btnMadeIt;
    private Button btnUrl;
    private ConstraintLayout constraintLayout2;

    private FragmentA fragmentA;
    private LvlParse lvlParse;

    public String shownRecipeId;

    private int keyIndex = 0;
    private ArrayList<String> arrKeys;

    /*
    We will log every time we enter one of the methods from the activity lifecycle
    It visualizes the android activity lifecycle
     */
    private static final String TAGState = "State";

    RecipeMetaData recipeMetaData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_AppCompat_Light_NoActionBar);
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_main);

        Log.v(TAGState, "onCreate");

        shownRecipeId = "";

        init();

        SharedPreferences prefs = getSharedPreferences("RecipeId", 0);
        String s = prefs.getString("savedId", null); // getting String

        if(s != null){
            shownRecipeId = s;
        }

        //Only run when app starts up
        if(savedInstanceState == null){
            connectToApi();
            updateLvlParse();
        }

        onInputASentLvlParse(lvlParse);
    }

    public void init(){
        fragmentA = (FragmentA) getSupportFragmentManager().findFragmentById(R.id.fragA);

        //String u = "https://goatme.me/m/BkiSKRQO.png";

        //The call limit per key for this API is 50 a day
        //By adding some keys and swapping them if they reached their limit we got 550 calls pr 24h
        arrKeys = new ArrayList<String>(Arrays.asList(
                "6ac17c21d8a3ce115023b92875a4dc33",
                "900bc72cf1f24643e18f28bc9410c6b0",
                "e2235303bb349e6cafbf72b7a3db1e1e",
                "5008128790b6a519bde84c27cf72c491",
                "54bedfdc5093e377ecbd08b52a4af329",
                "289500df2b806d9adc1a777f75e68c88",
                "ed3eed1040c6ed9307cbdcf86f98a8da",
                "9aee021e407ec9c8efaadbf8b8b93963",
                "f979a1e2b026ac55ffe469bd04f9e048",
                "ae2fc1aafb2fa5af99e4929b268e60f7",
                "9193293ab2c42efe1a9d53b25475fe25"
        ));

        recyclerView = findViewById(R.id.rvIngredients);

        tv_advice = (TextView) findViewById(R.id.tv_advice);
        tvScore = (TextView) findViewById(R.id.tvScore);
        img_food = (ImageView) findViewById(R.id.img_food);

        btn_ad = findViewById(R.id.btn_ad);
        btnMadeIt = findViewById(R.id.btnMadeIt);
        btnUrl = findViewById(R.id.btnURL);

        constraintLayout2 = findViewById(R.id.constraintLayout2);

        setNoPicture();

        lvlParse = new LvlParse();
    }


    //This section prevents data lose when changing screen orientation
    //When rotating the screen onCreate will be called again, therefor we need to save
    //Some variables and objects to prevent data loss
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.v(TAGState, "onSaveInstanceState");
        outState.putParcelable("r", recipeMetaData);

        outState.putParcelable("lvl", lvlParse);
        super.onSaveInstanceState(outState);
    }

    //This restores the data from the saved instance
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState){
        Log.v(TAGState, "onRestoreState");
        recipeMetaData = savedInstanceState.getParcelable("r");
        lvlParse = savedInstanceState.getParcelable("lvl");

        onInputASentLvlParse(lvlParse);
        fillInData();
        super.onRestoreInstanceState(savedInstanceState);
    }

    //When the we return to this activity this is called
    @Override
    protected void onResume(){
        super.onResume();
        Log.v(TAGState, "onResume");
        constraintLayout2.setEnabled(true); //Just prevents double press

        SharedPreferences sp = getSharedPreferences("RecipeId", 0);
        shownRecipeId = sp.getString("id", "");

        if(!shownRecipeId.equals("")){
            connectToApi();

            SharedPreferences.Editor editor = sp.edit();
            editor.putString("id", "");
            editor.apply();
        }
    }

    //When we call a new activity and expect a return after the child activity finishes,
    //This function is called when we reenter the parent activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.v(TAGState, "onActivityResult");

        if(requestCode == 1 && resultCode == Activity.RESULT_OK){
            //Reset the lvlParse
            lvlParse.resetLvlParse();

            btnMadeIt.setText(getString(R.string.btn_made));
            btnMadeIt.setEnabled(true);

            madeRecipeToDB();
            onInputASentLvlParse(lvlParse);
        }
    }

    //Fragment finished
    public void onClickBtn(View v) {
        shownRecipeId = "";
        connectToApi();
        btnMadeIt.setText(getString(R.string.btn_made));
        btnMadeIt.setEnabled(true);                 //NB if we by chance should get a already made recipe

    }

    //Click on the fragment constraint
    public void onClickDetail(View v){
        constraintLayout2.setEnabled(false);
        Intent intent = new Intent(v.getContext(), LvlDetailActivity.class);
        intent.putExtra("lvlObject", lvlParse);

        /*
        Starting an child activity this way will give the option
        to pass a object back to the parent activity
         */
        startActivityForResult(intent,1);

    }

    public void onClickBtnURL(View v) {

        if(recipeMetaData != null) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(recipeMetaData.getSourceUrl()));
            startActivity(intent);
        }

    }

    public void onClickMadeIt(View v) {
        if(recipeMetaData != null){

            btnMadeIt.setText(getString(R.string.btn_already_made));
            btnMadeIt.setEnabled(false);

            lvlParse.increaseScore((int) recipeMetaData.getSocialRank());

            Toast.makeText(this, "+ " + String.valueOf((int) recipeMetaData.getSocialRank()) + " points",
                    Toast.LENGTH_LONG).show();

            lvlParse.addArrTitleOfThingsMade(recipeMetaData.getTitle());
            lvlParse.addArrIdOfThingsMade(recipeMetaData.getRecipeId());
            lvlParse.addArrUrlPictureOfThingsMade(recipeMetaData.getImageUrl());

            Log.d("HELLO", this.toString());
            onInputASentLvlParse(lvlParse);

            ScoreRepository scoreRepository = new ScoreRepository(getApplicationContext());

            //As we only have one profile our primarykey key will always be 1
            Score s = new Score();
            s.setId(1);
            s.setScore(lvlParse.getScore());

            //If the the key already exist room will automatically ignore the insert task thanks to
            //@Insert(onConflict = OnConflictStrategy.IGNORE)
            scoreRepository.insertTask(s);
            scoreRepository.updateTask(s);

            madeRecipeToDB();
        }
    }

    public void madeRecipeToDB(){
        LvlRepository lvlRepository = new LvlRepository(getApplicationContext());

        String titleOfThing = recipeMetaData.getTitle();
        String idOfThing = recipeMetaData.getRecipeId();
        String urlOfThing = lvlParse.makeURLHttps((recipeMetaData.getImageUrl()));

        lvlRepository.insertTask(false, titleOfThing, idOfThing, urlOfThing);

    }

    /*
    This will enter all of the saved data into our lvlParse Object
     */
    public void updateLvlParse(){

        LvlRepository lvlRepository = new LvlRepository(getApplicationContext());
        final ArrayList<String> arrTitle = new ArrayList<>();
        final ArrayList<String> arrId = new ArrayList<>();
        final ArrayList<String> arrUrl = new ArrayList<>();

        lvlRepository.getTasks().observe(this, new Observer<List<ThingsMade>>() {
            @Override
            public void onChanged(@Nullable List<ThingsMade> thingsmadeList) {
                for(int i = 0; i < thingsmadeList.size(); i++){

                    ThingsMade thingsMade = thingsmadeList.get(i);
                    arrTitle.add(thingsMade.getTitleOfThing());
                    arrId.add(thingsMade.getIdOfThing());
                    arrUrl.add(thingsMade.getUrlOfThing());
                }

                lvlParse.setArrTitleOfThingsMade(arrTitle);
                lvlParse.setArrIdOfThingsMade(arrId);
                lvlParse.setArrUrlPictureOfThingsMade(arrUrl);
            }
        });

        ScoreRepository scoreRepository = new ScoreRepository(getApplicationContext());

        scoreRepository.getScores().observe(this, new Observer<List<Score>>() {
            @Override
            public void onChanged(@Nullable List<Score> scoreList) {
                try{
                    lvlParse.setScore(scoreList.get(0).getScore());
                }catch(IndexOutOfBoundsException e){
                    lvlParse.setScore(0);
                }

                onInputASentLvlParse(lvlParse);

            }
        });
    }

    public void fillInData(){
        if(recipeMetaData != null) {

            tv_advice.setText(recipeMetaData.getTitle());
            if(lvlParse.getArrTitleOfThingsMade().contains(tv_advice.getText())){
                btnMadeIt.setText(getString(R.string.btn_already_made));
                btnMadeIt.setEnabled(false);
            }
            tvScore.setText("Points to get: " + String.valueOf((int) recipeMetaData.getSocialRank()) + " ");


            recyclerView.setVisibility(View.VISIBLE);

            recyclerView.setLayoutManager(new LinearLayoutManager(this));

            List<String> arrIng = recipeMetaData.getIngredients();

            IngredientsAdapter adapter = new IngredientsAdapter(this, arrIng);
            //adapter.setClickListener(thisActivity);
            recyclerView.setAdapter(adapter);

            //String u = "https://static.food2fork.com/Jalapeno2BPopper2BGrilled2BCheese2BSandwich2B12B500fd186186.jpg";

            String u = recipeMetaData.getImageUrl();
            u = u.substring(0, 4) + "s" + u.substring(4);

            new ImageDownloadTask()
                    .execute(u);

            btnUrl.setVisibility(View.VISIBLE);
            btn_ad.setVisibility(View.VISIBLE);
            btnMadeIt.setVisibility(View.VISIBLE);


            //This code will save the id of the current recipe,
            //So when we restart the app we can load in the same recipe as before
            SharedPreferences sp = getSharedPreferences("RecipeId", 0);
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("savedId", recipeMetaData.getRecipeId());
            editor.apply();

            //onInputASentLvl(lvl);

            //onInputASentLvl(0);
        }

    }


    public void setNoPicture(){
        img_food.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.nopicture));
        recyclerView.setVisibility(View.GONE);

        btn_ad.setText(getString(R.string.btn_nxt_recipe));

        btnUrl.setVisibility(View.GONE);
        btn_ad.setVisibility(View.GONE);
        btnMadeIt.setVisibility(View.GONE);

        tv_advice.setText(getString(R.string.txt_loading));

    }

    /*
    This is where we connect to the API
    */
    public void connectToApi(){
        connectToApi(shownRecipeId);
    }

    public void connectToApi(final String shown){
        setNoPicture();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RecipeService recipeService = retrofit.create(RecipeService.class);


        //Ids go from 517 to 54695
        Log.d("Show id", shown+ " saved id");

        //if we don't want to display a previous displayed
        //recipe we random generate a id for a new one
        int idOfCurrentItem;
        if(shown.equals("")){
            int min = 517;
            int max = 54695;
            idOfCurrentItem = new Random().nextInt(max - min + 1) + min;
        }else{
            idOfCurrentItem = Integer.valueOf(shown);
        }

        //This is a quick fix, and im fully aware how disgusting this code is
        //This is just for saving the id temporary;
        SharedPreferences sp = getSharedPreferences("ReallyBadCode", 0);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("id", shown);
        editor.apply();

        //35381
        //Here we call the recipeService interface
        Call<Recipe> recipeCall = recipeService.getAnswers(arrKeys.get(keyIndex), idOfCurrentItem);
        Log.d("apiKey", arrKeys.get(keyIndex) + "    " + String.valueOf(keyIndex));

        recipeCall.enqueue(new Callback<Recipe>() {
            @Override
            public void onResponse(Call<Recipe> call, Response<Recipe> response) {
                //Our un-elegant way to check if we get a valid response
                try{
                   String s = response.body().getRecipe().getTitle();

                }catch(Exception e){
                    //arrKeys.remove(keyIndex); //this removes a faulty key, may cause trouble
                    if(keyIndex < arrKeys.size() - 1){
                        arrKeys.remove(keyIndex);
                        keyIndex++;

                        //The pinnacle of the shitty way to ensure access to a variable in a local class
                        SharedPreferences prefs = getSharedPreferences("ReallyBadCode", 0);
                        String s = prefs.getString("id", "");

                        connectToApi(s);
                    }else{
                        tv_advice.setText(getString(R.string.txt_daily_limit));
                    }
                }

                recipeMetaData = response.body().getRecipe();
                fillInData();
            }

            @Override
            public void onFailure(Call<Recipe> call, Throwable t) {
                t.printStackTrace();
                tv_advice.setText(getString(R.string.txt_failed_to_connect));

                setNoPicture();
                btn_ad.setText(getString(R.string.txt_reload));
                btn_ad.setVisibility(View.VISIBLE);
            }
        });
    }


    //Send to fragment
    @Override
    public void onInputASentLvlParse(LvlParse p) {
        fragmentA.updateLvlParse(p);
    }

    /*
    (Week 8) This is a Async task to prevent the UI from freezing while loading the img
     */
    @SuppressLint("StaticFieldLeak")
    private class ImageDownloadTask extends AsyncTask<String, Void, Bitmap> {

        protected Bitmap doInBackground(String... urls) {
            Bitmap mIcon11 = null;

            try {
                BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                bmOptions.inSampleSize = 1; // 1 = 100% if you write 4 means 1/4 = 25%
                mIcon11 = BitmapFactory.decodeStream((InputStream)new URL(urls[0]).getContent(), null, bmOptions);
            } catch (Exception e) {

                Log.e("Error", e.getMessage());
                //e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            img_food.setImageBitmap(result);
        }
    }

    /*
    This function is for Debugging only
    It prints out all the entries of the Recipe made table
    */
    private void printTableContent(){
        LvlRepository lvlRepository = new LvlRepository(getApplicationContext());
        lvlRepository.getTasks().observe(this, new Observer<List<ThingsMade>>() {
            @Override
            public void onChanged(@Nullable List<ThingsMade> thingsmadeList) {
                if (thingsmadeList != null) {
                    for(ThingsMade thingsMade : thingsmadeList){
                        String TAG = "Table";

                        Log.v(TAG, "-----------------------");
                        Log.v(TAG, thingsMade.getTitleOfThing());
                        Log.v(TAG, thingsMade.getIdOfThing());
                        Log.v(TAG, thingsMade.getUrlOfThing());
                    }
                }
            }
        });
    }

}
