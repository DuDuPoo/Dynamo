package com.dxterous.android.wallpapergenerator;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.opengl.GLSurfaceView;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

/**
 * Created by dudupoo on 22/2/17.
 */

public class MyGLSurfaceView extends GLSurfaceView
{
    MyGLRenderer renderer;
    float mPreviousX;
    float mPreviousY;

    public MyGLSurfaceView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init(context);
    }

    private void init(Context context)
    {
        this.setEGLContextClientVersion(2);
        renderer = new MyGLRenderer();
        this.setRenderer(renderer);
        this.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        float x = event.getX();
        float y = event.getY();
        Log.d("Red Color :: ", ""+x/getWidth());
        switch (event.getAction())
        {
            case MotionEvent.ACTION_UP:
            {
                float dx = x-mPreviousX;
                float dy = y-mPreviousY;
                Log.d("ACTION_UP :: ",""+dx/getWidth());
                renderer.setColor(dx/getWidth(), dy/getHeight(), Color.GREEN);
                requestRender();
                break;
            }
            case MotionEvent.ACTION_MOVE:
            {
                float dx = x-mPreviousX;
                float dy = y-mPreviousY;
                //Red:0.90 green:0.22 blue:0.21 alpha:1.0
                Log.d("ACTION_MOVE :: ",""+x/getWidth());
                renderer.setColor(x/getWidth(), y/getHeight(), Color.GREEN);
                requestRender();
                break;
            }
        }
        return true;

    }
}
