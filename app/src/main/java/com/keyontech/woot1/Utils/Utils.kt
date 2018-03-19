package com.keyontech.woot1.Utils

/**
 * Created by kot on 3/12/18.
 */


import android.app.Activity
import android.app.Notification
import android.app.NotificationManager
import android.media.RingtoneManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.support.v4.app.NotificationCompat
import android.util.Log
import android.widget.Toast
import java.io.IOException
import android.app.job.JobScheduler
import android.net.Uri
import android.preference.PreferenceManager
import android.support.design.widget.Snackbar
import android.view.View
import com.keyontech.woot1.Activities.ActivityGoToSite
import com.keyontech.woot1.ActivityMain
import java.util.*
import android.app.job.JobInfo
import android.content.ComponentName
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.keyontech.woot1.Models.*
import com.keyontech.woot1.R
import com.keyontech.woot1.Services.MJobScheduler


/*** Activity constants */
//val ACT_EXTRA_MEH_VIDEO_LINK = "ACT_EXTRA_MEH_VIDEO_LINK"
val ACT_EXTRA_GO_TO_SITE_URL = "ACT_EXTRA_GO_TO_SITE_URL"
//val ACT_FRAG_ARG_MEH_RESPONSE_STRING = "ACT_FRAG_ARG_MEH_RESPONSE_STRING"
val ACT_FRAG_JSON_RESPONSE = "ACT_FRAG_JSON_RESPONSE"
val ACT_FRAG_MAIN_ITEM_SELECTED = "ACT_FRAG_MAIN_ITEM_SELECTED"

val FRAG_ARG_PHOTO_URI = "FRAG_ARG_PHOTO_URI"

val PREF_KEY_WOOT1_SHOW_JOBSCHEDULER_NOTIFICATION = "PREF_KEY_WOOT1_SHOW_JOBSCHEDULER_NOTIFICATION"
val PREF_KEY_WOOT1_WOOT_JSON_RESPONSE = "PREF_KEY_WOOT1_WOOT_JSON_RESPONSE"
val PREF_KEY_WOOT1_MEH_DEAL_STRING = "PREF_KEY_WOOT1_MEH_DEAL_STRING"
val PREF_KEY_WOOT1_SHOW_NAV_DRAWER_ONSTART = "PREF_KEY_WOOT1_SHOW_NAV_DRAWER_ONSTART"

/*** this is used for the notification large image */
val NOTIFICATION_ID = 55551

/*** job scheduler */
val JSCHEDULER_JOB_ID = 55552
//val JS_PERSISTABLE_BUNDLE_DEAL_URL = "JS_PERSISTABLE_BUNDLE_DEAL_URL"
val JS_SCHEDULE_TEST_TIMER: Long = 5000 // 5 seconds setMinimum time to run will not work 15 min is the minimum
val JS_SCHEDULE_TIMER : Long = 14400000 // 4 hours setPeriodic 4 hours time to run will not work 15 min is the minimum
//val JS_SCHEDULE_TIMER : Long = 28800000 // 8 hours setPeriodic 8 hours time to run will not work 15 min is the minimum
//val JS_SCHEDULE_TIMER : Long = 43200000 // 12 hours setPeriodic 8 hours time to run will not work 15 min is the minimum
val JS_SCHEDULE_NOTIFICATION_TITLE = "NEW offers on Woot"

/*** AsyncTaskLoader */
val ASYNCTASKLOADER_ID = 55553
//val ASYNCTASKLOADER_BUNDLE_KEY_RELOAD_LOADER = "ASYNCTASKLOADER_BUNDLE_KEY_RELOAD_LOADER"
val ASYNCTASKLOADER_BUNDLE_KEY_RELOAD_JSON = "ASYNCTASKLOADER_BUNDLE_KEY_RELOAD_JSON"

val CONDITION_OFFER_PHOTO_TAG_KEY_FULLSIZE = "fullsize-0"
val CONDITION_OFFER_PHOTO_TAG_KEY_GALLERY = "gallery"
val CONDITION_OFFER_ITEM_ATTRIBUTE_KEY_CONDITION = "Condition"
val CONDITION_DEAL_SITE_TRIM_CONDITION = ".woot.com"

val PRIMARY_COLOR = "#00cc00"
val NAV_DRAWER_HEADER_COLOR = "#00cc00"
val PROGRESS_BAR_COLOR = "#3F51B5"


