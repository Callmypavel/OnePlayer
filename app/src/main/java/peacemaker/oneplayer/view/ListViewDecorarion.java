package peacemaker.oneplayer.view;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import peacemaker.oneplayer.R;
import peacemaker.oneplayer.activity.OneApplication;
import peacemaker.oneplayer.tool.ColorUtil;


/**
 * Created by pavel on 2018/3/13.
 */

public class ListViewDecorarion extends RecyclerView.ItemDecoration {
    private Paint paint;
    private int padding;
    public ListViewDecorarion(int padding){
        paint = new Paint();
        paint.setColor(ColorUtil.getColorByResId(OneApplication.context, R.color.colorDivider));
        this.padding = padding;
    }
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int position = parent.getChildAdapterPosition(view);
        if (parent.getChildAdapterPosition(view) != 0){
            outRect.top = padding;
        }
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        float dividerTop = parent.getTop() - padding;
        float dividerLeft = parent.getPaddingLeft();
        float dividerBottom = parent.getTop();
        float dividerRight = parent.getWidth() - parent.getPaddingRight();

        c.drawRect(dividerLeft,dividerTop,dividerRight,dividerBottom,paint);
    }
}
