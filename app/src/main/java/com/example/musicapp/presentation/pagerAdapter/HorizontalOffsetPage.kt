package com.example.musicapp.presentation.pagerAdapter

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class HorizontalOffsetPage(
    context: Context,
    spaceInDp: Int
): RecyclerView.ItemDecoration() {
    private val marginInPx = context.resources.getDimension(spaceInDp).toInt()

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        outRect.left = marginInPx
        outRect.right = marginInPx
    }
}