fun printToSnackbar(pView: View, pMessage: String, pShowLength: Int) {
    Snackbar.make(pView, pMessage, pShowLength).show()
}
fun printToToast(pContext: Context, vTag: String, vStr: String) {
    Toast.makeText(pContext, vTag + " - " + vStr, Toast.LENGTH_SHORT).show()
}
fun printToLog(vTag: String, vStr: String) {
    Log.d(vTag, vStr)
}
fun printToLog_10(vTag: String, vStr: String) {
    for (i in 1..10) {
        Log.d(vTag, vStr)
    }
}
fun printToErrorLog_10(vTag: String, vStr: String) {
    for (i in 1..10) {
        Log.e(vTag, vStr)
    }
}


// make sure the internet is turned on on the device
fun isConnected_To_Network(pContext: Context): Boolean {
    val connectivityManager = pContext.getSystemService(Activity.CONNECTIVITY_SERVICE) as ConnectivityManager
    val networkInfo = connectivityManager.activeNetworkInfo

    return networkInfo != null && networkInfo.isConnected
}

// Check whether this job is currently scheduled.
fun isNotificationJobScheduled(context: Context): Boolean {
    val js = context.getSystemService(JobScheduler::class.java)
    val jobs = js!!.allPendingJobs ?: return false

    return jobs.indices.any {
        jobs[it].id == JSCHEDULER_JOB_ID
    }
}

fun scheduleNotificationJob(pContext: Context) {
    println("util - scheduleNotificationJob - started")
    val componentName = ComponentName(pContext, MJobScheduler::class.java)
    /*** Job schedule paramters and conditions */
    val jobInfoBuilder = JobInfo.Builder(JSCHEDULER_JOB_ID, componentName)
    /*** this is the job to schedule  */
    var mJobScheduler = pContext.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler

    /*** Job scheduler extras */
//            var persistableBundle: PersistableBundle
//            persistableBundle = PersistableBundle()
//            persistableBundle.putString(JS_PERSISTABLE_BUNDLE_DEAL_URL, mehDealUrl)
//            jobInfoBuilder.setExtras(persistableBundle)


    /*** Job scheduler conditions  */
    /*** job should be delayed by the provided amount of time. */
//    /*** USE FOR TESTING ONLY 5 end */
//    jobInfoBuilder.setMinimumLatency(JS_SCHEDULE_TEST_TIMER) // testing 5 sec.s  YOUR_TIME_INTERVAL comment out setPeriodic to use this
//    println("444000 - TesT - Time Reached = $JS_SCHEDULE_TEST_TIMER miliseconds")

    /*** USE FOR LIVE recurring time interval, not more than once per period */
    jobInfoBuilder.setPeriodic(JS_SCHEDULE_TIMER)

    /*** require internet */
    jobInfoBuilder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)

    /*** persist this job across device reboots */
    jobInfoBuilder.setPersisted(true)

    /*** this is the job it self  */
    var mJobInfo = jobInfoBuilder.build()
    /*** start / schedule the job service  */
    mJobScheduler!!.schedule(mJobInfo!!)
}

fun cancelNotification(pContext: Context, pIntent: Intent) {
    val notificationManager = pContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    notificationManager.cancel(NOTIFICATION_ID)
//    notificationManager.cancel(pIntent.getIntExtra(NOTIFICATION_ID.toString(), NOTIFICATION_ID))
//    notificationManager.cancel(pIntent.extras.getInt(NOTIFICATION_ID.toString()))
}

fun cancelNotificationJobScheduled(pContext: Context) {
    /*** cancel all job services by ID  */
    var vJobScheduler = pContext.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
    vJobScheduler!!.cancel(JSCHEDULER_JOB_ID)
}

