package com.keyontech.woot1.Services

/**
 * Created by kot on 3/12/18.
 */


import android.content.Context
import android.graphics.Bitmap
import android.os.AsyncTask
import android.util.Log
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.keyontech.woot1.Models.JSONUrL
import com.keyontech.woot1.Models.ModelWootDeal
import com.keyontech.woot1.Models.modelJobExecuterAsyncTaskParams
import com.keyontech.woot1.R
import com.keyontech.woot1.Utils.*
import com.squareup.picasso.Picasso
import org.json.JSONException
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.nio.charset.Charset

open class MJobExecuter : AsyncTask<modelJobExecuterAsyncTaskParams, Void, String>() {
    /*** this is used for the notification large image */
    private var mehNotificationLargePhoto = ""

    override fun doInBackground(vararg params: modelJobExecuterAsyncTaskParams?): String? {
        var vContext = params[0]!!.cContext
        var vURL = params[0]!!.dealUrl

        val uString = fetchJSON_AsyncTask(vContext, vURL)
        return uString + "MJobExecuter - Background long running task finishes..."
    }

    @Throws(IOException::class)
    fun fetchJSON_AsyncTask(pContext: Context, pDealUrl: String = ""): String {
        var gson = GsonBuilder().serializeNulls().create() // include null opjects when null
        var urlFile = loadJsonFromFile("url.json", pContext)
        var jsonOutput = gson.fromJson( urlFile , JSONUrL::class.java )
//        var jsonURL = jsonOutput.mehurl
        var jsonURL = jsonOutput.allwooturl

        // jsonURL
        var jsonResponse = ""
        var urlConnection: HttpURLConnection? = null
        var inputStream: InputStream? = null

        try {
            var siteURL = createUrl2(jsonURL)

            urlConnection = siteURL?.openConnection() as HttpURLConnection
            urlConnection.requestMethod = "GET"
            urlConnection.readTimeout = 10000
            urlConnection.connectTimeout = 15000
            urlConnection.connect()

            inputStream = urlConnection.inputStream
            jsonResponse = readFromStream(inputStream)
//                println("666ccc    jsonResponse = " + jsonResponse )
            processReturn2(pContext, jsonResponse)
        } catch (e: IOException) {
            //
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect()
            }

            if (inputStream != null) {
                inputStream.close()
            }
        }
        return jsonResponse
    }


    fun createUrl2(stringUrl: String): URL? {
        var url: URL? = null
        try {
            url = URL(stringUrl)
        } catch (exception: MalformedURLException) {
            Log.e("createUrl", "Error with creating URL", exception)
            return null
        }

        return url
    }

    /**
     * Convert the [InputStream] into a String which contains the
     * whole JSON response from the server.
     */
    @Throws(IOException::class)
    fun readFromStream(inputStream: InputStream?): String {
        val output = StringBuilder()
        if (inputStream != null) {
            val inputStreamReader = InputStreamReader(inputStream, Charset.forName("UTF-8"))
            val reader = BufferedReader(inputStreamReader)
            var line = reader.readLine()
            while (line != null) {
                output.append(line)
                line = reader.readLine()
            }
        }
        return output.toString()
    }


    fun processReturn2(pContext: Context, jsonResponse: String){
        var dealUrl = ""
        var notificationLargePhoto = ""

        var notificationText = ""

        try {
            if (jsonResponse != null && jsonResponse.isNotEmpty()) {
                notificationText = wootNotificaitionText(pContext, jsonResponse)

                if (isNewDeal(pContext, notificationText)) {
                    /*** DONT show notification first time opening app */
                    if (showJobNotification(pContext)) {
                        /*** show notification */
                        createNotification(
                                pContext
                                , JS_SCHEDULE_NOTIFICATION_TITLE
                                , JS_SCHEDULE_NOTIFICATION_TITLE
                                , notificationText
                                , R.mipmap.ic_launcher
                                , R.mipmap.ic_launcher
                                , notificationLargePhoto
                                , R.mipmap.ic_launcher
                                , dealUrl
                        )
                    }
                }
            } else {
                /*** no internet response */
//                noInternetResponse()
            }
        } catch (e: JSONException) {
//            printToErrorLog_10("ActivityMain", "runOnUiThread try")
        }
    }

    fun createNotification( pContext: Context, tickerText: String = "", notificationTitle: String = "", notificationText: String, showactionRightButtonIcon: Int, showactionLeftButtonIcon: Int, largebitmapImageURL : String, smallIcon : Int, dealUrl: String) {
        var notificationLargeBitmap: Bitmap? = null
//        try {
//            notificationLargeBitmap  = Picasso
//                    .with(pContext)
//                    .load(largebitmapImageURL)
//                    .resize(512,512)
//                    .placeholder(R.drawable.ic_failed_to_load_image)
//                    .error(R.drawable.ic_failed_to_load_image)
//                    .get()

            showNotification(
                    pContext
                    ,tickerText
                    ,notificationTitle
                    ,notificationText
                    ,showactionRightButtonIcon
                    ,showactionLeftButtonIcon
//                    ,notificationLargeBitmap
                    ,smallIcon
                    ,dealUrl
                    ,NOTIFICATION_ID
            )
//        } catch (e: IOException) {
//            e.printStackTrace()
//        } finally {
//            if (notificationLargeBitmap != null) {
////
//            }
//        }
    }

}