package indi.toaok.common.helper;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @author Yun
 * @version 1.0  2019-12-19.
 */
public class SpaceItemDecoration extends RecyclerView.ItemDecoration {

    int leftSpace;
    int topSpace;
    int rightSpace;
    int bottomSpace;

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.left = leftSpace;
        outRect.right = rightSpace;
        outRect.bottom = bottomSpace;

        // Add top margin only for the first item to avoid double space between items
        if (parent.getChildLayoutPosition(view) == 0)
            outRect.top = topSpace;
    }

    public SpaceItemDecoration(int leftSpace, int topSpace, int rightSpace, int bottomSpace) {
        this.leftSpace = leftSpace;
        this.topSpace = topSpace;
        this.rightSpace = rightSpace;
        this.bottomSpace = bottomSpace;
    }

    public SpaceItemDecoration(int hSpace, int vSpace) {
        this.leftSpace = this.rightSpace = hSpace;
        this.topSpace = this.bottomSpace = vSpace;
    }

    public SpaceItemDecoration(int vSpace) {
        this.topSpace = this.bottomSpace = vSpace;
    }
}
