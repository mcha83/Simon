package com.bradleywilcox.simon;

import android.content.Context;
import android.util.Log;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Bradley Wilcox / Michael Cha
 * CSCI 4020
 * Assignment 2
 */

public class Player implements Serializable{

    private int[] highScores;
    private int currentScore;

    public Player(){
        highScores = new int[Simon.GameMode.values().length];
        resetScore();
    }

    public void resetScore(){
        currentScore = 0;
    }

    public void incrementScore(){
        currentScore++;
        Log.i("PLAYER", currentScore + "");
    }

    public int getCurrentScore(){
        return currentScore;
    }

    public void  setHighScore(Simon.GameMode mode){
        if(highScores[mode.ordinal()] < currentScore)
            highScores[mode.ordinal()] = currentScore;
    }

    public int getHighScore(Simon.GameMode mode){
        return highScores[mode.ordinal()];
    }

    public void show(Simon.GameMode mode){
        Log.i("STUFF", highScores[mode.ordinal()] + "");
    }


    // Save serializable data to file
    public void Save(Context context){
        try {
            FileOutputStream fos = context.openFileOutput("player", Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(this);
            oos.flush();
            oos.close();
        } catch (Exception ex) {
            Log.v("Err Saving Player Data", ex.getMessage());
            ex.printStackTrace();
        }
    }

    // load data from file, or return new instance
    public static Player loadPlayer(Context context){
        try
        {
            FileInputStream fis = context.openFileInput("player");
            ObjectInputStream ois = new ObjectInputStream(fis);
            Object o = ois.readObject();

            return (Player)o;
        }

        catch(Exception ex)
        {
            return new Player();
        }
    }

}
