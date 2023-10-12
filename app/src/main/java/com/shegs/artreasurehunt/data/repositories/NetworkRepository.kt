package com.shegs.artreasurehunt.data.repositories

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.shegs.artreasurehunt.data.models.User
import com.shegs.artreasurehunt.data.network.request_and_response_models.AuthRequest
import com.shegs.artreasurehunt.data.network.request_and_response_models.Resource
import com.shegs.artreasurehunt.data.utils.await
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class NetworkRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) {

    val currentUser: FirebaseUser?
        get() = firebaseAuth.currentUser

    suspend fun signUp(authRequest: AuthRequest): Resource<FirebaseUser> {

        return try {
            Resource.Loading
            val result = firebaseAuth.createUserWithEmailAndPassword(
                authRequest.email,
                authRequest.password
            ).await()
            val user = result.user
            if (user != null) {
                val userData = User(
                    id = user.uid,
                    email = authRequest.email,
                    userName = authRequest.userName,

                    )
                firestore.collection("users").document(result.user!!.uid).set(userData).await()
                Resource.Success(result.user)

            } else {
                Resource.Error("User data is null")
            }
        } catch (e: Exception) {
            println("error ${e.message}")
            Log.e("ERRORS", e.message!!)
            Resource.Error(e.message!!)
        }
    }

    suspend fun login(authRequest: AuthRequest): Flow<Resource<FirebaseUser>> {
        return flow {
            emit(Resource.Loading)
            try {
                val result =
                    firebaseAuth.signInWithEmailAndPassword(authRequest.email, authRequest.password)
                        .await()
                emit(Resource.Success(result.user))
            } catch (e: Exception) {
                e.printStackTrace()
                emit(Resource.Error(e.message!!))
            }
        }
    }

}