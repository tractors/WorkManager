package com.will.workmanager

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.work.*
import kotlinx.android.synthetic.main.activity_main.*
const val INPUT_DATA_KEY = "input_data_key"
const val OUTPUT_DATA_KEY = "output_data_key"
const val WORK_A_NAME = "work_a"
const val WORK_B_NAME = "work_b"
const val WORK_C_NAME = "work_c"
const val SHARED_PREFERENCES_NAME = "shp_name"
class MainActivity : AppCompatActivity(),SharedPreferences.OnSharedPreferenceChangeListener {
    private val workManager = WorkManager.getInstance(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val sp = getSharedPreferences(SHARED_PREFERENCES_NAME,Context.MODE_PRIVATE)
        sp.registerOnSharedPreferenceChangeListener(this)
        button.setOnClickListener {
            val workRequestA = createWork(WORK_A_NAME)
            val workRequestB = createWork(WORK_B_NAME)
            val workRequestC = createWork(WORK_C_NAME)

            workManager.beginWith(workRequestA)
                .then(workRequestB)
                .then(workRequestC)
                .enqueue()
        }

    }

    private fun updateView(){
        val sp = getSharedPreferences(SHARED_PREFERENCES_NAME,Context.MODE_PRIVATE)
        textView.text = sp.getInt(WORK_A_NAME,0).toString()
        textView2.text = sp.getInt(WORK_B_NAME,0).toString()
    }

    private fun createWork(name:String): OneTimeWorkRequest {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
            .build()

        return OneTimeWorkRequestBuilder<MyWorker>()
            .setConstraints(constraints)
            .setInputData(workDataOf(INPUT_DATA_KEY to name))
            .build()
    }

    override fun onSharedPreferenceChanged(p0: SharedPreferences?, p1: String?) {
        updateView()
    }
}