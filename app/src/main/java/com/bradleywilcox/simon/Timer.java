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
    private int seconds;

    public Timer(ISimonActivity activity, int seconds){
        this.activity = activity;
        this.seconds = seconds;
    }

    @Override
    protected Void doInBackground(Void... voids) {

        for(int i = 0; i < seconds; i++){
            if(isCancelled())
                return null;

            try {
                Thread.sleep(1000);
                Log.i("TIME", i+1 + " Seconds");

            }catch(InterruptedException e){

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
