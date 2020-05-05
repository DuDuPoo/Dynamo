package com.dxterous.android.wallpapergenerator;

import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.Log;

import com.dxterous.android.wallpapergenerator.designs.Cube;
import com.dxterous.android.wallpapergenerator.designs.Triangle;

import java.nio.IntBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.content.ContentValues.TAG;

public class MyGLRenderer implements GLSurfaceView.Renderer
{
    private int width, height;
    private Bitmap bitmap;
    private float red=1, blue=1, green=1;
    private Triangle triangle;
    private Cube cube;
    private boolean is2D;

    // mMVPMatrix is an abbreviation for "Model View Projection Matrix"
    private final float[] mMVPMatrix = new float[16];
    private final float[] mProjectionMatrix = new float[16];
    private final float[] mViewMatrix = new float[16];
    private final float[] mRotationMatrix = new float[16];
    private float mAngle =240;
    private float mTransY=0;
    private float mTransX=0;
    private static final float Z_NEAR = 1f;
    private static final float Z_FAR = 40f;

    public MyGLRenderer(boolean is2D) {
        this.is2D = is2D;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config)
    {
        if(is2D) {
            float[] triangleCoords =
                    {
                            0.0f, 0.0f, 0.0f,
                            -1.0f, -1.0f, 0.0f,
                            0.8f, -1.0f, 0.0f,
                            0.0f, 0.622008459f, 0.0f,
                            -0.5f, -0.311004243f, 0.0f,
                            0.5f, -0.311004243f, 0.0f
                    };
            triangle = new Triangle(triangleCoords);
        } else {
            GLES20.glClearColor(0.9f, .9f, 0.9f, 0.9f);
            cube = new Cube();
        }
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height)
    {
        this.width = width;
        this.height = height;
        GLES20.glViewport(0, 0, width, height);
        float aspect = (float) width / height;
        if(is2D) {


        } else {
            // this projection matrix is applied to object coordinates
            //no idea why 53.13f, it was used in another example and it worked.
            Matrix.perspectiveM(mProjectionMatrix, 0, 53.13f, aspect, Z_NEAR, Z_FAR);
        }
    }

    @Override
    public void onDrawFrame(GL10 unused)
    {
        if(is2D) {
            GLES20.glClearColor(red, green, blue, 0.0f);
            GLES20.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
            triangle.draw();
        } else {
            // Clear the color buffer  set above by glClearColor.
            GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
            //need this otherwise, it will over right stuff and the cube will look wrong!
            GLES20.glEnable(GLES20.GL_DEPTH_TEST);
            // Set the camera position (View matrix)  note Matrix is an include, not a declared method.
            Matrix.setLookAtM(mViewMatrix, 0, 0, 0, -3, 0f, 0f, 0f, 0f, 1.0f, 0.0f);

            // Create a rotation and translation for the cube
            Matrix.setIdentityM(mRotationMatrix, 0);

            //move the cube up/down and left/right
            Matrix.translateM(mRotationMatrix, 0, mTransX, mTransY, 0);

            //mangle is how fast, x,y,z which directions it rotates.
            Matrix.rotateM(mRotationMatrix, 0, mAngle, 1.0f, 1.0f, 1.0f);

            // combine the model with the view matrix
            Matrix.multiplyMM(mMVPMatrix, 0, mViewMatrix, 0, mRotationMatrix, 0);

            // combine the model-view with the projection matrix
            Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mMVPMatrix, 0);

            cube.draw(mMVPMatrix);


        }
        setBitmap();
    }//onDrawFrame

    private void setBitmap() {
        int[] b = new int[this.width * (this.height)];
        int[] bt = new int[this.width * this.height];
        IntBuffer ib = IntBuffer.wrap(b);
        ib.position(0);

        GLES20.glReadPixels(0, 0, this.width, this.height, GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, ib);

        for (int i = 0, k = 0; i < this.height; i++, k++)
        {//remember, that OpenGL bitmap is incompatible with Android bitmap
            //and so, some correction need.
            for (int j = 0; j < this.width; j++)
            {
                int pix = b[i * this.width + j];
                int pb = (pix >> 16) & 0xff;
                int pr = (pix << 16) & 0x00ff0000;
                int pix1 = (pix & 0xff00ff00) | pr | pb;
                bt[(this.height - k - 1) * this.width + j] = pix1;
            }
        }
        Bitmap sb = Bitmap.createBitmap(bt, this.width, this.height, Bitmap.Config.ARGB_8888);
        if (sb == null)
            Log.d("onSurfaceChanged :: ", "Null Bitmap");
        else
            Log.d("onSurfaceChanged :: ", "Bitmap is created");
        this.bitmap = sb;
    }

    public static int loadShader(int type, String shaderCode)
    {
        int[] compiled = new int[1];
        int shader = GLES20.glCreateShader(type);
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);
        // Check the compile status
        GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, compiled, 0);

        if (compiled[0] == 0) {
            Log.e(TAG, "Erorr!!!!");
            Log.e(TAG, GLES20.glGetShaderInfoLog(shader));
            GLES20.glDeleteShader(shader);
            return 0;
        }
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


    //used the touch listener to move the cube up/down (y) and left/right (x)
    public float getY() {
        return mTransY;
    }

    public void setY(float mY) {
        mTransY = mY;
    }

    public void changeAngle() {
        this.mAngle += .4;
    }

    public float getX() {
        return mTransX;
    }

    public void setX(float mX) {
        mTransX = mX;
    }

}
