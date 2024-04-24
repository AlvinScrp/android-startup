package com.rousetime.sample

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ProcessLifecycleOwner
import com.webuy.android_startup.model.TimeModel

/**
 * Created by idisfkj on 2020/7/24.
 * Email: idisfkj@gmail.com.
 */
class SampleApplication : Application() {


    companion object {
        const val TAG = "SampleApplication"

        @JvmStatic
        lateinit var context: Context

        @JvmStatic
        lateinit var app: SampleApplication

        // only in order to test on MainActivity.
        val costTimesLiveData = MutableLiveData<List<TimeModel>>()
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        context = base!!
        app = this
    }

    override fun onCreate() {
        super.onCreate()

//        registerProcessLifecycleObserver()
//
//        registerActivityLifeCycleCallBack()

    }

    fun registerProcessLifecycleObserver() {
        ProcessLifecycleOwner.get().lifecycle.addObserver(object : DefaultLifecycleObserver {
            override fun onCreate(owner: LifecycleOwner) {
                super.onCreate(owner)
                Log.d("alvin", "ProcessLifecycleOwner onCreate thread:${Thread.currentThread()}")
            }

            override fun onStart(owner: LifecycleOwner) {
                super.onStart(owner)
                Log.d("alvin", "ProcessLifecycleOwner onStart thread:${Thread.currentThread()}")
            }

            override fun onStop(owner: LifecycleOwner) {
                super.onStop(owner)
                Log.d("alvin", "ProcessLifecycleOwner onStop thread:${Thread.currentThread()}")
            }

            override fun onDestroy(owner: LifecycleOwner) {
                super.onDestroy(owner)
                Log.d("alvin", "ProcessLifecycleOwner onDestroy thread:${Thread.currentThread()}")
            }
        })
    }

    fun registerActivityLifeCycleCallBack() {
        app.registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                Log.d("alvin", "onActivityCreated activity:${activity} thread:${Thread.currentThread()}")
            }

            override fun onActivityStarted(activity: Activity) {
                Log.d("alvin", "onActivityCreated activity:${activity} thread:${Thread.currentThread()}")
            }

            override fun onActivityResumed(activity: Activity) {}

            override fun onActivityPaused(activity: Activity) {}

            override fun onActivityStopped(activity: Activity) {
                Log.d("alvin", "onActivityStopped activity:${activity} thread:${Thread.currentThread()}")
            }

            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}

            override fun onActivityDestroyed(activity: Activity) {}
        })
    }

}