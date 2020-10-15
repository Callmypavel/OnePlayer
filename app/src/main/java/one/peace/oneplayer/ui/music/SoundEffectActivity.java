package one.peace.oneplayer.ui.music;

import android.media.audiofx.BassBoost;
import android.media.audiofx.EnvironmentalReverb;
import android.media.audiofx.Equalizer;
import android.media.audiofx.PresetReverb;
import android.media.audiofx.Virtualizer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import one.peace.oneplayer.R;
import one.peace.oneplayer.base.OneApplication;
import one.peace.oneplayer.database.AppDatabase;
import one.peace.oneplayer.databinding.ActivitySoundEffectBinding;
import one.peace.oneplayer.global.config.SoundEffectConfig;
import one.peace.oneplayer.music.player.OnePlayer;
import one.peace.oneplayer.util.LogTool;
import one.peace.oneplayer.util.OneBitmapUtil;
import one.peace.oneplayer.util.StringUtil;


/**
 * Created by ouyan on 2016/9/26.
 */

public class SoundEffectActivity extends AppCompatActivity {
    private OneApplication oneApplication;
    private int band;
    private int level;

    private ArrayList<SeekBar> seekBars;
    private ActivitySoundEffectBinding binding;
    private int maxEQLevel;
    private int bandNumber;
    private SoundEffectConfig mSoundEffectConfig;
    private OnePlayer mOnePlayer;
    private String[] equalizerModeTexts = new String[]{"普通", "舞曲", "民谣", "重金属", "嘻哈", "爵士", "流行", "摇滚", "经典"};
    private int seekBarInitValue = 15;
    private int[][] equalizerPresetValues = new int[][]{
            {3, 0, 0, 0, 3},
            {6, 0, 2, 4, 1},
            {0, 0, 0, 0, 0},
            {3, 0, 0, 2, -1},
            {4, 1, 9, 3, 0},
            {5, 3, 0, 1, 3},
            {4, 2, -2, 2, 5},
            {-1, 2, 5, 1, 2},
            {5, 3, -1, 3, 5},
            {5, 3, -2, 4, 4}
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(SoundEffectActivity.this, R.layout.activity_sound_effect);
        mOnePlayer = OnePlayer.getInstance(this, new SoundEffectConfig.SoundEffectConfigListener() {
            @Override
            public void onSoundEffectConfigLoaded(SoundEffectConfig soundEffectConfig) {
                mSoundEffectConfig = soundEffectConfig;

                initEqualizer();
                initBassBoost();
                initPresetReverb();
                initVirtualizer();
                initEnvironmentalReverb();
                binding.toolbar.setNavigationIcon(OneBitmapUtil.getBackArrowDrawable(SoundEffectActivity.this));
                setSupportActionBar(binding.toolbar);
                binding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onBackPressed();
                    }
                });
                binding.idTablayout.setTabMode(TabLayout.MODE_SCROLLABLE);
                for (String equalizerModeText : equalizerModeTexts) {
                    binding.idTablayout.addTab(binding.idTablayout.newTab().setText(equalizerModeText));
                }
                binding.idTablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        int position = tab.getPosition();
                        int[] equalizerPresetValue = equalizerPresetValues[position];
                        for (int i = 0; i < equalizerPresetValue.length; i++) {
                            seekBars.get(i).setProgress(seekBarInitValue + equalizerPresetValue[i]);
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
                        mOnePlayer.activateSoundEffectsExceptVisualizer(b);
                        if (b) {
                            for (int i = 0; i < bandNumber; i++) {
                                seekBars.get(i).setProgress(mSoundEffectConfig.getBandLevels().get(i) + maxEQLevel / 100);
                            }
                            seekBars.get(bandNumber).setProgress(mSoundEffectConfig.getBassBoostStrenth());
                            seekBars.get(bandNumber + 1).setProgress(mSoundEffectConfig.getVirtualizerStrength());
                            seekBars.get(bandNumber + 2).setProgress(mSoundEffectConfig.getEnvironmentReverbConfig().getDecayTime() - 100);
                            seekBars.get(bandNumber + 3).setProgress(mSoundEffectConfig.getEnvironmentReverbConfig().getDecayHFTime() - 100);
                            seekBars.get(bandNumber + 4).setProgress(mSoundEffectConfig.getEnvironmentReverbConfig().getDensity());
                            seekBars.get(bandNumber + 5).setProgress(mSoundEffectConfig.getEnvironmentReverbConfig().getDiffusion());
                            seekBars.get(bandNumber + 6).setProgress(mSoundEffectConfig.getEnvironmentReverbConfig().getReflectionsDelay());
                            seekBars.get(bandNumber + 7).setProgress(mSoundEffectConfig.getEnvironmentReverbConfig().getReflectionsLevel() - 9000);
                            seekBars.get(bandNumber + 8).setProgress(mSoundEffectConfig.getEnvironmentReverbConfig().getReverbDelay() + 100);
                            seekBars.get(bandNumber + 9).setProgress(mSoundEffectConfig.getEnvironmentReverbConfig().getReverbLevel() + 9000);
                            seekBars.get(bandNumber + 10).setProgress(mSoundEffectConfig.getEnvironmentReverbConfig().getRoomLevel() * 1000 + 9000);
                            seekBars.get(bandNumber + 11).setProgress(mSoundEffectConfig.getEnvironmentReverbConfig().getRoomHFLevel() * 1000 + 9000);
                            LogTool.log("SoundEffectActivity", "检查decaytime去除" + mSoundEffectConfig.getEnvironmentReverbConfig().getDecayTime());
                            LogTool.log("SoundEffectActivity", "test" + mSoundEffectConfig.getEnvironmentReverbConfig().getDecayHFTime());
                            LogTool.log("SoundEffectActivity", "test" + mSoundEffectConfig.getEnvironmentReverbConfig().getDensity());
                            LogTool.log("SoundEffectActivity", "test" + mSoundEffectConfig.getEnvironmentReverbConfig().getDiffusion());
                            LogTool.log("SoundEffectActivity", "test" + mSoundEffectConfig.getEnvironmentReverbConfig().getReflectionsDelay());
                            LogTool.log("SoundEffectActivity", "test" + mSoundEffectConfig.getEnvironmentReverbConfig().getReflectionsLevel());
                            LogTool.log("SoundEffectActivity", "test" + mSoundEffectConfig.getEnvironmentReverbConfig().getReverbDelay());
                            LogTool.log("SoundEffectActivity", "test" + mSoundEffectConfig.getEnvironmentReverbConfig().getReverbLevel());
                            LogTool.log("SoundEffectActivity", "test" + mSoundEffectConfig.getEnvironmentReverbConfig().getRoomLevel());
                            LogTool.log("SoundEffectActivity", "test" + mSoundEffectConfig.getEnvironmentReverbConfig().getRoomHFLevel());
                        }

                    }
                });
            }

        });


