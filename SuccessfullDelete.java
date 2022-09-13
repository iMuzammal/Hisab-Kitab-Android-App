package com.example.AccountingAppAdmin;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;

public class SuccessfullDelete extends AppCompatActivity {

    LottieAnimationView lottieAnimationView;
    private final int SPLASH_DISPLAY_LENGTH = 3000;
    ImageView back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_successfull_delete);
        lottieAnimationView = (LottieAnimationView)findViewById( R.id.lottie );
        back=findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent(getApplication(), ViewVender.class);
                startActivity(intent);
            }
        });

        startCheckAnimation();

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {



                Intent intent = new Intent(getApplication(), ViewVender.class);
                startActivity(intent);

            }
        }, SPLASH_DISPLAY_LENGTH);


    }

    private void startCheckAnimation() {

        ValueAnimator valueAnimator = ValueAnimator.ofFloat( 0f, 1f ).setDuration( 3000 );
        valueAnimator.addUpdateListener( new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                lottieAnimationView.setProgress( (Float) animation.getAnimatedValue() );
            }
        } );
        if (lottieAnimationView.getProgress() == 0f) {

            valueAnimator.start();

        } else {
            lottieAnimationView.setProgress( 0f );
        }
    }
}
