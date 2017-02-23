package com.dxterous.android.wallpapergenerator;

import android.app.ProgressDialog;
import android.app.WallpaperManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener
{
    MyGLSurfaceView glSurfaceView;
    Button setWallpaper;
    Bitmap bitmap;
    ImageView imageView;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        glSurfaceView = (MyGLSurfaceView) findViewById(R.id.glSurfaceView);
        setWallpaper = (Button) findViewById(R.id.setWallpaperButton);
        imageView = (ImageView) findViewById(R.id.imageView);
        setWallpaper.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        if(v.getId() == R.id.setWallpaperButton)
        {
            bitmap = glSurfaceView.renderer.getBitmap();
            imageView.setImageBitmap(bitmap);
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
    }//onClick
}
