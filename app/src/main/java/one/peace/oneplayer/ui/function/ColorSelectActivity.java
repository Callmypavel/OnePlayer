package one.peace.oneplayer.ui.function;

import android.graphics.Color;
import android.view.View;
import android.widget.SeekBar;

import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.ViewModel;

import one.peace.oneplayer.R;
import one.peace.oneplayer.database.AppDatabase;
import one.peace.oneplayer.databinding.ActivityColorSelectBinding;
import one.peace.oneplayer.global.config.Config;
import one.peace.oneplayer.ui.base.BaseActivity;
import one.peace.oneplayer.ui.view.OnPreventFastClickListener;
import one.peace.oneplayer.util.ColorUtil;
import one.peace.oneplayer.util.ExecutorServiceUtil;
import one.peace.oneplayer.util.StringUtil;
import one.peace.oneplayer.util.ViewTool;

public class ColorSelectActivity extends BaseActivity<ColorSelectActivity.ColorSelectViewModel> {

    public static class ColorSelectViewModel extends ViewModel {
        private int redValue;
        private int greenValue;
        private int blueValue;
        private int alphaValue;
        private boolean isWhite = true;

        public int getThemeColor(){
            return Color.argb(alphaValue,redValue,greenValue,blueValue);
        }

        public int getRedValue() {
            return redValue;
        }

        public void setRedValue(int redValue) {
            this.redValue = redValue;
        }

        public int getGreenValue() {
            return greenValue;
        }

        public void setGreenValue(int greenValue) {
            this.greenValue = greenValue;
        }

        public int getBlueValue() {
            return blueValue;
        }

        public void setBlueValue(int blueValue) {
            this.blueValue = blueValue;
        }

        public int getAlphaValue() {
            return alphaValue;
        }

        public void setAlphaValue(int alphaValue) {
            this.alphaValue = alphaValue;
        }

        public void setWhite(boolean white) {
            isWhite = white;
        }
    }

    @Override
    protected Class getViewModelClass() {
        return ColorSelectViewModel.class;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_color_select;
    }

    @Override
    protected void onInitData(ColorSelectViewModel viewModel, ViewDataBinding viewDataBinding) {

    }

    @Override
    protected void onInitDataAfterConfigLoaded(final ColorSelectViewModel viewModel, final ViewDataBinding viewDataBinding, final Config config) {
        if (config != null){
            int themeColor = config.getThemeColor();
            viewModel.setRedValue(Color.red(themeColor));
            viewModel.setGreenValue(Color.green(themeColor));
            viewModel.setBlueValue(Color.blue(themeColor));
            viewModel.setAlphaValue(Color.alpha(themeColor));
            if (viewDataBinding instanceof ActivityColorSelectBinding) {
                final ActivityColorSelectBinding activityColorSelectBinding = (ActivityColorSelectBinding) viewDataBinding;
                activityColorSelectBinding.setConfig(config);
                activityColorSelectBinding.redColorSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        viewModel.setRedValue(progress);
                        updateColor(viewModel,activityColorSelectBinding);
                        activityColorSelectBinding.redTint.setText(StringUtil.getString(ColorSelectActivity.this,R.string.color_red,progress));
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });
                activityColorSelectBinding.greenColorSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        viewModel.setGreenValue(progress);
                        updateColor(viewModel,activityColorSelectBinding);
                        activityColorSelectBinding.greenTint.setText(StringUtil.getString(ColorSelectActivity.this,R.string.color_green,progress));
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });
                activityColorSelectBinding.blueColorSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        viewModel.setBlueValue(progress);
                        updateColor(viewModel,activityColorSelectBinding);
                        activityColorSelectBinding.blueTint.setText(StringUtil.getString(ColorSelectActivity.this,R.string.color_blue,progress));
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });
                activityColorSelectBinding.alphaColorSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        viewModel.setAlphaValue(progress);
                        updateColor(viewModel,activityColorSelectBinding);
                        activityColorSelectBinding.alphaTint.setText(StringUtil.getString(ColorSelectActivity.this,R.string.color_alpha,progress));
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });
                activityColorSelectBinding.redColorSeekbar.setProgress(viewModel.redValue);
                activityColorSelectBinding.redTint.setText(StringUtil.getString(ColorSelectActivity.this,R.string.color_red,viewModel.redValue));
                activityColorSelectBinding.greenColorSeekbar.setProgress(viewModel.greenValue);
                activityColorSelectBinding.greenTint.setText(StringUtil.getString(ColorSelectActivity.this,R.string.color_green,viewModel.greenValue));
                activityColorSelectBinding.blueColorSeekbar.setProgress(viewModel.blueValue);
                activityColorSelectBinding.blueTint.setText(StringUtil.getString(ColorSelectActivity.this,R.string.color_blue,viewModel.blueValue));
                activityColorSelectBinding.alphaColorSeekbar.setProgress(viewModel.alphaValue);
                activityColorSelectBinding.alphaTint.setText(StringUtil.getString(ColorSelectActivity.this,R.string.color_alpha,viewModel.alphaValue));
                activityColorSelectBinding.applyButton.setOnClickListener(new OnPreventFastClickListener() {
                    @Override
                    public void onPreventedFastClick(View view) {
                        //更新配置
                        config.setRedValue(viewModel.redValue);
                        config.setGreenValue(viewModel.greenValue);
                        config.setBlueValue(viewModel.blueValue);
                        config.setAlphaValue(viewModel.alphaValue);
                        ExecutorServiceUtil.submit(new Runnable() {
                            @Override
                            public void run() {
                                AppDatabase.getInstance(ColorSelectActivity.this).configDao().update(config);
                            }
                        });
                    }
                });
            }
        }

    }

    private void updateColor(ColorSelectViewModel colorSelectViewModel,ActivityColorSelectBinding activityColorSelectBinding){
        int themeColor = colorSelectViewModel.getThemeColor();
        ViewTool.setStatusColor(ColorSelectActivity.this,themeColor);
        activityColorSelectBinding.previewTextview.setBackgroundColor(themeColor);
        activityColorSelectBinding.toolbarTextview.setBackgroundColor(themeColor);
        activityColorSelectBinding.applyButton.setTextColor(themeColor);
        if(!ColorUtil.getContrast(themeColor, colorSelectViewModel.isWhite?Color.WHITE:Color.BLACK)){
            //对比度不够，需要切换黑白色
            colorSelectViewModel.setWhite(!colorSelectViewModel.isWhite);
        }
        activityColorSelectBinding.toolbarTextview.setTextColor(colorSelectViewModel.isWhite?Color.WHITE:Color.BLACK);
        activityColorSelectBinding.previewTextview.setTextColor(colorSelectViewModel.isWhite?Color.WHITE:Color.BLACK);
    }
}
