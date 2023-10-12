package com.shegs.artreasurehunt.data.network.services

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.shegs.artreasurehunt.data.models.User
import com.shegs.artreasurehunt.data.network.request_and_response_models.AuthRequest
import com.shegs.artreasurehunt.data.network.request_and_response_models.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import retrofit2.HttpException
import javax.inject.Inject

class ARTreasureHuntService @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth
) {
    suspend fun signUp(authRequest: AuthRequest): Flow<Resource<User>> {
        return flow {
            emit(Resource.Loading)
            try {
                val request = AuthRequest(
                    email = authRequest.email,
                    password = authRequest.password,
                    userName = authRequest.userName,
                )
                println(request)
                val authResult =
                    firebaseAuth.createUserWithEmailAndPassword(
                        request.email!!,
                        request.password!!
                    )
                if (authResult.isSuccessful) {
                    val user = authRequest.userName?.let {
                        User(
                            id = authResult.result.user!!.uid,
                            email = request.email,
                            userName = it
                        )
                    }
                    val userDocumentRef = firestore.collection("users").document(user!!.id)
                    userDocumentRef.set(user).await()
                    emit(Resource.Success(user))


                }
            } catch (e: Exception) {
                Log.e("exception", e.message.toString())
            } catch (e: HttpException) {
                emit(Resource.Error(e.message ?: "An error occurred"))
            }
        }
    }

    suspend fun login(authRequest: AuthRequest):Flow<Resource<User>> {
        return flow {

        }
    }
}