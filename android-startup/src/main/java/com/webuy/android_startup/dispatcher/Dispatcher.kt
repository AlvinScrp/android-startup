package com.webuy.android_startup.dispatcher

import com.webuy.android_startup.IJob
import com.webuy.android_startup.model.StartupSortStore

/**
 * Created by idisfkj on 2020/7/27.
 * Email: idisfkj@gmail.com.
 */
interface Dispatcher {

    /**
     * dispatch prepare
     */
    fun prepare()

    /**
     * dispatch startup to executing.
     */
    fun dispatch(job: IJob<*>, sortStore: StartupSortStore)

    /**
     * notify children when dependency startup completed.
     */
    fun notifyChildren(dependencyParent: IJob<*>, result: Any?, sortStore: StartupSortStore)
}