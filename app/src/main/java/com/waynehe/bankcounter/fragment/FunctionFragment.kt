package com.waynehe.bankcounter.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.waynehe.bankcounter.R

class FunctionFragment : Fragment() {
    private lateinit var currentNumberText: TextView
    private lateinit var waitingPersonText: TextView
    private var needResetAmount = false
    private var storageArray = arrayOf("0", "0")

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = LayoutInflater.from(context).inflate(R.layout.framgent_function, container, false)
        waitingPersonText = rootView.findViewById(R.id.tvWaitingNumber)
        currentNumberText = rootView.findViewById(R.id.tvCurrentNumber)

        return rootView
    }

    override fun onResume() {
        super.onResume()

        // update current number
        if (needResetAmount){
            updateWaitingPerson("0")
            updateCurrentNumber("0")
            needResetAmount = false
        } else {
            updateWaitingPerson(storageArray[0])
            updateCurrentNumber(storageArray[1])
        }
    }

    override fun onPause() {
        super.onPause()
        storageArray[0] = waitingPersonText.text.toString()
        storageArray[1] = currentNumberText.text.toString()
    }

    fun updateWaitingPerson(waitingPerson: String){
        waitingPersonText.text = waitingPerson
    }

    fun updateCurrentNumber(currentNumber: String){
        currentNumberText.text = currentNumber
    }

    fun setNeedResetAmount(needReset: Boolean){
        needResetAmount = needReset
    }
}