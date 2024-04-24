package com.webuy.android_startup.model

import com.webuy.android_startup.IJob

/**
 * Created by idisfkj on 2020/8/10.
 * Email: idisfkj@gmail.com.
 */
data class TimeModel(
    val job: IJob<*>,
    val startTime: Long,
    var endTime: Long = 0L
)
