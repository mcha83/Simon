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

    public SimonRunner(ISimonActivity activity, Simon simon){
        this.activity = activity;
        this.simon = simon;
    }

    @Override
    protected Void doInBackground(Void... voids){

        for(int p : simon.getPattern()) {
            try {
                Log.i("SIMON", p + "");

                publishProgress(p);
                // time to display the button 'ON'
                Thread.sleep(1000);

                publishProgress(-1);
                // time to pause after turning button 'OFF' before turning next one 'ON'
                Thread.sleep(500);

            } catch (Exception e) {

            }
        }

        Log.i("SIMON", "____________________");

        return null;
    }

    @Override
    protected void onProgressUpdate(Integer... values){
        activity.updateButtons(values[0]);
    }

    @Override
    protected void onPostExecute(Void v){
        super.onPostExecute(v);


    }


}
