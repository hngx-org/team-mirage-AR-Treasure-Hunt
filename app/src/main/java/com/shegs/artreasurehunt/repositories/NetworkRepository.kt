package com.shegs.artreasurehunt.repositories

import com.shegs.artreasurehunt.data.models.User
import com.shegs.artreasurehunt.network.request_and_response_models.AuthRequest
import com.shegs.artreasurehunt.network.request_and_response_models.Resource
import com.shegs.artreasurehunt.network.services.ARTreasureHuntService
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class NetworkRepository @Inject constructor(private val arTreasureHuntService: ARTreasureHuntService) {
    suspend fun login(authRequest: AuthRequest) : Flow<Resource<User>> {
        return arTreasureHuntService.login(authRequest = authRequest)
    }

}