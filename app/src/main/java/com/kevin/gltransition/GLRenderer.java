package com.kevin.gltransition;

import android.opengl.GLSurfaceView;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by zhaoliangtai on 2018/8/15.
 */

public class GLRenderer implements GLSurfaceView.Renderer {

    private static final String TAG = GLRenderer.class.getSimpleName();

    private boolean isRefresh = false;
    private EGLConfig mEGLConfig;
    private int mWidth, mHeight;

    private Transition mTransition;

    public void setTransition(Transition transition) {
        mTransition = transition;
        isRefresh = true;
    }


    public void setProgress(float progress) {
        if (null != mTransition) {
            mTransition.setProgress(progress);
        }
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        mEGLConfig = config;
        mTransition.onSurfaceCreated(gl, config);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        mWidth = width;
        mHeight = height;
        mTransition.onSurfaceChanged(gl, width, height);
    }


    @Override
    public void onDrawFrame(GL10 gl) {
        if (isRefresh && mWidth != 0 && mHeight != 0) {
            mTransition.onSurfaceCreated(gl, mEGLConfig);
            mTransition.onSurfaceChanged(gl, mWidth, mHeight);
            isRefresh = false;
        }
        mTransition.onDrawFrame(gl);
    }
}
