package com.shegs.artreasurehunt.data.network.services

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.shegs.artreasurehunt.data.models.User
import com.shegs.artreasurehunt.data.network.request_and_response_models.AuthRequest
import com.shegs.artreasurehunt.data.network.request_and_response_models.NetworkResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ARTreasureHuntService @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth
) {
    suspend fun signUp(authRequest: AuthRequest): Flow<NetworkResult<User>> {
        return flow {
            emit(NetworkResult.Loading())
            try {
                val request = AuthRequest(
                    email = authRequest.email,
                    password = authRequest.password,
                    userName = authRequest.userName,
                )
                println(request)
                val authResult =
                    firebaseAuth.createUserWithEmailAndPassword(
                        request.email,
                        request.password
                    )
                if (authResult.isSuccessful) {
                    val user = authRequest.userName?.let {
                        User(
                            id = authResult.result.user!!.uid,
                            email = request.email,
                            userName = it
                        )
                    }
                    val userDocumentRef = user!!.id?.let { firestore.collection("users").document(it) }
                    userDocumentRef?.set(user)?.await()
                    emit(NetworkResult.Success(user))


                }
            } catch (e: Exception) {
                Log.e("exception", e.message.toString())
            } catch (e: Throwable) {
                emit(NetworkResult.Error(e))
            }
        }
    }

    suspend fun login(authRequest: AuthRequest):Flow<NetworkResult<User>> {
        return flow {

        }
    }
}