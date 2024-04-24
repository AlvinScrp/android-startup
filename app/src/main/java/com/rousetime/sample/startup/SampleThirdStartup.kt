package com.rousetime.sample.startup

import android.content.Context
import android.util.Log
import com.webuy.android_startup.AndroidJob
import com.webuy.android_startup.IJob

/**
 * Created by idisfkj on 2020/7/24.
 * Email: idisfkj@gmail.com.
 */
class SampleThirdStartup : AndroidJob<Long>() {

    override fun runOnMainThread(): Boolean = false

    override fun blockMainThread(): Boolean = false

    override fun call(context: Context): Long? {
        Thread.sleep(3000)
        return 10L
    }

    override fun dependenciesByName(): List<String> {
        return listOf(
            "com.rousetime.sample.startup.SampleFirstStartup",
            "com.rousetime.sample.startup.SampleSecondStartup"
        )
    }

    override fun onDependenciesCompleted(job: IJob<*>, result: Any?) {
        Log.d("SampleThirdStartup", "onDependenciesCompleted: ${IJob::class.java.simpleName}, $result")
    }
}