package wadru.dcapp.Extra;

import android.content.Context;
import android.graphics.PointF;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.util.DisplayMetrics;

/**
 * Created by hodongkim on 2016. 11. 21..
 */

public class SmoothScroll extends LinearLayoutManager {
    private Context mContext;
    private static final float MILLISECONDS_PER_INCH = 10f;

    public SmoothScroll(Context context) {
        super(context);
        this.mContext = context;
    }

    @Override
    public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {
        super.smoothScrollToPosition(recyclerView, state, position);

        LinearSmoothScroller smoothScroller = new LinearSmoothScroller(mContext) {
            @Override
            public PointF computeScrollVectorForPosition(int targetPosition) {

                return SmoothScroll.this.computeScrollVectorForPosition(targetPosition);
            }

            @Override
            protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
                return MILLISECONDS_PER_INCH/displayMetrics.densityDpi;
            }
        };

        smoothScroller.setTargetPosition(position);

        startSmoothScroll(smoothScroller);
    }
}


