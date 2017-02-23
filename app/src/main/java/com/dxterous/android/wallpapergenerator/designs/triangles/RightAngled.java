package com.dxterous.android.wallpapergenerator.designs.triangles;

import android.opengl.GLES20;
import android.util.Log;

import com.dxterous.android.wallpapergenerator.MyGLRenderer;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.Random;

/**
 * Created by dudupoo on 23/2/17.
 */

public class RightAngled
{
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

    private FloatBuffer vertexBuffer;
    private ShortBuffer drawListBuffer;

    static final int COORDS_PER_VERTEX = 3;
    static float squareCoords[] = {
      -1.0f, 1.0f, 0.0f,
      -1.0f, -1.0f, 0.0f,
      1.0f, -1.0f, 0.0f,
    };

    private short drawOrder[] = {0,1,2,0,2,3};
    float red, green, blue, alpha;
    private int positionHandle;
    private int colorHandle;
    private final int vertexStride = COORDS_PER_VERTEX * 4; // 4 bytes per vertex
    private final int vertexCount = squareCoords.length / COORDS_PER_VERTEX;
    private final int program;

    public RightAngled()
    {
        ByteBuffer bb = ByteBuffer.allocateDirect(squareCoords.length * 4);
        bb.order(ByteOrder.nativeOrder());

        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(squareCoords);
        vertexBuffer.position(0);

        int vertexShader = MyGLRenderer.loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode);
        int fragmentShader = MyGLRenderer.loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);

        program = GLES20.glCreateProgram();
        //add the vertexShader to program
        GLES20.glAttachShader(program, vertexShader);
        GLES20.glAttachShader(program, fragmentShader);
        GLES20.glLinkProgram(program);
    }

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
    }
}
