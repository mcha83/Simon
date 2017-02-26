package com.bradleywilcox.simon;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class TestActivity extends AppCompatActivity implements View.OnClickListener, ISimonActivity{
    ImageButton btnGreen, btnRed, btnYellow, btnBlue;
    Button btnStart, btnhowTo, btnBegn, btnAbout, btnQuit;
    RadioButton rbNormal, rbReverse, rbTurbo;
    TextView tvScore, tvHighScore;
    private Simon simon;
    private SimonRunner simonRunner;
    private Timer timer;
    private Player player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        btnGreen = (ImageButton)findViewById(R.id.imageButton);
        btnRed = (ImageButton)findViewById(R.id.imageButton2);
        btnYellow = (ImageButton)findViewById(R.id.imageButton4);
        btnBlue = (ImageButton)findViewById(R.id.imageButton3);
        btnStart = (Button)findViewById(R.id.btnStart);
        btnBegn = (Button)findViewById(R.id.button6);
        btnhowTo = (Button)findViewById(R.id.button7);
        btnAbout = (Button)findViewById(R.id.button8);
        btnQuit = (Button)findViewById(R.id.button9);

        btnGreen.setVisibility(View.INVISIBLE);
        btnRed.setVisibility(View.INVISIBLE);
        btnBlue.setVisibility(View.INVISIBLE);
        btnYellow.setVisibility(View.INVISIBLE);
        btnStart.setVisibility(View.INVISIBLE);
        btnQuit.setVisibility(View.INVISIBLE);

        tvScore = (TextView) findViewById(R.id.tvScore);
        tvHighScore = (TextView) findViewById(R.id.tvHighScore);
        tvScore.setVisibility(View.INVISIBLE);
        tvHighScore.setVisibility(View.INVISIBLE);

        rbNormal = (RadioButton) findViewById(R.id.rbNormal);
        rbReverse = (RadioButton) findViewById(R.id.rbReverse);
        rbTurbo = (RadioButton) findViewById(R.id.rbTurbo);
        rbNormal.setVisibility(View.INVISIBLE);
        rbReverse.setVisibility(View.INVISIBLE);
        rbTurbo.setVisibility(View.INVISIBLE);


        btnGreen.setOnClickListener(this);
        btnRed.setOnClickListener(this);
        btnYellow.setOnClickListener(this);
        btnBlue.setOnClickListener(this);
        btnStart.setOnClickListener(this);
        btnBegn.setOnClickListener(this);
        btnhowTo.setOnClickListener(this);
        btnAbout.setOnClickListener(this);
        btnQuit.setOnClickListener(this);

        player = Player.loadPlayer(this);
    }

    @Override
    public void onClick(View view) {
        cancelTimer();


        if(view == btnStart){
            resetAllButtonDisplays();
            cancelSimonRunner();

            btnStart.setVisibility(View.INVISIBLE);
            rbNormal.setVisibility(View.INVISIBLE);
            rbTurbo.setVisibility(View.INVISIBLE);
            rbReverse.setVisibility(View.INVISIBLE);
            btnGreen.setVisibility(View.VISIBLE);
            btnRed.setVisibility(View.VISIBLE);
            btnBlue.setVisibility(View.VISIBLE);
            btnYellow.setVisibility(View.VISIBLE);
            tvScore.setVisibility(View.VISIBLE);
            tvHighScore.setVisibility(View.VISIBLE);


            if(rbNormal.isChecked()){
                simon = new Simon(Simon.GameMode.normal);
            }
            else if(rbReverse.isChecked()){
                simon = new Simon(Simon.GameMode.backwards);
            }
            else if(rbTurbo.isChecked()){
                simon = new Simon(Simon.GameMode.turbo);
            }

            player.resetScore();

            setScoreDisplay(0);
            setHighScoreDisplay(player.getHighScore(simon.getGameMode()));

            startSimonRunner();
        }
        else if(view==btnhowTo)
        {
            startActivity(new Intent(this, howto.class));
        }
        else if(view==btnBegn)
        {
            btnBegn.setVisibility(View.INVISIBLE);
            rbTurbo.setVisibility(View.VISIBLE);
            rbReverse.setVisibility(View.VISIBLE);
            rbNormal.setVisibility(View.VISIBLE);
            btnStart.setVisibility(View.VISIBLE);
            btnhowTo.setVisibility(View.INVISIBLE);
            btnAbout.setVisibility(View.INVISIBLE);
        }
        else if(view==btnAbout)
        {
            startActivity(new Intent(this, About.class));
        }
        else if(view==btnQuit)
        {
            startActivity(new Intent(this, TestActivity.class));
        }

        else {

            boolean isCorrect = false;

            if (view == btnGreen) {
                isCorrect = simon.isPressCorrect(Simon.Buttons.green);
                btnGreen.setImageResource(R.drawable.grnbri);
                //pause//
                btnGreen.setImageResource(R.drawable.grndul);

            } else if (view == btnRed) {
                isCorrect = simon.isPressCorrect(Simon.Buttons.red);
                btnRed.setImageResource(R.drawable.redbri);
                //pause//
                btnRed.setImageResource(R.drawable.reddul);

            } else if (view == btnYellow) {
                isCorrect = simon.isPressCorrect(Simon.Buttons.yellow);
                btnYellow.setImageResource(R.drawable.yelbri);
                //pause//
                btnYellow.setImageResource(R.drawable.yeldul);

            } else if (view == btnBlue) {
                isCorrect = simon.isPressCorrect(Simon.Buttons.blue);
                btnBlue.setImageResource(R.drawable.blubri);
                //pause//
                btnBlue.setImageResource(R.drawable.bludul);

            }
            if(!isCorrect){
                Toast.makeText(this, "Wrong Guess, You Lose", Toast.LENGTH_LONG).show();
                cancelTimer();
                cancelSimonRunner();

                btnGreen.setVisibility(View.INVISIBLE);
                btnRed.setVisibility(View.INVISIBLE);
                btnBlue.setVisibility(View.INVISIBLE);
                btnYellow.setVisibility(View.INVISIBLE);
                btnQuit.setVisibility(View.VISIBLE);

            }else if(simon.isPlayerTurnOver()){
                // all guesses where correct, add another to simon
                simon.addToPattern();
                player.incrementScore();
                player.setHighScore(simon.getGameMode());
                startSimonRunner();

                setScoreDisplay(player.getCurrentScore());
                setHighScoreDisplay(player.getHighScore(simon.getGameMode()));

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

    private void cancelSimonRunner(){
        if(simonRunner != null){
            simonRunner.cancel(true);
            simonRunner = null;
        }
    }

    private void setScoreDisplay(int score){
        tvScore.setText("Score " + score);
    }

    private void setHighScoreDisplay(int score){
        tvHighScore.setText("High Score " + score);
    }


    @Override
    public void updateButtons(int button) {
        if(button == Simon.Buttons.green.ordinal())
            btnGreen.setImageResource(R.drawable.grnbri);
        else if(button == Simon.Buttons.red.ordinal())
            btnRed.setImageResource(R.drawable.redbri);
        else if(button == Simon.Buttons.yellow.ordinal())
            btnYellow.setImageResource(R.drawable.yelbri);
        else if(button == Simon.Buttons.blue.ordinal())
            btnBlue.setImageResource(R.drawable.blubri);
        else{
            resetAllButtonDisplays();
        }
    }

    private void resetAllButtonDisplays(){
        btnGreen.setImageResource(R.drawable.grndul);
        btnRed.setImageResource(R.drawable.reddul);
        btnYellow.setImageResource(R.drawable.yeldul);
        btnBlue.setImageResource(R.drawable.bludul);
    }

    @Override
    public void timerDone(){
        Toast.makeText(this, "Time up, YOU LOSE", Toast.LENGTH_LONG).show();
    }

    @Override
    public void runnerDone(){
       startTimer();
    }

    @Override
    public void onPause(){
        super.onPause();
        player.Save(this);
    }
}
