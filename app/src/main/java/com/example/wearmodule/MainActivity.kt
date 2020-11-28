package com.example.wearmodule

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.CalendarContract.EXTRA_EVENT_ID
import android.support.wearable.activity.WearableActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import com.example.wearmodule.`interface`.ViajeApiService
import com.example.wearmodule.models.Viaje
import com.example.wearmodule.util.ApiClient
import com.example.wearmodule.util.URL_API
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : WearableActivity() {
    private lateinit var viajeService: ViajeApiService
    private var notificationId = 1
    var arrive=false
    private val id = "my_channel_01"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (arrive==false){
            tvMessage.text= getString(R.string.driver_on_way)
        } else{
            tvMessage.text= getString(R.string.driver_arrive)
        }
        // Enables Always-on
        setAmbientEnabled()
        var eventId = 1
        viajeService = ApiClient.retrofit.create(ViajeApiService::class.java)
        var userId: Long = 0
        viajeService.getPasajeroInfo(userId).enqueue(object :Callback<Viaje>{
            override fun onResponse(call:Call<Viaje>?,response:Response<Viaje>?){
                var conductor = response?.body()
                if (conductor != null){
                    tvDriverName.text=conductor.nombres
                    tvCarModel.text=conductor.modelo
                    tvCarPlaca.text=conductor.placa
                }
                else{

                }
            }

            override fun onFailure(call: Call<Viaje>?, t: Throwable?) {
                t?.printStackTrace()
            }
        })
        val driverIntent = Intent(this, DriverPhotoActivity::class.java).let {
            it.putExtra(EXTRA_EVENT_ID, eventId)
            PendingIntent.getActivity(this, REQUEST_CODE, it, 0)
        }

        val notificationBuilder = NotificationCompat.Builder(this, id)
            .setSmallIcon(android.R.drawable.stat_notify_more)
            .setContentTitle("Jorge")
            .setStyle(
                NotificationCompat.BigTextStyle().bigText(getString(R.string.driver_arrive))
            )

        createNotificationChannel()
        tvDriverName.setOnClickListener {
            val intent = Intent(this@MainActivity, DriverPhotoActivity::class.java)
            startActivity(intent)

        }
        tvTimeLeft.setOnClickListener {
            NotificationManagerCompat.from(this).apply {
                notify(notificationId, notificationBuilder.build())
            }
            notificationId++
            arrive=true
            updateMessages()
        }
    }
    private fun createNotificationChannel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Wear OS Notification Channel"
            val description = "Notificacion Channel"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(id, name, importance)
            channel.description = description
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

}
    private fun updateMessages(){
        if (arrive==false){
            tvMessage.text= getString(R.string.driver_on_way)
            tvTimeLeft.text=getString(R.string.timeleft)
        } else{
            tvMessage.text= getString(R.string.driver_arrive)
            tvTimeLeft.text=getString(R.string.timedone)
        }

    }

    companion object {
        const val EXTRA_EVENT_ID = "extra_event_id"
        const val REQUEST_CODE = 1

    }

}