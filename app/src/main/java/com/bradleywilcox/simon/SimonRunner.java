package com.bradleywilcox.simon;

import android.os.AsyncTask;
import android.util.Log;

/**
 * Bradley Wilcox / Michael Cha
 * CSCI 4020
 * Assignment 2
 */

public class SimonRunner extends AsyncTask<Void, Integer, Void> {

    private Simon simon;
    private ISimonActivity activity;
    private int buttonOnTime;
    private int buttonOffTime;

    public SimonRunner(ISimonActivity activity, Simon simon){
        this.activity = activity;
        this.simon = simon;
        if(simon.getGameMode() == Simon.GameMode.turbo){
            buttonOnTime = 250;
            buttonOffTime = 125;
        }else{
            buttonOnTime = 1000;
            buttonOffTime = 500;
        }
    }

    @Override
    protected Void doInBackground(Void... voids){

        try {
            Thread.sleep(1000);
        }catch(InterruptedException e){

        }

        for(int p : simon.getPattern()) {
            if(isCancelled())
                return null;

            try {
                Log.i("SIMON", p + "");

                publishProgress(p);
                // time to display the button 'ON'
                Thread.sleep(buttonOnTime);

                publishProgress(-1);
                // time to pause after turning button 'OFF' before turning next one 'ON'
                Thread.sleep(buttonOffTime);

            } catch (InterruptedException e) {

            }
        }

        Log.i("SIMON", "____________________");

        return null;
    }

    @Override
    protected  void onPreExecute(){
        ///try {
         //   Thread.sleep(1000);
       // }catch(InterruptedException e){

       // }
    }

    @Override
    protected void onProgressUpdate(Integer... values){
        activity.updateButtons(values[0]);
    }

    @Override
    protected void onPostExecute(Void v){
        super.onPostExecute(v);

        activity.runnerDone();
    }


}