//        closeButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                SoundEffectActivity.this.finish();
//            }
//        });

    }

    public void saveEffect(View view) {
        AppDatabase.getInstance(this).soundEffectConfigDao().update(mSoundEffectConfig);
        Snackbar.make(findViewById(R.id.save_sound_effects_button), StringUtil.getString(this, R.string.saved_sound_effects), Snackbar.LENGTH_SHORT).show();

    }


    private void initEqualizer() {
        seekBars = new ArrayList<>();
        final Equalizer equalizer = mOnePlayer.getEqualizer();

        short[] bandLevelRange = equalizer.getBandLevelRange();
        final int minEQLevel = bandLevelRange[0];
        maxEQLevel = bandLevelRange[1];
        LogTool.log("TestActivity", "最小值为" + minEQLevel + "最大值为" + maxEQLevel);
        bandNumber = (short) mSoundEffectConfig.getBandLevels().size();
        //LogTool.log("TestActivity", "band数量" + bandNumber);
        for (short i = 0; i < bandNumber; i++) {
            View view = LayoutInflater.from(this).inflate(R.layout.item_equalizer, null, false);
            final TextView frequencyTextView = findViewById(R.id.frequency_text);
            frequencyTextView.setText(StringUtil.getString(this, R.string.frequency, equalizer.getCenterFreq(i) / 1000));
            SeekBar seekbar = findViewById(R.id.decibel_seekbar);
            seekbar.setMax((maxEQLevel - minEQLevel) / 100);
            final TextView decibelTextView = findViewById(R.id.decibel_text);
            final short finalI = i;
            seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    short value = (short) (progress + minEQLevel / 100);
                    LogTool.log("TestActivity", "band" + finalI + "bandLevel" + value);
                    LogTool.log("TestActivity", "均衡器是否受控" + equalizer.hasControl());
                    //equalizer.setEnabled(false);
                    equalizer.setBandLevel(finalI, (short) (value * 100));
                    //equalizer.setEnabled(true);
                    decibelTextView.setText(StringUtil.getString(SoundEffectActivity.this, R.string.decibel, value));
                    ArrayList<Integer> bandLevels = mSoundEffectConfig.getBandLevels();
                    bandLevels.set(finalI, (int) value);
                    mSoundEffectConfig.setBandLevels(bandLevels);
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
            //LogTool.log("TestActivity","initEqualizer()初始化值"+mConfig.toString());
            //LogTool.log("TestActivity","initEqualizer()初始化值"+mConfig.getBandLevels().get(i)+maxEQLevel);
            seekbar.setProgress(mSoundEffectConfig.getBandLevels().get(i) + maxEQLevel / 100);
            seekBars.add(seekbar);
            binding.equailizerField.addView(view);
        }
    }

    private void initBassBoost() {
        final BassBoost bassBoost = mOnePlayer.getBassBoost();
        SeekBar seekbar = findViewById(R.id.bassboost_power_seekbar);
        seekbar.setMax(1000);
        final TextView textView = findViewById(R.id.bassboost_power_text);
        textView.setText(StringUtil.getString(SoundEffectActivity.this, R.string.value, 0));
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                bassBoost.setStrength((short) progress);
                textView.setText(StringUtil.getString(SoundEffectActivity.this, R.string.value, progress));
                mSoundEffectConfig.setBassBoostStrength(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        seekbar.setProgress(mSoundEffectConfig.getBassBoostStrenth());
        seekBars.add(seekbar);
    }

    private void initPresetReverb() {
        final PresetReverb presetreverb = mOnePlayer.getPresetReverb();
        final ArrayList<String> presetStrings = new ArrayList<>();
        presetStrings.add("无");
        presetStrings.add("小房间");
        presetStrings.add("中房间");
        presetStrings.add("大房间");
        presetStrings.add("中礼堂");
        presetStrings.add("大礼堂");
        presetStrings.add("板式混响");
        Spinner spinner = findViewById(R.id.presetreverb_spinner);
        spinner.setAdapter(new ArrayAdapter<>(this, R.layout.item_spinner, presetStrings));
        spinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                presetreverb.setPreset((short) position);
                mSoundEffectConfig.setPresetReverb(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner.setSelection(mSoundEffectConfig.getPresetReverb());
    }

    private void initVirtualizer() {
        final Virtualizer virtualizer = mOnePlayer.getVirtualizer();
        SeekBar seekbar = findViewById(R.id.virtualizer_power_seekbar);
        seekbar.setMax(1000);
        final TextView textView = findViewById(R.id.virtualizer_power_text);
        textView.setText(StringUtil.getString(this, R.string.value, 0));
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                virtualizer.setStrength((short) progress);
                textView.setText(StringUtil.getString(SoundEffectActivity.this, R.string.value, progress));
                mSoundEffectConfig.setVirtualizerStrength(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        seekbar.setProgress(mSoundEffectConfig.getVirtualizerStrength());
        seekBars.add(seekbar);
    }

    private void initEnvironmentalReverb() {
        final EnvironmentalReverb environmentalReverb = mOnePlayer.getEnvironmentalReverb();
        initDecayTime(environmentalReverb);
        initDecayHFTime(environmentalReverb);
        initDensity(environmentalReverb);
        initDiffusion(environmentalReverb);
        initReflectionDelay(environmentalReverb);
        initReflectionsLevel(environmentalReverb);
        initReverbDelay(environmentalReverb);
        initReverbLevel(environmentalReverb);
        initRoomLevel(environmentalReverb);
        initRoomHFLevel(environmentalReverb);
    }

    private void initReverbDelay(final EnvironmentalReverb environmentalReverb) {
        SeekBar seekbar = findViewById(R.id.reverbDelay_seekbar);
        seekbar.setMax(100);
        final TextView textView = findViewById(R.id.reverbDelay_value_text);
        textView.setText(StringUtil.getString(this, R.string.timeMeasurement, 0));
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                environmentalReverb.setReverbDelay(progress);
                textView.setText(StringUtil.getString(SoundEffectActivity.this, R.string.timeMeasurement, progress));
                mSoundEffectConfig.getEnvironmentReverbConfig().setReverbDelay(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        seekbar.setProgress(mSoundEffectConfig.getEnvironmentReverbConfig().getReverbDelay() + 100);
        seekBars.add(seekbar);
    }

    private void initRoomHFLevel(final EnvironmentalReverb environmentalReverb) {
        SeekBar seekbar = findViewById(R.id.roomHFLevel_seekbar);
        seekbar.setMax(9);
        final TextView textView = findViewById(R.id.roomHFLevel_value_text);
        textView.setText(StringUtil.getString(this, R.string.value, 0));
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int value = progress - 9;
                environmentalReverb.setRoomHFLevel((short) (value * 1000));
                textView.setText(StringUtil.getString(SoundEffectActivity.this, R.string.decibel, value));
                mSoundEffectConfig.getEnvironmentReverbConfig().setRoomHFLevel((short) value);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        seekbar.setProgress(mSoundEffectConfig.getEnvironmentReverbConfig().getRoomHFLevel() * 1000 + 9000);
        seekBars.add(seekbar);
    }

    private void initRoomLevel(final EnvironmentalReverb environmentalReverb) {
        SeekBar seekbar = findViewById(R.id.roomLevel_seekbar);
        seekbar.setMax(9);
        final TextView textView = findViewById(R.id.roomLevel_value_text);
        textView.setText(StringUtil.getString(this, R.string.decibel, 0));
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int value = progress - 9;
                environmentalReverb.setRoomLevel((short) value);
                textView.setText(StringUtil.getString(SoundEffectActivity.this, R.string.decibel, value));
                mSoundEffectConfig.getEnvironmentReverbConfig().setRoomLevel((short) (value * 1000));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        seekbar.setProgress(mSoundEffectConfig.getEnvironmentReverbConfig().getRoomLevel() * 1000 + 9000);
        seekBars.add(seekbar);
    }

    private void initReverbLevel(final EnvironmentalReverb environmentalReverb) {
        SeekBar seekbar = findViewById(R.id.reverbLevel_seekbar);
        seekbar.setMax(10);
        final TextView textView = findViewById(R.id.reverbLevel_value_text);
        textView.setText(StringUtil.getString(this, R.string.decibel, 0));
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int value = progress - 9;
                environmentalReverb.setReverbLevel((short) value);
                textView.setText(StringUtil.getString(SoundEffectActivity.this, R.string.decibel, value));
                mSoundEffectConfig.getEnvironmentReverbConfig().setReverbLevel((short) (value * 1000));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        seekbar.setProgress(mSoundEffectConfig.getEnvironmentReverbConfig().getReverbLevel() + 9000);
        seekBars.add(seekbar);
    }

    private void initReflectionsLevel(final EnvironmentalReverb environmentalReverb) {
        SeekBar seekbar = findViewById(R.id.reflectionLevel_seekbar);
        seekbar.setMax(10);
        final TextView textView = findViewById(R.id.reflectionLevel_value_text);
        textView.setText(StringUtil.getString(this, R.string.value, 0));
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int value = progress - 9;
                LogTool.log("TestActivity", "查看反射等级" + (short) value);
                environmentalReverb.setReflectionsLevel((short) value);
                textView.setText(StringUtil.getString(SoundEffectActivity.this, R.string.decibel, value));
                mSoundEffectConfig.getEnvironmentReverbConfig().setReflectionsLevel((short) (value * 1000));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        seekbar.setProgress(mSoundEffectConfig.getEnvironmentReverbConfig().getReflectionsLevel() + 9000);
        seekBars.add(seekbar);
    }

    private void initReflectionDelay(final EnvironmentalReverb environmentalReverb) {
        SeekBar seekbar = findViewById(R.id.reflectionDelay_seekbar);
        seekbar.setMax(300);
        final TextView textView = findViewById(R.id.reflectionDelay_value_text);
        textView.setText(StringUtil.getString(this, R.string.timeMeasurement, 0));
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                environmentalReverb.setReflectionsDelay(progress);
                textView.setText(StringUtil.getString(SoundEffectActivity.this, R.string.timeMeasurement, progress));
                mSoundEffectConfig.getEnvironmentReverbConfig().setReflectionsDelay(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        seekbar.setProgress(mSoundEffectConfig.getEnvironmentReverbConfig().getReflectionsDelay());
        seekBars.add(seekbar);
    }

    private void initDiffusion(final EnvironmentalReverb environmentalReverb) {
        SeekBar seekbar = findViewById(R.id.diffusion_seekbar);
        seekbar.setMax(1000);
        final TextView textView = findViewById(R.id.diffusion_value_text);
        textView.setText(StringUtil.getString(this, R.string.value, 0));
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                environmentalReverb.setDiffusion((short) progress);
                textView.setText(StringUtil.getString(SoundEffectActivity.this, R.string.value, progress));
                mSoundEffectConfig.getEnvironmentReverbConfig().setDiffusion((short) progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        seekbar.setProgress(mSoundEffectConfig.getEnvironmentReverbConfig().getDiffusion());
        seekBars.add(seekbar);
    }

    private void initDensity(final EnvironmentalReverb environmentalReverb) {
        SeekBar seekbar = findViewById(R.id.density_seekbar);
        seekbar.setMax(1000);
        final TextView textView = findViewById(R.id.density_value_text);
        textView.setText(StringUtil.getString(this, R.string.value, 0));
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                LogTool.log("TestActivity", "查看欲设" + (short) progress);
                environmentalReverb.setDensity((short) progress);
                textView.setText(StringUtil.getString(SoundEffectActivity.this, R.string.value, progress));
                mSoundEffectConfig.getEnvironmentReverbConfig().setDensity((short) progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        seekbar.setProgress(mSoundEffectConfig.getEnvironmentReverbConfig().getDensity());
        seekBars.add(seekbar);
    }

    private void initDecayHFTime(final EnvironmentalReverb environmentalReverb) {
        SeekBar seekbar = findViewById(R.id.decayHF_seekbar);
        seekbar.setMax(19);
        final TextView textView = findViewById(R.id.decayHF_value_text);
        textView.setText(StringUtil.getString(this, R.string.timeMeasurement, 0));
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int value = progress * 100 + 100;
                environmentalReverb.setDecayHFRatio((short) value);
                textView.setText(StringUtil.getString(SoundEffectActivity.this, R.string.timeMeasurement, value));
                mSoundEffectConfig.getEnvironmentReverbConfig().setDecayHFTime(value);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        seekbar.setProgress(mSoundEffectConfig.getEnvironmentReverbConfig().getDecayHFTime() - 100);
        seekBars.add(seekbar);
    }

    private void initDecayTime(final EnvironmentalReverb environmentalReverb) {
        SeekBar seekbar = findViewById(R.id.decay_seekbar);
        seekbar.setMax(199);
        final TextView textView = findViewById(R.id.decay_value_text);
        textView.setText(StringUtil.getString(this, R.string.timeMeasurement, 0));
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int value = progress * 100 + 100;
                environmentalReverb.setDecayTime((short) value);
                textView.setText(StringUtil.getString(SoundEffectActivity.this, R.string.timeMeasurement, value));
                mSoundEffectConfig.getEnvironmentReverbConfig().setDecayTime(value);
                LogTool.log("TestActivity", "检查decaytime" + value);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        seekbar.setProgress(mSoundEffectConfig.getEnvironmentReverbConfig().getDecayTime() - 100);
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