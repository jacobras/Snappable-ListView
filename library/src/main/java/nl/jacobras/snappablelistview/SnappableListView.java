package nl.jacobras.snappablelistview;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

/**
 * ListView that "snaps". Top item will never be left cut off, unless the user has scrolled to
 * the bottom of the list.
 * Created by Jacob Ras on 12-5-2015.
 */
public class SnappableListView extends ListView {
    public SnappableListView(Context context) {
        super(context);
        setScrollListener();
    }

    public SnappableListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setScrollListener();
    }

    public SnappableListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setScrollListener();
    }

    /**
     * Initialize the "snappy scroll" functionality.
     */
    private void setScrollListener() {
        setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int scrollState) {

                // The user has stopped scrolling.
                if (scrollState == SCROLL_STATE_IDLE) {
                    View view = absListView.getChildAt(0);
                    View lastView = absListView.getChildAt(absListView.getChildCount() - 1);

                    // Calculate position to scroll to.
                    int top = Math.abs(view.getTop());
                    int bottom = Math.abs(view.getBottom());
                    int scrollBy = top >= bottom ? bottom : -top;

                    // Already at exact position?
                    if (scrollBy == 0) {
                        return;
                    }

                    // At the bottom of the list?
                    if (lastView.getBottom() <= absListView.getHeight()) {
                        return;
                    }

                    smoothScroll(scrollBy, (ListView) absListView);
                }
            }

            /**
             * Smoothly scroll.
             * @param scrollByF Distance to scroll in pixels.
             * @param listView The ListView to scroll on.
             */
            private void smoothScroll(final int scrollByF, final ListView listView) {
                new Handler().post(new Runnable() {

                    @Override
                    public void run() {
                        listView.smoothScrollBy(scrollByF, 150);
                    }
                });
            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            }
        });
    }
}