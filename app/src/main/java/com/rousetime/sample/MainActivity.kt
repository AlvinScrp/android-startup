package com.rousetime.sample

import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.webuy.android_startup.utils.StartupListener
import com.webuy.android_startup.AppStartup
import com.webuy.android_startup.model.StartupConfig
import com.webuy.android_startup.model.TimeModel
import com.rousetime.sample.job.Job1
import com.rousetime.sample.job.Job2
import com.rousetime.sample.job.Job3
import com.rousetime.sample.job.Job4

class MainActivity : AppCompatActivity(), StartupListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        AppStartup.Builder()
            .setConfig(StartupConfig(logEnable = true, listener = this@MainActivity))
            .addAllStartup(listOf(Job1(), Job2(), Job3(), Job4()))
            .build(this)
            .start()
            .await()
        Log.d("alvin", "after start")

        Handler().postDelayed({
            SampleApplication.app.registerProcessLifecycleObserver()
            SampleApplication.app.registerActivityLifeCycleCallBack()
        }, 3000)

        Log.d("alvin", " Job1().name:${ Job1().name()}")


    }

    override fun onCompleted(costTime: Long, models: List<TimeModel>) {
        Log.d("alvin", "onCompleted: costTime->${costTime}, size:${models.size}")
    }
}