package com.waynehe.bankcounter

import android.app.Service
import android.content.Intent
import android.os.Handler
import android.os.IBinder

class ClientAssignmentService : Service() {
    companion object {
        var isRunning: Boolean = false
    }

    private var mHandler: Handler = Handler()
    private lateinit var mRunnable: Runnable
    private var assignWorkCounter: Int = 0

    override fun onBind(intent: Intent): IBinder? {
        throw UnsupportedOperationException("Not yet implemented")
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        assignWorkCounter = 0

        mRunnable = Runnable { assignWork() }
        mHandler.post(mRunnable)

        isRunning = true

        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        mHandler.removeCallbacks(mRunnable)
        isRunning = false
    }

    private fun assignWork(){
        val bankData = MainActivity.bankData
        if (assignWorkCounter >= 10 && bankData.clientQueue.size == 0){
            onDestroy()
            return
        }
        while (!bankData.clientQueue.isEmpty()
            && !bankData.isOverLoading()){
            val client = bankData.clientQueue.peek()!!
            for (bankCounter in bankData.bankCounters){
                if (bankCounter.clientService(client)){
                    bankData.clientQueue.remove(client)
                    assignWorkCounter = 0
                    break
                }
            }
        }
        assignWorkCounter++
        mHandler.postDelayed(mRunnable, 100)
    }
}