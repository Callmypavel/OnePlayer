package com.example.peacemaker.oneplayer;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * Created by ouyan on 2016/7/22.
 */

public class KitKatTool {
    public static void setCollapsingToolbarStatus(AppBarLayout appBarLayout,final View mStatusBarView) {
        ViewCompat.setAlpha(mStatusBarView, 1);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                int maxScroll = appBarLayout.getTotalScrollRange();
                float percentage = (float) Math.abs(verticalOffset) / (float) maxScroll;
                ViewCompat.setAlpha(mStatusBarView, percentage);
            }
        });
    }

    public static void setKKStatusBar(Activity activity) {
        ViewGroup contentView = (ViewGroup) activity.findViewById(android.R.id.content);
        View mStatusBarView = contentView.getChildAt(0);
        //改变颜色时避免重复添加statusBarView
        if (mStatusBarView != null && mStatusBarView.getMeasuredHeight() == getStatusBarHeight(activity)) {
            mStatusBarView.setBackgroundColor(ContextCompat.getColor(activity,R.color.colorPrimary));
            return;
        }
        mStatusBarView = new View(activity);
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                getStatusBarHeight(activity));
        mStatusBarView.setBackgroundColor(ContextCompat.getColor(activity, R.color.colorPrimary));
        contentView.addView(mStatusBarView, lp);
    }

    public static int getStatusBarHeight(Context context) {
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        return context.getResources().getDimensionPixelSize(resourceId);
    }
    public static void setCollapsingToolbar(Activity activity, CoordinatorLayout coordinatorLayout,
                                            AppBarLayout appBarLayout, ImageView imageView, Toolbar toolbar) {
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
            coordinatorLayout.setFitsSystemWindows(false);
            appBarLayout.setFitsSystemWindows(false);
            imageView.setFitsSystemWindows(false);
            toolbar.setFitsSystemWindows(true);
            CollapsingToolbarLayout.LayoutParams lp = (CollapsingToolbarLayout.LayoutParams) toolbar.getLayoutParams();
            lp.height = (int) (getStatusBarHeight(activity) +
                    activity.getResources().getDimension(R.dimen.abc_action_bar_default_height_material));
            toolbar.setLayoutParams(lp);
            setKKStatusBar(activity);
            setCollapsingToolbarStatus(appBarLayout,((ViewGroup)activity.findViewById(android.R.id.content)).getChildAt(0));
        }
    }
}
