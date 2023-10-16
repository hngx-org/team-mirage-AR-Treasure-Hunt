// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath ("com.google.android.libraries.mapsplatform.secrets-gradle-plugin:secrets-gradle-plugin:2.0.1")
        classpath ("com.google.dagger:hilt-android-gradle-plugin:2.42")
    }
}

plugins {
    id("com.android.application") version "8.1.0" apply false
    id("org.jetbrains.kotlin.android") version "1.8.10" apply false
    id ("com.google.dagger.hilt.android") version "2.45" apply false
    id("org.jetbrains.kotlin.jvm") version "1.8.10" apply false
    id("com.google.gms.google-services") version "4.4.0" apply false
}