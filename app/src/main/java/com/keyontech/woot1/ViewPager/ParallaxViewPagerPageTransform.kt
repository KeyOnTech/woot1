package com.keyontech.woot1.ViewPager

import android.support.v4.view.ViewPager
import android.view.View
import com.keyontech.woot1.R

/**
 * Created by kot on 3/15/18.
 */

class ParallaxViewPagerPageTransform: ViewPager.PageTransformer {
    var fragmentImage: View? = null

    override fun transformPage(page: View?, position: Float) {
        val pageWidth = page?.width ?: return

        /*** swipe over effect */
//        var vX = .75
        var vX = 2

        fragmentImage = page.findViewById(R.id.imageView_Deal_Photos)
        /*** use this to make a slide over effect when swiping */
        fragmentImage?.translationX = -position * (pageWidth / vX.toFloat()) // works
    }
}