package com.keyontech.woot1

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_nav.*
import android.app.LoaderManager
import android.content.Intent
import android.content.Loader
import android.graphics.Color
import com.google.gson.GsonBuilder
import org.json.JSONException
import android.preference.PreferenceManager
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.view.Gravity
import android.view.View
import com.google.gson.reflect.TypeToken
import com.keyontech.woot1.Activities.ActivityAbout
import com.keyontech.woot1.Adapters.AdapterRecyclerViewActivityMain
import com.keyontech.woot1.Models.JSONUrL
import com.keyontech.woot1.Models.ModelWootDeal
import com.keyontech.woot1.Models.ModelWootOffer
import com.keyontech.woot1.Services.APIAsyncTaskLoader
import com.keyontech.woot1.Utils.*
import kotlinx.android.synthetic.main.content_nav.*
import kotlinx.android.synthetic.main.nav_header_nav.*

class ActivityMain : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, LoaderManager.LoaderCallbacks<String> {

    /*** api request url */
    var jsonURL = ""
    /*** api response string */
    var jsonResponse = ""

    var mehVideoLink = ""

//    // define View Pager
    private lateinit var adapterActivityMain: AdapterRecyclerViewActivityMain

    var asyncTaskReload = false
    val asyncTaskLoaderBundle = Bundle()


    /*** AsyncTaskLoader */
    // Creating the loader
    override fun onCreateLoader(id: Int, args: Bundle): Loader<String> {
        /*** show progressbar */
        loading_indicator_ActivityMain.visibility = View.VISIBLE

//        return APIAsyncTaskLoader(this, args.getString("444queryString")!!)
        return APIAsyncTaskLoader(this, asyncTaskLoaderBundle)
    }

    // Loader onLoadFinished method
    override fun onLoadFinished(loader: Loader<String>, data: String) {
        // Hide loading indicator because the data has been loaded
        //        val loadingIndicator = findViewById(R.id.loading_indicator)
        //        loadingIndicator.setVisibility(View.GONE)

        /*** hide progress bar */
        loading_indicator_ActivityMain.visibility = View.GONE

        if (data.isNullOrEmpty()) {
            //            /*** no internet response */
            noInternetResponse()
        } else {
            ////            finalResultTxtView?.setText("The Sum of Numbers between 1 to 1 million \n = $data")
            ////            Log.d(TAG, "Final Result = $data")
            ////            jsonResponse = data
            processReturn(data)
        }
    }

    // Loader onLoaderReset method
    override fun onLoaderReset(loader: Loader<String>) {
    }








    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbarNavDrawer_ActivityMain)


        /*** setup Nav Drawer */
        var toggle = object : ActionBarDrawerToggle(
                this, drawer_layout, toolbarNavDrawer_ActivityMain, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            override fun onDrawerClosed(drawerView: View?) {
                super.onDrawerClosed(drawerView)
                /*** dont show nav bar on start */
                PreferenceManager.getDefaultSharedPreferences(applicationContext)
                        .edit()
                        .putBoolean(PREF_KEY_WOOT1_SHOW_NAV_DRAWER_ONSTART, false)
                        .apply()
            }
        }

        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view_ActivityMain.setNavigationItemSelectedListener(this)


        /*** auto open nav drawer */
        /*** show nav drawer first time */
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        var showNavDrawerOnStart = sharedPreferences.getBoolean(PREF_KEY_WOOT1_SHOW_NAV_DRAWER_ONSTART, true)

        if (showNavDrawerOnStart) {
            drawer_layout.openDrawer(Gravity.LEFT)
        }


        /*** used by notification action button to cancel notification */
        cancelNotification(this, intent)
        /*** start job scheduler */
        if (!isNotificationJobScheduled(this))
            scheduleNotificationJob(this)




// get url from file
// fetch data
        var gson = GsonBuilder().serializeNulls().create() // include null opjects when null
        var urlFile = loadJsonFromFile("url.json", this)
        var jsonOutput = gson.fromJson( urlFile , JSONUrL::class.java )
        jsonURL = jsonOutput.mehurl

