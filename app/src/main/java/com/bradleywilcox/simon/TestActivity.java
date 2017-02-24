package com.bradleywilcox.simon;

import android.graphics.Color;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class TestActivity extends AppCompatActivity implements View.OnClickListener, ISimonActivity{
    Button btnGreen, btnRed, btnYellow, btnBlue, btnStart;
    RadioButton rbNormal, rbReverse, rbTurbo;
    TextView tvScore, tvHighScore;
    private Simon simon;
    private SimonRunner simonRunner;
    private Timer timer;
    private Player player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        btnGreen = (Button)findViewById(R.id.button);
        btnRed = (Button)findViewById(R.id.button2);
        btnYellow = (Button)findViewById(R.id.button3);
        btnBlue = (Button)findViewById(R.id.button4);
        btnStart = (Button)findViewById(R.id.btnStart);

        tvScore = (TextView) findViewById(R.id.tvScore);
        tvHighScore = (TextView) findViewById(R.id.tvHighScore);

        rbNormal = (RadioButton) findViewById(R.id.rbNormal);
        rbReverse = (RadioButton) findViewById(R.id.rbReverse);
        rbTurbo = (RadioButton) findViewById(R.id.rbTurbo);

        btnGreen.setBackgroundColor(Color.WHITE);
        btnRed.setBackgroundColor(Color.WHITE);
        btnYellow.setBackgroundColor(Color.WHITE);
        btnBlue.setBackgroundColor(Color.WHITE);

        btnGreen.setOnClickListener(this);
        btnRed.setOnClickListener(this);
        btnYellow.setOnClickListener(this);
        btnBlue.setOnClickListener(this);
        btnStart.setOnClickListener(this);

        player = Player.loadPlayer(this);
    }

    @Override
    public void onClick(View view) {
        cancelTimer();

        if(view == btnStart){
            resetAllButtonDisplays();
            cancelSimonRunner();

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
            btnGreen.setBackgroundColor(Color.GREEN);
        else if(button == Simon.Buttons.red.ordinal())
            btnRed.setBackgroundColor(Color.RED);
        else if(button == Simon.Buttons.yellow.ordinal())
            btnYellow.setBackgroundColor(Color.YELLOW);
        else if(button == Simon.Buttons.blue.ordinal())
            btnBlue.setBackgroundColor(Color.BLUE);
        else{
            resetAllButtonDisplays();
        }
    }

    private void resetAllButtonDisplays(){
        btnGreen.setBackgroundColor(Color.WHITE);
        btnRed.setBackgroundColor(Color.WHITE);
        btnYellow.setBackgroundColor(Color.WHITE);
        btnBlue.setBackgroundColor(Color.WHITE);
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
