package peacemaker.oneplayer.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;


import peacemaker.oneplayer.activity.OneApplication;
import peacemaker.oneplayer.tool.LogTool;

/**
 * Created by ouyan on 2018/1/23.
 */

public class OneListView extends RecyclerView {
    protected int page = 1;
    protected Boolean isLoading = false;
    protected Boolean isEnd = false;
    protected int lastVisiablePosition;
    protected int pageSize = 5;
    protected int lastState = RecyclerView.SCROLL_STATE_IDLE;
    protected OnLoadDataListener onLoadDataListener;
    //private PowerfulSwipeRefreshLayout powerfulSwipeRefreshLayout;
    public interface OnLoadDataListener{
        void OnLoadData(int page, int pageSize, Boolean isRefresh);
        void OnPermissionDenied();
    }

    public void setOnLoadDataListener(OnLoadDataListener onLoadDataListener) {
        this.onLoadDataListener = onLoadDataListener;
        refresh();
    }

//    public void setPowerfulSwipeRefreshLayout(PowerfulSwipeRefreshLayout powerfulSwipeRefreshLayout){
//        this.powerfulSwipeRefreshLayout = powerfulSwipeRefreshLayout;
//    }

    public void refresh(){
        if(onLoadDataListener!=null){
            page = 1;
            isLoading = true;
            isEnd = false;
            if(OneApplication.isStorePermissionGranted) {
                onLoadDataListener.OnLoadData(page, pageSize, true);
            }else {
                onLoadDataListener.OnPermissionDenied();
            }
        }
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public OneListView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setLayoutManager(new LinearLayoutManager(context));
        addOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                Adapter adapter = getAdapter();

                //LogTool.log(this,"纪检委树新风"+canScrollVertically(1));
                int itemcount = adapter.getItemCount();
                //LogTool.log(this,"纪检委抓作风"+lastVisiablePosition+","+itemcount);
                post(new Runnable() {
                    @Override
                    public void run() {

                    }
                });
                if ((lastState== RecyclerView.SCROLL_STATE_SETTLING||lastState== RecyclerView.SCROLL_STATE_DRAGGING)&&newState == RecyclerView.SCROLL_STATE_IDLE && lastVisiablePosition + 1 == itemcount&& !isLoading && !isEnd) {
                    if(onLoadDataListener!=null){
//                        if (powerfulSwipeRefreshLayout!=null){
//                            powerfulSwipeRefreshLayout.setHitBottom(true);
//                        }
                        LogTool.log(this,"到底了");

                        isLoading = true;
                        page++;
                        if(OneApplication.isStorePermissionGranted) {
                            onLoadDataListener.OnLoadData(page, pageSize, false);
                        }else {
                            onLoadDataListener.OnPermissionDenied();
                        }
                    }
                }else {
//                    if (powerfulSwipeRefreshLayout!=null){
//                        powerfulSwipeRefreshLayout.setHitBottom(false);
//                    }
                }
                lastState = newState;
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //LogTool.log(this,"看看dy"+dy);
                lastVisiablePosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastVisibleItemPosition();

            }
        });
    }
    public void onResultBack(Boolean isEnd){
        isLoading = false;
        this.isEnd = isEnd;

    }
}
