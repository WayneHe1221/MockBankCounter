package com.waynehe.bankcounter.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.NumberPicker
import androidx.fragment.app.Fragment
import com.waynehe.bankcounter.MainActivity
import com.waynehe.bankcounter.R

class SettingFragment : Fragment(){
    private lateinit var npCounterNumber: NumberPicker
    private var currentAmountOfCounter: Int = MainActivity.NUMBER_OF_BANK_COUNTER

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = LayoutInflater.from(context).inflate(R.layout.fragment_settings, container, false)
        npCounterNumber = rootView.findViewById(R.id.npCounterNumber)
        npCounterNumber.minValue = 1
        npCounterNumber.maxValue = 15
        npCounterNumber.value = currentAmountOfCounter
        npCounterNumber.setOnValueChangedListener { numberPicker, oldVal, newVal ->
            currentAmountOfCounter = newVal
        }

        return rootView
    }

    fun getCurrentAmountOfCounter(): Int{
        return currentAmountOfCounter
    }

}