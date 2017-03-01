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
        Random random = new Random();
        switch (event.getAction())
        {
            case MotionEvent.ACTION_UP:
            {
                renderer.setColor(random.nextFloat(), random.nextFloat(), random.nextFloat());
                requestRender();
                Log.e("MyGLSurfaceView", "onTouchEvent: ACTION_UP");
                break;
            }
        }
        return true;

    }
}
