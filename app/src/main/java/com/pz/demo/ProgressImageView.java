package com.pz.demo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by Administrator on 2016/6/13.
 */
public class ProgressImageView extends ImageView{

    private int progress=0;
    private static final int MAX_PROGRESS=100;

    public ProgressImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ProgressImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ProgressImageView(Context context) {
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawProgress(canvas);
    }

    void drawProgress(Canvas can){
        float rotation=getRotation();
        Paint paint=new Paint();
        paint.setColor(Color.RED);
        paint.setAntiAlias(true);
        LinearGradient lg=new LinearGradient(0,0,getWidth(),getHeight(),new int[]{Color.RED,Color.RED},new float[]{0.0f,1.0f}, Shader.TileMode.CLAMP);
        can.rotate(-rotation,getWidth()/2,getHeight()/2);
        paint.setShader(lg);
        can.drawRect(0,0,getWidth(),getHeight(),paint);
        invalidate();
    }
}
