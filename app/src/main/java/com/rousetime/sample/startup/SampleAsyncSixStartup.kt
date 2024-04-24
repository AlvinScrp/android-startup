package com.rousetime.sample.startup

import android.content.Context
import com.webuy.android_startup.AndroidJob

/**
 * Created by idisfkj on 2020/8/18.
 * Email : idisfkj@gmail.com.
 */
class SampleAsyncSixStartup: AndroidJob<String>() {

    override fun runOnMainThread(): Boolean = false

    override fun call(context: Context): String? {
        Thread.sleep(2000)
        return "async six"
    }

    override fun blockMainThread(): Boolean = false

}