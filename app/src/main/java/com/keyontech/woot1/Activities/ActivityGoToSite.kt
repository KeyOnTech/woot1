package com.keyontech.woot1.Activities

import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.keyontech.woot1.R
import com.keyontech.woot1.Utils.ACT_EXTRA_GO_TO_SITE_URL
import com.keyontech.woot1.Utils.PRIMARY_COLOR
import com.keyontech.woot1.Utils.cancelNotification
import kotlinx.android.synthetic.main.activity_go_to_site_url.*

/**
 * Created by jonesq on 3/16/18.
 */

class ActivityGoToSite : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /*** used by notification action button to cancel notification */
        cancelNotification(this, intent)

        setContentView(R.layout.activity_go_to_site_url)
        val urlLink = intent.getStringExtra(ACT_EXTRA_GO_TO_SITE_URL)
        /***        to make the webview allow java from pages add the following - start */
        webView_activity_go_to_site_url.settings.javaScriptEnabled = true
        webView_activity_go_to_site_url.settings.loadWithOverviewMode = true
        webView_activity_go_to_site_url.settings.useWideViewPort = true
        /***        to make the webview allow java from pages add the following - end */
        webView_activity_go_to_site_url.loadUrl(urlLink)

        try {
            /*** set the color scheme  --- START */
            // notification bar
            window.statusBarColor = Color.parseColor(PRIMARY_COLOR)
            // botom of phone nav buttons
            window.navigationBarColor = Color.parseColor(PRIMARY_COLOR)
        }catch (e: Exception){

        }
    }
}