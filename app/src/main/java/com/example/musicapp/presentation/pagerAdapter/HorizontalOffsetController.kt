package com.example.musicapp.presentation.pagerAdapter

import android.view.View
import androidx.annotation.DimenRes
import androidx.viewpager2.widget.ViewPager2

class HorizontalOffsetController {
    fun setPreviewOffsetBottomPager(
        viewPager2: ViewPager2,
        @DimenRes nextItemVisibleSize: Int,
        @DimenRes currentItemHorizontalMargin: Int
    ) {
        viewPager2.clipChildren = false
        viewPager2.clipToPadding = false
        viewPager2.offscreenPageLimit = 1

        val nextItemVisiblePx = viewPager2.context.resources.getDimension(nextItemVisibleSize)
        val currentItemHorizontalMarginPx = viewPager2.context.resources.getDimension(currentItemHorizontalMargin)
        val pageTranslationX = nextItemVisiblePx + currentItemHorizontalMarginPx

        val pageTransformer = ViewPager2.PageTransformer { page: View, position: Float ->
            page.translationX = -pageTranslationX * position
        }

        viewPager2.setPageTransformer(pageTransformer)

        val itemDecoration = HorizontalOffsetPage(
            viewPager2.context,
            currentItemHorizontalMargin
        )

        viewPager2.addItemDecoration(itemDecoration)
    }

    fun setPreviewOffsetMainPager(
        viewPager2: ViewPager2,
        @DimenRes nextItemVisibleSize: Int,
        @DimenRes currentItemHorizontalMargin: Int
    ) {
        viewPager2.clipChildren = false
        viewPager2.clipToPadding = false
        viewPager2.offscreenPageLimit = 1

        val nextItemVisiblePx = viewPager2.context.resources.getDimension(nextItemVisibleSize)
        val currentItemHorizontalMarginPx = viewPager2.context.resources.getDimension(currentItemHorizontalMargin)
        val pageTranslationX = nextItemVisiblePx + currentItemHorizontalMarginPx

        val pageTransformer = ViewPager2.PageTransformer { page: View, position: Float ->
            page.translationX = -pageTranslationX * position
            page.scaleY = 1 - (0.25f * kotlin.math.abs(position))
        }

        viewPager2.setPageTransformer(pageTransformer)

        val itemDecoration = HorizontalOffsetPage(
            viewPager2.context,
            currentItemHorizontalMargin
        )

        viewPager2.addItemDecoration(itemDecoration)
    }
}