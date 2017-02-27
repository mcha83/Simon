package com.bradleywilcox.simon;

import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Set;

public class GameActivity extends AppCompatActivity implements View.OnClickListener, ISimonActivity{
    ImageButton btnGreen, btnRed, btnYellow, btnBlue;
    Button btnStart;
    RadioButton rbNormal, rbReverse, rbTurbo;
    TextView tvScore, tvHighScore;
    private Simon simon;
    private SimonRunner simonRunner;
    private Timer timer;
    private Player player;
    MediaPlayer btnClk, btnLose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

         btnClk = MediaPlayer.create(this,R.raw.tone);
        btnLose = MediaPlayer.create(this,R.raw.buzz);

        btnGreen = (ImageButton)findViewById(R.id.imageButton);
        btnRed = (ImageButton)findViewById(R.id.imageButton2);
        btnYellow = (ImageButton)findViewById(R.id.imageButton4);
        btnBlue = (ImageButton)findViewById(R.id.imageButton3);
        btnStart = (Button)findViewById(R.id.btnStart);

        tvScore = (TextView) findViewById(R.id.tvScore);
        tvHighScore = (TextView) findViewById(R.id.tvHighScore);

        rbNormal = (RadioButton) findViewById(R.id.rbNormal);
        rbReverse = (RadioButton) findViewById(R.id.rbReverse);
        rbTurbo = (RadioButton) findViewById(R.id.rbTurbo);

        btnGreen.setOnClickListener(this);
        btnRed.setOnClickListener(this);
        btnYellow.setOnClickListener(this);
        btnBlue.setOnClickListener(this);
        btnStart.setOnClickListener(this);


        disableSimonButtons();

        player = Player.loadPlayer(this);
    }

    @Override
    public void onClick(View view) {
        cancelTimer();


        if(view == btnStart){
            resetAllButtonDisplays();
            cancelSimonRunner();
            if(btnLose.isPlaying())
            {
                btnLose.pause();
                btnLose.seekTo(0);
            }


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

        else {
            if(btnClk.isPlaying())
            {
                btnClk.pause();
                btnClk.seekTo(0);
            }

            boolean isCorrect = false;

            if (view == btnGreen) {
                isCorrect = simon.isPressCorrect(Simon.Buttons.green);
                btnClk.start();
            } else if (view == btnRed) {
                isCorrect = simon.isPressCorrect(Simon.Buttons.red);
                btnClk.start();
            } else if (view == btnYellow) {
                isCorrect = simon.isPressCorrect(Simon.Buttons.yellow);
                btnClk.start();
            } else if (view == btnBlue) {
                isCorrect = simon.isPressCorrect(Simon.Buttons.blue);
                btnClk.start();
            }

            if(!isCorrect){
                Toast.makeText(this, "Wrong Guess, You Lose", Toast.LENGTH_LONG).show();
                cancelTimer();
                cancelSimonRunner();
                btnClk.pause();
                btnLose.start();


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
        disableSimonButtons();

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
        btnGreen.setImageResource(R.drawable.green_selector);
        btnRed.setImageResource(R.drawable.red_selector);
        btnYellow.setImageResource(R.drawable.yellow_selector);
        btnBlue.setImageResource(R.drawable.blue_selector);
    }

    private void disableSimonButtons(){
        btnGreen.setClickable(false);
        btnRed.setClickable(false);
        btnYellow.setClickable(false);
        btnBlue.setClickable(false);
    }

    private void enableSimonButtons(){
        btnGreen.setClickable(true);
        btnRed.setClickable(true);
        btnYellow.setClickable(true);
        btnBlue.setClickable(true);
    }

    @Override
    public void timerDone(){

        btnLose.start();
        Toast.makeText(this, "Time up, YOU LOSE", Toast.LENGTH_LONG).show();

    }

    @Override
    public void runnerDone(){
        enableSimonButtons();
        startTimer();
    }

    @Override
    public void onPause(){
        super.onPause();
        cancelSimonRunner();
        cancelTimer();

        player.Save(this);
    }
}
