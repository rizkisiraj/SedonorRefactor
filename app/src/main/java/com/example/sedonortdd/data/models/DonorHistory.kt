package com.example.sedonortdd.data.models

import com.google.firebase.Timestamp

data class DonorHistory(
    val weight: Int = 0,
    val heartRate: Int = 0,
    val hemoglobinLevel: String = "",
    val status: Boolean? = false,
    val temperature: Int = 0,
    val donorDate: Timestamp = Timestamp.now(),
    val diastolicPressure: Int = 0,
    val systolicPressure: Int = 0,
    val donorLocation: String = "",
)