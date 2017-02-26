package com.bradleywilcox.simon;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class About extends Activity implements View.OnClickListener{


    Button back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        back = (Button)findViewById(R.id.button5About);
        back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        if(v==back)
        {
            startActivity(new Intent(this, MainActivity.class));

        }
    }

}
