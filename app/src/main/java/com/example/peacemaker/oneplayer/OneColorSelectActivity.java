package com.example.peacemaker.oneplayer;

import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.peacemaker.oneplayer.databinding.OneColorSelectActivityBinding;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ouyan on 2016/10/8.
 */

public class OneColorSelectActivity extends AppCompatActivity {
    private OneApplication oneApplication;
    @BindView(R.id.one_red_color_seekbar)
    public SeekBar redSeekBar;
    @BindView(R.id.one_green_color_seekbar)
    public SeekBar greenSeekBar;
    @BindView(R.id.one_blue_color_seekbar)
    public SeekBar blueSeekBar;
    @BindView(R.id.red_tint)
    public TextView redText;
    @BindView(R.id.green_tint)
    public TextView greenText;
    @BindView(R.id.blue_tint)
    public TextView blueText;
    @BindView(R.id.apply_button)
    public Button applyThemeButton;
    @BindView(R.id.toolbar)
    public Toolbar toolbar;
    private int red;
    private int green;
    private int blue;
    private OneColorSelectActivityBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT > 19) {
            setTheme(R.style.Oneplayer);
            Log.v("MainActivity", "我选择OnePlayer主题");
        } else {
            setTheme(R.style.One);
            Log.v("MainActivity", "我选择One主题");
        }
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(OneColorSelectActivity.this,R.layout.one_color_select_activity);
        ButterKnife.bind(this);
        oneApplication = (OneApplication)getApplication();
        int initColor = oneApplication.getOneConfig().getThemeColor();
        red = Color.red(initColor);
        green = Color.green(initColor);
        blue = Color.blue(initColor);
        OneStatusUtil.setStatusColor(this,initColor);
        binding.setOneConfig(oneApplication.getOneConfig());
//        redText.setText(StringUtil.getString(OneColorSelectActivity.this,R.string.color_red,red));
//        greenText.setText(StringUtil.getString(OneColorSelectActivity.this,R.string.color_green,green));
//        blueText.setText(StringUtil.getString(OneColorSelectActivity.this,R.string.color_blue,blue));
        redSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                red = i;
                int themeColor = Color.argb(255,red,green,blue);
                oneApplication.getOneConfig().setThemeColor(themeColor);
                OneStatusUtil.setStatusColor(OneColorSelectActivity.this,themeColor);
                redText.setText(StringUtil.getString(OneColorSelectActivity.this,R.string.color_red,red));

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        greenSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                green = i;
                int themeColor = Color.argb(255,red,green,blue);
                oneApplication.getOneConfig().setThemeColor(themeColor);
                OneStatusUtil.setStatusColor(OneColorSelectActivity.this,themeColor);
                greenText.setText(StringUtil.getString(OneColorSelectActivity.this,R.string.color_green,green));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        blueSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                blue = i;
                int themeColor = Color.argb(255,red,green,blue);
                oneApplication.getOneConfig().setThemeColor(themeColor);
                OneStatusUtil.setStatusColor(OneColorSelectActivity.this,themeColor);
                blueText.setText(StringUtil.getString(OneColorSelectActivity.this,R.string.color_blue,blue));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        redSeekBar.setProgress(red);
        greenSeekBar.setProgress(green);
        blueSeekBar.setProgress(blue);



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

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }
}
