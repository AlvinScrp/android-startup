package com.webuy.android_startup

import android.content.Context
import android.os.Looper
import androidx.core.os.TraceCompat
import com.webuy.android_startup.dispatcher.StartupDispatcher
import com.webuy.android_startup.model.StartupConfig
import com.webuy.android_startup.model.StartupSortStore
import com.webuy.android_startup.utils.TopologySort
import com.webuy.android_startup.utils.StartupTracker
import com.webuy.android_startup.utils.StartupLog
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger

/**
 * Created by idisfkj on 2020/7/24.
 * Email : idisfkj@gmail.com.
 */
class AppStartup private constructor(
    private val context: Context,
    private val startupList: List<AndroidJob<*>>,
    private val needAwaitCount: AtomicInteger,
    private val config: StartupConfig,
    private val tracker: StartupTracker = StartupTracker()
) {

    private var mAwaitCountDownLatch: CountDownLatch? = null

    init {
        StartupLog.enable = config.logEnable
    }

    fun start() = apply {
        if (Looper.getMainLooper() != Looper.myLooper()) {
            throw IllegalStateException("start method must be call in MainThread.")
        }

        if (mAwaitCountDownLatch != null) {
            throw IllegalStateException("start method repeated call.")
        }
        mAwaitCountDownLatch = CountDownLatch(needAwaitCount.get())

        if (startupList.isNullOrEmpty()) {
            StartupLog.e { "startupList is empty in the current process." }
            return@apply
        }

        tracker.mainStart(this)
        TopologySort.sort(startupList).run { execute(this) }
        if (needAwaitCount.get() <= 0) {
            tracker.mainEnd()
        }
    }

    private fun execute(sortStore: StartupSortStore) {
        mDefaultManagerDispatcher.prepare()
        sortStore.result.forEach { mDefaultManagerDispatcher.dispatch(it, sortStore) }
    }

    /**
     * Startup dispatcher
     */
    private val mDefaultManagerDispatcher by lazy {
        StartupDispatcher(
            context,
            needAwaitCount,
            mAwaitCountDownLatch,
            startupList.size,
            config.listener,
            tracker
        )
    }

    /**
     * to await startup completed
     * block main thread.
     */
    fun await() {
        if (mAwaitCountDownLatch == null) {
            throw IllegalStateException("must be call start method before call await method.")
        }

        val count = needAwaitCount.get()
        try {
            mAwaitCountDownLatch?.await(config.awaitTimeout, TimeUnit.MILLISECONDS)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }

        if (count > 0) {
            tracker.endTime = System.nanoTime()
            TraceCompat.endSection()
        }
    }

    class Builder {
        private var mStartupList = mutableListOf<AndroidJob<*>>()
        private var mNeedAwaitCount = AtomicInteger()

        private var mConfig: StartupConfig? = null

        fun addStartup(startup: AndroidJob<*>) = apply {
            mStartupList.add(startup)
        }

        fun addAllStartup(list: List<AndroidJob<*>>) = apply {
            list.forEach {
                addStartup(it)
            }
        }

        fun setConfig(config: StartupConfig?) = apply {
            mConfig = config
        }

        fun build(context: Context): AppStartup {
            val realStartupList = mutableListOf<AndroidJob<*>>()
            mStartupList.forEach {
                realStartupList.add(it)
                if (it.blockMainThread() && !it.runOnMainThread()) {
                    mNeedAwaitCount.incrementAndGet()
                }
            }

            return AppStartup(
                context,
                realStartupList,
                mNeedAwaitCount,
                mConfig ?: StartupConfig()
            )
        }
    }

}