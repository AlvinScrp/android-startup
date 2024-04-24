package com.rousetime.sample.startup

import android.content.Context
import com.webuy.android_startup.AndroidJob

/**
 * Created by idisfkj on 2020/7/24.
 * Email: idisfkj@gmail.com.
 */
class SampleFirstStartup : AndroidJob<String>() {

    override fun runOnMainThread(): Boolean = true

    override fun blockMainThread(): Boolean = false

    override fun call(context: Context): String? {
        return this.javaClass.simpleName
    }

}