fun wootNotificaitionText(pContext: Context, jsonResponse: String): String {
    /*** get shared preferences */
    val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(pContext)
    var wootSavedJSONResponse = sharedPreferences.getString(PREF_KEY_WOOT1_WOOT_JSON_RESPONSE, "")
    var notificationText = "New: "

    if (wootSavedJSONResponse.isNullOrEmpty()) {
//        /*** save string to preferences */
//        PreferenceManager.getDefaultSharedPreferences(pContext)
//                .edit()
//                .putString(PREF_KEY_WOOT1_WOOT_JSON_RESPONSE, jsonResponse)
//                .apply()
        var gson = GsonBuilder().serializeNulls().create()
        var woot: List<ModelWootDeal> = gson.fromJson(jsonResponse, object : TypeToken<List<ModelWootDeal>>() {}.type)
        var wootSorted = woot.sortedWith(compareBy({ it.Site}))

        wootSorted.forEach {
            var deal = it

            if (deal != null) {
                println("Site = " + deal.Id)
                println("Site = " + deal.Site)
                println("deal = " + deal.toString())
                /*** get shared preferences */
//                            var showNotification = sharedPreferences.getBoolean(PREF_KEY_WOOT1_SHOW_JOBSCHEDULER_NOTIFICATION, false)

                /*** save string to preferences */
                PreferenceManager.getDefaultSharedPreferences(pContext)
                        .edit()
                        .putString(PREF_KEY_WOOT1_WOOT_JSON_RESPONSE, jsonResponse)
                        .apply()

                notificationText += ", " + deal.Site
            }
        }
    }else {
        var gson = GsonBuilder().serializeNulls().create()
        var woot: List<ModelWootDeal> = gson.fromJson(jsonResponse, object : TypeToken<List<ModelWootDeal>>() {}.type)
        var wootSorted = woot.sortedWith(compareBy({ it.Site}))
        var savedWoot: List<ModelWootDeal> = gson.fromJson(wootSavedJSONResponse, object : TypeToken<List<ModelWootDeal>>() {}.type)
        var savedWootSorted = savedWoot.sortedWith(compareBy({ it.Site}))

        savedWootSorted.forEach {
            var savedSite = it.Site
            var savedId = it.Id

            wootSorted.forEach {
                var deal = it

                if (deal != null) {
                    if (it.Site.equals(savedSite, true))
                    {
                        if (it.Id.equals(savedId, true)) {
                            println("Site = " + deal.Id)
                            println("Site = " + deal.Site)
                            println("deal = " + deal.toString())
                            /*** get shared preferences */
                            //            var showNotification = sharedPreferences.getBoolean(PREF_KEY_WOOT1_SHOW_JOBSCHEDULER_NOTIFICATION, false)

                            if (notificationText.equals("New: ")) {
                                notificationText += deal.Site
                            }else{
                                notificationText += ", " + deal.Site
                            }
                        }
                    }
                }
            }
        }

        /*** save string to preferences */
        PreferenceManager.getDefaultSharedPreferences(pContext)
                .edit()
                .putString(PREF_KEY_WOOT1_WOOT_JSON_RESPONSE, jsonResponse)
                .apply()
    }

    notificationText = notificationText.replace(CONDITION_DEAL_SITE_TRIM_CONDITION,"",true)
    return notificationText
}

fun showJobNotification(pContext: Context): Boolean {
    val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(pContext)
    var showNotification = sharedPreferences.getBoolean(PREF_KEY_WOOT1_SHOW_JOBSCHEDULER_NOTIFICATION, false)

    return if(showNotification) {
        true
    }else{
        /*** save string to preferences */
        PreferenceManager.getDefaultSharedPreferences(pContext)
                .edit()
                .putBoolean(PREF_KEY_WOOT1_SHOW_JOBSCHEDULER_NOTIFICATION, true)
                .apply()
        false
    }
}

fun isNewDeal(pContext: Context, pNotificationText: String): Boolean {
    val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(pContext)
    var savedNotification = sharedPreferences.getString(PREF_KEY_WOOT1_MEH_DEAL_STRING, "")

    if(savedNotification.equals(pNotificationText,true))
    {
        return false
    }else{
        /*** save string to preferences */
        PreferenceManager.getDefaultSharedPreferences(pContext)
                .edit()
                .putString(PREF_KEY_WOOT1_MEH_DEAL_STRING, pNotificationText)
                .apply()
        return true
    }
}

fun startRandomAnimation(): Int {
    /*** set custom swipe animations */
    val randomNumberCreator = Random()
    val randomNumber = randomNumberCreator.nextInt(3)

    return when (randomNumber) {
        0 -> android.R.anim.fade_in
        1 -> android.R.anim.slide_out_right
        else -> android.R.anim.slide_in_left
    }
}

fun goToURL(pContext: Context, pURL: String) {
    println("pUrl = " + pURL)
    var vIntent = Intent(pContext, ActivityGoToSite::class.java)
    vIntent.putExtra(ACT_EXTRA_GO_TO_SITE_URL, pURL)
    pContext.startActivity(vIntent)
}

fun rateApp(pContext: Context): Intent {
    val vIntent = Intent(Intent.ACTION_VIEW)
    vIntent.data = Uri.parse(pContext.getString(R.string.app_store_url))
    return vIntent
}


