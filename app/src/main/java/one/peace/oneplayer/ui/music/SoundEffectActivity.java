package one.peace.oneplayer.ui.music;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.media.audiofx.BassBoost;
import android.media.audiofx.EnvironmentalReverb;
import android.media.audiofx.Equalizer;
import android.media.audiofx.PresetReverb;
import android.media.audiofx.Virtualizer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.net.MalformedURLException;
import java.util.ArrayList;

import peacemaker.oneplayer.R;
import peacemaker.oneplayer.activity.OneApplication;
import peacemaker.oneplayer.databinding.SoundEffectActivityBinding;
import peacemaker.oneplayer.entity.OneConfig;
import peacemaker.oneplayer.tool.DrawableTool;
import peacemaker.oneplayer.tool.LogTool;
import peacemaker.oneplayer.tool.OneStatusUtil;
import peacemaker.oneplayer.tool.StringUtil;


/**
 * Created by ouyan on 2016/9/26.
 */

public class SoundEffectActivity extends AppCompatActivity {
    private OneApplication oneApplication;
    private int band;
    private int level;
//    @BindView(R.id.equailizer_field)
//    public LinearLayout equalizerField;
//    @BindView(R.id.bassboost_field)
//    public LinearLayout bassBoostField;
//    @BindView(R.id.presetreverb_field)
//    public LinearLayout preserreverbField;
//    @BindView(R.id.virtualizer_field)
//    public LinearLayout virtualizerField;
//    @BindView(R.id.environmental_reverb_field)
//    public LinearLayout environmentalReverbField;
//    @BindView(R.id.address_edit_text)
//    public EditText urlEditText;
//    @BindView(R.id.content_edit_text)
//    public EditText contentEditText;
//    @BindView(R.id.id_tablayout)
//    public TabLayout tabLayout;
//    @BindView(R.id.sound_effect_switch)
//    public Switch generalSwitch;
////    @BindView(R.id.close_button)
////    public ImageButton closeButton;
//    @BindView(R.id.toolbar)
//    public Toolbar toolbar;
    private ArrayList<SeekBar> seekBars;
    private SoundEffectActivityBinding binding;
    private int maxEQLevel;
    private int bandNumber;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(SoundEffectActivity.this, R.layout.sound_effect_activity);
        oneApplication = (OneApplication) getApplication();
        //oneApplication.setOneConfig(DatabaseOperator.loadConfig(this));
        binding.setOneConfig(oneApplication.getOneConfig());
        OneStatusUtil.setStatusColor(this, oneApplication.getThemeColor());
        initEqualizer();
        initBassBoost();
        initPresetReverb();
        initVirtualizer();
        initEnvironmentalReverb();
        binding.toolbar.setNavigationIcon(DrawableTool.getBackArrowDrawable(this));
        setSupportActionBar(binding.toolbar);
        binding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        binding.idTablayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        binding.idTablayout.addTab(binding.idTablayout.newTab().setText("普通"));
        binding.idTablayout.addTab(binding.idTablayout.newTab().setText("舞曲"));
        binding.idTablayout.addTab(binding.idTablayout.newTab().setText("平淡"));
        binding.idTablayout.addTab(binding.idTablayout.newTab().setText("民谣"));
        binding.idTablayout.addTab(binding.idTablayout.newTab().setText("重金属"));
        binding.idTablayout.addTab(binding.idTablayout.newTab().setText("嘻哈"));
        binding.idTablayout.addTab(binding.idTablayout.newTab().setText("爵士"));
        binding.idTablayout.addTab(binding.idTablayout.newTab().setText("流行"));
        binding.idTablayout.addTab(binding.idTablayout.newTab().setText("摇滚"));
        binding.idTablayout.addTab(binding.idTablayout.newTab().setText("经典"));
        binding.idTablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                switch (position){
                    case 0:
                        seekBars.get(0).setProgress(3+15);
                        seekBars.get(1).setProgress(0+15);
                        seekBars.get(2).setProgress(0+15);
                        seekBars.get(3).setProgress(0+15);
                        seekBars.get(4).setProgress(3+15);
                        break;
                    case 1:
                        seekBars.get(0).setProgress(6+15);
                        seekBars.get(1).setProgress(0+15);
                        seekBars.get(2).setProgress(2+15);
                        seekBars.get(3).setProgress(4+15);
                        seekBars.get(4).setProgress(1+15);
                        break;
                    case 2:
                        seekBars.get(0).setProgress(0+15);
                        seekBars.get(1).setProgress(0+15);
                        seekBars.get(2).setProgress(0+15);
                        seekBars.get(3).setProgress(0+15);
                        seekBars.get(4).setProgress(0+15);
                        break;
                    case 3:
                        seekBars.get(0).setProgress(3+15);
                        seekBars.get(1).setProgress(0+15);
                        seekBars.get(2).setProgress(0+15);
                        seekBars.get(3).setProgress(2+15);
                        seekBars.get(4).setProgress(-1+15);
                        break;
                    case 4:
                        seekBars.get(0).setProgress(4+15);
                        seekBars.get(1).setProgress(1+15);
                        seekBars.get(2).setProgress(9+15);
                        seekBars.get(3).setProgress(3+15);
                        seekBars.get(4).setProgress(0+15);
                        break;
                    case 5:
                        seekBars.get(0).setProgress(5+15);
                        seekBars.get(1).setProgress(3+15);
                        seekBars.get(2).setProgress(0+15);
                        seekBars.get(3).setProgress(1+15);
                        seekBars.get(4).setProgress(3+15);
                        break;
                    case 6:
                        seekBars.get(0).setProgress(4+15);
                        seekBars.get(1).setProgress(2+15);
                        seekBars.get(2).setProgress(-2+15);
                        seekBars.get(3).setProgress(2+15);
                        seekBars.get(4).setProgress(5+15);
                        break;
                    case 7:
                        seekBars.get(0).setProgress(-1+15);
                        seekBars.get(1).setProgress(2+15);
                        seekBars.get(2).setProgress(5+15);
                        seekBars.get(3).setProgress(1+15);
                        seekBars.get(4).setProgress(-2+15);
                        break;
                    case 8:
                        seekBars.get(0).setProgress(5+15);
                        seekBars.get(1).setProgress(3+15);
                        seekBars.get(2).setProgress(-1+15);
                        seekBars.get(3).setProgress(3+15);
                        seekBars.get(4).setProgress(5+15);
                        break;
                    case 9:
                        seekBars.get(0).setProgress(5+15);
                        seekBars.get(1).setProgress(3+15);
                        seekBars.get(2).setProgress(-2+15);
                        seekBars.get(3).setProgress(4+15);
                        seekBars.get(4).setProgress(4+15);
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        binding.soundEffectSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                oneApplication.getOnePlayer().activateSoundEffectsExceptVisualizer(b);
                if(b){
                    for(int i=0;i<bandNumber;i++){
                        seekBars.get(i).setProgress(oneApplication.getOneConfig().getBandLevels().get(i)+maxEQLevel/100);
                    }
                    seekBars.get(bandNumber).setProgress(oneApplication.getOneConfig().getBassBoostStrenth());
                    seekBars.get(bandNumber+1).setProgress(oneApplication.getOneConfig().getVirtualizerStrenth());
                    seekBars.get(bandNumber+2).setProgress(oneApplication.getOneConfig().getEnvironmentReverbConfig().getDecayTime()-100);
                    seekBars.get(bandNumber+3).setProgress(oneApplication.getOneConfig().getEnvironmentReverbConfig().getDecayHFTime()-100);
                    seekBars.get(bandNumber+4).setProgress(oneApplication.getOneConfig().getEnvironmentReverbConfig().getDensity());
                    seekBars.get(bandNumber+5).setProgress(oneApplication.getOneConfig().getEnvironmentReverbConfig().getDiffusion());
                    seekBars.get(bandNumber+6).setProgress(oneApplication.getOneConfig().getEnvironmentReverbConfig().getReflectionsDelay());
                    seekBars.get(bandNumber+7).setProgress(oneApplication.getOneConfig().getEnvironmentReverbConfig().getReflectionsLevel()-9000);
                    seekBars.get(bandNumber+8).setProgress(oneApplication.getOneConfig().getEnvironmentReverbConfig().getReverbDelay()+100);
                    seekBars.get(bandNumber+9).setProgress(oneApplication.getOneConfig().getEnvironmentReverbConfig().getReverbLevel()+9000);
                    seekBars.get(bandNumber+10).setProgress(oneApplication.getOneConfig().getEnvironmentReverbConfig().getRoomLevel()*1000+9000);
                    seekBars.get(bandNumber+11).setProgress(oneApplication.getOneConfig().getEnvironmentReverbConfig().getRoomHFLevel()*1000+9000);
                    LogTool.log("SoundEffectActivity","检查decaytime去除"+oneApplication.getOneConfig().getEnvironmentReverbConfig().getDecayTime());
                    LogTool.log("SoundEffectActivity","test"+oneApplication.getOneConfig().getEnvironmentReverbConfig().getDecayHFTime());
                    LogTool.log("SoundEffectActivity","test"+oneApplication.getOneConfig().getEnvironmentReverbConfig().getDensity());
                    LogTool.log("SoundEffectActivity","test"+oneApplication.getOneConfig().getEnvironmentReverbConfig().getDiffusion());
                    LogTool.log("SoundEffectActivity","test"+oneApplication.getOneConfig().getEnvironmentReverbConfig().getReflectionsDelay());
                    LogTool.log("SoundEffectActivity","test"+oneApplication.getOneConfig().getEnvironmentReverbConfig().getReflectionsLevel());
                    LogTool.log("SoundEffectActivity","test"+oneApplication.getOneConfig().getEnvironmentReverbConfig().getReverbDelay());
                    LogTool.log("SoundEffectActivity","test"+oneApplication.getOneConfig().getEnvironmentReverbConfig().getReverbLevel());
                    LogTool.log("SoundEffectActivity","test"+oneApplication.getOneConfig().getEnvironmentReverbConfig().getRoomLevel());
                    LogTool.log("SoundEffectActivity","test"+oneApplication.getOneConfig().getEnvironmentReverbConfig().getRoomHFLevel());
                }

            }
        });

