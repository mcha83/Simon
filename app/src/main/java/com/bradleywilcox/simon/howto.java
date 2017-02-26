package com.bradleywilcox.simon;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class howto extends Activity implements View.OnClickListener{


    Button back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.howto);

        back = (Button)findViewById(R.id.button5);
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