fun shareDeal(pURL: String): Intent {
    var pSubject = ""
    var pMessage = ""
    var vIntent = Intent(Intent.ACTION_SEND)

    try {
        pSubject = "Check out this deal on Woot Today app"
        pMessage = pURL

        vIntent.type = "text/plain"
        vIntent.putExtra(Intent.EXTRA_EMAIL, "")
        vIntent.putExtra(Intent.EXTRA_SUBJECT, pSubject)
        vIntent.putExtra(Intent.EXTRA_TEXT, pMessage)
        vIntent = Intent.createChooser(vIntent, "Share This Deal")

        return vIntent
    } catch (e: Exception) {
        e.printStackTrace()
        return vIntent
    }
}

fun shareApp(pContext: Context): Intent? {
    var pSubject = ""
    var pMessage = ""
    var vIntent = Intent(Intent.ACTION_SEND)

    try {
        pSubject = "Check out the Woot Today app"
        pMessage = pContext.getString(R.string.app_store_url)

        vIntent.type = "text/plain"
        vIntent.putExtra(Intent.EXTRA_EMAIL, "")
        vIntent.putExtra(Intent.EXTRA_SUBJECT, pSubject)
        vIntent.putExtra(Intent.EXTRA_TEXT, pMessage)
        vIntent = Intent.createChooser(vIntent, "Share This App ")

        return vIntent
    } catch (e: Exception) {
        e.printStackTrace()
        return vIntent
    }
    // try
}

fun loadJsonFromFile(filename: String, context: Context): String {
    var json = ""

    try {
        val input = context.assets.open(filename)
        val size = input.available()
        val buffer = ByteArray(size)
        input.read(buffer)
        input.close()
        json = buffer.toString(Charsets.UTF_8)
    } catch (e: IOException) {
        e.printStackTrace()
    }

    return json
}

fun mehSoldOut(modelMehDeal: ModelMehDeal): Boolean {
    return if (modelMehDeal.launches != null) {
        !modelMehDeal.launches[0].soldOutAt.isNullOrEmpty()
    } else {
        false
    }
}


fun mehPriceLowtoHigh(modelMehDeal: ModelMehDeal): String {
    try {
        var vMin = modelMehDeal.items[0].price.toInt()
        var vMax = modelMehDeal.items[0].price.toInt()

        for (i in modelMehDeal.items) {
            if (vMin > i.price) vMin = i.price.toInt()
            if (vMax < i.price) vMax = i.price.toInt()
        }

        var vReturn = if (vMin != vMax) {
            "$$vMin - $$vMax"
        }else{
            "$$vMax"
        }

        if (mehSoldOut(modelMehDeal)) {
            return "SOLD OUT !!! - " + vReturn
        }else {
            return vReturn
        }
    }catch (e: Exception){
        return ""
    }
}

fun wootPriceLowtoHigh(wootOffer: ModelWootOffer): String {
    try {
        var vMin: Double = wootOffer.Items[0].SalePrice.toDouble()
        var vMax: Double = wootOffer.Items[0].SalePrice.toDouble()

        for (i in wootOffer.Items) {
            if (vMin > i.SalePrice.toDouble()) vMin = i.SalePrice.toDouble()
            if (vMax < i.SalePrice.toDouble()) vMax = i.SalePrice.toDouble()
        }

        val fMin = String.format("%.02f", vMin)
        val fMax = String.format("%.02f", vMax)

        var vReturn = if (vMin != vMax) {
            "$$fMin - $$fMax"
        } else {
            "$$fMax"
        }

        return if (wootOffer.SoldOut) {
            "SOLD OUT !!! - " + vReturn
        } else {
            vReturn
        }
    }catch (e: Exception){
        return ""
    }
}

fun getMaxVotes(poll: ModelMehPoll): Int {
    var vMax = poll.answers[0].voteCount

    for (a in poll.answers) {
        if (vMax < a.voteCount) vMax = a.voteCount
    }

    return vMax
}

