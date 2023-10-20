package com.shegs.artreasurehunt.di.modules

import android.app.Application
import android.content.Context
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
//import com.shegs.artreasurehunt.data.repositories.GeofencingRepository
import com.shegs.artreasurehunt.data.repositories.LocationRepository
import com.shegs.artreasurehunt.data.repositories.NetworkRepository
import com.shegs.artreasurehunt.viewmodels.ArenaViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {


    @Provides
    @Singleton
    fun provideGeofencingClient(application: Application): GeofencingClient {
        return LocationServices.getGeofencingClient(application)
    }

//    @Provides
//    @Singleton
//    fun provideGeofencingRepository(geofencingClient: GeofencingClient): GeofencingRepository {
//      //  return GeofencingRepository(geofencingClient)
//    }

    @Provides
    @Singleton
    fun provideFirebase(@ApplicationContext context: Context): FirebaseApp {
        return FirebaseApp.initializeApp(context) ?: FirebaseApp.getInstance()

    }

    @Provides
    @Singleton
    fun provideFireStore(firebaseApp: FirebaseApp): FirebaseFirestore {
        return FirebaseFirestore.getInstance(firebaseApp)
    }

    @Provides
    @Singleton
    fun provideFirebaseAuth(firebaseApp: FirebaseApp): FirebaseAuth {
        return FirebaseAuth.getInstance(firebaseApp)
    }

    @Provides
    @Singleton
    fun provideNetworkRepository(
        firebaseAuth: FirebaseAuth,
        firestore: FirebaseFirestore
    ): NetworkRepository {
        return NetworkRepository(firebaseAuth = firebaseAuth, firestore = firestore)
    }

    @Provides
    @Singleton
    fun providesFusedLocationProviderClient(
        @ApplicationContext context: Context
    ): FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    @Provides
    @Singleton
    fun providesLocationRepository(
        fusedLocationProviderClient: FusedLocationProviderClient,
        @ApplicationContext context: Context
    ): LocationRepository {
        return LocationRepository(
            fusedLocationProviderClient = fusedLocationProviderClient,
            context = context
        )
    }


//    @Provides
//    @Singleton
//    fun provideARTreasureHuntService(firebaseAuth: FirebaseAuth, firebaseFirestore: FirebaseFirestore) : ARTreasureHuntService {
//        return ARTreasureHuntService(firebaseAuth = firebaseAuth, firestore = firebaseFirestore)
//    }
}