package com.rmuttproject.bios.computer_science_assistant;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import static java.lang.Thread.sleep;

public class SpalshActivity extends AppCompatActivity {
    private TextView txt1;
    private ImageView img1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spalsh);
        initWidget();
        final Intent i = new Intent(this, FirstTime.class);
        Thread Timer = new Thread() {
            public void run() {
                try {
                    sleep(3000);
                } catch (InterruptedException e)

                {
                    e.printStackTrace();
                } finally {
                    startActivity(i);
                    finish();   //Kill the activity from which you will go to next activity
                }
            }
        };
        Timer.start();

    }


    private void initWidget(){
        txt1 = (TextView) findViewById(R.id.txt);
        img1 = (ImageView) findViewById(R.id.img);

        Animation myanim = AnimationUtils.loadAnimation(this,R.anim.mytransition);
        img1.startAnimation(myanim);
        txt1.startAnimation(myanim);
    }
}
