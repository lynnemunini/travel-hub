plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    kotlin("kapt")
    id("com.google.dagger.hilt.android")
    // Add the Google services Gradle plugin
    id("com.google.gms.google-services")

}

android {
    namespace = "com.grayseal.travelhub"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.grayseal.travelhub"
        minSdk = 24
        targetSdk = 34
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    // Dependency injection with Hilt
    implementation("com.google.dagger:hilt-android:2.44.2")
    kapt("com.google.dagger:hilt-android-compiler:2.44")
    // Splash Screen
    implementation("androidx.core:core-splashscreen:1.1.0-alpha02")
    // Import the Firebase BoM
    implementation(platform("com.google.firebase:firebase-bom:32.3.1"))
    // Analytics
    implementation("com.google.firebase:firebase-analytics-ktx")
    // Add Firebase Authentication
    implementation("com.google.firebase:firebase-auth-ktx")
    implementation("com.google.android.gms:play-services-auth:20.7.0")
    implementation("androidx.activity:activity-ktx:1.8.0")
    implementation("com.google.code.gson:gson:2.10.1")
    // For ViewModelScope
    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")
    // EventBus
    implementation ("org.greenrobot:eventbus:3.3.1")
    // Picasso
    implementation ("com.squareup.picasso:picasso:2.71828")
}

// Allow references to generated code
kapt {
    correctErrorTypes = true
}