package com.rousetime.sample.startup

import android.content.Context
import com.webuy.android_startup.AndroidJob

/**
 * Created by idisfkj on 2020/7/24.
 * Email: idisfkj@gmail.com.
 */
class SampleFourthStartup : AndroidJob<Any>() {

    override fun runOnMainThread(): Boolean = false

    override fun blockMainThread(): Boolean = false

    override fun call(context: Context): Any? {
        Thread.sleep(100)
        return null
    }

    override fun dependenciesByName(): List<String> {
        return listOf(
            "com.rousetime.sample.startup.SampleFirstStartup",
            "com.rousetime.sample.startup.SampleSecondStartup",
            "com.rousetime.sample.startup.SampleThirdStartup"
        )
    }
}