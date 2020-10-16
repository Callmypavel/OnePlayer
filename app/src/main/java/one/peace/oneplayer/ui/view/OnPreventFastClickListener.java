package one.peace.oneplayer.ui.view;

import android.view.View;


/**
 * Created by pavel on 2020/3/23.
 */
public abstract class OnPreventFastClickListener implements View.OnClickListener {
    private final int MIN_INTERVAL_TIME = 1000;
    private long lastClickTime = 0;

    public abstract void onPreventedFastClick(View view);

    @Override
    public void onClick(View v) {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastClickTime > MIN_INTERVAL_TIME) {
            lastClickTime = currentTime;
            onPreventedFastClick(v);
        }
    }
}
