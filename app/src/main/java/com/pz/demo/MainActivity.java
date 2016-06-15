package com.pz.demo;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.TypeEvaluator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private TextView tv;
    private ProgressImageView piv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv= (TextView) findViewById(R.id.tv);
        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PropertyValuesHolder pvX=PropertyValuesHolder.ofFloat("translationX",0,300);
                pvX.setEvaluator(new MyEvaluator());
                PropertyValuesHolder pvY=PropertyValuesHolder.ofFloat("translationY",-100,100,-100,100,-100,100,-100,100);
                ObjectAnimator animator=ObjectAnimator.ofPropertyValuesHolder(tv,pvX,pvY);
                animator.setDuration(10000);
                animator.start();
            }
        });
        piv= (ProgressImageView) findViewById(R.id.piv);
        ObjectAnimator oa=ObjectAnimator.ofFloat(piv,"progress",0,100);
        oa.setDuration(20000);
        oa.start();

    }
    private class MyEvaluator implements TypeEvaluator{

        @Override
        public Object evaluate(float fraction, Object startValue, Object endValue) {
            Log.d("DD","fraction"+fraction);
            float sa=(Float)startValue;
            float ea=(Float)endValue;
            if(fraction>0.3f){
                return -300+300*fraction;
            }else {
                return (Float) startValue + fraction * (ea - sa);
            }
        }
    }
}
