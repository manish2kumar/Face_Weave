plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.example.faceweave"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.faceweave"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        isCoreLibraryDesugaringEnabled = true
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        viewBinding = true
    }

}

dependencies {
    // AndroidX Core
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)

    // CameraX
    implementation("androidx.camera:camera-core:1.4.2")
    implementation("androidx.camera:camera-camera2:1.4.2")
    implementation("androidx.camera:camera-lifecycle:1.4.2")
    implementation("androidx.camera:camera-view:1.4.2")

    // ML Kit
    implementation("com.google.mlkit:face-detection:16.1.7")
    implementation("com.google.mlkit:vision-common:17.3.0")
    implementation("com.google.mlkit:face-mesh-detection:16.0.0-beta2")
    implementation("com.google.mlkit:image-labeling:17.0.9")
    implementation(libs.ads.mobile.sdk)

    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // Splash
    implementation ("androidx.core:core-splashscreen:1.0.1")

    // ViewModel Scope
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.9.1")

    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.1.5")


}
