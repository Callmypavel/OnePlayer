package com.example.peacemaker.oneplayer;

import android.databinding.DataBindingUtil;
<<<<<<< HEAD
import android.media.audiofx.BassBoost;
import android.media.audiofx.Equalizer;
import android.media.audiofx.PresetReverb;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.peacemaker.oneplayer.databinding.TestActivityBinding;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;
=======
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
>>>>>>> parent of 530f60b... 增加测试功能均衡器

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
<<<<<<< HEAD
    @BindView(R.id.equailizer_field)
    public LinearLayout equalizerField;
    @BindView(R.id.bassboost_field)
    public LinearLayout bassBoostField;
    @BindView(R.id.presetreverb_field)
    public LinearLayout preserreverbField;
    @BindView(R.id.toolbar)
    public Toolbar toolbar;
    private TestActivityBinding binding;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

=======
>>>>>>> parent of 530f60b... 增加测试功能均衡器
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
<<<<<<< HEAD
        binding = DataBindingUtil.setContentView(TestActivity.this, R.layout.test_activity);
        ButterKnife.bind(this);
        oneApplication = (OneApplication) getApplication();
        binding.setOneConfig(oneApplication.getOneConfig());
        OneStatusUtil.setStatusColor(this, oneApplication.getThemeColor());
        initEqualizer();
        initBassBoost();
        initPresetReverb();
        toolbar.setNavigationIcon(DrawableTool.getBackArrowDrawable(this));
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TestActivity.this.finish();
            }
        });

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void initEqualizer() {
        final Equalizer equalizer = oneApplication.getOnePlayer().getEqualizer();
        short[] bandLevelRange = equalizer.getBandLevelRange();
        final int minEQLevel = bandLevelRange[0];
        int maxEQLevel = bandLevelRange[1];
        final short bandNumber = equalizer.getNumberOfBands();
        //LogTool.log("TestActivity", "band数量" + bandNumber);
        for (short i = 0; i < bandNumber; i++) {
            View view = LayoutInflater.from(this).inflate(R.layout.equalizer_field, null, false);
            final TextView textView = (TextView) (view.findViewById(R.id.frequency_text));
            textView.setText(StringUtil.getString(this,R.string.frequency,equalizer.getCenterFreq(i) / 100));
            SeekBar seekbar = (SeekBar) (view.findViewById(R.id.decibel_seekbar));
            seekbar.setMax(maxEQLevel - minEQLevel);
            seekbar.setProgress(equalizer.getBandLevel(i));
            final TextView textView1 = (TextView) (view.findViewById(R.id.decibel_text));
            final short finalI = i;
            seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    short value = (short) (progress + minEQLevel);
                    //LogTool.log("TestActivity","band"+finalI+"bandLevel"+value);
                    equalizer.setBandLevel(finalI, value);
                    textView1.setText(StringUtil.getString(TestActivity.this,R.string.decibel,value));
                    //LogTool.log("TestActivity","band分贝"+(short) (progress + minEQLevel));
                }
=======
        setContentView(R.layout.test_activity);
        ButterKnife.bind(this);
        oneApplication = (OneApplication)getApplication();
        levelSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                level = i;
                oneApplication.getOnePlayer().setBandAndLevel((short)band,(short)level);
            }
>>>>>>> parent of 530f60b... 增加测试功能均衡器

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

<<<<<<< HEAD
                }
            });
            textView1.setText(StringUtil.getString(TestActivity.this,R.string.decibel,0));
            equalizerField.addView(view);
        }
    }
=======
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
>>>>>>> parent of 530f60b... 增加测试功能均衡器

    private void initBassBoost() {
        final BassBoost bassBoost = oneApplication.getOnePlayer().getBassBoost();
        View view = LayoutInflater.from(this).inflate(R.layout.bassboost_field, null, false);
        SeekBar seekbar = (SeekBar) (view.findViewById(R.id.bassboost_power_seekbar));
        seekbar.setMax(1000);
        seekbar.setProgress(bassBoost.getRoundedStrength());
        final TextView textView = (TextView) (view.findViewById(R.id.bassboost_power_text));
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                bassBoost.setStrength((short) progress);
                textView.setText(StringUtil.getString(TestActivity.this,R.string.bassvalue,progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        textView.setText(StringUtil.getString(TestActivity.this,R.string.bassvalue,0));
        bassBoostField.addView(view);
    }

    private void initPresetReverb() {
        final PresetReverb presetreverb = oneApplication.getOnePlayer().getPresetReverb();
        final ArrayList<String> presetStrings = new ArrayList<>();
        presetStrings.add("无");
        presetStrings.add("小房间");
        presetStrings.add("中房间");
        presetStrings.add("大房间");
        presetStrings.add("中礼堂");
        presetStrings.add("大礼堂");
        presetStrings.add("板式混响");
        View view = LayoutInflater.from(this).inflate(R.layout.presetreverb_field, null, false);
        Spinner spinner = (Spinner) (view.findViewById(R.id.presetreverb_spinner));
        spinner.setAdapter(new ArrayAdapter<>(this,R.layout.one_spinner,presetStrings));
        spinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                presetreverb.setPreset((short)position);
            }

            @Override
<<<<<<< HEAD
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        preserreverbField.addView(view);
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Test Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
=======
            public void onClick(View v) {
                OneColorSelectActivity.this.finish();
            }
        });
>>>>>>> parent of 530f60b... 增加测试功能均衡器
    }
}
