package com.bradleywilcox.simon;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Button btnHowTo, btnBegin, btnAbout;
    MediaPlayer gmStrt2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnHowTo = (Button) findViewById(R.id.btnHowTo);
        btnBegin = (Button) findViewById(R.id.btnBegin);
        btnAbout = (Button) findViewById(R.id.btnAbout);

        btnHowTo.setOnClickListener(this);
        btnBegin.setOnClickListener(this);
        btnAbout.setOnClickListener(this);

        gmStrt2 = MediaPlayer.create(this, R.raw.intro);
        gmStrt2.start();
        gmStrt2.setLooping(true);
    }

    @Override
    public void onClick(View view) {

        gmStrt2.release();
        if(view == btnHowTo)
            startActivity(new Intent(this, howto.class));
        else if(view == btnBegin)
            startActivity(new Intent(this, GameActivity.class));
        else if(view == btnAbout)
            startActivity(new Intent(this, About.class));

    }
}
