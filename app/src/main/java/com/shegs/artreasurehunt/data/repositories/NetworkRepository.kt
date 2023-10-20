package com.shegs.artreasurehunt.data.repositories

import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.ktx.Firebase
import com.shegs.artreasurehunt.data.models.ArenaModel
import com.shegs.artreasurehunt.data.models.User
import com.shegs.artreasurehunt.data.network.request_and_response_models.AuthRequest
import com.shegs.artreasurehunt.data.network.request_and_response_models.NetworkResult
import com.shegs.artreasurehunt.data.utils.await
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.withContext
import javax.inject.Inject

const val ARENAS_COLLECTION_REF = "arenas"
class NetworkRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    firestore: FirebaseFirestore
) {

    private val arenasRef: CollectionReference = firestore.collection(ARENAS_COLLECTION_REF)


    val currentUser: FirebaseUser?
        get() = firebaseAuth.currentUser

    private var userData: User? = null


    suspend fun signUp(authRequest: AuthRequest): AuthResult = withContext(Dispatchers.IO) {
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
            arenasRef.document(result.user!!.uid).set(userData!!).await()
        }
        return@withContext result
    }

    suspend fun login(authRequest: AuthRequest): AuthResult = withContext(Dispatchers.IO) {
        firebaseAuth.signInWithEmailAndPassword(authRequest.email, authRequest.password)
            .await()

    }

    fun signOut() = Firebase.auth.signOut()

    fun saveArena(arena: ArenaModel) {
        val documentId = arenasRef.document().id
        arenasRef
            .document(documentId)
            .set(arena)
    }

    suspend fun fetchArenas(): Flow<NetworkResult<List<ArenaModel>>> = callbackFlow {
        var snapShotListener: ListenerRegistration? = null

        try {
            snapShotListener = arenasRef
                .addSnapshotListener { snapShot, error ->
                    val response = if (snapShot != null) {
                        val arenas = snapShot.toObjects(ArenaModel::class.java)
                        NetworkResult.Success(data = arenas)
                    } else {
                        NetworkResult.Error(throwable = error?.cause)
                    }

                    trySend(response)
                }
        } catch (e: Exception) {
            trySend(NetworkResult.Error(throwable = e.cause))
            e.printStackTrace()
        }

        awaitClose {
            snapShotListener?.remove()
        }
    }
}