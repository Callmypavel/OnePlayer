package com.example.peacemaker.oneplayer;

import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.SeekBar;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ouyan on 2016/9/26.
 */

public class TestActivity extends AppCompatActivity {
    private OneApplication oneApplication;
    @BindView(R.id.one_first_seekbar)
    public SeekBar bandSeekBar;
    @BindView(R.id.one_second_seekbar)
    public SeekBar levelSeekBar;
    private int band;
    private int level;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_activity);
        ButterKnife.bind(this);
        oneApplication = (OneApplication)getApplication();
        levelSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                level = i;
                oneApplication.getOnePlayer().setBandAndLevel((short)band,(short)level);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        bandSeekBar.setProgress(oneApplication.getOnePlayer().getEqualizer().);
        greenSeekBar.setProgress(green);
        blueSeekBar.setProgress(blue);
        redText.setText("红"+red);
        greenText.setText("绿"+green);
        blueText.setText("蓝"+blue);
        applyThemeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseOperator.saveConfig(oneApplication.getOneConfig(),OneColorSelectActivity.this);
                OneColorSelectActivity.this.finish();
            }
        });
        final Bitmap bitmap = OneBitmapUtil.zoomImg(this,R.drawable.ic_arrow_back_white_48dp,12);
        Drawable drawable = new Drawable() {
            Paint paint = new Paint();
            @Override
            public void draw(Canvas canvas) {

                canvas.drawBitmap(bitmap,canvas.getWidth()/2-bitmap.getWidth()/2,canvas.getHeight()/2-bitmap.getHeight()/2,paint);
            }

            @Override
            public void setAlpha(int alpha) {

            }

            @Override
            public void setColorFilter(ColorFilter colorFilter) {

            }

            @Override
            public int getOpacity() {
                return PixelFormat.OPAQUE;
            }
        };
        toolbar.setNavigationIcon(drawable);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OneColorSelectActivity.this.finish();
            }
        });
    }
}
