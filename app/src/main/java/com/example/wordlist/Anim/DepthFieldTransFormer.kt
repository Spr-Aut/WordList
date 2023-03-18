package com.example.wordlist.Anim

import android.view.View
import androidx.viewpager2.widget.ViewPager2
import kotlin.math.abs


private const val MIN_SCALE = 0.85f
private const val MIN_SCALEX = 0.9f

class DepthFieldTransformer : ViewPager2.PageTransformer {

    override fun transformPage(page: View, position: Float) {
        page.apply {
            when {
                position < 1 && position > -1 -> {
                    val absPos = abs(position)
                    val scale = if (absPos > 1) 0F else 1 - absPos
                    page.scaleX = MIN_SCALEX + (1f - MIN_SCALEX) * scale
                    page.scaleY = MIN_SCALE + (1f - MIN_SCALE) * scale
                    //page.alpha = MIN_SCALE + (1f - MIN_SCALE) * scale
                }
                else -> {
                    page.scaleX = MIN_SCALEX
                    page.scaleY = MIN_SCALE
                    //page.alpha = 0f
                }
            }
        }


    }
}