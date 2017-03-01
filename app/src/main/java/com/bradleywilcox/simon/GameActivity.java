package com.bradleywilcox.simon;

import android.media.AudioAttributes;
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

import java.util.HashSet;
import java.util.Set;
import java.util.jar.Attributes;


public class GameActivity extends AppCompatActivity implements View.OnClickListener, ISimonActivity{
    ImageButton btnGreen, btnRed, btnYellow, btnBlue;
    Button btnStart;
    RadioButton rbNormal, rbReverse, rbTurbo;
    TextView tvScore, tvHighScore;
    private Simon simon;
    private SimonRunner simonRunner;
    private Timer timer;
    private Player player;

    private SoundPool soundPool;
    private Set<Integer> soundsLoaded;
    private int soundGreenId, soundRedId, soundYellowId, soundBlueId, soundLoseId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        soundsLoaded = new HashSet<Integer>();

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
                soundPool.play(soundGreenId, 1.0f, 1.0f, 0, 0, 1.0f);
            } else if (view == btnRed) {
                isCorrect = simon.isPressCorrect(Simon.Buttons.red);
                soundPool.play(soundRedId, 1.0f, 1.0f, 0, 0, 1.0f);
            } else if (view == btnYellow) {
                isCorrect = simon.isPressCorrect(Simon.Buttons.yellow);
                soundPool.play(soundYellowId, 1.0f, 1.0f, 0, 0, 1.0f);
            } else if (view == btnBlue) {
                isCorrect = simon.isPressCorrect(Simon.Buttons.blue);
                soundPool.play(soundBlueId, 1.0f, 1.0f, 0, 0, 1.0f);
            }

            if(!isCorrect){
                disableSimonButtons();
                Toast.makeText(this, "Wrong Guess, You Lose", Toast.LENGTH_LONG).show();
                cancelTimer();
                cancelSimonRunner();

                soundPool.play(soundLoseId, 1.0f, 1.0f, 0, 0, 1.0f);

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
        if(button == Simon.Buttons.green.ordinal()) {
            btnGreen.setImageResource(R.drawable.grnbri);
            soundPool.play(soundGreenId, 1.0f, 1.0f, 0, 0, 1.0f);
        }else if(button == Simon.Buttons.red.ordinal()) {
            btnRed.setImageResource(R.drawable.redbri);
            soundPool.play(soundRedId, 1.0f, 1.0f, 0, 0, 1.0f);
        }else if(button == Simon.Buttons.yellow.ordinal()) {
            btnYellow.setImageResource(R.drawable.yelbri);
            soundPool.play(soundYellowId, 1.0f, 1.0f, 0, 0, 1.0f);
        }else if(button == Simon.Buttons.blue.ordinal()) {
            btnBlue.setImageResource(R.drawable.blubri);
            soundPool.play(soundBlueId, 1.0f, 1.0f, 0, 0, 1.0f);
        }else{
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
        disableSimonButtons();
        soundPool.play(soundLoseId, 1.0f, 1.0f, 0, 0, 1.0f);
        Toast.makeText(this, "Time up, You Lose", Toast.LENGTH_LONG).show();
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

        if(soundPool != null){
            soundPool.release();
            soundPool = null;

            soundsLoaded.clear();
        }
    }

    @Override
    public void onResume(){
        super.onResume();

        AudioAttributes.Builder attrBuilder = new AudioAttributes.Builder();
        attrBuilder.setUsage(AudioAttributes.USAGE_GAME);

        SoundPool.Builder spBuilder = new SoundPool.Builder();
        spBuilder.setAudioAttributes(attrBuilder.build());
        spBuilder.setMaxStreams(2);
        soundPool = spBuilder.build();

        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener(){
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status){
                if(status == 0){
                    soundsLoaded.add(sampleId);
                }else{

                }
            }
        });

        soundGreenId = soundPool.load(this, R.raw.tone, 1);
        soundRedId = soundPool.load(this, R.raw.tone2, 1);
        soundYellowId = soundPool.load(this, R.raw.tone3, 1);
        soundBlueId = soundPool.load(this, R.raw.tone4, 1);
        soundLoseId = soundPool.load(this, R.raw.buzz, 1);
    }
}
