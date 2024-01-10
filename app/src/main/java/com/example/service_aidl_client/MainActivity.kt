package com.example.service_aidl_client

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.os.RemoteException
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.RemoteViews
import com.example.service_aidl_server.IMyAidlInterface

class MainActivity : AppCompatActivity() {
    var iMyAidlInterface : IMyAidlInterface? = null
    private var TAG : String = "vietnam"
    private var mServiceConnection : ServiceConnection = object : ServiceConnection{
        //Called when the connection with the service is established.
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            // gets an instance of the IMyAidlInterface, which we can use to call on the service.
            iMyAidlInterface = IMyAidlInterface.Stub.asInterface(service)
            Log.e(TAG, "Remote config Service Connected!!!")
        }
        // Called when the connection with the service disconnects unexpectedly.
        override fun onServiceDisconnected(name: ComponentName?) {
            Log.e(TAG, "Service has unexpectedly disconnected")
            iMyAidlInterface = null
        }

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var intent : Intent = Intent("MyChangeColorService")// same action name in service of server app
        intent.setPackage("com.example.service_aidl_server")//Set an explicit application package name that limits the components this Intent will resolve to.
        // If left to the default value of null, all components in all applications will considered. If non-null, the Intent can only match the components in the given application package

        bindService(intent,mServiceConnection, BIND_AUTO_CREATE)
        Log.e(TAG, "bindservice is called!!!")
        //Create an onClickListener
        var btn : Button = findViewById(R.id.button)
        btn.setOnClickListener{

            try {
                var color = iMyAidlInterface?.color
                if (color != null) {
                    it.setBackgroundColor(color)
                }
            } catch (e : RemoteException){

            }
        }

    }
}