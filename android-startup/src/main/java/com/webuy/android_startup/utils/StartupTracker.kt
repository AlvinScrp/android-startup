package com.webuy.android_startup.utils

import androidx.core.os.TraceCompat
import com.webuy.android_startup.IJob
import com.webuy.android_startup.AppStartup
import com.webuy.android_startup.model.TimeModel
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.CopyOnWriteArrayList

/**
 * Created by idisfkj on 2020/8/10.
 * Email: idisfkj@gmail.com.
 */
class StartupTracker {
    private var timeMap = ConcurrentHashMap<IJob<*>, TimeModel>()
    private val list = CopyOnWriteArrayList<IJob<*>>()

    companion object {
        private const val ACCURACY = 1000 * 1000L
    }

    private var startTime = 0L
    var endTime: Long? = null

    private val mainThreadTimes get() = (endTime ?: System.nanoTime()) - startTime

    val mainCostMillis get() = mainThreadTimes / ACCURACY

    fun mainStart(manager: AppStartup) {
        TraceCompat.beginSection(manager.javaClass.simpleName)
        startTime = System.nanoTime()
    }

    fun mainEnd() {
        endTime = System.nanoTime()
        TraceCompat.endSection()
    }

    fun recordStart(job: IJob<*>) {
        StartupLog.d { "${job.name()} being create." }
        TraceCompat.beginSection(job.name())
        list.add(job)
        timeMap[job] = TimeModel(job, System.nanoTime() / ACCURACY, -1)
    }

    fun recordEnd(job: IJob<*>) {
        TraceCompat.endSection()
        StartupLog.d { "${job.name()} was completed." }
        timeMap[job]?.endTime = System.nanoTime() / ACCURACY
    }

    fun clear() {
        endTime = null
        timeMap.clear()
        list.clear()
    }

    fun printAll() {
        StartupLog.d {
            buildString {
                append("| ==== startup cost times: ===================\n")
                list.forEach {
                    val cost = timeMap[it]?.let { time -> time.endTime - time.startTime }
                    append(
                        String.format(
                            "|  %4d ms  | %40s  | runOnMain--%5s |  block--%5s \n", cost,
                            it.javaClass.simpleName,
                            it.runOnMainThread(),
                            it.blockMainThread()
                        )
                    )
                }
                append("|========= main-total: ${mainThreadTimes / ACCURACY} ms =======================")
            }
        }
    }

    fun getTimeModels(): List<TimeModel> {
        return list.mapNotNull { timeMap[it] }.toList()
    }


}