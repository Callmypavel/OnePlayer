package com.example.peacemaker.oneplayer;

import android.content.Context;
import android.databinding.tool.util.L;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.AudioManager;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RunnableFuture;
import java.util.logging.LogRecord;

/**
 * Created by ouyan on 2016/9/17.
 */

public class IndexView extends View implements View.OnTouchListener,GestureDetector.OnGestureListener{
    private int width;
    private float offsetY;
    private int unitHeight;
    private int unitWidth;
    private int indexY = 0;
    private Paint paint = new Paint();
    private Paint highlightPaint = new Paint();
    private ArrayList<IndexedMusic> indexedMusics;
    private GestureDetector gestureDetector;
    private RecyclerView recyclerView;
    private float distance;
    private float fixedspeed;
    private int max;
    private int childHeight;
    private int[] positionIndex;
    private int previousReference;
    private int nextReference;
    private boolean isAutoScolling = false;
    private boolean isScrollBy;
    private boolean isNull = false;

    public int getHighlightColor() {
        return highlightColor;
    }

    public void setHighlightColor(int highlightColor) {
        this.highlightColor = highlightColor;
        highlightPaint.setColor(highlightColor);
        invalidate();
    }

    private int highlightColor = Color.BLUE;
    public IndexView(Context context) {
        super(context);
    }
    public IndexView(Context context, AttributeSet attrs){
        super(context, attrs, 0);
        DisplayMetrics dm =getResources().getDisplayMetrics();
        width = dm.widthPixels;
        unitWidth = width/10;
        unitHeight = width/20;
        fixedspeed = unitHeight/2;
        offsetY = unitHeight/2+150;
        paint.setTextSize(40);
        paint.setStrokeWidth(10);
        highlightPaint.setColor(highlightColor);
        highlightPaint.setTextSize(40);
        highlightPaint.setStrokeWidth(10);
        gestureDetector = new GestureDetector(context,this);
        setOnTouchListener(this);

    }

    public IndexView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    public void setIndexedMusics(ArrayList<IndexedMusic> indexedMusics){
        this.indexedMusics = indexedMusics;
        if(indexedMusics.size()==0){
            isNull = true;
        }
        max = indexedMusics.size()-1;
        int position = 0;
        positionIndex = new int[max+1];
        for(int i=0;i<indexedMusics.size();i++){
            positionIndex[i] = position;
            //positionIndex.add(position);
            position+=indexedMusics.get(i).getMusics().size();
            //Log.v("IndexView","添加位置点"+position);

        }
        nextReference = 1;
        previousReference = 0;
        //Log.v("IndexView","初始化下个参考点"+nextReference);
        invalidate();
    }

    public void setRecyclerView(RecyclerView recyclerView, final boolean isScrollBy) {
        this.recyclerView = recyclerView;
        this.isScrollBy = isScrollBy;
        //Log.v("IndexView","把isScrollBy设为"+isScrollBy+this.isScrollBy);
        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(!isAutoScolling){
                int position = ((LinearLayoutManager)recyclerView.getLayoutManager()).findLastVisibleItemPosition();
                //Log.v("IndexView","最后可见位置"+position);
                if(position>=positionIndex[nextReference]){
                    indexY+=1;
                    if(indexY>max){
                        indexY = max;
                    }
                    //Log.v("IndexView","更新下个indexY"+indexY);
                    //Log.v("IndexView","更新下个参考点"+nextReference);
                }else if(position<positionIndex[previousReference]){
                    indexY-=1;
                    if(indexY<0){
                        indexY=0;
                    }
                }
                if(indexY<0){
                    indexY=0;
                }
                if(isScrollBy){
                    if(position<=3){
                        indexY=0;
                    }
                }else {
                    if(position<=6){
                        indexY=0;
                    }
                }
                previousReference = indexY;
                nextReference = indexY+1;
                if(nextReference>max){
                    nextReference = max;
                }
                //findIndex(position);

                   // Log.v("IndexView","滚动距离"+distance);
                   // Log.v("IndexView","indexY"+indexY);
                    autoScoll();
                }
                invalidate();
            }
        });
        int initposition = ((LinearLayoutManager)recyclerView.getLayoutManager()).findLastVisibleItemPosition();
        if(initposition<0){
            initposition =0;

        }
        findIndex(initposition);
    }
    private void findIndex(int position){
        int insert = 0;
        if(isScrollBy){
            insert = -Arrays.binarySearch(positionIndex,position)-2;
        }else {
            insert = -Arrays.binarySearch(positionIndex,position)-3;
        }
        //Log.v("IndexView","二分搜索结果:"+insert);
        if(insert>=0) {
            indexY = insert;
        }else if(insert<0) {
            indexY = -insert;
            indexY-=2;
        }
        //Log.v("IndexView","插入点position:"+position+",index"+insert+",indexY"+indexY);
        //updateMiddle(max/2,position);
    }
    private void autoScoll(){
        if(indexY>max){
            indexY = max;
        }else if(indexY>=23){
            if(distance<(max-23)*unitHeight){
                isAutoScolling = true;
                autoFix(true);
            }
        }else if(indexY<=12){
            if(distance>0){
                isAutoScolling = true;
                autoFix(false);
            }
        }
        invalidate();
    }
