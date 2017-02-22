package com.bradleywilcox.simon;

import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class TestActivity extends AppCompatActivity implements View.OnClickListener, ISimonActivity{
    Button btnGreen, btnRed, btnYellow, btnBlue, btnStart, btnStartReverse, btnStartTurbo;
    private Simon simon;
    private SimonRunner simonRunner;
    private Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        btnGreen = (Button)findViewById(R.id.button);
        btnRed = (Button)findViewById(R.id.button2);
        btnYellow = (Button)findViewById(R.id.button3);
        btnBlue = (Button)findViewById(R.id.button4);
        btnStart = (Button)findViewById(R.id.btnStart);
        btnStartReverse = (Button)findViewById(R.id.btnStartReverse);
        btnStartTurbo = (Button)findViewById(R.id.btnStartTurbo);

        btnGreen.setBackgroundColor(Color.WHITE);
        btnRed.setBackgroundColor(Color.WHITE);
        btnYellow.setBackgroundColor(Color.WHITE);
        btnBlue.setBackgroundColor(Color.WHITE);

        btnGreen.setVisibility(View.INVISIBLE);
        btnYellow.setVisibility(View.INVISIBLE);
        btnRed.setVisibility(View.INVISIBLE);
        btnBlue.setVisibility(View.INVISIBLE);

        btnGreen.setOnClickListener(this);
        btnRed.setOnClickListener(this);
        btnYellow.setOnClickListener(this);
        btnBlue.setOnClickListener(this);
        btnStart.setOnClickListener(this);
        btnStartReverse.setOnClickListener(this);
        btnStartTurbo.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        cancelTimer();

        if(view == btnStart){
            btnGreen.setVisibility(View.VISIBLE);
            btnYellow.setVisibility(View.VISIBLE);
            btnRed.setVisibility(View.VISIBLE);
            btnBlue.setVisibility(View.VISIBLE);
            btnStart.setVisibility(View.INVISIBLE);
            btnStartReverse.setVisibility(View.INVISIBLE);
            btnStartTurbo.setVisibility(View.INVISIBLE);

            simon = new Simon(Simon.GameMode.normal);
            startSimonRunner();
        }
        else if(view == btnStartReverse){
            btnGreen.setVisibility(View.VISIBLE);
            btnYellow.setVisibility(View.VISIBLE);
            btnRed.setVisibility(View.VISIBLE);
            btnBlue.setVisibility(View.VISIBLE);
            btnStart.setVisibility(View.INVISIBLE);
            btnStartReverse.setVisibility(View.INVISIBLE);
            btnStartTurbo.setVisibility(View.INVISIBLE);

            simon = new Simon(Simon.GameMode.backwards);
            startSimonRunner();
        }
        else if(view == btnStartTurbo){
            btnGreen.setVisibility(View.VISIBLE);
            btnYellow.setVisibility(View.VISIBLE);
            btnRed.setVisibility(View.VISIBLE);
            btnBlue.setVisibility(View.VISIBLE);
            btnStart.setVisibility(View.INVISIBLE);
            btnStartReverse.setVisibility(View.INVISIBLE);
            btnStartTurbo.setVisibility(View.INVISIBLE);

            simon = new Simon(Simon.GameMode.turbo);
            startSimonRunner();
        }
        else {

            boolean isCorrect = false;

            if (view == btnGreen) {
                isCorrect = simon.isPressCorrect(Simon.Buttons.green);
            } else if (view == btnRed) {
                isCorrect = simon.isPressCorrect(Simon.Buttons.red);
            } else if (view == btnYellow) {
                isCorrect = simon.isPressCorrect(Simon.Buttons.yellow);
            } else if (view == btnBlue) {
                isCorrect = simon.isPressCorrect(Simon.Buttons.blue);
            }

            if(!isCorrect){
                Toast.makeText(this, "Wrong Guess, You Lose", Toast.LENGTH_LONG).show();
            }else if(simon.isPlayerTurnOver()){
                // all guesses where correct, add another to simon
                simon.addToPattern();
                startSimonRunner();
            }else{
                //it was a correct guess, so start timer over
                startTimer();
            }
        }
    }

    private void startSimonRunner(){
        if(simonRunner != null && simonRunner.getStatus() == AsyncTask.Status.FINISHED) {
            simonRunner = null;
        }

        simonRunner = new SimonRunner(this, simon);
        simonRunner.execute();
    }

    private void startTimer(){
        cancelTimer();
        // 5 seconds for normal modes, 1 second for turbo mode
        timer = new Timer(this, simon.getGameMode() == Simon.GameMode.turbo ? 1 : 5);
        timer.execute();
    }

    private void cancelTimer(){
        if(timer != null) {
            timer.cancel(true);
            timer = null;
        }
    }


    @Override
    public void updateButtons(int button) {
        if(button == Simon.Buttons.green.ordinal())
            btnGreen.setBackgroundColor(Color.GREEN);
        else if(button == Simon.Buttons.red.ordinal())
            btnRed.setBackgroundColor(Color.RED);
        else if(button == Simon.Buttons.yellow.ordinal())
            btnYellow.setBackgroundColor(Color.YELLOW);
        else if(button == Simon.Buttons.blue.ordinal())
            btnBlue.setBackgroundColor(Color.BLUE);
        else{
            btnGreen.setBackgroundColor(Color.WHITE);
            btnRed.setBackgroundColor(Color.WHITE);
            btnYellow.setBackgroundColor(Color.WHITE);
            btnBlue.setBackgroundColor(Color.WHITE);
        }
    }

    @Override
    public void timerDone(){
        Toast.makeText(this, "Time up, YOU LOSE", Toast.LENGTH_LONG).show();
    }

    @Override
    public void runnerDone(){
       startTimer();
    }
}
