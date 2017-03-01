package com.dxterous.android.wallpapergenerator.activities;

import android.app.ProgressDialog;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dxterous.android.wallpapergenerator.MyGLSurfaceView;
import com.dxterous.android.wallpapergenerator.R;
import com.dxterous.android.wallpapergenerator.fragments.PatternsFragment;

public class MainActivity extends AppCompatActivity
{
    /**/
    private DrawerLayout mDrawer;
    private Toolbar toolbar;
    private NavigationView nvDrawer;
    private ActionBarDrawerToggle drawerToggle;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUI();

        /*glSurfaceView = (MyGLSurfaceView) findViewById(R.id.glSurfaceView);
        setWallpaper = (Button) findViewById(R.id.setWallpaperButton);
        imageView = (ImageView) findViewById(R.id.imageView);
        setWallpaper.setOnClickListener(this);*/
    }//onCreate()

    private void initUI()
    {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        nvDrawer = (NavigationView) findViewById(R.id.nvView);
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        setupDrawerContent(nvDrawer);
        View headerLayout = nvDrawer.getHeaderView(0);
        TextView navHeaderTextView = (TextView) headerLayout.findViewById(R.id.navHeaderTextView);
        navHeaderTextView.setText("Dynamo");
        drawerToggle = new ActionBarDrawerToggle(this, mDrawer, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawerToggle.setDrawerIndicatorEnabled(true);
        mDrawer.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        PatternsFragment fragment = new PatternsFragment();
        initFragment(fragment);
    }

    private void setupDrawerContent(NavigationView navigationView)
    {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener()
                {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem)
                    {
                        selectDrawerItem(menuItem);
                        return true;
                    }
                });
    }

    private void selectDrawerItem(MenuItem menuItem)
    {
        /*Go to selected fragment*/
        menuItem.setChecked(true);
        setTitle(menuItem.getTitle());
        mDrawer.closeDrawers();
        switch (menuItem.getItemId())
        {
            case R.id.nav_patterns:
            {
                PatternsFragment fragment = new PatternsFragment();
                initFragment(fragment);
                break;
            }
        }
    }

    private void initFragment(Fragment fragment)
    {
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.container, fragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        switch (item.getItemId())
        {
            case android.R.id.home:
                mDrawer.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        drawerToggle.onConfigurationChanged(newConfig);
    }

    /*@Override
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
    }//onClick*/
}
