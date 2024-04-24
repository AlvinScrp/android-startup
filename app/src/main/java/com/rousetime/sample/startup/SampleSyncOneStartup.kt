package com.rousetime.sample.startup

import android.content.Context
import com.webuy.android_startup.AndroidJob

/**
 * Created by idisfkj on 2020/8/17.
 * Email: idisfkj@gmail.com.
 */
class SampleSyncOneStartup: AndroidJob<String>() {

    override fun call(context: Context): String? {
        return "sync one"
    }

    override fun runOnMainThread(): Boolean = true

    override fun blockMainThread(): Boolean = false
}