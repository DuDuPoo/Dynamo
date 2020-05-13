package com.dxterous.android.wallpapergenerator.designs;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;

import com.dxterous.android.wallpapergenerator.util.ESShader;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

public class CubeTx {

    private final float[] verticesData =
            {
                    -0.5f,  0.5f, 0.0f, // Position 0
                    0.0f,  0.0f,       // TexCoord 0
                    -0.5f, -0.5f, 0.0f, // Position 1
                    0.0f,  1.0f,       // TexCoord 1
                    0.5f, -0.5f, 0.0f, // Position 2
                    1.0f,  1.0f,       // TexCoord 2
                    0.5f,  0.5f, 0.0f, // Position 3
                    1.0f,  0.0f        // TexCoord 3
            };

    private final short[] indicesData =
            {
                    0, 1, 2, 0, 2, 3
            };

    private FloatBuffer vertices;
    private ShortBuffer indices;
    private Context context;
    private int programObject;
    // Sampler location
    private int mBaseMapLoc;
    private int mLightMapLoc;
    // Texture handle
    private int mBaseMapTexId;
    private int mLightMapTexId;

    public CubeTx(Context context){
        this.context = context;

        vertices = ByteBuffer.allocateDirect ( verticesData.length * 4 )
                .order ( ByteOrder.nativeOrder() ).asFloatBuffer();
        vertices.put ( verticesData ).position ( 0 );
        indices = ByteBuffer.allocateDirect ( indicesData.length * 2 )
                .order ( ByteOrder.nativeOrder() ).asShortBuffer();
        indices.put ( indicesData ).position ( 0 );
        // Load shaders from 'assets' and get a linked program object
        programObject = ESShader.loadProgramFromAsset ( context,
                "shaders/vertexShaderCubeTx.vert",
                "shaders/fragmentShaderCubeTx.frag");

        // Get the sampler locations
        mBaseMapLoc = GLES20.glGetUniformLocation ( programObject, "s_baseMap" );
        mLightMapLoc = GLES20.glGetUniformLocation ( programObject, "s_lightMap" );

        // Load the texture images from 'assets'
        mBaseMapTexId = loadTextureFromAsset ( "textures/basemap.png" );
        mLightMapTexId = loadTextureFromAsset ( "textures/lightmap.png" );

    }

    public void draw() {
        // Use the program object
        GLES20.glUseProgram ( programObject );

        // Load the vertex position
        vertices.position ( 0 );
        GLES20.glVertexAttribPointer ( 0, 3, GLES20.GL_FLOAT,
                false,
                5 * 4, vertices );
        // Load the texture coordinate
        vertices.position ( 3 );
        GLES20.glVertexAttribPointer ( 1, 2, GLES20.GL_FLOAT,
                false,
                5 * 4,
                vertices );

        GLES20.glEnableVertexAttribArray ( 0 );
        GLES20.glEnableVertexAttribArray ( 1 );


        // Bind the base map
        GLES20.glActiveTexture ( GLES20.GL_TEXTURE0 );
        GLES20.glBindTexture ( GLES20.GL_TEXTURE_2D, mBaseMapTexId );

        // Set the base map sampler to texture unit to 0
        GLES20.glUniform1i ( mBaseMapLoc, 0 );

        // Bind the light map
//        GLES20.glActiveTexture ( GLES20.GL_TEXTURE1 );
//        GLES20.glBindTexture ( GLES20.GL_TEXTURE_2D, mLightMapTexId );
//
//        // Set the light map sampler to texture unit 1
//        GLES20.glUniform1i ( mLightMapLoc, 1 );

        GLES20.glDrawElements ( GLES20.GL_TRIANGLES, 6, GLES20.GL_UNSIGNED_SHORT, indices );
    }

    private int loadTextureFromAsset ( String fileName )
    {
        int[] textureId = new int[1];
        Bitmap bitmap = null;
        InputStream is = null;

        try
        {
            is = context.getAssets().open ( fileName );
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

        GLES20.glGenTextures ( 1, textureId, 0 );
        GLES20.glBindTexture ( GLES20.GL_TEXTURE_2D, textureId[0] );

        GLUtils.texImage2D ( GLES20.GL_TEXTURE_2D, 0, bitmap, 0 );

        GLES20.glTexParameteri ( GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR );
        GLES20.glTexParameteri ( GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR );
        GLES20.glTexParameteri ( GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE );
        GLES20.glTexParameteri ( GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE );

        return textureId[0];
    }
}
