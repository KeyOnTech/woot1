package com.keyontech.woot1.Activities

/**
 * Created by kot on 3/15/18.
 */

import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.Html
import android.view.Menu
import android.view.MenuItem
import android.view.animation.AnimationUtils
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.keyontech.woot1.Adapters.AdapterViewPagerActivityMain
import com.keyontech.woot1.Models.ModelWootDeal
import com.keyontech.woot1.Models.ModelWootOfferPhoto
import com.keyontech.woot1.R
import com.keyontech.woot1.Utils.*
import com.keyontech.woot1.ViewPager.DepthViewPagerPageTransform
import com.keyontech.woot1.ViewPager.ParallaxViewPagerPageTransform
import kotlinx.android.synthetic.main.activity_details_v2.*
import kotlinx.android.synthetic.main.activity_details_include_content_v2.*
import org.json.JSONException
import java.util.*
import kotlin.collections.ArrayList

class DetailsActivity: AppCompatActivity() {
    var dealURL = ""
    var discussionURL = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details_v2)

        /*** fab buttons */
        fabbutton_ActivityDetails_Deal_URL.setOnClickListener { view ->
            goToURL(this, dealURL)
        }

        fabbutton_ActivityDetails_Discussion_URL.setOnClickListener { view ->
            goToURL(this, discussionURL)
        }

        /*** display output */
        val jsonResponse = intent.getStringExtra(ACT_FRAG_JSON_RESPONSE)
        val itemSelected = intent.getIntExtra(ACT_FRAG_MAIN_ITEM_SELECTED, 0)
        displayResults(jsonResponse, itemSelected)
    }

    fun displayResults(jsonResponse: String, itemSelected: Int) {
        try {
            if (jsonResponse != null && jsonResponse.isNotEmpty()) {
                var gson = GsonBuilder().serializeNulls().create()
                var woot: List<ModelWootDeal> = gson.fromJson(jsonResponse, object : TypeToken<List<ModelWootDeal>>() {}.type)
                var wootSorted = woot.sortedWith(compareBy({ it.Site}))
                val deal = wootSorted[itemSelected]

                if (deal != null) {
                    if (deal.Offers.count() > 0) {
                        val offer = deal.Offers[0]

                        // Show the title
                        textView_content_offer_details.text = offer.Title

                        // fab button click url
                        dealURL = offer.Url
                        // fab button click url
                        discussionURL = offer.DiscussionUrl

                        var strOutput = ""
                        strOutput += " \n \n  <br/> <h1> " + deal.Title + " </h1> "
                        strOutput += " \n \n  <b> Price: </b>" + wootPriceLowtoHigh(offer)
                        strOutput += " \n \n  <br/> <br/> <b> Features: </b>" + offer.Features
                        strOutput += " \n \n  <br/> <br/> <b> Specs: </b>" + offer.Specs
                        strOutput += " \n \n  <br/> <br/> <hr /> "
                        strOutput += " \n \n  <br/> <br/> <b> Woot Talk: </b>  <i> " + offer.Subtitle + " </i> "
                        strOutput += " \n \n  <br/> <br/> <b> " + offer.Teaser + " </b> "
                        strOutput += " \n \n  <br/> <br/> <i> " + offer.WriteUp + " </i> "

                        // get our html content
                        val htmlAsSpanned = Html.fromHtml(strOutput) // used by TextView

                        // set the html content on the TextView
                        textView_content_offer_details.text = htmlAsSpanned
                        //                webView_content_offer_details.loadDataWithBaseURL(null, strOutput, "text/html", "utf-8", null);

                        textView_content_offer_details.text = Html.fromHtml(strOutput)

                        // set progress bar how much of item is remaining.
                        progressBar_ActivityDetails.max = 100
                        progressBar_ActivityDetails.progress = offer.PercentageRemaining
                        progressBar_ActivityDetails.scaleY = 3f
//                        progressBar_ActivityDetails.scaleY = 2f

                        if (offer.Photos != null) {
                            /*** set photos viewPager */
                            //                    val showPhotos: ArrayList<ModelWootOfferPhoto>()
                            var showPhotos: ArrayList<ModelWootOfferPhoto> = ArrayList<ModelWootOfferPhoto>()

                            offer.Photos.forEach {
                                val xP = it

                                it.Tags.forEach {
                                    if (it == CONDITION_OFFER_PHOTO_TAG_KEY_GALLERY) {
                                        showPhotos.add(xP)
                                    }
                                }
                            }

                            //                    var adapterActivityMain = AdapterViewPagerActivityMain(supportFragmentManager, offer.Photos)
                            var adapterActivityMain = AdapterViewPagerActivityMain(supportFragmentManager, showPhotos)
                            viewPager_ActivityDetails_v2.offscreenPageLimit = 3
                            viewPager_ActivityDetails_v2.adapter = adapterActivityMain

                            /*** set custom swipe animations */
                            val randomNumberCreator = Random()
                            val randomNumber = randomNumberCreator.nextInt(3)

                            when (randomNumber) {
                                0 -> viewPager_ActivityDetails_v2.setPageTransformer(false, DepthViewPagerPageTransform())
                                else -> viewPager_ActivityDetails_v2.setPageTransformer(false, ParallaxViewPagerPageTransform())
                            }

                            /*** setup viewPager indicator buttons */
                            tab_layout_viewpager_indicator_dots_ActivityMain.setupWithViewPager(viewPager_ActivityDetails_v2, true)

                            /*** animate on start */
                            viewPager_ActivityDetails_v2.startAnimation(AnimationUtils.loadAnimation(this, startRandomAnimation()))
                            //                cardView_ActivityAbout_1.startAnimation(AnimationUtils.loadAnimation(this, android.R.anim.slide_out_right))
                            cardView_ActivityAbout_2.startAnimation(AnimationUtils.loadAnimation(this, startRandomAnimation()))
                            //                cardView_ActivityAbout_3.startAnimation(AnimationUtils.loadAnimation(this, android.R.anim.slide_out_right))

                            try {
                                /*** set the color scheme  --- START */
                                // notification bar
                                window.statusBarColor = Color.parseColor(PRIMARY_COLOR)
                                // botom of phone nav buttons
                                window.navigationBarColor = Color.parseColor(PRIMARY_COLOR)
                            }catch (e: Exception){

                            }
                        } else {
                            println("set view pager to null repsonse image")
                        }
                    }
                } else {
//                    println("no deals")
                }
            } else {
                /*** no internet response */
//                noInternetResponse()
            }
        } catch (e: JSONException) {
//            printToErrorLog_10("ActivityMain", "runOnUiThread try")
        }
    }



    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_activity_details, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) =
        when (item.itemId) {
            R.id.menu_share_deal -> {
                // This ID represents the Home or Up button. In the case of this
                // activity, the Up button is shown. For
                // more details, see the Navigation pattern on Android Design:
                //
                // http://developer.android.com/design/patterns/navigation.html#up-vs-back

                startActivity(shareDeal(dealURL))
                true
            }
            else -> super.onOptionsItemSelected(item)
    }

}