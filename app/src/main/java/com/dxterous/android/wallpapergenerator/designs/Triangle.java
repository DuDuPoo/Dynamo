package com.dxterous.android.wallpapergenerator.designs;

import android.opengl.GLES20;
import android.util.Log;
import com.dxterous.android.wallpapergenerator.MyGLRenderer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.Random;

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

    private static final int COORDS_PER_VERTEX = 3;
    private final int program;

    private int vertexCount;
    private int vertexStride;

    public Triangle(float[] triangleCoords)
    {
        this.vertexCount = triangleCoords.length / COORDS_PER_VERTEX;
        this.vertexStride = COORDS_PER_VERTEX *4; //4 bytes per row

        ByteBuffer bb = ByteBuffer.allocateDirect(triangleCoords.length * 4);  //allocateDirect(Number of coordinate values * 4 bytes per float)
        bb.order(ByteOrder.nativeOrder());  //Use the device hardware's native byte order
        vertexBuffer = bb.asFloatBuffer(); //create a floating point buffer from ByteBuffer
        vertexBuffer.put(triangleCoords); //add the triangle coords to floatbuffer
        vertexBuffer.position(0);
        int vertexShader = MyGLRenderer.loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode);
        int fragmentShader = MyGLRenderer.loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);
        program = GLES20.glCreateProgram();
        GLES20.glAttachShader(program, vertexShader); //add the vertexShader to program
        GLES20.glAttachShader(program, fragmentShader);
        GLES20.glLinkProgram(program);
    }//Constructor

    public void draw()
    {

        GLES20.glUseProgram(program);
        int positionHandle = GLES20.glGetAttribLocation(program, "vPosition");
        GLES20.glEnableVertexAttribArray(positionHandle);
        GLES20.glVertexAttribPointer(positionHandle,  COORDS_PER_VERTEX,  GLES20.GL_FLOAT
        , false, vertexStride, vertexBuffer);

        int colorHandle = GLES20.glGetUniformLocation(program, "vColor");
        GLES20.glUniform4fv(colorHandle, 1, MyColor.getRandomColor(), 0);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0 , vertexCount);
        GLES20.glDisableVertexAttribArray(positionHandle);
    }//draw()

}
