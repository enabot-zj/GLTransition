package com.kevin.gltransition;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.opengl.Matrix;
import android.util.Log;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by zhaoliangtai on 2018/8/16.
 */

public class Transition {

    private static final String TAG = Transition.class.getSimpleName();

    private static final float[] VERTEX = {
            -1.0f, -1.0f,
            1.0f, -1.0f,
            -1.0f, 1.0f,
            1.0f, 1.0f
    };

    private static final float[] TEX_COORDS = {
            0f, 1f,
            1f, 1f,
            0f, 0f,
            1f, 0f
    };

    private float[] mProjectMatrix = new float[16];
    private float[] mViewMatrix = new float[16];
    private float[] mMVPMatrix = new float[16];

    private Context mContext;

    private int mProgram;

    private int mPositionLoc;
    private int mCoordLoc;
    private int mMatrixLoc;
    private int mProgressLoc;
    private int mFromTexture;
    private int mToTexture;

    private float mProgress;

    private String mVertexPath, mFragmentPath;

    private FloatBuffer mVertexBuffer;
    private FloatBuffer mTexCoordsBuffer;
    private Bitmap mBitmap;
    private Bitmap mToBitmap;

    public Transition(Context context, String vertexPath, String fragmentPath) {
        mContext = context;
        mVertexPath = vertexPath;
        mFragmentPath = fragmentPath;
        mVertexBuffer = ByteBuffer.allocateDirect(VERTEX.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(VERTEX);
        mVertexBuffer.position(0);
        mTexCoordsBuffer = ByteBuffer.allocateDirect(TEX_COORDS.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(TEX_COORDS);
        mTexCoordsBuffer.position(0);
        try {
            mBitmap = BitmapFactory.decodeStream(context.getAssets().open("a.png"));
            mToBitmap = BitmapFactory.decodeStream(context.getAssets().open("b.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setProgress(float progress) {
        mProgress = progress;
    }

    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        String vertexSrc = AssetsReadContent.readAssetsContent(mContext, mVertexPath);
        String fragmentSrc = AssetsReadContent.readAssetsContent(mContext, mFragmentPath);
        int vertexShader = GLES20.glCreateShader(GLES20.GL_VERTEX_SHADER);
        GLES20.glShaderSource(vertexShader, vertexSrc);
        GLES20.glCompileShader(vertexShader);
        int fragmentShader = GLES20.glCreateShader(GLES20.GL_FRAGMENT_SHADER);
        GLES20.glShaderSource(fragmentShader, fragmentSrc);
        GLES20.glCompileShader(fragmentShader);
        mProgram = GLES20.glCreateProgram();
        GLES20.glAttachShader(mProgram, vertexShader);
        GLES20.glAttachShader(mProgram, fragmentShader);
        GLES20.glLinkProgram(mProgram);

        mPositionLoc = GLES20.glGetAttribLocation(mProgram, "vPosition");
        mCoordLoc = GLES20.glGetAttribLocation(mProgram, "vCoord");
        mMatrixLoc = GLES20.glGetUniformLocation(mProgram, "vMatrix");
        mFromTexture = GLES20.glGetUniformLocation(mProgram, "samplerFrom");
        mToTexture = GLES20.glGetUniformLocation(mProgram, "samplerTo");
        mProgressLoc = GLES20.glGetUniformLocation(mProgram, "progress");
        createTexture();
    }

    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0, 0, width, height);

        int w = mBitmap.getWidth();
        int h = mBitmap.getHeight();
        float sWH = w / (float) h;
        float sWidthHeight = width / (float) height;
        if (width > height) {
            if (sWH > sWidthHeight) {
                Matrix.orthoM(mProjectMatrix, 0, -sWidthHeight * sWH, sWidthHeight * sWH, -1, 1, 3, 5);
            } else {
                Matrix.orthoM(mProjectMatrix, 0, -sWidthHeight / sWH, sWidthHeight / sWH, -1, 1, 3, 5);
            }
        } else {
            if (sWH > sWidthHeight) {
                Matrix.orthoM(mProjectMatrix, 0, -1, 1, -1 / sWidthHeight * sWH, 1 / sWidthHeight * sWH, 3, 5);
            } else {
                Matrix.orthoM(mProjectMatrix, 0, -1, 1, -sWH / sWidthHeight, sWH / sWidthHeight, 3, 5);
            }
        }
        //设置相机位置
        Matrix.setLookAtM(mViewMatrix, 0, 0, 0, 5.0f, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
        //计算变换矩阵
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectMatrix, 0, mViewMatrix, 0);
    }

    public void onDrawFrame(GL10 gl) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        GLES20.glUseProgram(mProgram);
        GLES20.glUniformMatrix4fv(mMatrixLoc, 1, false, mMVPMatrix, 0);
        checkGlError("glUniformMatrix4fv");
        GLES20.glEnableVertexAttribArray(mPositionLoc);
        checkGlError("glEnableVertexAttribArray position");
        GLES20.glVertexAttribPointer(mPositionLoc, 2, GLES20.GL_FLOAT, false, 8, mVertexBuffer);
        checkGlError("glVertexAttribPointer position");
        GLES20.glEnableVertexAttribArray(mCoordLoc);
        GLES20.glVertexAttribPointer(mCoordLoc, 2, GLES20.GL_FLOAT, false, 8, mTexCoordsBuffer);
        GLES20.glUniform1i(mFromTexture, 0);
        GLES20.glUniform1i(mToTexture, 1);
        GLES20.glUniform1f(mProgressLoc, mProgress);
        Log.d(TAG, "onDrawFrame " + mProgress);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);
        GLES20.glDisableVertexAttribArray(mPositionLoc);
        GLES20.glDisableVertexAttribArray(mCoordLoc);
    }

    private void createTexture() {
        int[] textures = new int[2];
        GLES20.glGenTextures(2, textures, 0);
        for (int i = 0; i < 2; i++) {
            GLES20.glActiveTexture(GLES20.GL_TEXTURE0 + i);
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textures[i]);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER,
                    GLES20.GL_LINEAR);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER,
                    GLES20.GL_LINEAR);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S,
                    GLES20.GL_REPEAT);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T,
                    GLES20.GL_REPEAT);
            if (i == 0)
                GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, mBitmap, 0);
            else
                GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, mToBitmap, 0);
        }

    }

    public static void checkGlError(String op) {
        int error = GLES20.glGetError();
        if (error != GLES20.GL_NO_ERROR) {
            String msg = op + ": glError 0x" + Integer.toHexString(error);
            Log.e(TAG, msg);
            throw new RuntimeException(msg);
        }
    }
}
