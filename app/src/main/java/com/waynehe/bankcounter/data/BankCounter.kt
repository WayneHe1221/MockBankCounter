package com.waynehe.bankcounter.data

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.MutableLiveData
import java.util.*

data class BankCounter(val name: String){
    val processingClient: MutableLiveData<Int> = MutableLiveData()
    val processedClient: MutableList<Int> = LinkedList<Int>()

    constructor(name: String, client: Int) : this(name) {
        processingClient.value = client
    }

    fun clientService(client: Int) : Boolean{
        if (processingClient.value!! == 0){
            processingClient.value = client
            Handler(Looper.getMainLooper()).postDelayed({
                processedClient.add(processingClient.value!!)
                processingClient.value = 0
            }, ((5..15).random() * 100).toLong())
            return true
        }
        return false
    }

    fun isProcessing() : Boolean{
        return processingClient.value!! > 0
    }
}
