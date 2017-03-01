package com.dxterous.android.wallpapergenerator;

import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.Log;

import com.dxterous.android.wallpapergenerator.designs.squares.Square;
import com.dxterous.android.wallpapergenerator.designs.triangles.RightAngled;
import com.dxterous.android.wallpapergenerator.designs.triangles.Triangle;

import java.nio.IntBuffer;
import java.util.Random;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/*
 * Created by dudupoo on 22/2/17.
 */

public class MyGLRenderer implements GLSurfaceView.Renderer
{
    private int width, height;
    private Bitmap bitmap;
    private float red=1, blue=1, green=1;
    private Triangle triangle, triangle2, triangle3;

    // mMVPMatrix is an abbreviation for "Model View Projection Matrix"
    private final float[] mMVPMatrix = new float[16];
    private final float[] mProjectionMatrix = new float[16];
    private final float[] mViewMatrix = new float[16];
    private final float[] mRotationMatrix = new float[16];

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config)
    {
        Random random = new Random();
        float triangleCoords[] =
                {
                        0.0f, 0.0f, 0.0f,
                        -1.0f, -1.0f, 0.0f,
                        0.8f, -1.0f, 0.0f
                };
        float triangleCoords2[] =
                {
                        0.3f, 0.15f, 0.0f,
                        -1.0f, -1.0f, 0.0f,
                        1.1f, -1.0f, 0.0f
                };
        float triangleCoords3[] =
                {
                        0.7f, -0.2f, 0.0f,
                        -1.0f, -1.0f, 0.0f,
                        1.4f, -1.0f, 0.0f
                };
        triangle = new Triangle(triangleCoords);
        triangle2 = new Triangle(triangleCoords2);
        triangle3 = new Triangle(triangleCoords3);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height)
    {
        GLES20.glViewport(0, 0, width, height);
        this.width = width;
        this.height = height;
        float ratio = (float) width / height;

        // this projection matrix is applied to object coordinates
        // in the onDrawFrame() method
//        Matrix.frustumM(mProjectionMatrix, 0, -ratio, ratio, -1, 1, 3, 7);

    }

    @Override
    public void onDrawFrame(GL10 gl)
    {
        gl.glClearColor(red, green, blue, 0.0f);
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
        triangle3.draw();
        triangle2.draw();
        triangle.draw();
//        Matrix.setLookAtM(mViewMatrix, 0, 0, 0, -3, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
//        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);
//        square.draw(mMVPMatrix);

        int b[] = new int[width * (height)];
        int bt[] = new int[width * height];
        IntBuffer ib = IntBuffer.wrap(b);
        ib.position(0);
        gl.glReadPixels(0, 0, width, height, GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, ib);

        for (int i = 0, k = 0; i < height; i++, k++)
        {//remember, that OpenGL bitmap is incompatible with Android bitmap
            //and so, some correction need.
            for (int j = 0; j < width; j++)
            {
                int pix = b[i * width + j];
                int pb = (pix >> 16) & 0xff;
                int pr = (pix << 16) & 0x00ff0000;
                int pix1 = (pix & 0xff00ff00) | pr | pb;
                bt[(height - k - 1) * width + j] = pix1;
            }
        }
        Bitmap sb = Bitmap.createBitmap(bt, width, height, Bitmap.Config.ARGB_8888);
        if (sb == null)
            Log.d("onSurfaceChanged :: ", "Null Bitmap");
        else
            Log.d("onSurfaceChanged :: ", "Bitmap is created");
        this.bitmap = sb;

    }//onDrawFrame

    public static int loadShader(int type, String shaderCode)
    {
        int shader = GLES20.glCreateShader(type);
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);
        return shader;
    }//loadShader

    public void setColor(float red, float blue, float green)
    {
        this.red = red;
        this.blue = blue;
        this.green = green;
    }

    public Bitmap getBitmap()
    {
        return bitmap;
    }

    public static void checkGlError(String glOperation) {
        int error;
        while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
            Log.e("", glOperation + ": glError " + error);
            throw new RuntimeException(glOperation + ": glError " + error);
        }
    }

}
