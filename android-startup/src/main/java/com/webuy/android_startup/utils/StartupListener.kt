package com.webuy.android_startup.utils

import com.webuy.android_startup.model.TimeModel

/**
 * Created by idisfkj on 2020/8/10.
 * Email: idisfkj@gmail.com.
 */
interface StartupListener {

    /**
     * call when all startup completed.
     * @param costTimeMillisSecond cost times of main thread.
     * @param models list of cost times for every startup.
     */
    fun onCompleted(costTimeMillisSecond: Long, models: List<TimeModel>)
}

