package com.waynehe.bankcounter

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.waynehe.bankcounter.data.Bank
import com.waynehe.bankcounter.fragment.FunctionFragment
import com.waynehe.bankcounter.fragment.SettingFragment


class MainActivity : AppCompatActivity() {
    companion object {
        var NUMBER_OF_BANK_COUNTER = 4
        var bankData: Bank = Bank(NUMBER_OF_BANK_COUNTER)
    }

    private lateinit var flbNext: FloatingActionButton
    private lateinit var bankCounterRecyclerView: RecyclerView
    private lateinit var flbSetting: FloatingActionButton
    private lateinit var clFunctionArea: ConstraintLayout
    private var bankCounterAdapter: BankCounterAdapter = BankCounterAdapter()
    private var settingFragment: SettingFragment = SettingFragment()
    private var functionFragment: FunctionFragment = FunctionFragment()
    private var isSettingOn = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        flbNext = findViewById(R.id.flbNext)
        bankCounterRecyclerView = findViewById(R.id.rvCounterWorking)
        flbSetting = findViewById(R.id.flbSetting)
        clFunctionArea = findViewById(R.id.clFunctionArea)

        functionFragment.setNeedResetAmount(true)
        addFragment(functionFragment)

        bankCounterRecyclerView.adapter = bankCounterAdapter
        bankCounterRecyclerView.layoutManager = LinearLayoutManager(this)

        flbNext.setOnClickListener {
            if (isSettingOn) return@setOnClickListener
            if (!ClientAssignmentService.isRunning) startClientAssignmentService()
            bankData.addClient()
            functionFragment.updateWaitingPerson(bankData.clientQueue.size.toString())
            functionFragment.updateCurrentNumber((bankData.clientNum + 1).toString())
        }
        flbSetting.setOnClickListener {
            if (!isSettingOn){
                replaceFragment(settingFragment)
                flbSetting.setImageResource(R.drawable.ic_checkbox)
                flbNext.setImageResource(R.drawable.ic_cross)
            }else{
                functionFragment.setNeedResetAmount(NUMBER_OF_BANK_COUNTER != settingFragment.getCurrentAmountOfCounter())
                replaceFragment(functionFragment)
                flbSetting.setImageResource(R.drawable.ic_settings)
                flbNext.setImageResource(R.drawable.ic_plus)
                if (NUMBER_OF_BANK_COUNTER != settingFragment.getCurrentAmountOfCounter()){
                    NUMBER_OF_BANK_COUNTER = settingFragment.getCurrentAmountOfCounter()
                    bankData = Bank(NUMBER_OF_BANK_COUNTER)
                    observeData()
                }
            }
            isSettingOn = !isSettingOn
        }

        clFunctionArea.layoutParams.height = getDeviceHeight() / 4

        observeData()
    }

    override fun onDestroy() {
        stopClientAssignmentService()
        super.onDestroy()
    }

    private fun startClientAssignmentService(){
        val serviceClass = ClientAssignmentService::class.java
        val intent = Intent(this, serviceClass)
        startService(intent)
    }

    private fun stopClientAssignmentService(){
        val serviceClass = ClientAssignmentService::class.java
        val intent = Intent(this, serviceClass)
        stopService(intent)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun observeData(){
        for (bankCounter in bankData.bankCounters){
            bankCounter.processingClient.observe(this, Observer {
                bankCounterAdapter.notifyDataSetChanged()
                if(it == 0){
                    functionFragment.updateWaitingPerson(bankData.clientQueue.size.toString())
                }
            })
        }
    }

    private fun addFragment(f: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.clFunctionArea, f)
        transaction.commit()
    }

    private fun replaceFragment(f : Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.clFunctionArea, f)
        transaction.commit()
    }

    private fun getDeviceHeight(): Int{
        val displayMetrics = DisplayMetrics()

        if (Build.VERSION.SDK_INT >= 30){
            display?.apply {
                getRealMetrics(displayMetrics)
            }
        }else{
            windowManager.defaultDisplay.getMetrics(displayMetrics)
        }

        return displayMetrics.heightPixels
    }
}