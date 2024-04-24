package com.rousetime.sample

import android.content.ComponentName
import android.content.ServiceConnection
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.webuy.android_startup.AndroidJob
import com.webuy.android_startup.AppStartup
//import com.rousetime.android_startup.manager.StartupCacheManager
import com.rousetime.sample.startup.*
import kotlinx.android.synthetic.main.activity_common.*

/**
 * Created by idisfkj on 2020/8/13.
 * Email: idisfkj@gmail.com.
 */
class SampleCommonActivity : AppCompatActivity() {

    private var mMultipleProcessService: IMultipleProcessServiceInterface? = null

    private val mMultipleProcessServiceConnection by lazy {
        object : ServiceConnection {
            override fun onServiceDisconnected(name: ComponentName?) {}

            override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                mMultipleProcessService = IMultipleProcessServiceInterface.Stub.asInterface(service)
                mMultipleProcessService?.addServiceListener(object : IServiceListenerInterface.Stub() {
                    override fun onCompleted(result: String?, totalMainThreadCostTime: Long) {
                        result?.let {
                            Handler(Looper.getMainLooper()).post {
                                showResult(result)
                            }
                        }
                    }
                })
                mMultipleProcessService?.initStartup()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_common)

        start()
    }

    fun onClick(view: View) {
        if (R.id.clear == view.id) {
            mMultipleProcessService?.clear()
//            StartupCacheManager.instance.clear()
            content.text = getString(R.string.clear_cache_success)
        } else if (R.id.get == view.id) {
            unbindService()
            start()
        }
    }

    private fun start() {
        // show initialize tips
        content.text = getString(R.string.sample_startup_not_initialized)

        val list = mutableListOf<AndroidJob<*>>()
        when (intent.getIntExtra("id", -1)) {
            R.id.sync_and_sync -> {
                list.add(SampleSyncOneStartup())
                list.add(SampleSyncTwoStartup())
            }

            R.id.sync_and_async -> {
                list.add(SampleSyncThreeStartup())
                list.add(SampleAsyncOneStartup())
            }

            R.id.async_and_sync -> {
                list.add(SampleAsyncTwoStartup())
                list.add(SampleSyncFourStartup())
            }

            R.id.async_and_async -> {
                list.add(SampleAsyncFiveStartup())
                list.add(SampleAsyncThreeStartup())
            }

            R.id.async_and_async_await_main_thread -> {
                list.add(SampleAsyncSixStartup())
                list.add(SampleAsyncFourStartup())
            }

            R.id.manual_dispatch -> {
                list.add(SampleManualDispatchStartup())
                list.add(SampleAsyncSevenStartup())
                list.add(SampleSyncFiveStartup())
            }

            R.id.thread_priority -> {
            }

            R.id.multiply_process -> {
//                bindService(
//                    Intent(this, MultipleProcessService::class.java),
//                    mMultipleProcessServiceConnection,
//                    Service.BIND_AUTO_CREATE
//                )
            }
        }

        // because some scenarios startup block on main thread.
        // in order to show initialize tips,to delay a frame times.
        Handler().postDelayed({
            AppStartup.Builder()
                .addAllStartup(list)
                .build(this)
                .start()
                .await()
        }, 16)
    }

    private fun showResult(result: String) {
        content.text = result
    }

    private fun unbindService() {
        mMultipleProcessService?.let {
            unbindService(mMultipleProcessServiceConnection)
        }
    }

    override fun onDestroy() {
        unbindService()
        super.onDestroy()
    }
}