//        /*** remove title ba text */
//        setTitle("")

        /*** call json service */
        fetchJSON()
    }

    fun goToURL(pURL: String) {
        println("pUrl = " + pURL)
//        var intent = Intent(baseContext, ActivityGoToSite::class.java)
//        intent.putExtra(ACT_EXTRA_GO_TO_SITE_URL, pURL)
//        startActivity(intent)
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_bar_about-> {
                var intent = Intent(baseContext, ActivityAbout::class.java)
                startActivity(intent)
            }
            R.id.nav_bar_rate_app-> {
                startActivity(rateApp(this))
            }
            R.id.nav_bar_share_app-> {
                startActivity(shareApp(this))
            }
            R.id.nav_bar_refresh-> {
                fetchJSON()
            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }



    fun startAsyncTaskLoader() {
//        /*** show progressbar */
//        loading_indicator.visibility = View.VISIBLE

        /***  */
/*
Starting a Loader
Use the LoaderManager class to manage one or more Loader instances within an activity or fragment. Use initLoader() to initialize a loader and make it active. Typically, you do this within the activity’s onCreate() method or onViewCreated() in  a fragment.
 */
/*
starting a loader for a fragment
To start AsyncTaskLoader, in an Activity, call getSupportLoaderManager().initLoader(LOADER_ID, BUNDLE, LoaderManager.LoaderCallbacks<T>) or in a Fragment call getLoaderManager().initLoader(LOADER_ID, BUNDLE, LoaderManager.LoaderCallbacks<T>).
For Fragments make sure you import android.support.v4.app.Fragment, android.support.v4.app.LoaderManager.
 */




        // Initialize the loader. Pass in the int ID constant defined above and pass in null for
        // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
        // because this activity implements the LoaderCallbacks interface)
        // BUNDLE is Bundle paramenter that you must pass, it gives us an option to send data to our AsyncTaskLoaderSubClass

//        var mTParams = modelJobExecuterAsyncTaskParams()
//        mTParams.cContext = this
//        mTParams.dealUrl = "www.google.com"

//        var mTParams2 = modelJobExecuterAsyncTaskParamsSerializable()
//        mTParams2.cContext = this
//        mTParams2.dealUrl = "aaaawwwaaaa.aaaaaaagoogleaaaaa.aaaacomaaaa"

//        val asyncTaskLoaderBundle = Bundle()
////        asyncTaskLoaderBundle.putSerializable(BUNDLE_KEY_SERIALIZABLE, mTParams2 as Serializable)
        asyncTaskLoaderBundle.putString(ASYNCTASKLOADER_BUNDLE_KEY_RELOAD_JSON, null)
////        supportLoaderManager.initLoader(ASYNCTASK_LOADER_ID, bundleTEST, this)


// Get a reference to the LoaderManager, in order to interact with loaders.
        val loaderManager = loaderManager
//        loaderManager.initLoader(ASYNCTASKLOADER_ID, null, this)
        loaderManager.initLoader(ASYNCTASKLOADER_ID, asyncTaskLoaderBundle, this)
        /****  end */
    }

    fun cancelAsyncTaskLoader() {
        val loaderManager = loaderManager
        loaderManager.destroyLoader(ASYNCTASKLOADER_ID)
    }

    fun fetchJSON() {
        cancelAsyncTaskLoader()
        startAsyncTaskLoader()
    }


    fun fetchMockInterface() {
        println("Mock Data initiated ")

        try {
            println("LOAD :  MOCK data")
//            var mockData = loadJsonFromFile("sample1.json", this)
            var mockData = loadJsonFromFile("sampleNULLsTopics.json", this)
            jsonResponse = mockData

            println("jsonResponse = " + jsonResponse)
            processMOCKReturn(jsonResponse)
        } catch (e: JSONException) {
            println("fetchMockInterface Error: " + e)
        } // try

    } //

    fun processReturn(response: String){
        // setup interface
        runOnUiThread{
            displayResults(response)
        }
    }

    fun displayResults(jsonResponse: String) {
        try {
            if (jsonResponse != null && jsonResponse.isNotEmpty()) {
                var gson = GsonBuilder().serializeNulls().create()
                var woot: List<ModelWootDeal> = gson.fromJson(jsonResponse, object : TypeToken<List<ModelWootDeal>>() {}.type)
                var wootSorted = woot.sortedWith(compareBy({ it.Site}))

                // set poll RecyclerView
                recyclerView_main.layoutManager = LinearLayoutManager(this)
                recyclerView_main.adapter = AdapterRecyclerViewActivityMain(supportFragmentManager, wootSorted, jsonResponse)
//                    recyclerView_main.setLayerType(new GridLayoutManager(this,3))
                recyclerView_main.layoutManager = GridLayoutManager(applicationContext, 1)

                try {
                    /*** set the color scheme  --- START */
                    // notification bar
                    window.statusBarColor = Color.parseColor(PRIMARY_COLOR)
                    // botom of phone nav buttons
                    window.navigationBarColor = Color.parseColor(PRIMARY_COLOR)
                    // tool title bar color
                    toolbarNavDrawer_ActivityMain.setBackgroundColor(Color.parseColor(PRIMARY_COLOR))
                    // Nav drawer header background color
                    nav_drawer_header_linear_layout.setBackgroundColor(Color.parseColor(NAV_DRAWER_HEADER_COLOR))
                }catch (e: Exception){

                }
                return
            }else {
                /*** no internet response */
                noInternetResponse()
            }
        } catch (e: JSONException) {
//            printToErrorLog_10("ActivityMain", "runOnUiThread try")
        }

    }

    fun noInternetResponse() {
//        cardView_Recyclerview_Main_Row_v2.visibility = View.GONE
//        fabbuttonNavDrawer.visibility = View.GONE

        // set poll RecyclerView
        var failedToLoadModelWootDealOffer: ModelWootOffer = ModelWootOffer(
            Title = ""
            , PercentageRemaining = 0
            , Features = ""
            , Specs = ""
            , SoldOut = false
            , Subtitle = ""
            , WriteUp = ""
            , DiscussionUrl = ""
            , Url = ""
            , Teaser = ""
        )

        val failedToLoadOffers : java.util.ArrayList<ModelWootOffer> = java.util.ArrayList()
        failedToLoadOffers.add(failedToLoadModelWootDealOffer)

        var failedToLoadModelWootDeal: ModelWootDeal = ModelWootDeal(
                Type = ""
                , Id = ""
                , Title = ""
                , Site = ""
                , StartDate = ""
                , EndDate = ""
                , Offers = failedToLoadOffers
        )

        val failedToLoadDeal: java.util.ArrayList<ModelWootDeal> = java.util.ArrayList()
        failedToLoadDeal.add(failedToLoadModelWootDeal)


//        failedToLoadModelWootDeal.Offers.add(failedToLoadModelWootDealOffer)

//        val failedToLoadPhoto: ArrayList<ModelWootDeal> = arrayListOf<ModelWootDeal>()

//        val failedToLoadPhoto : ArrayList<String> = ArrayList()
//        failedToLoadPhoto.add("")


        recyclerView_main.layoutManager = LinearLayoutManager(this)
        recyclerView_main.adapter = AdapterRecyclerViewActivityMain(supportFragmentManager, failedToLoadDeal, "")
//                    recyclerView_main.setLayerType(new GridLayoutManager(this,3))
//        recyclerView_main.layoutManager = GridLayoutManager(applicationContext, 1)




//        /*** set photos viewPager */
//        recyclerView_main = AdapterRecyclerViewActivityMain(supportFragmentManager, failedToLoadPhoto )
//        recyclerView_main.offscreenPageLimit = 1
//        recyclerView_main.adapter = adapterActivityMain

        Snackbar.make(
                findViewById(android.R.id.content)
                , "Unable to fetch data please check internet connection"
                , Snackbar.LENGTH_INDEFINITE)
                .setAction("Retry") {
                    fetchJSON()
                    printToSnackbar(
                            findViewById(android.R.id.content)
                            , "Attempting to fetch data!"
                            , Snackbar.LENGTH_SHORT
                    )
//                            Snackbar.make(
//                                    findViewById(android.R.id.content)
//                                    , "Attempting to fetch data!"
//                                    , Snackbar.LENGTH_SHORT).show()
                }.show()
    }

    fun processMOCKReturn(response: String){
        displayResults(response)
        println("review meh2 utils class amd move all to here")
    }

}