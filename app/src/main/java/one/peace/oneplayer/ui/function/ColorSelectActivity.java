package one.peace.oneplayer.ui.function;

import android.graphics.Color;
import android.view.View;
import android.widget.SeekBar;

import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.ViewModel;

import one.peace.oneplayer.R;
import one.peace.oneplayer.database.AppDatabase;
import one.peace.oneplayer.database.dao.ConfigDAO;
import one.peace.oneplayer.databinding.ActivityColorSelectBinding;
import one.peace.oneplayer.global.config.Config;
import one.peace.oneplayer.ui.base.BaseActivity;
import one.peace.oneplayer.ui.view.OnPreventFastClickListener;
import one.peace.oneplayer.util.ViewTool;

public class ColorSelectActivity extends BaseActivity<ColorSelectActivity.ColorSelectViewModel> {

    public static class ColorSelectViewModel extends ViewModel {
        private int redValue;
        private int greenValue;
        private int blueValue;
        private int alphaValue;

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
                ActivityColorSelectBinding activityColorSelectBinding = (ActivityColorSelectBinding) viewDataBinding;
                activityColorSelectBinding.setConfig(config);
                activityColorSelectBinding.redColorSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        viewModel.setRedValue(progress);
                        int themeColor = viewModel.getThemeColor();
                        ViewTool.setStatusColor(ColorSelectActivity.this,themeColor);
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
                        int themeColor = viewModel.getThemeColor();
                        ViewTool.setStatusColor(ColorSelectActivity.this,themeColor);
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
                        int themeColor = viewModel.getThemeColor();
                        ViewTool.setStatusColor(ColorSelectActivity.this,themeColor);
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
                        int themeColor = viewModel.getThemeColor();
                        ViewTool.setStatusColor(ColorSelectActivity.this,themeColor);
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });
                activityColorSelectBinding.redColorSeekbar.setProgress(viewModel.redValue);
                activityColorSelectBinding.greenColorSeekbar.setProgress(viewModel.greenValue);
                activityColorSelectBinding.blueColorSeekbar.setProgress(viewModel.blueValue);
                activityColorSelectBinding.alphaColorSeekbar.setProgress(viewModel.alphaValue);
                activityColorSelectBinding.applyButton.setOnClickListener(new OnPreventFastClickListener() {
                    @Override
                    public void onPreventedFastClick(View view) {
                        //更新配置
                        config.setRedValue(viewModel.redValue);
                        config.setGreenValue(viewModel.greenValue);
                        config.setBlueValue(viewModel.blueValue);
                        config.setAlphaValue(viewModel.alphaValue);
                        AppDatabase.getInstance(ColorSelectActivity.this).configDao().update(config);
                    }
                });
            }
        }

    }
}
