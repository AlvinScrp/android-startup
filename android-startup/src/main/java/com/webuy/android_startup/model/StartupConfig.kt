package com.webuy.android_startup.model

import com.webuy.android_startup.utils.StartupListener

/**
 * Created by idisfkj on 2020/7/31.
 * Email: idisfkj@gmail.com.
 */
class StartupConfig(
    val logEnable: Boolean = false,
    val awaitTimeout: Long = 12000L,
    val listener: StartupListener? = null,
)
