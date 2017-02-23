package com.dxterous.android.wallpapergenerator.designs.triangles;

import android.opengl.GLES20;
import android.util.Log;

import com.dxterous.android.wallpapergenerator.MyGLRenderer;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.Random;

/*
 * Created by dudupoo on 23/2/17.
 */

public class Triangle
{
    private FloatBuffer vertexBuffer;

    private final String vertexShaderCode =
            "attribute vec4 vPosition;"+
            "void main(){"+
            "    gl_Position = vPosition;"+
            "}";

    private final String fragmentShaderCode =
            "precision mediump float;"+
            "uniform vec4 vColor;"+
            "void main(){"+
            "    gl_FragColor = vColor;"+
            "}";

    static final int COORDS_PER_VERTEX = 3;
    private final int program;
    /*static float triangleCoords[] =
            {
              0.0f, 0.622008459f, 0.0f,
              -0.5f, -0.311004243f, 0.0f,
              0.5f, -0.311004243f, 0.0f
            };*/
    static float triangleCoords[] =
            {
                    0.0f, 2.0f, 0.0f,
                    -1.0f, -1.0f, 0.0f,
                    1.0f, -1.0f, 0.0f
            };

    //Red:0.96 green:0.00 blue:0.34
    float red, green, blue, alpha;

    private int positionHandle, colorHandle;
    private final int vertexCount = triangleCoords.length / COORDS_PER_VERTEX;
    private final int vertexStride = COORDS_PER_VERTEX *4; //4 bytes per row

    public Triangle()
    {
       //allocateDirect(Number of coordinate values * 4 bytes per float)
        ByteBuffer bb = ByteBuffer.allocateDirect(triangleCoords.length * 4);
        //Use the device hardware's native byte order
        bb.order(ByteOrder.nativeOrder());
        //create a floating point buffer from ByteBuffer
        vertexBuffer = bb.asFloatBuffer();
        //add the triangle coords to floatbuffer
        vertexBuffer.put(triangleCoords);
        vertexBuffer.position(0);

        int vertexShader = MyGLRenderer.loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode);
        int fragmentShader = MyGLRenderer.loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);

        program = GLES20.glCreateProgram();
        //add the vertexShader to program
        GLES20.glAttachShader(program, vertexShader);
        GLES20.glAttachShader(program, fragmentShader);
        GLES20.glLinkProgram(program);
    }//Constructor

    public void draw()
    {
        Random random = new Random();
        red = random.nextFloat();
        blue = random.nextFloat();
        green = random.nextFloat();
        alpha = 1.0f;

        GLES20.glUseProgram(program);
        positionHandle = GLES20.glGetAttribLocation(program, "vPosition");
        GLES20.glEnableVertexAttribArray(positionHandle);
        GLES20.glVertexAttribPointer(positionHandle,  COORDS_PER_VERTEX,  GLES20.GL_FLOAT
        , false, vertexStride, vertexBuffer);
        colorHandle = GLES20.glGetUniformLocation(program, "vColor");
        Log.d("Triangle Color :: ","");
        GLES20.glUniform4fv(colorHandle, 1, new float[]{red, green, blue, alpha}, 0);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0 , vertexCount);
        GLES20.glDisableVertexAttribArray(positionHandle);
    }//draw()

}
