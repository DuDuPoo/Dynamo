package com.dxterous.android.wallpapergenerator.activities;

import android.app.WallpaperManager;
import android.graphics.Bitmap;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.dxterous.android.wallpapergenerator.MyGLSurfaceView;
import com.dxterous.android.wallpapergenerator.R;

public class PatternLoaderActivity extends AppCompatActivity implements View.OnClickListener
{
    MyGLSurfaceView glSurfaceView;
    FloatingActionButton setWallpaperButton;
    FloatingActionButton changeDesignButton;
    Bitmap bitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pattern_loader);
        initUI();
    }

    private void initUI()
    {
        glSurfaceView = (MyGLSurfaceView) findViewById(R.id.glSurfaceView);
        setWallpaperButton = (FloatingActionButton) findViewById(R.id.setWallpaperButton);
        setWallpaperButton.setOnClickListener(this);
        changeDesignButton = (FloatingActionButton) findViewById(R.id.changeDesignButton);
        changeDesignButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        switch(v.getId()) {
            case R.id.setWallpaperButton: {
                bitmap = glSurfaceView.renderer.getBitmap();
                Log.d("LoadWallpaperTask :: ", "setting wallpaper");
                WallpaperManager wallpaperManager = WallpaperManager.getInstance(this);
                try
                {
                    wallpaperManager.setWallpaperOffsetSteps(1, 1);
                    wallpaperManager.setBitmap(bitmap);

                } catch (Exception e)
                {
                    Log.d("LoadWallpaperTask :: ", "Wallpaper Fail to set");
                    e.printStackTrace();
                }
                Log.d("LoadWallpaperTask :: ", "Wallpaper Set");
            }
            break;
            case R.id.changeDesignButton: {
                this.glSurfaceView.renderer.changeDesign_id();
                this.glSurfaceView.requestRender();
            }
        }
    }
}
