package com.dxterous.android.wallpapergenerator;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import java.util.Random;

/*
 * Created by dudupoo on 22/2/17.
 */

public class MyGLSurfaceView extends GLSurfaceView
{
    public MyGLRenderer renderer;
    private float mPreviousX;
    private float mPreviousY;
    private static final float TOUCH_SCALE_FACTOR = 0.015f;

    public MyGLSurfaceView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init(context);
    }

    private void init(Context context)
    {
        this.setEGLContextClientVersion(2);
        renderer = new MyGLRenderer(false);
        this.setRenderer(renderer);
        this.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        Random random = new Random();
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction())
        {
            /*case MotionEvent.ACTION_UP:
            {
                renderer.setColor(random.nextFloat(), random.nextFloat(), random.nextFloat());
                requestRender();
                Log.e("MyGLSurfaceView", "onTouchEvent: ACTION_UP");
                break;
            }*/

            case MotionEvent.ACTION_MOVE: {
                float dx = x - mPreviousX;
                //subtract, so the cube moves the same direction as your finger.
                //with plus it moves the opposite direction.
                renderer.setX(renderer.getX() - (dx * TOUCH_SCALE_FACTOR));

                float dy = y - mPreviousY;
                renderer.setY(renderer.getY() - (dy * TOUCH_SCALE_FACTOR));
                renderer.changeAngle();
                renderer.setColor(random.nextFloat(), random.nextFloat(), random.nextFloat());
            }
        }
        mPreviousX = x;
        mPreviousY = y;
        return true;

    }
}
