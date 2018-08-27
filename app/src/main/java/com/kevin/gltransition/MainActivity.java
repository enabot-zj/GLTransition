package com.kevin.gltransition;

import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.SeekBar;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private GLSurfaceView mGLDisplayView;
    private GLRenderer mRenderer;
    private SeekBar mSeekBar;
    private List<Transition> mTransitionList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mGLDisplayView = findViewById(R.id.gl_display_view);
        mTransitionList = new ArrayList<>();
        Transition wipeRight = new Transition(this, "vertex.glsl", "water_drop.glsl");
        Transition linearBlur = new Transition(this, "vertex.glsl", "linear_blur.glsl");
        Transition polkDotsCurtain = new Transition(this, "vertex.glsl", "polk_dots_curtain.glsl");
        Transition simpleZoom = new Transition(this, "vertex.glsl", "glitch_memories.glsl");
        mTransitionList.add(wipeRight);
        mTransitionList.add(linearBlur);
        mTransitionList.add(polkDotsCurtain);
        mTransitionList.add(simpleZoom);
        mRenderer = new GLRenderer();
        mRenderer.setTransition(mTransitionList.get(0));
        mGLDisplayView.setEGLContextClientVersion(2);
        mGLDisplayView.setRenderer(mRenderer);
        mGLDisplayView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
        mSeekBar = findViewById(R.id.seekbar);
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mRenderer.setProgress(progress / 100f);
                mGLDisplayView.requestRender();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        findViewById(R.id.bt_wipe_right).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSeekBar.setProgress(0);
                mRenderer.setTransition(mTransitionList.get(0));
                mGLDisplayView.requestRender();
            }
        });
        findViewById(R.id.bt_linear_blur).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSeekBar.setProgress(0);
                mRenderer.setTransition(mTransitionList.get(1));
                mGLDisplayView.requestRender();
            }
        });
        findViewById(R.id.bt_dot_curtain).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSeekBar.setProgress(0);
                mRenderer.setTransition(mTransitionList.get(2));
                mGLDisplayView.requestRender();
            }
        });
        findViewById(R.id.bt_simple_zoom).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSeekBar.setProgress(0);
                mRenderer.setTransition(mTransitionList.get(3));
                mGLDisplayView.requestRender();
            }
        });
    }


}
