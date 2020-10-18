package com.example.wearmodule

import android.support.wearable.activity.WearableActivity
import android.os.Bundle

class DriverPhotoActivity : WearableActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_driver_photo)
    }
}