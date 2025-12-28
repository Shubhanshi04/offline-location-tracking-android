package com.shubhanshi.offlinelocationlivetracking.data.remote

data class LocationUploadDto(
    val employeeId: String,
    val latitude: Double,
    val longitude: Double,
    val accuracy: Float,
    val timestamp: Long,
    val speed: Float
) {
    fun toJson(): String {
        return """
            {
              "employeeId": "$employeeId",
              "latitude": $latitude,
              "longitude": $longitude,
              "accuracy": $accuracy,
              "timestamp": $timestamp,
              "speed": $speed
            }
        """.trimIndent()
    }
}
