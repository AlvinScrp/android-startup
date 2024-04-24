package com.rousetime.sample.startup

import android.content.Context
import com.webuy.android_startup.AndroidJob
import com.webuy.android_startup.IJob

/**
 * Created by idisfkj on 2020/8/18.
 * Email : idisfkj@gmail.com.
 */
class SampleAsyncFourStartup : AndroidJob<String>() {

    private var mResult: String? = null

    override fun runOnMainThread(): Boolean = false

    override fun call(context: Context): String? {
        Thread.sleep(1000)
        return "$mResult + async four"
    }

    override fun blockMainThread(): Boolean = true

    override fun dependenciesByName(): List<String> {
        return listOf("com.rousetime.sample.startup.SampleAsyncSixStartup")
    }

    override fun onDependenciesCompleted(job: IJob<*>, result: Any?) {
        mResult = result as? String?
    }
}