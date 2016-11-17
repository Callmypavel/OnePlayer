package com.example.peacemaker.oneplayer;

import android.databinding.DataBindingUtil;
import android.media.audiofx.BassBoost;
import android.media.audiofx.EnvironmentalReverb;
import android.media.audiofx.Equalizer;
import android.media.audiofx.PresetReverb;
import android.media.audiofx.Virtualizer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.MalformedJsonException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.peacemaker.oneplayer.databinding.TestActivityBinding;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.net.MalformedURLException;
import java.security.spec.ECField;
import java.util.ArrayList;
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
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ouyan on 2016/9/26.
 */

public class TestActivity extends AppCompatActivity {
    private OneApplication oneApplication;
    private int band;
    private int level;
    @BindView(R.id.equailizer_field)
    public LinearLayout equalizerField;
    @BindView(R.id.bassboost_field)
    public LinearLayout bassBoostField;
    @BindView(R.id.presetreverb_field)
    public LinearLayout preserreverbField;
    @BindView(R.id.virtualizer_field)
    public LinearLayout virtualizerField;
    @BindView(R.id.environmental_reverb_field)
    public LinearLayout environmentalReverbField;
    @BindView(R.id.toolbar)
    public Toolbar toolbar;
    @BindView(R.id.address_edit_text)
    public EditText urlEditText;
    @BindView(R.id.content_edit_text)
    public EditText contentEditText;

