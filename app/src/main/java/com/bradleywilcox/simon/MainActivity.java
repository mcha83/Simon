package com.bradleywilcox.simon;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Button btnHowTo, btnBegin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnHowTo = (Button) findViewById(R.id.btnHowTo);
        btnBegin = (Button) findViewById(R.id.btnBegin);

        btnHowTo.setOnClickListener(this);
        btnBegin.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        if(view == btnHowTo)
            startActivity(new Intent(this, howto.class));
        else if(view == btnBegin)
            startActivity(new Intent(this, GameActivity.class));

    }
}
