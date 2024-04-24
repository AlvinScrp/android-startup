package com.webuy.android_startup.model

import com.webuy.android_startup.IJob

/**
 * Created by idisfkj on 2020/7/27.
 * Email: idisfkj@gmail.com.
 */
data class StartupSortStore(
    val result: MutableList<IJob<*>>,
    val jobMap: Map<String, IJob<*>>,
    val startupChildrenMap: Map<String, MutableList<String>>
)
