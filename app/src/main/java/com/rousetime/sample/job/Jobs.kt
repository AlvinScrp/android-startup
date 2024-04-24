package com.rousetime.sample.job

import android.content.Context
import android.util.Log
import com.webuy.android_startup.AndroidJob

abstract class Job : AndroidJob<String>() {

    fun print() {
        Log.d("alvin", "${System.currentTimeMillis()} call:${this.name()},${Thread.currentThread()}")
    }


}

class Job1 : Job() {

    override fun call(context: Context): String? {
        Thread.sleep(1000)
        print()
        return Job1::class.java.name
    }

    override fun dependenciesByName() = null
    override fun runOnMainThread(): Boolean = true
    override fun blockMainThread(): Boolean = true

}

class Job2 : Job() {


    override fun dependenciesByName() = listOf(Job1::class.java.name)

    override fun call(context: Context): String? {

        Thread.sleep(1000)
        print()
        return Job2::class.java.name
    }

    override fun runOnMainThread(): Boolean = false

    override fun blockMainThread(): Boolean = true

}

class Job3 : Job() {


    override fun dependenciesByName(): List<String>? {
        return listOf(Job1::class.java.name)
    }

    override fun call(context: Context): String? {
        Thread.sleep(1000)
        print()
        return Job3::class.java.name
    }

    override fun runOnMainThread(): Boolean = false

    override fun blockMainThread(): Boolean = true

}


class Job4 : Job() {


    override fun dependenciesByName(): List<String>? {
        return listOf(Job2::class.java.name, Job3::class.java.name)
    }

    override fun call(context: Context): String? {
        Thread.sleep(1000)
        print()
        return Job4::class.java.name
    }

    override fun runOnMainThread(): Boolean = true

    override fun blockMainThread(): Boolean = true

}

