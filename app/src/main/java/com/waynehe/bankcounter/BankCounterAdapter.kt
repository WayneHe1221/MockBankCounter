package com.waynehe.bankcounter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.waynehe.bankcounter.data.BankCounter

class BankCounterAdapter : RecyclerView.Adapter<BankCounterAdapter.CounterViewHolder>() {

    inner class CounterViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        private val counterName: TextView = itemView.findViewById(R.id.tvCounterName)
        private val processingNumber: TextView = itemView.findViewById(R.id.tvCounterProcessingNumber)
        private val processedNumber: TextView = itemView.findViewById(R.id.tvCounterProcessedNumber)

        fun updateCounter(bankCounter: BankCounter){
            counterName.text = bankCounter.name

            var reversedProcessedClientStr = bankCounter.processedClient.reversed().toString()
            reversedProcessedClientStr = reversedProcessedClientStr.subSequence(1, reversedProcessedClientStr.length - 1) as String
            processedNumber.text = reversedProcessedClientStr

            if(bankCounter.processingClient.value!! == 0){
                processingNumber.text = itemView.context.getString(R.string.bank_counter_idle_text)
            }else{
                processingNumber.text = bankCounter.processingClient.value.toString()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CounterViewHolder {
        return CounterViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.layout_counter_display, parent, false))
    }

    override fun onBindViewHolder(holder: CounterViewHolder, position: Int) {
        holder.updateCounter(MainActivity.bankData.bankCounters[position])
    }

    override fun getItemCount(): Int {
        return MainActivity.bankData.bankCounters.size
    }

}