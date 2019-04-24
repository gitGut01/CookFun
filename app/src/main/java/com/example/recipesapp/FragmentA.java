package com.example.recipesapp;

import android.arch.persistence.room.Room;
import android.content.Context;

import android.graphics.Color;
import android.os.Bundle;

import android.support.v4.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;


public class FragmentA extends Fragment {

    private ImageView imgLvl;
    private TextView tvLvl;
    private TextView tvLvlDesc;
    private TextView tvLvlPts;

    private TextView tvCurrLvl;
    private TextView tvNxtLvl;

    private ProgressBar progressBar;

    private LvlParse lvlParse;

    private int score = 0;


    private FragmentAListener listener;

    public interface FragmentAListener{
        void onInputASentLvlParse(LvlParse p);

    }


    //Room
    private static final String PROFILE_DATABASE = "profile_db";


    public FragmentA(){

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_a,
                container, false);




        imgLvl = (ImageView) view.findViewById(R.id.imgLvl);
        tvLvl = (TextView) view.findViewById(R.id.tvLvl);
        tvLvlDesc = (TextView) view.findViewById(R.id.tvLvlDesc);
        tvLvlPts = (TextView) view.findViewById(R.id.tvLvlPts);

        tvCurrLvl = (TextView) view.findViewById(R.id.tvCurrLvl);
        tvNxtLvl = (TextView) view.findViewById(R.id.tvNxtLvl);

        progressBar = (ProgressBar) view.findViewById(R.id.progressBar2);




        //Add click listener to the fragment

        /*
        ConstraintLayout clClick =(ConstraintLayout) view.findViewById(R.id.clClick);
        clClick.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.d("HELLO", getActivity().toString());


                Intent intent = new Intent(view.getContext(), LvlDetailActivity.class);
                //LvlParse lp = new LvlParse();
                //Log.d("HELLO", lvl.toString());


                intent.putExtra("lvlObject", lvlParse);

                startActivity(intent);

            }
        });
        */

        return view;
    }


    //Implementing interface methods for fragments

    public void updateLvlParse(final LvlParse lvlParse){

        //imgLvl.setImageBitmap(lvlParse.getPicture(lvlNr));
        this.lvlParse = lvlParse;
        tvLvl.setText("Level " + String.valueOf(lvlParse.getCurrentLvl()) + ":");
        tvLvlDesc.setText(lvlParse.getCurrentLvlDescription());


        progressBar.setProgress(lvlParse.getProgressToNxtLvl());
        tvCurrLvl.setText(String.valueOf(lvlParse.getCurrLvlScore()));
        tvNxtLvl.setText(String.valueOf(lvlParse.getNxtLvlScore()));

        progressBar.getProgressDrawable().setColorFilter(
                Color.YELLOW, android.graphics.PorterDuff.Mode.SRC_IN);

        System.out.println(" - - - - - - -  - - - - - - -  - - - - - - - -  - - - - - " + String.valueOf(lvlParse.getProgressToNxtLvl()));

        int id = getResources().getIdentifier(
                "com.example.recipesapp:drawable/" + lvlParse.getCurrentLvlPicture(),
                null, null);

        imgLvl.setImageResource(id);

        tvLvlPts.setText(String.valueOf(lvlParse.getScore()) + " pts");





        /*

        new Thread(new Runnable() {
            @Override
            public void run() {
                Profile p =new Profile();
                p.setProfileId("1");
                p.setCurrentLvl(lvlParse.getCurrentLvl());
                p.setScore(lvlParse.getScore());
                p.setLvlName(lvlParse.getLvlName());
                profileDatabase.daoAccess() . insertProfileId(p);
            }
        }) .start();

*/
    }

    public void updateScore(int score){
        this.score = score;
        /*
        int lvlNr = lvl.getLvl(score);
        Log.d("HELLO", String.valueOf(lvlNr));

        imgLvl.setImageBitmap(lvl.getPicture(lvlNr));

        tvLvl.setText("Level " + String.valueOf(lvlNr) + ":");
        tvLvlDesc.setText(lvl.getLvlName(lvlNr));

        tvLvlPts.setText(String.valueOf(score) + " pts");
        */
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);

        if(context instanceof  FragmentAListener){
            listener = (FragmentAListener) context;
        }else{
            throw new RuntimeException(context.toString() + "must implement FragmentAListener");
        }

    }

    @Override
    public void onDetach(){
        super.onDetach();
        listener = null;
    }


}
