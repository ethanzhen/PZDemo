package com.pz.demo;

import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader;
import android.graphics.Xfermode;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

/**
 * Created by Administrator on 2016/6/13.
 */
public class ProgressImageView extends ImageView{

    private float progress=20;
    private static final int MAX_PROGRESS=100;
    private float density;
    public ProgressImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
       init(context);
    }

    public ProgressImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ProgressImageView(Context context) {
        super(context);
        init(context);
    }

    void init(Context context){
        density=context.getResources().getDisplayMetrics().density;

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawProgress(canvas);
    }

    void drawProgress(Canvas can){
        float rotation=getRotation();
        Point rectPoint=caculateMaskRectWidthAndHeight(getWidth(),getHeight());
        int xOffset=(rectPoint.x-getWidth())/2;
        int yOffset=(rectPoint.y-getHeight())/2;
        Paint paint=new Paint();
        paint.setColor(Color.BLACK);
        paint.setAlpha(122);
        paint.setAntiAlias(true);
        can.rotate(-rotation,getWidth()/2,getHeight()/2);
        float height=caculateProgress(rectPoint.y);
        can.translate(0,height);
        can.clipRect((float) (getWidth()*Math.cos(Math.toRadians(Math.abs(rotation)))),getHeight()+yOffset,yOffset,-yOffset);
        can.drawRect(-xOffset,-yOffset,rectPoint.x-xOffset,rectPoint.y-yOffset,paint);
        drawText(can,rectPoint.y-yOffset,height,xOffset,yOffset,rectPoint.y);
        invalidate();
    }


    float caculateProgress(int height){
        return (int) (-(progress/100)*height);
    }

    Point caculateMaskRectWidthAndHeight(int width,int height){
        double radius=Math.toRadians(Math.abs(getRotation()));
        Point p=new Point();
        p.x= (int) (width*Math.cos(radius)+height*Math.sin(radius));
        p.y= (int) (height*Math.cos(radius)+width*Math.sin(radius));
        return p;
    }

    void drawText(Canvas canvas,float y,float height,float xOffset,float yOffset,int maskHeight){
        Paint paint=new Paint();
        paint.setColor(Color.WHITE);
        paint.setTextSize(18*density);
        String text=(int)progress+"%";
        int textWidth=getTextWidth(paint,text);
        double radius=Math.toRadians(Math.abs(getRotation()));
        float he=Math.abs(height);
        float yRight=(float) (getHeight()*Math.cos(radius));
        canvas.drawText(text,caculateTextCenterX(height,xOffset,yOffset,maskHeight)-textWidth/2,y-5*density,paint);
    }

    public void setProgress(float progress) {
        this.progress = progress;
        invalidate();
    }

     int getTextWidth(Paint paint, String str) {
        int iRet = 0;
        if (str != null && str.length() > 0) {
            int len = str.length();
            float[] widths = new float[len];
            paint.getTextWidths(str, widths);
            for (int j = 0; j < len; j++) {
                iRet += (int) Math.ceil(widths[j]);
            }
        }
        return iRet;
    }


    int caculateTextCenterX(float height,float xOffset,float yOffset,int maskHeight){
        double radius=Math.toRadians(Math.abs(getRotation()));
        boolean isRotationNegative=getRotation()>0?false:true;
        float he=Math.abs(height);
        float yLeft= (float) (getWidth()*Math.sin(radius));
        float yRight=(float) (getHeight()*Math.cos(radius));
        int v;
        if(he<=yLeft){
            v = (int) ((getWidth() - he / Math.sin(radius)) * Math.cos(radius) + he * (Math.tan(radius) + 1 / Math.tan(radius)) / 2 - xOffset);
            if(isRotationNegative){
                v=getWidth()-v;
            }
        }else if(he <yRight){
            if(isRotationNegative){
                v=(int) ((getWidth()-yLeft/Math.sin(radius))*Math.cos(radius)+yLeft*(Math.tan(radius)+1/Math.tan(radius))/2-xOffset);
                v= (int) (getWidth()-v-(he-yLeft)*Math.tan(radius));

            }else{
                v=(int) ((getWidth()-yLeft/Math.sin(radius))*Math.cos(radius)+yLeft*(Math.tan(radius)+1/Math.tan(radius))/2-xOffset+(he-yLeft)*Math.tan(radius));
            }



        }else{
            he=maskHeight-he;
            v=(int) (getWidth()-((getWidth()-he/Math.sin(radius))*Math.cos(radius)+he*(Math.tan(radius)+1/Math.tan(radius))/2-xOffset));
            if(isRotationNegative){
                v=getWidth()-v;
            }
        }

        return v;
    }
}
