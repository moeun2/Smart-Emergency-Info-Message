package com.passta.a2ndproj.utility;
import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.passta.a2ndproj.R;

public class RecyclerViewDecoration extends RecyclerView.ItemDecoration {

    private final int divHeight;

    public RecyclerViewDecoration(int divHeight)
    {
        this.divHeight = divHeight;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state)
    {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.top = divHeight;
    }
}