//        closeButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                SoundEffectActivity.this.finish();
//            }
//        });

    }
    public void saveEffect(View view){
        oneApplication.saveOneConfig(SoundEffectActivity.this);
        Snackbar.make(findViewById(R.id.save_sound_effects_button), StringUtil.getString(this,R.string.saved_sound_effects),Snackbar.LENGTH_SHORT).show();

    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Intent intent = new Intent();
        intent.setClass(this,MainActivity.class);
        startActivity(intent);
    }
    public void sendUrl(View v){
        try {
            //JsonUtil.sendContent(contentEditText.getText().toString(),urlEditText.getText().toString(),this);
        }catch (Exception e){
            e.printStackTrace();
            if(e instanceof MalformedURLException){
                Toast.makeText(this,"URL协议错误", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void initEqualizer() {
        seekBars = new ArrayList<>();
        final Equalizer equalizer = oneApplication.getOnePlayer().getEqualizer();
        OneConfig oneConfig = oneApplication.getOneConfig();
        short[] bandLevelRange = equalizer.getBandLevelRange();
        final int minEQLevel = bandLevelRange[0];
        maxEQLevel = bandLevelRange[1];
        LogTool.log("TestActivity","最小值为"+minEQLevel+"最大值为"+maxEQLevel);
        bandNumber = (short)oneConfig.getBandLevels().size();
        //LogTool.log("TestActivity", "band数量" + bandNumber);
        for (short i = 0; i < bandNumber; i++) {
            View view = LayoutInflater.from(this).inflate(R.layout.equalizer_field, null, false);
            final TextView textView = (TextView) (view.findViewById(R.id.frequency_text));
            textView.setText(StringUtil.getString(this,R.string.frequency,equalizer.getCenterFreq(i) / 1000));
            SeekBar seekbar = (SeekBar) (view.findViewById(R.id.decibel_seekbar));
            seekbar.setMax((maxEQLevel - minEQLevel)/100);
            final TextView textView1 = (TextView) (view.findViewById(R.id.decibel_text));
            final short finalI = i;
            seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    short value = (short) (progress + minEQLevel/100);
                    LogTool.log("TestActivity","band"+finalI+"bandLevel"+value);
                    LogTool.log("TestActivity","均衡器是否受控"+equalizer.hasControl());
                    //equalizer.setEnabled(false);
                    equalizer.setBandLevel(finalI,(short)(value*100));
                    //equalizer.setEnabled(true);
                    textView1.setText(StringUtil.getString(SoundEffectActivity.this, R.string.decibel, value));
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
            //LogTool.log("TestActivity","initEqualizer()初始化值"+oneApplication.getOneConfig().toString());
            //LogTool.log("TestActivity","initEqualizer()初始化值"+oneApplication.getOneConfig().getBandLevels().get(i)+maxEQLevel);
            seekbar.setProgress(oneApplication.getOneConfig().getBandLevels().get(i)+maxEQLevel/100);
            seekBars.add(seekbar);
            binding.equailizerField.addView(view);
        }
    }

    private void initBassBoost() {
        final BassBoost bassBoost = oneApplication.getOnePlayer().getBassBoost();
        View view = LayoutInflater.from(this).inflate(R.layout.bassboost_field, null, false);
        SeekBar seekbar = (SeekBar) (view.findViewById(R.id.bassboost_power_seekbar));
        seekbar.setMax(1000);
        final TextView textView = (TextView) (view.findViewById(R.id.bassboost_power_text));
        textView.setText(StringUtil.getString(SoundEffectActivity.this,R.string.value,0));
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                bassBoost.setStrength((short) progress);
                textView.setText(StringUtil.getString(SoundEffectActivity.this,R.string.value,progress));
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
        binding.bassboostField.addView(view);
        seekBars.add(seekbar);
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
        binding.presetreverbField.addView(view);
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
                textView.setText(StringUtil.getString(SoundEffectActivity.this,R.string.value,progress));
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
        binding.virtualizerField.addView(view);
        seekBars.add(seekbar);
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
        binding.environmentalReverbField.addView(view);
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
                textView.setText(StringUtil.getString(SoundEffectActivity.this,R.string.timeMeasurement,progress));
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
        seekBars.add(seekbar);
    }

    private void initRoomHFLevel(final EnvironmentalReverb environmentalReverb, View view) {
        SeekBar seekbar = (SeekBar) (view.findViewById(R.id.roomHFLevel_seekbar));
        seekbar.setMax(9);
        final TextView textView = (TextView) (view.findViewById(R.id.roomHFLevel_value_text));
        textView.setText(StringUtil.getString(this,R.string.value,0));
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int value = progress-9;
                environmentalReverb.setRoomHFLevel((short)(value*1000));
                textView.setText(StringUtil.getString(SoundEffectActivity.this,R.string.decibel,value));
                oneApplication.getOneConfig().getEnvironmentReverbConfig().setRoomHFLevel((short)value);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        seekbar.setProgress(oneApplication.getOneConfig().getEnvironmentReverbConfig().getRoomHFLevel()*1000+9000);
        seekBars.add(seekbar);
    }

    private void initRoomLevel(final EnvironmentalReverb environmentalReverb, View view) {
        SeekBar seekbar = (SeekBar) (view.findViewById(R.id.roomLevel_seekbar));
        seekbar.setMax(9);
        final TextView textView = (TextView) (view.findViewById(R.id.roomLevel_value_text));
        textView.setText(StringUtil.getString(this,R.string.decibel,0));
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int value = progress-9;
                environmentalReverb.setRoomLevel((short)value);
                textView.setText(StringUtil.getString(SoundEffectActivity.this,R.string.decibel,value));
                oneApplication.getOneConfig().getEnvironmentReverbConfig().setRoomLevel((short)(value*1000));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        seekbar.setProgress(oneApplication.getOneConfig().getEnvironmentReverbConfig().getRoomLevel()*1000+9000);
        seekBars.add(seekbar);
    }

    private void initReverbLevel(final EnvironmentalReverb environmentalReverb, View view) {
        SeekBar seekbar = (SeekBar) (view.findViewById(R.id.reverbLevel_seekbar));
        seekbar.setMax(10);
        final TextView textView = (TextView) (view.findViewById(R.id.reverbLevel_value_text));
        textView.setText(StringUtil.getString(this,R.string.decibel,0));
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int value = progress-9;
                environmentalReverb.setReverbLevel((short)value);
                textView.setText(StringUtil.getString(SoundEffectActivity.this,R.string.decibel,value));
                oneApplication.getOneConfig().getEnvironmentReverbConfig().setReverbLevel((short)(value*1000));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        seekbar.setProgress(oneApplication.getOneConfig().getEnvironmentReverbConfig().getReverbLevel()+9000);
        seekBars.add(seekbar);
    }

    private void initReflectionsLevel(final EnvironmentalReverb environmentalReverb, View view) {
        SeekBar seekbar = (SeekBar) (view.findViewById(R.id.reflectionLevel_seekbar));
        seekbar.setMax(10);
        final TextView textView = (TextView) (view.findViewById(R.id.reflectionLevel_value_text));
        textView.setText(StringUtil.getString(this,R.string.value,0));
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int value = progress-9;
                LogTool.log("TestActivity","查看反射等级"+(short)value);
                environmentalReverb.setReflectionsLevel((short)value);
                textView.setText(StringUtil.getString(SoundEffectActivity.this,R.string.decibel,value));
                oneApplication.getOneConfig().getEnvironmentReverbConfig().setReflectionsLevel((short)(value*1000));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        seekbar.setProgress(oneApplication.getOneConfig().getEnvironmentReverbConfig().getReflectionsLevel()+9000);
        seekBars.add(seekbar);
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
                textView.setText(StringUtil.getString(SoundEffectActivity.this,R.string.timeMeasurement,progress));
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
        seekBars.add(seekbar);
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
                textView.setText(StringUtil.getString(SoundEffectActivity.this,R.string.value,progress));
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
        seekBars.add(seekbar);
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
                textView.setText(StringUtil.getString(SoundEffectActivity.this,R.string.value,progress));
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
        seekBars.add(seekbar);
    }

    private void initDecayHFTime(final EnvironmentalReverb environmentalReverb, View view) {
        SeekBar seekbar = (SeekBar) (view.findViewById(R.id.decayHF_seekbar));
        seekbar.setMax(19);
        final TextView textView = (TextView) (view.findViewById(R.id.decayHF_value_text));
        textView.setText(StringUtil.getString(this,R.string.timeMeasurement,0));
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int value = progress*100+100;
                environmentalReverb.setDecayHFRatio((short)value);
                textView.setText(StringUtil.getString(SoundEffectActivity.this,R.string.timeMeasurement,value));
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
        seekBars.add(seekbar);
    }

    private void initDecayTime(final EnvironmentalReverb environmentalReverb, View view) {
        SeekBar seekbar = (SeekBar) (view.findViewById(R.id.decay_seekbar));
        seekbar.setMax(199);
        final TextView textView = (TextView) (view.findViewById(R.id.decay_value_text));
        textView.setText(StringUtil.getString(this,R.string.timeMeasurement,0));
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int value = progress*100+100;
                environmentalReverb.setDecayTime((short)value);
                textView.setText(StringUtil.getString(SoundEffectActivity.this,R.string.timeMeasurement,value));
                oneApplication.getOneConfig().getEnvironmentReverbConfig().setDecayTime(value);
                LogTool.log("TestActivity","检查decaytime"+value);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        seekbar.setProgress(oneApplication.getOneConfig().getEnvironmentReverbConfig().getDecayTime()-100);
        seekBars.add(seekbar);
    }



    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onStop() {
        super.onStop();

    }

}