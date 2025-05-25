package com.example.sedonortdd.ui.checkin

import com.example.sedonortdd.viewModel.CheckInViewModel
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.annotation.OptIn
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.sedonortdd.R
import com.example.sedonortdd.data.repositories.CheckInRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import java.util.concurrent.Executors

class CheckInActivity : AppCompatActivity() {
    private lateinit var previewView: PreviewView
    private lateinit var imageAnalysis: ImageAnalysis
    private lateinit var viewModel: CheckInViewModel

    private var isScanning = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_check_in)

        previewView = findViewById(R.id.previewView)

        val firestore = FirebaseFirestore.getInstance()
        val checkInRepository = CheckInRepository(firestore)
        viewModel = ViewModelProvider(this, CheckInViewModel.Factory(checkInRepository))[CheckInViewModel::class.java]

        startCamera()
        observeLocation()
    }

    @OptIn(ExperimentalGetImage::class)
    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(previewView.surfaceProvider)
            }

            imageAnalysis = ImageAnalysis.Builder().build().also {
                it.setAnalyzer(Executors.newSingleThreadExecutor()) { imageProxy ->
                    processImageProxy(imageProxy)
                }
            }

            cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageAnalysis)
        }, ContextCompat.getMainExecutor(this))
    }

    @ExperimentalGetImage
    private fun processImageProxy(imageProxy: ImageProxy) {
        if (!isScanning) {
            imageProxy.close()
            return
        }

        val barcodeScanner = BarcodeScanning.getClient()
        val mediaImage = imageProxy.image ?: return
        val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)

        barcodeScanner.process(image)
            .addOnSuccessListener { barcodes ->
                barcodes.firstOrNull()?.displayValue?.let { scannedValue ->
                    Log.d("CheckInActivity", "Barcode scanned: $scannedValue")
                    isScanning = false // Stop scanning sementara

                    viewModel.fetchLocation(scannedValue)

                    // Boleh ganti delay sesuai kebutuhan, misal 2 detik
                    imageProxy.close()
                    previewView.postDelayed({
                        isScanning = true // Mulai scan lagi
                    }, 2000)
                } ?: run {
                    Log.d("CheckInActivity", "No barcode detected")
                    imageProxy.close()
                }
            }
            .addOnFailureListener { e ->
                Log.e("CheckInActivity", "Barcode scanning failed", e)
                imageProxy.close()
            }
            .addOnCompleteListener {
                    imageProxy.close()

            }
    }

    private fun observeLocation() {
        viewModel.location.observe(this) { location ->
            if (location != null) {
                Log.d("CheckInActivity", "Navigating to CheckInDetail: ${location.name}")
                val intent = Intent(this, CheckInDetail::class.java).apply {
                    putExtra("NAMADONOR", location.name)
                    putExtra("ALAMATLOKASI", location.address)
                    putExtra("IMAGELOKASI", location.photo)
                    putExtra("JADWAL", "April 28, 2025 at 1:59:32PM UTC+7")
                }
                startActivity(intent)
                finish()
            } else {
                Log.d("CheckInActivity", "Location is null, waiting for update...")
            }
        }
    }

    fun back() {
        finish()
    }
}
