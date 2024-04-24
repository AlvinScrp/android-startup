package com.webuy.android_startup

import com.webuy.android_startup.executor.ExecutorManager
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executor

/**
 * Created by idisfkj on 2020/7/23.
 * Email: idisfkj@gmail.com.
 */
abstract class AndroidJob<T> : IJob<T> {

    private val mWaitCountDown by lazy { CountDownLatch(getDependenciesCount()) }
    private val mObservers by lazy { mutableListOf<IJob<*>>() }

    override fun name(): String = this.javaClass.name


    override fun tryWait() {
        try {
            mWaitCountDown.await()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }

    override fun tryNotify() {
        mWaitCountDown.countDown()
    }

    override fun createExecutor(): Executor = ExecutorManager.instance.ioExecutor

    override fun dependenciesByName(): List<String>? = null

    override fun getDependenciesCount(): Int = dependenciesByName()?.size ?: 0

    override fun onDependenciesCompleted(job: IJob<*>, result: Any?) {}

    override fun manualDispatch(): Boolean = false

    override fun registerDispatcher(dispatcher: IJob<*>) {
        mObservers.add(dispatcher)
    }

    override fun onDispatch() {
        mObservers.forEach { it.tryNotify() }
    }
}