//
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(unitWidth, heightMeasureSpec);

    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if(!isNull) {
            float y = motionEvent.getY();
            //Log.v("IndexView", "index:y" + y);
            //Log.v("IndexView", "index:offsetY" + offsetY);
            //Log.v("IndexView", "index:unitHeight" + unitHeight);
            indexY = (int) ((y - offsetY) / unitHeight);
            //Log.v("IndexView", "index:" + indexY);
            if (!isAutoScolling) {
                autoScoll();
            }
            //Log.v("IndexView","index:"+indexY+",value="+indexedMusics.get(indexY).getIndexName());
            if (recyclerView != null) {
                //Log.v("IndexView","Position:"+positionIndex[indexY]);
//            LinearLayoutManager linearLayoutManager = (LinearLayoutManager)recyclerView.getLayoutManager();
                if (indexY > max) {
                    indexY = max;
                }
                if (indexY < 0) {
                    indexY = 0;
                }
                int index = positionIndex[indexY];
                //LogTool.log("IndexView", "寻找的最终Index" + index);
                //LogTool.log("IndexView", "寻找的最终Index索引" + indexY);
                moveToPosition(index);
//            int firstVisiblePosition = linearLayoutManager.findFirstCompletelyVisibleItemPosition();
//            int top = linearLayoutManager.getPaddingTop();
//            Log.v("IndexView","FirstVisiblePosition:"+firstVisiblePosition+",top:"+top);
            }
            return gestureDetector.onTouchEvent(motionEvent);
        }else {
            return true;
        }
    }

//    private void updateMiddle(int index,int position){
//        Log.v("IndexView","updateMiddle:"+index+",position" +position);
//        int value = positionIndex[index];
//        int newindex;
//        if(position>value){
//            newindex = (max-index)/2;
//        }else {
//            if(position>positionIndex[index-1]){
//                Log.v("IndexView","updateMiddle获得index"+relativeindex);
//                relativeindex = index;
//                return;
//            }
//            newindex = index/2;
//        }
//        updateMiddle(newindex,position);
//    }
    private void moveToPosition(int index){
        childHeight = recyclerView.getChildAt(0).getHeight();
        //Log.v("IndexView","childHeight"+childHeight);
        LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        int top = recyclerView.getChildAt(0).getTop();
        recyclerView.scrollBy(0,top);
        int first = linearLayoutManager.findFirstCompletelyVisibleItemPosition();
        int last = linearLayoutManager.findLastVisibleItemPosition();
        //Log.v("IndexView","第一位置"+first+"末尾位置"+last+"顶部距离"+top);
        //Log.v("IndexView","isScrollBy"+isScrollBy);
        if(isScrollBy){
            isAutoScolling = true;
            recyclerView.scrollBy(0,(index-first)*childHeight);
            isAutoScolling = false;
        }else {
            isAutoScolling = true;
            //Log.v("IndexView","滚去位置"+index);
            recyclerView.scrollToPosition(index);
            isAutoScolling = false;
        }
//        if (index <= 6||(index>=first&&index<=last)) {
//            int distance = top+childHeight*(index-first);
//            recyclerView.scrollBy(0,distance);
//            Log.v("IndexView","滚动经过"+distance);
//        }else {
//            if(index!=0) {
//                index+=6;
//            }
//            linearLayoutManager.scrollToPosition(index);
//        }

    }

    private void autoFix(final boolean isDownward){
        final Handler handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message message) {
                if(message.what==111){
                    invalidate();
                }
                return false;
            }
        });
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                if(isDownward){
                    while(distance<=(max-23)*unitHeight){
                        offsetY -= fixedspeed;
                        //Log.v("IndexView","autoFix()"+offsetY);
                        Message message = new Message();
                        message.what=111;
                        handler.sendMessage(message);
                        try {
                            Thread.sleep(10);
                        }catch (Exception e){
                        }
                        distance+=fixedspeed;
                    }
                }else {
                    while(distance>=0){
                        offsetY += fixedspeed;
                        //Log.v("IndexView","autoFix()"+offsetY);
                        Message message = new Message();
                        message.what=111;
                        handler.sendMessage(message);
                        try {
                            Thread.sleep(10);
                        }catch (Exception e){
                        }
                        distance-=fixedspeed;
                    }
                }

                isAutoScolling = false;

            }
        });
        thread.start();

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //drawBackground(canvas);
        drawText(canvas);
    }
    private void drawText(Canvas canvas){
        for(int i=0;i<indexedMusics.size();i++){
            IndexedMusic indexedMusic = indexedMusics.get(i);
            if(i==indexY){
                //Log.v("IndexView","drawText()选中"+i+","+indexedMusic.getIndexName()+"为天选");
                canvas.drawText(indexedMusic.getIndexName(),unitWidth*0.5f,unitHeight*(i+0.5f)+offsetY,highlightPaint);
            }else {
                canvas.drawText(indexedMusic.getIndexName(),unitWidth*0.5f,unitHeight*(i+0.5f)+offsetY,paint);
            }

        }

    }
    private void drawBackground(Canvas canvas){
        canvas.drawRect(0,offsetY+unitHeight*indexedMusics.size(),unitWidth,offsetY,highlightPaint);
    }

    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return true;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        //indexY=-1;
        return true;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return true;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return true;
    }
}
