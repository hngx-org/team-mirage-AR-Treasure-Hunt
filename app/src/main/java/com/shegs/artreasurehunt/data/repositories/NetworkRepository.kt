package com.shegs.artreasurehunt.data.repositories

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.ktx.Firebase
import com.shegs.artreasurehunt.data.models.ArenaModel
import com.shegs.artreasurehunt.data.models.User
import com.shegs.artreasurehunt.data.network.request_and_response_models.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

const val ARENAS_COLLECTION_REF = "arenas"

class NetworkRepository @Inject constructor(
    val firebaseAuth: FirebaseAuth,
    firestore: FirebaseFirestore
) {

    private val arenasDB: CollectionReference = firestore.collection(ARENAS_COLLECTION_REF)


    private var userData: User? = null

    private val documentId = arenasDB.document().id

    suspend fun signUp(
        user: User,
        onComplete: (Boolean) -> Unit,
    ) = withContext(Dispatchers.IO) {
        val authResult =
            firebaseAuth.createUserWithEmailAndPassword(user.email, user.password).await()

        if (authResult.user != null) {
            userData = User(
                id = authResult.user!!.uid,
                email = user.email,
                userName = user.userName,
                password = user.password,
            )

            arenasDB.document(authResult.user!!.uid).set(userData!!).await()

            onComplete(true) // Signal success after both signup and data save
        } else {
            onComplete(false)
        }

        Log.i("NetworkRepo", "UserSignUP =$userData")
    }

    suspend fun getSignUpDetails(userId: String): User? {
        return try {
            Log.i("NetworkRepo,", "UserIDetails =$userId")
            val userSnapshot = arenasDB
                .document(userId)
                .get()
                .await()
            Log.i("NetworkRepo,", "UserIDetails2 =${userSnapshot.toObject(User::class.java)}")
            userSnapshot.toObject(User::class.java)

        } catch (exception: Exception) {
            // Toast.makeText(context,"${exception.cause?.message}", Toast.LENGTH_SHORT).show()
            null
        }
    }


    suspend fun login(
        user: User,
        onComplete: (Boolean) -> Unit,
    ) = withContext(Dispatchers.IO) {
        firebaseAuth.signInWithEmailAndPassword(user.email, user.password).addOnCompleteListener {
            if (it.isSuccessful) {
                onComplete(true)
            } else {
                onComplete(false)
            }
        }.await()
        Log.i("NetworkRepo,", "Userlogin =${firebaseAuth.currentUser?.uid}")
        Log.i("NetworkRepo,", "User2 =$user")
    }


    fun signOut() = Firebase.auth.signOut()

    suspend fun deleteAccount(onComplete: (Boolean) -> Unit) {
        arenasDB
            .document(Firebase.auth.currentUser!!.uid)
            .delete()
        Firebase.auth.currentUser?.delete()?.addOnCompleteListener {
            if (it.isSuccessful) {
                onComplete(true)
            } else {
                onComplete(false)
            }
        }?.await()
    }

    fun saveArena(arena: ArenaModel) {
        arenasDB
            .document(documentId)
            .set(arena)
    }

    //get All Arenas
    suspend fun fetchArenas(): Flow<NetworkResult<List<ArenaModel>>> = callbackFlow {
        var snapShotListener: ListenerRegistration? = null

        try {
            snapShotListener = arenasDB
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