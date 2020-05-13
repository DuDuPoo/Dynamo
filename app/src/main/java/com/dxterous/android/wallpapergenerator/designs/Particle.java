package com.dxterous.android.wallpapergenerator.designs;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES30;
import android.opengl.GLUtils;
import android.os.SystemClock;

import com.dxterous.android.wallpapergenerator.util.ESShader;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class Particle {

    private int programObject;
    // Uniform location
    private int timeLoc;
    private int colorLoc;
    private int centerPositionLoc;
    private int samplerLoc;
    private final int NUM_PARTICLES = 1000;
    private final int PARTICLE_SIZE = 7;
    // Particle vertex data
    private float [] particleData = new float[ NUM_PARTICLES * PARTICLE_SIZE ];
    private FloatBuffer particles;
    private Context context;
    private int textureId;
    private float time;
    private long lastTime = 0;

    public Particle(Context context) {
        this.context = context;
        // Fill in particle data array
        for ( int i = 0; i < ( NUM_PARTICLES * PARTICLE_SIZE ); i += PARTICLE_SIZE )
        {
            // Lifetime of particle
            particleData[i + 0] = ( ( float ) ( ( int ) ( Math.random() * 10000 ) % 10000 ) / 10000.0f );;

            // End position of particle
            particleData[i + 1] = ( ( float ) ( ( int ) ( Math.random() * 10000 ) % 10000 ) / 5000.0f ) - 1.0f;
            particleData[i + 2] = ( ( float ) ( ( int ) ( Math.random() * 10000 ) % 10000 ) / 5000.0f ) - 1.0f;
            particleData[i + 3] = ( ( float ) ( ( int ) ( Math.random() * 10000 ) % 10000 ) / 5000.0f ) - 1.0f;

            // Start position of particle
            particleData[i + 4] = ( ( float ) ( ( int ) ( Math.random() * 10000 ) % 10000 ) / 40000.0f ) - 0.125f;
            particleData[i + 5] = ( ( float ) ( ( int ) ( Math.random() * 10000 ) % 10000 ) / 40000.0f ) - 0.125f;
            particleData[i + 6] = ( ( float ) ( ( int ) ( Math.random() * 10000 ) % 10000 ) / 40000.0f ) - 0.125f;
        }

        particles = ByteBuffer.allocateDirect ( particleData.length * 4 )
                .order ( ByteOrder.nativeOrder() ).asFloatBuffer();
        particles.put ( particleData ).position ( 0 );

        // Load shaders from 'assets' and get a linked program object
        programObject = ESShader.loadProgramFromAsset ( context,
                "shaders/vertexShaderParticle.vert",
                "shaders/fragmentShaderParticle.frag");
        // Get the uniform locations
        timeLoc = GLES30.glGetUniformLocation ( programObject, "u_time" );
        centerPositionLoc = GLES30.glGetUniformLocation ( programObject, "u_centerPosition" );
        colorLoc = GLES30.glGetUniformLocation ( programObject, "u_color" );
        samplerLoc = GLES30.glGetUniformLocation ( programObject, "s_texture" );
        // Load the texture images from 'assets'
        textureId = loadTextureFromAsset ( "textures/smoke.png" );
        // Initialize time to cause reset on first update
        time = 1.0f;
    }

    private void update()
    {
        if ( lastTime == 0 )
        {
            lastTime = SystemClock.uptimeMillis();
        }

        long curTime = SystemClock.uptimeMillis();
        long elapsedTime = curTime - lastTime;
        float deltaTime = elapsedTime / 1000.0f;
        lastTime = curTime;

        time += deltaTime;

        GLES30.glUseProgram ( programObject );

        if ( time >= 1.0f )
        {
            float [] centerPos = new float[3];
            float [] color = new float[4];

            time = 0.0f;

            // Pick a new start location and color
            centerPos[0] = ( ( float ) ( ( int ) ( Math.random() * 10000 ) % 10000 ) / 10000.0f ) - 0.5f;
            centerPos[1] = ( ( float ) ( ( int ) ( Math.random() * 10000 ) % 10000 ) / 10000.0f ) - 0.5f;
            centerPos[2] = ( ( float ) ( ( int ) ( Math.random() * 10000 ) % 10000 ) / 10000.0f ) - 0.5f;

            GLES30.glUniform3f ( centerPositionLoc, centerPos[0], centerPos[1], centerPos[2] );

            // Random color
            color[0] = ( ( float ) ( ( int ) ( Math.random() * 1000 ) % 10000 ) / 20000.0f ) + 0.5f;
            color[1] = ( ( float ) ( ( int ) ( Math.random() * 1000 ) % 10000 ) / 20000.0f ) + 0.5f;
            color[2] = ( ( float ) ( ( int ) ( Math.random() * 1000 ) % 10000 ) / 20000.0f ) + 0.5f;
            color[3] = 0.5f;

            GLES30.glUniform4f ( colorLoc, color[0], color[1], color[2], color[3] );
        }

        // Load uniform time variable
        GLES30.glUniform1f ( timeLoc, time );
    }


    public void draw() {
//        update();
        // Use the program object
        GLES30.glUseProgram ( programObject );

        // Load the vertex attributes
        //[0]
        particles.position ( 0 );
        int ATTRIBUTE_LIFETIME_LOCATION = 0;
        GLES30.glVertexAttribPointer (ATTRIBUTE_LIFETIME_LOCATION, 1, GLES30.GL_FLOAT,
                false, PARTICLE_SIZE * ( 4 ),
                particles );

        //[1]
        particles.position ( 1 );
        int ATTRIBUTE_ENDPOSITION_LOCATION = 2;
        GLES30.glVertexAttribPointer (ATTRIBUTE_ENDPOSITION_LOCATION, 3, GLES30.GL_FLOAT,
                false, PARTICLE_SIZE * ( 4 ),
                particles );

        //[4]
        particles.position ( 4 );
        int ATTRIBUTE_STARTPOSITION_LOCATION = 1;
        GLES30.glVertexAttribPointer (ATTRIBUTE_STARTPOSITION_LOCATION, 3, GLES30.GL_FLOAT,
                false, PARTICLE_SIZE * ( 4 ),
                particles );


        GLES30.glEnableVertexAttribArray (ATTRIBUTE_LIFETIME_LOCATION);
        GLES30.glEnableVertexAttribArray (ATTRIBUTE_ENDPOSITION_LOCATION);
        GLES30.glEnableVertexAttribArray (ATTRIBUTE_STARTPOSITION_LOCATION);

        // Blend particles
        GLES30.glEnable ( GLES30.GL_BLEND );
        GLES30.glBlendFunc ( GLES30.GL_SRC_ALPHA, GLES30.GL_ONE );

        // Bind the texture
        GLES30.glActiveTexture ( GLES30.GL_TEXTURE0 );
        GLES30.glBindTexture ( GLES30.GL_TEXTURE_2D, textureId );

        // Set the sampler texture unit to 0
        GLES30.glUniform1i ( samplerLoc, 0 );

        GLES30.glDrawArrays ( GLES30.GL_POINTS, 0, NUM_PARTICLES );
    }

    private int loadTextureFromAsset ( String fileName )
    {
        int[] textureId = new int[1];
        Bitmap bitmap = null;
        InputStream is = null;

        try
        {
            is = this.context.getAssets().open ( fileName );
        }
        catch ( IOException ioe )
        {
            is = null;
        }

        if ( is == null )
        {
            return 0;
        }

        bitmap = BitmapFactory.decodeStream ( is );

        GLES30.glGenTextures ( 1, textureId, 0 );
        GLES30.glBindTexture ( GLES30.GL_TEXTURE_2D, textureId[0] );

        GLUtils.texImage2D ( GLES30.GL_TEXTURE_2D, 0, bitmap, 0 );

        GLES30.glTexParameteri ( GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MIN_FILTER, GLES30.GL_LINEAR );
        GLES30.glTexParameteri ( GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MAG_FILTER, GLES30.GL_LINEAR );
        GLES30.glTexParameteri ( GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_S, GLES30.GL_CLAMP_TO_EDGE );
        GLES30.glTexParameteri ( GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_T, GLES30.GL_CLAMP_TO_EDGE );

        return textureId[0];
    }
}
