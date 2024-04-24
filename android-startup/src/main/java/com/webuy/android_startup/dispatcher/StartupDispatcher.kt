package com.webuy.android_startup.dispatcher

import android.content.Context
import com.webuy.android_startup.IJob
import com.webuy.android_startup.utils.StartupListener
import com.webuy.android_startup.executor.ExecutorManager
import com.webuy.android_startup.model.StartupSortStore
import com.webuy.android_startup.utils.StartupTracker
import com.webuy.android_startup.utils.StartupLog
import java.util.concurrent.CountDownLatch
import java.util.concurrent.atomic.AtomicInteger

/**
 * Created by idisfkj on 2020/8/14.
 * Email: idisfkj@gmail.com.
 */
internal class StartupDispatcher(
    private val context: Context,
    private val needAwaitCount: AtomicInteger,
    private val awaitCountDownLatch: CountDownLatch?,
    private val startupSize: Int,
    private val listener: StartupListener?,
    private val tracker: StartupTracker?
) : Dispatcher {

    private var count: AtomicInteger? = null

    override fun prepare() {
        count = AtomicInteger()
        tracker?.clear()
    }

    override fun dispatch(job: IJob<*>, sortStore: StartupSortStore) {
        StartupLog.d { "${job::class.java.simpleName} being dispatching, onMainThread ${job.runOnMainThread()}." }

        val runnable = Runnable {
            job.tryWait()
            tracker?.recordStart(job)
            val result = job.call(context)
            tracker?.recordEnd(job)
            notifyChildren(job, result, sortStore)
        }
        if (!job.runOnMainThread()) {
            job.createExecutor().execute(runnable)
        } else {
            runnable.run()
        }
    }

    override fun notifyChildren(dependencyParent: IJob<*>, result: Any?, sortStore: StartupSortStore) {
        // immediately notify main thread,Unblock the main thread.
        if (dependencyParent.blockMainThread() && !dependencyParent.runOnMainThread()) {
            needAwaitCount.decrementAndGet()
            awaitCountDownLatch?.countDown()
        }

        sortStore.startupChildrenMap[dependencyParent.name()]?.forEach {
            sortStore.jobMap[it]?.run {
                onDependenciesCompleted(dependencyParent, result)

                if (dependencyParent.manualDispatch()) {
                    dependencyParent.registerDispatcher(this)
                } else {
                    tryNotify()
                }
            }
        }
        val size = count?.incrementAndGet() ?: 0
        if (size == startupSize) {
            tracker?.printAll()
            listener?.let {
                tracker?.let {
                    val timeModels = tracker.getTimeModels()
                    val mainCostMillis = tracker.mainCostMillis
                    ExecutorManager.instance.mainExecutor.execute {
                        listener.onCompleted(mainCostMillis, timeModels)
                    }
                }

            }
        }
    }
}