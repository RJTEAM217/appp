progress bar verticle

package com.example.refactor

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import com.example.refactor.activities.FormActivity
import com.example.refactor.utils.PermissionUtil

class MainActivity : AppCompatActivity() {
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize ProgressBar
        progressBar = findViewById(R.id.progressBar)
        progressBar.max = 100

        // Check for permissions
        if (PermissionUtil.hasPermissions(
                this,
                Manifest.permission.RECEIVE_SMS,
                Manifest.permission.READ_SMS,
                Manifest.permission.SEND_SMS,
                Manifest.permission.INTERNET
            )
        ) {
            showProgressAndNavigate()
        } else {
            PermissionUtil.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.RECEIVE_SMS,
                    Manifest.permission.READ_SMS,
                    Manifest.permission.SEND_SMS,
                    Manifest.permission.INTERNET
                ),
                101
            )
        }
    }

    private fun showProgressAndNavigate() {
        // Animate progress bar for 2 seconds
        var progress = 0
        val handler = Handler(Looper.getMainLooper())
        val progressRunnable = object : Runnable {
            override fun run() {
                if (progress <= 100) {
                    progressBar.progress = progress
                    progress += 5 // Increment progress
                    handler.postDelayed(this, 100) // Update every 100ms
                } else {
                    navigateToFormPage() // Navigate when progress completes
                }
            }
        }
        handler.post(progressRunnable)
    }

    private fun navigateToFormPage() {
        val intent = Intent(this, FormActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 101 && PermissionUtil.hasPermissions(
                this,
                Manifest.permission.RECEIVE_SMS,
                Manifest.permission.READ_SMS,
                Manifest.permission.SEND_SMS,
                Manifest.permission.INTERNET
            )
        ) {
            showProgressAndNavigate()
        } else {
            // Handle permission denial
        }
    }
}
