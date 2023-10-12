package com.shegs.artreasurehunt.data.network.request_and_response_models

data class AuthRequest(
    val email: String = "",
    val password: String = "",
    val userName:String = "",
    val lat: Double? = 0.0,
    val lng: Double? = 0.0,
)
{
    override fun toString(): String {
        return "AuthRequest(email='$email', password='$password', lat=$lat, lng=$lng)"
    }
}
