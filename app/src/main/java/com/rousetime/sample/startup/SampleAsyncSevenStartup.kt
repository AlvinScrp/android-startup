package com.rousetime.sample.startup

import android.content.Context
import com.webuy.android_startup.AndroidJob
import com.webuy.android_startup.IJob

/**
 * Created by idisfkj on 2020/8/18.
 * Email: idisfkj@gmail.com.
 */
class SampleAsyncSevenStartup : AndroidJob<String>() {

    private var mResult: String? = null

    override fun call(context: Context): String? {
        Thread.sleep(3000)
        return "$mResult + async seven"
    }

    override fun runOnMainThread(): Boolean = false

    override fun blockMainThread(): Boolean = false

    override fun dependenciesByName(): List<String> {
        return listOf("com.rousetime.sample.startup.SampleManualDispatchStartup")
    }

    override fun onDependenciesCompleted(job: IJob<*>, result: Any?) {
        mResult = result as? String?
    }
}