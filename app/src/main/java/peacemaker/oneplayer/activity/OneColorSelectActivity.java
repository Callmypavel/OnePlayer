package peacemaker.oneplayer.activity;

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

import peacemaker.oneplayer.tool.DatabaseOperator;
import peacemaker.oneplayer.activity.OneApplication;
import peacemaker.oneplayer.R;
import peacemaker.oneplayer.databinding.OneColorSelectActivityBinding;
import peacemaker.oneplayer.tool.OneBitmapUtil;
import peacemaker.oneplayer.tool.OneStatusUtil;


/**
 * Created by ouyan on 2016/10/8.
 */

public class OneColorSelectActivity extends AppCompatActivity {
    private OneApplication oneApplication;
//    @BindView(R.id.one_red_color_seekbar)
//    public SeekBar redSeekBar;
//    @BindView(R.id.one_green_color_seekbar)
//    public SeekBar greenSeekBar;
//    @BindView(R.id.one_blue_color_seekbar)
//    public SeekBar blueSeekBar;
//    @BindView(R.id.one_alpha_seekbar)
//    public SeekBar alphaSeekBar;
//    @BindView(R.id.one_radius_seekbar)
//    public SeekBar radiusSeekBar;
//    @BindView(R.id.apply_button)
//    public Button applyThemeButton;
//    @BindView(R.id.toolbar)
//    public Toolbar toolbar;
    private int red;
    private int green;
    private int blue;
    private int alpha;
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
        oneApplication = (OneApplication)getApplication();
        int initColor = oneApplication.getOneConfig().getThemeColor();
        red = Color.red(initColor);
        green = Color.green(initColor);
        blue = Color.blue(initColor);
        alpha = Color.alpha(initColor);
        OneStatusUtil.setStatusColor(this,initColor);
        binding.setOneConfig(oneApplication.getOneConfig());
        binding.setMusicState(oneApplication.musicState);
//        redText.setText(StringUtil.getString(OneColorSelectActivity.this,R.string.color_red,red));
//        greenText.setText(StringUtil.getString(OneColorSelectActivity.this,R.string.color_green,green));
//        blueText.setText(StringUtil.getString(OneColorSelectActivity.this,R.string.color_blue,blue));
        binding.oneRedColorSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                red = i;
                int themeColor = Color.argb(alpha,red,green,blue);
                oneApplication.getOneConfig().setThemeColor(themeColor);
                OneStatusUtil.setStatusColor(OneColorSelectActivity.this,themeColor);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        binding.oneGreenColorSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                green = i;
                int themeColor = Color.argb(alpha,red,green,blue);
                oneApplication.getOneConfig().setThemeColor(themeColor);
                OneStatusUtil.setStatusColor(OneColorSelectActivity.this,themeColor);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        binding.oneBlueColorSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                blue = i;
                int themeColor = Color.argb(alpha,red,green,blue);
                oneApplication.getOneConfig().setThemeColor(themeColor);
                OneStatusUtil.setStatusColor(OneColorSelectActivity.this,themeColor);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        binding.oneAlphaSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                alpha = i;
                int themeColor = Color.argb(alpha,red,green,blue);
                oneApplication.getOneConfig().setThemeColor(themeColor);
                OneStatusUtil.setStatusColor(OneColorSelectActivity.this,themeColor);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        binding.oneRadiusSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            private int previous;
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if(i+1!=previous){
                    oneApplication.getOneConfig().setBlurRadius(i+1);
                    previous = i+1;
                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        binding.oneRedColorSeekbar.setProgress(red);
        binding.oneGreenColorSeekbar.setProgress(green);
        binding.oneBlueColorSeekbar.setProgress(blue);
        binding.oneAlphaSeekbar.setProgress(alpha);
        binding.oneRadiusSeekbar.setProgress(oneApplication.getOneConfig().getBlurRadius()-1);
        binding.applyButton.setOnClickListener(new View.OnClickListener() {
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
        binding.toolbar.setNavigationIcon(drawable);
        setSupportActionBar(binding.toolbar);
        binding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
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
