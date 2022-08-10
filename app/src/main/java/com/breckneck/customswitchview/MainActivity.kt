package com.breckneck.customswitchview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.widget.SwitchCompat
import com.breckneck.customswitchview.ui.AppleSwitch

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        val defaultSwitch: SwitchCompat = findViewById(R.id.defaultSwitch)
//        defaultSwitch.setOnCheckedChangeListener { compoundButton, b -> Log.e("TAG", "Changed") }
//
//        val customSwitch: AppleSwitch = findViewById(R.id.customSwitch)
//        customSwitch.setOnClickListener {
//            Log.e("TAG", "Changed")
//        }
    }
}