package com.webuy.android_startup

import android.content.Context
import java.util.concurrent.Executor

/**
 * Created by idisfkj on 2020/7/23.
 * Email: idisfkj@gmail.com.
 */
interface IJob<T> {
    //    val name:String
    fun name(): String
    fun call(context: Context): T?

    fun dependenciesByName(): List<String>?

    fun getDependenciesCount(): Int

    fun onDependenciesCompleted(job: IJob<*>, result: Any?)

    fun manualDispatch(): Boolean

    fun registerDispatcher(dispatcher: IJob<*>)

    /**
     * Start to dispatch when [manualDispatch] return true.
     */
    fun onDispatch()

    /**
     * Return true call the create function on main thread otherwise false.
     */
    fun runOnMainThread(): Boolean

    /**
     * Return true block the main thread until the startup completed otherwise false.
     *
     * Note: If the function [runOnMainThread] return true, main thread default block.
     */
    fun blockMainThread(): Boolean

    /**
     * To wait dependencies startup completed.
     */
    fun tryWait()

    /**
     * To notify the startup when dependencies startup completed.
     */
    fun tryNotify()

    fun createExecutor(): Executor


}