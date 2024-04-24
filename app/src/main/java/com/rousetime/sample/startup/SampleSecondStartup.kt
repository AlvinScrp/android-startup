package com.rousetime.sample.startup

import android.content.Context
import android.util.Log
import com.webuy.android_startup.AndroidJob
import com.webuy.android_startup.IJob
import com.webuy.android_startup.executor.ExecutorManager
import java.util.concurrent.Executor

/**
 * Created by idisfkj on 2020/7/24.
 * Email: idisfkj@gmail.com.
 */
class SampleSecondStartup : AndroidJob<Boolean>() {

    override fun runOnMainThread(): Boolean = false

    override fun blockMainThread(): Boolean = true

    override fun call(context: Context): Boolean {
        Thread.sleep(5000)
        return true
    }

    override fun createExecutor(): Executor {
        return ExecutorManager.instance.cpuExecutor
    }

    override fun dependenciesByName(): List<String> {
        return listOf("com.rousetime.sample.startup.SampleFirstStartup")
    }

    override fun onDependenciesCompleted(job: IJob<*>, result: Any?) {
        Log.d("SampleSecondStartup", "onDependenciesCompleted: ${IJob::class.java.simpleName}, $result")
    }
}