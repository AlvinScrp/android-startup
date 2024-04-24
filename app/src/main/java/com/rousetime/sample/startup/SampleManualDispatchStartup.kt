package com.rousetime.sample.startup

import android.content.Context
import com.webuy.android_startup.AndroidJob

/**
 * Created by idisfkj on 2020/8/18.
 * Email: idisfkj@gmail.com.
 */
class SampleManualDispatchStartup : AndroidJob<String>() {

    override fun call(context: Context): String? {
        Thread {
            Thread.sleep(2000)
            // manual dispatch
            onDispatch()
        }.start()
        return "manual dispatch"
    }

    override fun runOnMainThread(): Boolean = true

    override fun blockMainThread(): Boolean = false

    override fun manualDispatch(): Boolean = true

}