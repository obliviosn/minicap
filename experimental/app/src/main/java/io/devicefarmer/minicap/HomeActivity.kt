package io.devicefarmer.minicap

import android.app.Activity
import android.os.Bundle
import android.os.Looper
import android.widget.Button
import android.widget.Toast
import io.devicefarmer.minicap.R
import io.devicefarmer.minicap.provider.SurfaceProvider
import java.io.DataOutputStream
import java.lang.Exception
import kotlin.system.exitProcess

class HomeActivity : Activity() {
    var provider: SurfaceProvider? = null
    var btnStart: Button? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        btnStart = findViewById(R.id.btn_start_service)
        btnStart?.setOnClickListener {
            var sucess = upgradeRootPermission(packageCodePath)
            if (sucess) {
                Toast.makeText(this, "请求超级权限成功", Toast.LENGTH_SHORT).show()
                startSocketAndCaptureService()
            } else {
                Toast.makeText(this, "请求超级权限失败", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun startSocketAndCaptureService() {
        //called because we are not run from the Android environment
        provider = SurfaceProvider(Size(1280, 720))
        provider?.let {
            it.quality = 10
            it.frameRate = 30f //fps
            //the stf process reads this
            System.err.println("PID: ${android.os.Process.myPid()}")
            val server = SimpleServer("socket", it)
            server.start()
        }
    }

    fun upgradeRootPermission(pkgCodePath: String): Boolean {
        var process: Process? = null
        var os: DataOutputStream? = null
        try {
            val cmd = "chmod 777 $pkgCodePath"
            process = Runtime.getRuntime().exec("su") //切换到root帐号
            os = DataOutputStream(process.outputStream)
            os.writeBytes(" $cmd\n")
            os.writeBytes("exit\n")
            os.flush()
            process.waitFor()
        } catch (e: Exception) {
            return false
        } finally {
            try {
                os?.close()
                process!!.destroy()
            } catch (e: Exception) {
            }
        }
        return true
    }


}