//fun showNotification(pContext: Context, vNotification_TickerText: String, vNotification_Title: String, vNotification_Text: String, vShow_Action_Right_Button_Icon_INT: Int, vShow_Action_Left_Button_Icon_INT: Int, vShow_Large_Icon_Bitmap: Bitmap, vShow_Small_Icon_Int: Int, vBuyURL: String, vNotificationID: Int) {
fun showNotification(pContext: Context, vNotification_TickerText: String, vNotification_Title: String, vNotification_Text: String, vShow_Action_Right_Button_Icon_INT: Int, vShow_Action_Left_Button_Icon_INT: Int, vShow_Small_Icon_Int: Int, vBuyURL: String, vNotificationID: Int) {
    // new --- start
    try {
        // Gets a PendingIntent containing the entire back stack
        // define what activity should appear when the user clicks the notification

        // details button activity
        var vIntentShowActivity = Intent(pContext, ActivityMain::class.java)

        // buy button activity
        var vIntentShowActivity2 = Intent(pContext, ActivityMain::class.java)
        vIntentShowActivity2 .putExtra(ACT_EXTRA_GO_TO_SITE_URL, vBuyURL)

        // Android Wear
        var vIntent_AndroidWear = Intent(pContext, ActivityMain::class.java)
//        /*** used to cancel notifications on notification action button press */
//        vIntent_AndroidWear.putExtra(NOTIFICATION_ID.toString(), NOTIFICATION_ID)

        // Because clicking the notification opens a new ("special") activity, there's
        // no need to create an artificial back stack.
        // details button
        var vPendingIntent = PendingIntent.getActivity(
                pContext,
                0,
                vIntentShowActivity,
                PendingIntent.FLAG_UPDATE_CURRENT
        )

        // buy button
        var vPendingIntent2 = PendingIntent.getActivity(
                pContext,
                0,
                vIntentShowActivity2,
                PendingIntent.FLAG_UPDATE_CURRENT
        )

        // android wear button
        var pPendingIntent_AndroidWear = PendingIntent.getActivity(
                pContext, 0, vIntent_AndroidWear, PendingIntent.FLAG_UPDATE_CURRENT
        )

        // android wear button
        var vNotification_Action = NotificationCompat.Action.Builder(
                vShow_Small_Icon_Int, vNotification_Title, pPendingIntent_AndroidWear
        ).build()

        // Play sound
        var pNotification_Sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        //  BigPictureStyle --- start
        var pNotification_Big_Picture_Style = NotificationCompat.BigPictureStyle()
//        pNotification_Big_Picture_Style.bigLargeIcon(vShow_Large_Icon_Bitmap)
//        pNotification_Big_Picture_Style.bigPicture(vShow_Large_Icon_Bitmap)
//                    if (expandedIconUrl != null) {
//                        pNotification_Big_Picture_Style.bigLargeIcon(Picasso.with(context).load(expandedIconUrl).get());
//                    } else if (expandedIconResId > 0) {
//                        pNotification_Big_Picture_Style.bigLargeIcon(BitmapFactory.decodeResource(context.getResources(), expandedIconResId));
//                    } // if

        var pNotification_Build = NotificationCompat.Builder(pContext)
                .setTicker(vNotification_TickerText)  //  vResources.getString( R.string.polling_new_item_title ) )
                .setContentTitle(vNotification_Title) // vResources.getString( R.string.polling_new_item_title ) )
                .setContentText(vNotification_Text)
//                .setLargeIcon(vShow_Large_Icon_Bitmap) // pPicasso_Image )
                .setSmallIcon(R.mipmap.ic_launcher) // vShow_Small_Icon_Int) //   R.drawable.logo_32_x_32_2)
                .setContentIntent(vPendingIntent)

                /*** VIBRATE Lights ETC
                 * ADD PERMISSION TO MANIFEST
                 *      <!-- Used for START Polling Service on StartUp USE -->
                 *      < uses - permission android:name="android.permission.VIBRATE" />
                 *
                 *      before api 26 color and vibrate are deprecated and changed
                 *      https://medium.com/exploring-android/exploring-android-o-notification-channels-94cd274f604c
                 */
                .setDefaults(Notification.DEFAULT_ALL)
//                    .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE)

                // Play sound
                //            https://www.youtube.com/watch?v=WZX4ovWDzpI
                .setSound(pNotification_Sound)

                //  BigPictureStyle
//                .setStyle(pNotification_Big_Picture_Style)

                // Android Wear --- start
                .extend(NotificationCompat.WearableExtender().addAction(vNotification_Action))

                .setAutoCancel(true)

                .setDefaults(Notification.DEFAULT_ALL)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))

//                .addAction(vShow_Action_Left_Button_Icon_INT, "Details", vPendingIntent)
//                .addAction(vShow_Action_Right_Button_Icon_INT, "Buy", vPendingIntent2)

        var vNotification_Show = pNotification_Build.build()
        var vNotificationManager = pContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        vNotificationManager.notify(vNotificationID, vNotification_Show)
    } catch (e: Exception) {
        println("Util - showNotification - error: ")
    }

}