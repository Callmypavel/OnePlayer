package peacemaker.oneplayer.view;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by ouyan on 2018/1/8.
 */

public class GridViewDecoration extends RecyclerView.ItemDecoration {
    int padding;
    int columnNumber;

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int position = parent.getChildAdapterPosition(view);
        int column = position/columnNumber;
        if(column==0){
            outRect.top = padding;
        }
        outRect.bottom = padding;
//        int index = position % columnNumber;
//        if(index==columnNumber-1){
//            outRect.right = 0;
//        }else {
//            outRect.right = padding/2;
//        }
//        if(index==0){
//            outRect.left = 0;
//        }else {
//            outRect.left = padding/2;
//        }

        outRect.left = padding;

//        if (parent.getChildLayoutPosition(view) %columnNumber==0) {
//            outRect.left = 0;
//        }
//        if (position % 2==0 ) {
//            //左边的
//            outRect.right = padding;
//        }else {
//            //右边的
//            outRect.left = padding;
//        }

    }

    public GridViewDecoration(int padding, int columnNumber) {
        this.padding = padding;
        this.columnNumber = columnNumber;
    }
}
