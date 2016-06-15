package com.pz.demo.fragment;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.TypeEvaluator;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pz.demo.ProgressImageView;
import com.pz.demo.R;

public class FragmentMain extends Fragment implements View.OnClickListener{
    private static final String TAG="FragmentMain";
    private TextView tv;
    private ProgressImageView piv;
    private View rootView;
    public FragmentMain() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(rootView==null){
          rootView=inflater.inflate(R.layout.fragment_main, container, false);
        }
        initView(rootView);
        return rootView;
    }

    void initView(View view){
        view.findViewById(R.id.fragment_main_btn).setOnClickListener(this);
        tv= (TextView) view.findViewById(R.id.fragment_main_tv);
        piv= (ProgressImageView) view.findViewById(R.id.fragment_main_piv);
        ObjectAnimator oa=ObjectAnimator.ofFloat(piv,"progress",0,100);
        oa.setDuration(20000);
        oa.start();
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.fragment_main_btn:
                PropertyValuesHolder pvX=PropertyValuesHolder.ofFloat("translationX",0,300);
                pvX.setEvaluator(new MyEvaluator());
                PropertyValuesHolder pvY=PropertyValuesHolder.ofFloat("translationY",-100,100,-100,100,-100,100,-100,100);
                ObjectAnimator animator=ObjectAnimator.ofPropertyValuesHolder(tv,pvX,pvY);
                animator.setDuration(10000);
                animator.start();
                break;
        }

    }

    private class MyEvaluator implements TypeEvaluator {

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
