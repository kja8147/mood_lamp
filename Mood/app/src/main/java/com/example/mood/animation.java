package com.example.mood;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

/**
 * Created by 김진아 on 2018-11-13.
 */

public class animation extends AppCompatActivity {

    ImageView left,right;

    Handler handler = new Handler();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.door);

        left=(ImageView)findViewById(R.id.left);
        right=(ImageView)findViewById(R.id.right);

        Animation animright= AnimationUtils.loadAnimation(this,R.anim.animation);
        Animation animleft= AnimationUtils.loadAnimation(this,R.anim.animleft);

        left.startAnimation(animleft);
        right.startAnimation(animright);
        handler.postDelayed(r, 3000); // 3초 후 러너블


    }

    Runnable r = new Runnable() {
        @Override
        public void run() {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();


        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(r); // 예약 취소
    }



}