    private TestActivityBinding binding;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(TestActivity.this, R.layout.test_activity);
        ButterKnife.bind(this);
        oneApplication = (OneApplication) getApplication();
        //oneApplication.setOneConfig(DatabaseOperator.loadConfig(this));
        binding.setOneConfig(oneApplication.getOneConfig());
        OneStatusUtil.setStatusColor(this, oneApplication.getThemeColor());
        initEqualizer();
        initBassBoost();
        initPresetReverb();
        initVirtualizer();
        initEnvironmentalReverb();
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
    public void saveEffect(View view){
        oneApplication.saveOneConfig(TestActivity.this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
    public void sendUrl(View v){
        try {
            JsonUtil.sendContent(contentEditText.getText().toString(),urlEditText.getText().toString(),this);
        }catch (Exception e){
            e.printStackTrace();
            if(e instanceof MalformedURLException){
                Toast.makeText(this,"URL协议错误",Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void initEqualizer() {
        final Equalizer equalizer = oneApplication.getOnePlayer().getEqualizer();
        final short[] bandLevelRange = equalizer.getBandLevelRange();
        final int minEQLevel = bandLevelRange[0];
        int maxEQLevel = bandLevelRange[1];
        final short bandNumber = equalizer.getNumberOfBands();
        //LogTool.log("TestActivity", "band数量" + bandNumber);
        for (short i = 0; i < bandNumber; i++) {
            View view = LayoutInflater.from(this).inflate(R.layout.equalizer_field, null, false);
            final TextView textView = (TextView) (view.findViewById(R.id.frequency_text));
            textView.setText(StringUtil.getString(this,R.string.frequency,equalizer.getCenterFreq(i) / 1000));
            SeekBar seekbar = (SeekBar) (view.findViewById(R.id.decibel_seekbar));
            seekbar.setMax(maxEQLevel - minEQLevel);
            final TextView textView1 = (TextView) (view.findViewById(R.id.decibel_text));
            final short finalI = i;
            seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    short value = (short) (progress + minEQLevel);
                    //LogTool.log("TestActivity","band"+finalI+"bandLevel"+value);
                    equalizer.setBandLevel(finalI, value);
                    textView1.setText(StringUtil.getString(TestActivity.this, R.string.decibel, value/100));
                    ArrayList<Integer> bandLevels = oneApplication.getOneConfig().getBandLevels();
                    bandLevels.set(finalI,(int)value);
                    oneApplication.getOneConfig().setBandLevels(bandLevels);
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
            LogTool.log("TestActivity","initEqualizer()初始化值"+oneApplication.getOneConfig().toString());
            LogTool.log("TestActivity","initEqualizer()初始化值"+oneApplication.getOneConfig().getBandLevels().get(i)+maxEQLevel);
            seekbar.setProgress(oneApplication.getOneConfig().getBandLevels().get(i)+maxEQLevel);
            equalizerField.addView(view);
        }
    }

    private void initBassBoost() {
        final BassBoost bassBoost = oneApplication.getOnePlayer().getBassBoost();
        View view = LayoutInflater.from(this).inflate(R.layout.bassboost_field, null, false);
        SeekBar seekbar = (SeekBar) (view.findViewById(R.id.bassboost_power_seekbar));
        seekbar.setMax(1000);
        final TextView textView = (TextView) (view.findViewById(R.id.bassboost_power_text));
        textView.setText(StringUtil.getString(TestActivity.this,R.string.value,0));
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                bassBoost.setStrength((short) progress);
                textView.setText(StringUtil.getString(TestActivity.this,R.string.value,progress));
                oneApplication.getOneConfig().setBassBoostStrenth(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        seekbar.setProgress(oneApplication.getOneConfig().getBassBoostStrenth());
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
                oneApplication.getOneConfig().setPresetReverb(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner.setSelection(oneApplication.getOneConfig().getPresetReverb());
        preserreverbField.addView(view);
    }
    private void initVirtualizer(){
        final Virtualizer virtualizer = oneApplication.getOnePlayer().getVirtualizer();
        View view = LayoutInflater.from(this).inflate(R.layout.virtualizer_field, null, false);
        SeekBar seekbar = (SeekBar) (view.findViewById(R.id.virtualizer_power_seekbar));
        seekbar.setMax(1000);
        final TextView textView = (TextView) (view.findViewById(R.id.virtualizer_power_text));
        textView.setText(StringUtil.getString(this,R.string.value,0));
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                virtualizer.setStrength((short)progress);
                textView.setText(StringUtil.getString(TestActivity.this,R.string.value,progress));
                oneApplication.getOneConfig().setVirtualizerStrenth(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        seekbar.setProgress(oneApplication.getOneConfig().getVirtualizerStrenth());
        virtualizerField.addView(view);

    }
    private void initEnvironmentalReverb(){
        final EnvironmentalReverb environmentalReverb = oneApplication.getOnePlayer().getEnvironmentalReverb();
        View view = LayoutInflater.from(this).inflate(R.layout.environment_reverb_field, null, false);
        initDecayTime(environmentalReverb, view);
        initDecayHFTime(environmentalReverb, view);
        initDensity(environmentalReverb, view);
        initDiffusion(environmentalReverb, view);
        initReflectionDelay(environmentalReverb, view);
        initReflectionsLevel(environmentalReverb, view);
        initReverbDelay(environmentalReverb, view);
        initReverbLevel(environmentalReverb, view);
        initRoomLevel(environmentalReverb, view);
        initRoomHFLevel(environmentalReverb, view);
        environmentalReverbField.addView(view);
    }

    private void initReverbDelay(final EnvironmentalReverb environmentalReverb, View view) {
        SeekBar seekbar = (SeekBar) (view.findViewById(R.id.reverbDelay_seekbar));
        seekbar.setMax(100);
        final TextView textView = (TextView) (view.findViewById(R.id.reverbDelay_value_text));
        textView.setText(StringUtil.getString(this,R.string.timeMeasurement,0));
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                environmentalReverb.setReverbDelay(progress);
                textView.setText(StringUtil.getString(TestActivity.this,R.string.timeMeasurement,progress));
                oneApplication.getOneConfig().getEnvironmentReverbConfig().setReverbDelay(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        seekbar.setProgress(oneApplication.getOneConfig().getEnvironmentReverbConfig().getReverbDelay()+100);
    }

    private void initRoomHFLevel(final EnvironmentalReverb environmentalReverb, View view) {
        SeekBar seekbar = (SeekBar) (view.findViewById(R.id.roomHFLevel_seekbar));
        seekbar.setMax(9000);
        final TextView textView = (TextView) (view.findViewById(R.id.roomHFLevel_value_text));
        textView.setText(StringUtil.getString(this,R.string.value,0));
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int value = progress-9000;
                environmentalReverb.setRoomHFLevel((short)value);
                textView.setText(StringUtil.getString(TestActivity.this,R.string.value,value));
                oneApplication.getOneConfig().getEnvironmentReverbConfig().setRoomHFLevel((short)value);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        seekbar.setProgress(oneApplication.getOneConfig().getEnvironmentReverbConfig().getRoomHFLevel()+9000);
    }

    private void initRoomLevel(final EnvironmentalReverb environmentalReverb, View view) {
        SeekBar seekbar = (SeekBar) (view.findViewById(R.id.roomLevel_seekbar));
        seekbar.setMax(9000);
        final TextView textView = (TextView) (view.findViewById(R.id.roomLevel_value_text));
        textView.setText(StringUtil.getString(this,R.string.value,0));
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int value = progress-9000;
                environmentalReverb.setRoomLevel((short)value);
                textView.setText(StringUtil.getString(TestActivity.this,R.string.value,value));
                oneApplication.getOneConfig().getEnvironmentReverbConfig().setRoomLevel((short)value);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        seekbar.setProgress(oneApplication.getOneConfig().getEnvironmentReverbConfig().getRoomLevel()+9000);
    }

    private void initReverbLevel(final EnvironmentalReverb environmentalReverb, View view) {
        SeekBar seekbar = (SeekBar) (view.findViewById(R.id.reverbLevel_seekbar));
        seekbar.setMax(11000);
        final TextView textView = (TextView) (view.findViewById(R.id.reverbLevel_value_text));
        textView.setText(StringUtil.getString(this,R.string.value,0));
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int value = progress-9000;
                environmentalReverb.setReverbLevel((short)value);
                textView.setText(StringUtil.getString(TestActivity.this,R.string.value,value));
                oneApplication.getOneConfig().getEnvironmentReverbConfig().setReverbLevel((short)value);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        seekbar.setProgress(oneApplication.getOneConfig().getEnvironmentReverbConfig().getReverbLevel()+9000);
    }

    private void initReflectionsLevel(final EnvironmentalReverb environmentalReverb, View view) {
        SeekBar seekbar = (SeekBar) (view.findViewById(R.id.reflectionLevel_seekbar));
        seekbar.setMax(10000);
        final TextView textView = (TextView) (view.findViewById(R.id.reflectionLevel_value_text));
        textView.setText(StringUtil.getString(this,R.string.value,0));
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int value = progress-9000;
                LogTool.log("TestActivity","查看反射等级"+(short)value);
                environmentalReverb.setReflectionsLevel((short)value);
                textView.setText(StringUtil.getString(TestActivity.this,R.string.value,value));
                oneApplication.getOneConfig().getEnvironmentReverbConfig().setReflectionsLevel((short)value);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        seekbar.setProgress(oneApplication.getOneConfig().getEnvironmentReverbConfig().getReflectionsLevel()+9000);
    }

    private void initReflectionDelay(final EnvironmentalReverb environmentalReverb, View view) {
        SeekBar seekbar = (SeekBar) (view.findViewById(R.id.reflectionDelay_seekbar));
        seekbar.setMax(300);
        final TextView textView = (TextView) (view.findViewById(R.id.reflectionDelay_value_text));
        textView.setText(StringUtil.getString(this,R.string.timeMeasurement,0));
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                environmentalReverb.setReflectionsDelay(progress);
                textView.setText(StringUtil.getString(TestActivity.this,R.string.timeMeasurement,progress));
                oneApplication.getOneConfig().getEnvironmentReverbConfig().setReflectionsDelay(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        seekbar.setProgress(oneApplication.getOneConfig().getEnvironmentReverbConfig().getReflectionsDelay());
    }

    private void initDiffusion(final EnvironmentalReverb environmentalReverb, View view) {
        SeekBar seekbar = (SeekBar) (view.findViewById(R.id.diffusion_seekbar));
        seekbar.setMax(1000);
        final TextView textView = (TextView) (view.findViewById(R.id.diffusion_value_text));
        textView.setText(StringUtil.getString(this,R.string.value,0));
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                environmentalReverb.setDiffusion((short)progress);
                textView.setText(StringUtil.getString(TestActivity.this,R.string.value,progress));
                oneApplication.getOneConfig().getEnvironmentReverbConfig().setDiffusion((short)progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        seekbar.setProgress(oneApplication.getOneConfig().getEnvironmentReverbConfig().getDiffusion());
    }

    private void initDensity(final EnvironmentalReverb environmentalReverb, View view) {
        SeekBar seekbar = (SeekBar) (view.findViewById(R.id.density_seekbar));
        seekbar.setMax(1000);
        final TextView textView = (TextView) (view.findViewById(R.id.density_value_text));
        textView.setText(StringUtil.getString(this,R.string.value,0));
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                LogTool.log("TestActivity","查看欲设"+(short)progress);
                environmentalReverb.setDensity((short)progress);
                textView.setText(StringUtil.getString(TestActivity.this,R.string.value,progress));
                oneApplication.getOneConfig().getEnvironmentReverbConfig().setDensity((short)progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        seekbar.setProgress(oneApplication.getOneConfig().getEnvironmentReverbConfig().getDensity());
    }

    private void initDecayHFTime(final EnvironmentalReverb environmentalReverb, View view) {
        SeekBar seekbar = (SeekBar) (view.findViewById(R.id.decayHF_seekbar));
        seekbar.setMax(1900);
        final TextView textView = (TextView) (view.findViewById(R.id.decayHF_value_text));
        textView.setText(StringUtil.getString(this,R.string.timeMeasurement,0));
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int value = progress+100;
                environmentalReverb.setDecayHFRatio((short)value);
                textView.setText(StringUtil.getString(TestActivity.this,R.string.timeMeasurement,value));
                oneApplication.getOneConfig().getEnvironmentReverbConfig().setDecayHFTime(value);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        seekbar.setProgress(oneApplication.getOneConfig().getEnvironmentReverbConfig().getDecayHFTime()-100);
    }

    private void initDecayTime(final EnvironmentalReverb environmentalReverb, View view) {
        SeekBar seekbar = (SeekBar) (view.findViewById(R.id.decay_seekbar));
        seekbar.setMax(19900);
        final TextView textView = (TextView) (view.findViewById(R.id.decay_value_text));
        textView.setText(StringUtil.getString(this,R.string.timeMeasurement,0));
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int value = progress+100;
                environmentalReverb.setDecayTime((short)value);
                textView.setText(StringUtil.getString(TestActivity.this,R.string.timeMeasurement,value));
                oneApplication.getOneConfig().getEnvironmentReverbConfig().setDecayTime(value);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        seekbar.setProgress(oneApplication.getOneConfig().getEnvironmentReverbConfig().getDecayTime()-100);
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
    }

}
