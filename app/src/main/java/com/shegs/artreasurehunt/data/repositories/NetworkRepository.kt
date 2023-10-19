package com.shegs.artreasurehunt.data.repositories

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.shegs.artreasurehunt.data.models.ArenaModel
import com.shegs.artreasurehunt.data.models.User
import com.shegs.artreasurehunt.data.network.request_and_response_models.AuthRequest
import com.shegs.artreasurehunt.data.network.request_and_response_models.Resource
import com.shegs.artreasurehunt.data.utils.await
import javax.inject.Inject

class NetworkRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) {

    val currentUser: FirebaseUser?
        get() = firebaseAuth.currentUser

    var userData: User? = null
    suspend fun signUp(authRequest: AuthRequest): Resource<FirebaseUser> {

        return try {
            Resource.Loading
            val result = firebaseAuth.createUserWithEmailAndPassword(
                authRequest.email,
                authRequest.password
            ).await()
            val user = result.user
            if (user != null) {
                userData = User(
                    id = user.uid,
                    email = authRequest.email,
                    userName = authRequest.userName,
                )
                firestore.collection("users").document(result.user!!.uid).set(userData!!).await()

                Resource.Success(result.user)

            } else {
                Resource.Error("User data is null")
            }
        } catch (e: Exception) {
            println("error ${e.message}")
            Log.e("ERRORS", e.message!!)
            firestore.terminate()
            Resource.Error(e.message!!)
        }
    }

    suspend fun login(authRequest: AuthRequest): Resource<FirebaseUser> {

        Resource.Loading
        return try {
            val result =
                firebaseAuth.signInWithEmailAndPassword(authRequest.email, authRequest.password)
                    .await()
            Resource.Success(result.user)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(e.message!!)
        }
    }

    fun signOut() = Firebase.auth.signOut()

    suspend fun saveArena(arena: ArenaModel): Resource<Unit> {
        return try {
            Resource.Loading
            
            val collection = firestore.collection("arenas")

            collection.add(arena).await()

            Resource.Success(Unit)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(e.message!!)
        }
    }

}