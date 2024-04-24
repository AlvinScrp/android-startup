package com.rousetime.sample.startup

import android.content.Context
import com.webuy.android_startup.AndroidJob

/**
 * Created by idisfkj on 2020/8/18.
 * Email : idisfkj@gmail.com.
 */
class SampleSyncThreeStartup : AndroidJob<String>() {
    override fun runOnMainThread(): Boolean = true

    override fun call(context: Context): String? {
        return "sync three"
    }

    override fun blockMainThread(): Boolean = false
}