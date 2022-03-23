package com.waynehe.bankcounter.data

import java.util.*

class Bank(counterNum: Int) {
    var bankCounters: MutableList<BankCounter> = LinkedList<BankCounter>()
    var clientQueue: Queue<Int> = LinkedList()
    var clientNum: Int = 0

    init {
        if (counterNum > 0){
            for (i in 1..counterNum){
                bankCounters.add(BankCounter("BankCounter $i", 0))
            }
        }
    }

    fun addClient(){
        clientNum++
        clientQueue.add(clientNum)
    }

    fun isOverLoading() : Boolean{
        for(bankCounter in bankCounters){
            if(!bankCounter.isProcessing()){
                return false
            }
        }
        return true
    }
}