package com.keyontech.woot1.Adapters

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import com.keyontech.woot1.Models.ModelWootOfferPhoto
import com.keyontech.woot1.ViewPager.FragmentViewPager1

/**
 * Created by kot on 3/15/18.
 */

class AdapterViewPagerActivityMain (fragmentManager: FragmentManager, private val modelMehDealPhotos: ArrayList<ModelWootOfferPhoto>) :
        FragmentStatePagerAdapter(fragmentManager) {

    override fun getItem(position: Int): Fragment {
//        println("photo = " + modelMehDealPhotos[position].Url)
        return FragmentViewPager1.newInstance(modelMehDealPhotos[position].Url)
    }

    override fun getCount(): Int {
        return modelMehDealPhotos.size
    }
}