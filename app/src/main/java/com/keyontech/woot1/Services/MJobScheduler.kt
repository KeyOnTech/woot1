package com.keyontech.woot1.Services


import android.app.job.JobParameters
import android.app.job.JobService
import com.keyontech.woot1.Models.modelJobExecuterAsyncTaskParams

/**
 * Created by kot on 3/12/18.
 *
 */

class MJobScheduler : JobService() {
    lateinit var mJobExecuter: MJobExecuter

    override fun onStartJob(jobParameters: JobParameters): Boolean
    {
        println("MJobScheduler  - onStartJob  ")
        var params = modelJobExecuterAsyncTaskParams()
        params.cContext = applicationContext
        mJobExecuter = object : MJobExecuter() {
            override fun onPostExecute(s: String) {
                println("MJobScheduler  - onStartJob  -  onPostExecute")
                jobFinished(jobParameters, false)
            }
        }

        mJobExecuter.execute(params)
        return true
    }

    override fun onStopJob(jobParameters: JobParameters): Boolean {
        println("MJobScheduler  - onStopJob")
        mJobExecuter.cancel(true)
        return false
    }
}