package com.webuy.android_startup.utils

import androidx.core.os.TraceCompat
import com.webuy.android_startup.IJob
import com.webuy.android_startup.model.StartupSortStore
import java.util.*

/**
 * Created by idisfkj on 2020/7/24.
 * Email: idisfkj@gmail.com.
 */
internal object TopologySort {

    fun sort(jobList: List<IJob<*>>): StartupSortStore {
        TraceCompat.beginSection(TopologySort.javaClass.simpleName)

        val mainResult = mutableListOf<IJob<*>>()
        val ioResult = mutableListOf<IJob<*>>()
        val temp = mutableListOf<IJob<*>>()
        val jobMap = hashMapOf<String, IJob<*>>()
        val zeroDeque = ArrayDeque<String>()
        val startupChildrenMap = hashMapOf<String, MutableList<String>>()
        val inDegreeMap = hashMapOf<String, Int>()

        jobList.forEach {
            val uniqueKey = it.name()
            if (!jobMap.containsKey(uniqueKey)) {
                jobMap[uniqueKey] = it
                // save in-degree
                inDegreeMap[uniqueKey] = it.getDependenciesCount()
                if (it.dependenciesByName().isNullOrEmpty()) {
                    zeroDeque.offer(uniqueKey)
                } else {
                    // add key parent, value list children
                    it.dependenciesByName()?.forEach { parent ->
                        if (startupChildrenMap[parent] == null) {
                            startupChildrenMap[parent] = arrayListOf()
                        }
                        startupChildrenMap[parent]?.add(uniqueKey)
                    }
                }
            } else {
                throw IllegalStateException("$it multiple add.")
            }
        }

        while (!zeroDeque.isEmpty()) {
            zeroDeque.poll()?.let {
                jobMap[it]?.let { androidStartup ->
                    temp.add(androidStartup)
                    // add zero in-degree to result list
                    if (androidStartup.runOnMainThread()) {
                        mainResult.add(androidStartup)
                    } else {
                        ioResult.add(androidStartup)
                    }
                }
                startupChildrenMap[it]?.forEach { children ->
                    inDegreeMap[children] = inDegreeMap[children]?.minus(1) ?: 0
                    // add zero in-degree to deque
                    if (inDegreeMap[children] == 0) {
                        zeroDeque.offer(children)
                    }
                }
            }
        }

        if (mainResult.size + ioResult.size != jobList.size) {
            throw IllegalStateException("lack of dependencies or have circle dependencies.")
        }

        val result = mutableListOf<IJob<*>>().apply {
            addAll(ioResult)
            addAll(mainResult)
        }
        printResult(temp)

        TraceCompat.endSection()

        return StartupSortStore(
            result,
            jobMap,
            startupChildrenMap
        )
    }

    private fun printResult(result: List<IJob<*>>) {
        StartupLog.d {
            buildString {
                append("TopologySort result:  |================================================================\n")
                result.forEachIndexed { index, it ->
                    append(
                        String.format(
                            "|  [%3d]  | %50s  | Dependencies(%2d)  | runOnMain--%5s |  block--%5s \n",
                            index + 1,
                            it.javaClass.simpleName,
                            it.getDependenciesCount(),
                            it.runOnMainThread(),
                            it.blockMainThread()
                        )
                    )
                }
            }
        }
    }
}