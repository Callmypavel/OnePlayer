package one.peace.oneplayer.ui.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import one.peace.oneplayer.util.ExecutorServiceUtil;
import one.peace.oneplayer.util.LogTool;

/**
 * Created by pavel on 2019/11/19.
 */
public class StretchLayout extends RelativeLayout {
    private int minHeight;
    private int maxHeight;
    private int currentHeight;
    private int childcurrentHeight;
    private int stretchSpeed;
    private int frameRate = 60;
    private int totalTime = 1000;
    private int timeInterval;
    private Handler handler;
    private View childView;
    private OnReachListener mOnReachListener;
    private final static int STRETCH_ALL = 1;
    private final static int STRETCH_CHILD = 2;
    private final static int STRETCH_TO_MAX = 3;
    private final static int STRETCH_TO_MIN = 4;
    public StretchLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    public StretchLayout(Context context) {
        super(context);
        init();
    }
    public StretchLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public interface OnReachListener {
        void onReachMax();
        void onReachMin();
    }

    public void setOnReachMaxListener(OnReachListener mOnReachListener){
        this.mOnReachListener = mOnReachListener;
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if(minHeight == 0) {
            minHeight = getHeight();
            currentHeight = minHeight;
            childcurrentHeight = minHeight;
        }
    }

    private void init(){
        WindowManager wm = (WindowManager) getContext()
                .getSystemService(Context.WINDOW_SERVICE);
        maxHeight = wm.getDefaultDisplay().getHeight();

        timeInterval = (int)(1000.f/frameRate);
        LogTool.log(this,"时间单位"+timeInterval);
        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if(msg.what==STRETCH_ALL||msg.what==STRETCH_CHILD){
                    changeHeight();
                }
                if(msg.what==STRETCH_CHILD){
                    changeChildHeight();
                }
                if(msg.what== STRETCH_TO_MAX){
                    mOnReachListener.onReachMax();
                }
                if(msg.what== STRETCH_TO_MIN){
                    mOnReachListener.onReachMin();
                }
                return false;
            }
        });

        post(new Runnable() {
            @Override
            public void run() {
                LogTool.log(this,"看看高度是否正确:"+getHeight());
                stretchSpeed = (int)((maxHeight - minHeight)*timeInterval*1.f/totalTime);
                LogTool.log(this,"看看伸缩速度:"+ stretchSpeed);
                LogTool.log(this,"看看子View:"+getChildAt(0));
                childView = getChildAt(0);
            }
        });
    }
    public void toMaxHeight(){
        ExecutorServiceUtil.submit(new Runnable() {
            @Override
            public void run() {
                Message message = Message.obtain();
                while(currentHeight<maxHeight) {
                    currentHeight += stretchSpeed;
                    message.what = STRETCH_ALL;
                    if(currentHeight>=maxHeight-minHeight){
                        if(childcurrentHeight>=0) {
                            childcurrentHeight -= stretchSpeed;
                            if(childcurrentHeight<0){
                                childcurrentHeight=0;
                            }
                            message.what = STRETCH_CHILD;
                        }
                    }
                    handler.sendMessage(message);
                    try {
                        Thread.sleep(timeInterval);
                    } catch (Exception e) {

                    }
                }
                message.what = STRETCH_TO_MAX;
                handler.sendMessage(message);
                currentHeight = maxHeight;
            }
        });

    }
    public void toMinHeight(){
        ExecutorServiceUtil.submit(new Runnable() {
            @Override
            public void run() {
                Message message = new Message();
                while(currentHeight>minHeight) {
                    currentHeight -= stretchSpeed;
                    message.what = STRETCH_ALL;
                    if(currentHeight<=maxHeight-minHeight){
                        if(childcurrentHeight<=minHeight) {
                            childcurrentHeight += stretchSpeed;
                            if(childcurrentHeight>minHeight){
                                childcurrentHeight=minHeight;
                            }
                            message.what = STRETCH_CHILD;
                        }
                    }
                    handler.sendMessage(message);
                    try {
                        Thread.sleep(timeInterval);
                    } catch (Exception e) {

                    }
                }
                currentHeight = minHeight;
                message.what = STRETCH_TO_MIN;
                handler.sendMessage(message);
            }
        });
    }
    private void changeHeight(){
        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        layoutParams.height = currentHeight;
        setLayoutParams(layoutParams);
        invalidate();
    }
    private void changeChildHeight(){
        if(childView!=null) {
            ViewGroup.LayoutParams layoutParams = childView.getLayoutParams();
            layoutParams.height = childcurrentHeight;
            childView.setLayoutParams(layoutParams);
            invalidate();
        }
    }
}