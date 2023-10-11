package com.shegs.artreasurehunt.di.modules

import android.content.Context
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.shegs.artreasurehunt.network.services.ARTreasureHuntService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideFirebase(@ApplicationContext context: Context){
        FirebaseApp.initializeApp(context)
    }

    @Provides
    @Singleton
    fun provideFireStore(firebaseApp: FirebaseApp):FirebaseFirestore{
        return FirebaseFirestore.getInstance(firebaseApp)
    }

    @Provides
    @Singleton
    fun provideFirebaseAuth(firebaseApp: FirebaseApp):FirebaseAuth{
        return FirebaseAuth.getInstance(firebaseApp)
    }

    @Provides
    @Singleton
    fun provideARTreasureHuntService(firebaseAuth: FirebaseAuth, firebaseFirestore: FirebaseFirestore) : ARTreasureHuntService{
        return ARTreasureHuntService(firebaseAuth = firebaseAuth, firestore = firebaseFirestore)
    }
}