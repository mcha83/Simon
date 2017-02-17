package com.bradleywilcox.simon;

import android.os.AsyncTask;
import android.util.Log;

/**
 * Bradley Wilcox / Michael Cha
 * CSCI 4020
 * Assignment 2
 */

public class Timer extends AsyncTask<Void, Void, Void> {

    ISimonActivity activity;

    public Timer(ISimonActivity activity){
        this.activity = activity;
    }

    @Override
    protected Void doInBackground(Void... voids) {

        for(int i = 0; i < 5; i++){

            try {
                Thread.sleep(1000);
                Log.i("TIME", i+1 + " Seconds");

            }catch(Exception e){

            }

        }

        return null;
    }

    @Override
    protected void onPostExecute(Void v){
        super.onPostExecute(v);

        activity.timerDone();
    }
}
