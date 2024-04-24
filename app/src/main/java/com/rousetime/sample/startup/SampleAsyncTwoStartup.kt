package com.rousetime.sample.startup

import android.content.Context
import com.webuy.android_startup.AndroidJob

/**
 * Created by idisfkj on 2020/8/17.
 * Email: idisfkj@gmail.com.
 */
class SampleAsyncTwoStartup: AndroidJob<String>() {

    override fun call(context: Context): String? {
        Thread.sleep(3000)
        return "async two"
    }

    override fun runOnMainThread(): Boolean = false

    override fun blockMainThread(): Boolean = false
}