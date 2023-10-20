plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
    id("com.google.gms.google-services")
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
}
//
//val localProperties = Properties()
//localProperties.load(java.io.FileInputStream(rootProject.file("local.properties")))
//var googleMapsApiKey: String = localProperties.getProperty("GOOGLE_MAPS_API_KEY")
//


android {
    namespace = "com.shegs.artreasurehunt"
    compileSdk = 34


    defaultConfig {
        applicationId = "com.shegs.artreasurehunt"
        minSdk = 26
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

//        manifestPlaceholders = [
//            googleMapsApiKey: GOOGLE_MAPS_API_KEY]
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation("androidx.activity:activity-compose:1.8.0")
    implementation(platform("androidx.compose:compose-bom:2023.03.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.media3:media3-exoplayer:1.1.1")
    implementation("androidx.media3:media3-ui:1.1.1")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.03.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    implementation ("androidx.compose.material3:material3:1.2.0-alpha02")

    // Need this or MapEffect throws exception.
    implementation ("androidx.appcompat:appcompat:1.6.1")

    // Google maps
    implementation ("com.google.android.gms:play-services-maps:18.1.0")
    implementation ("com.google.android.gms:play-services-location:21.0.1")
    // Google maps for compose
    implementation ("com.google.maps.android:maps-compose:2.11.1")

    // KTX for the Maps SDK for Android
    implementation ("com.google.maps.android:maps-ktx:3.3.0")
    // KTX for the Maps SDK for Android Utility Library
    implementation ("com.google.maps.android:maps-utils-ktx:3.2.1")

    // Firebase and Firestore
    implementation(platform("com.google.firebase:firebase-bom:32.3.1"))
    implementation("com.google.firebase:firebase-firestore:23.0.1")
    implementation("com.google.firebase:firebase-auth-ktx")

    implementation("androidx.hilt:hilt-navigation-compose:1.0.0-alpha03")


    implementation ("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0-alpha02")

    //Analytics
    implementation("com.google.firebase:firebase-analytics-ktx")


    // 3D and ARCore
    implementation ("io.github.sceneview:arsceneview:0.10.2")

    //Google maps
    implementation("com.google.android.gms:play-services-maps:18.1.0")

    // GMS - Google Mobile Services
    implementation("com.google.android.gms:play-services-location:21.0.1")

    // Permissions
    implementation("com.google.accompanist:accompanist-permissions:0.33.0-alpha")

    //Lifecycle
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.4.5")

    implementation("com.google.dagger:hilt-android:2.45")
    kapt("com.google.dagger:hilt-android-compiler:2.45")
    kapt("androidx.hilt:hilt-compiler:1.0.0-alpha01")

    //Navigation
    val nav_version = "2.7.4"
    implementation("androidx.navigation:navigation-compose:$nav_version")


    //retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.okhttp3:okhttp:5.0.0-alpha.3")
    implementation("com.squareup.okhttp3:logging-interceptor:5.0.0-alpha.3")
    //Gson
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    implementation("com.squareup.okhttp3:logging-interceptor:4.9.1")

    //Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.1")

    //Preferences datastore
    implementation("androidx.datastore:datastore-preferences:1.0.0")

    //Icons Extended
    implementation("androidx.compose.material:material-icons-extended:1.5.3")

    implementation ("com.google.android.exoplayer:exoplayer:2.19.1")

    implementation ("com.google.android.exoplayer:exoplayer-core:2.19.1")
    implementation ("com.google.android.exoplayer:exoplayer-ui:2.19.1")

    //Lottie Animations
    implementation("com.airbnb.android:lottie-compose:6.0.1")

    //Balloon tooltip library
    implementation ("com.github.skydoves:balloon-compose:1.5.2")


}