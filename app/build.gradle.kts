plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.google.gms.google.services)
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.example.musicapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.musicapp"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        manifestPlaceholders["YANDEX_CLIENT_ID"] = "14e4968bd5104304a62234e22f2d6242"
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    buildFeatures.viewBinding = true

    kotlinOptions {
        jvmTarget = "11"
    }

    tasks.withType<Test> { useJUnitPlatform() }
}

dependencies {
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.core.ktx)
    implementation(libs.gson)

    /**
     * ExoPlayer
     */
    implementation(libs.androidx.media3.exoplayer)
    implementation(libs.androidx.media3.exoplayer.dash)
    implementation(libs.androidx.media3.ui)

    /**
     * Room
     */
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.room.runtime)

    /**
     * Firebase
     */
    implementation(libs.firebase.auth)
    implementation(libs.firebase.firestore)

    /**
     * Koin
     */
    implementation(libs.koin.android)
    implementation(libs.koin.core)

    /**
     * Navigation API
     */
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)

    /**
     * UI library
     */
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.glide)
    implementation(libs.androidx.legacy.support.v4)
    implementation(libs.lottie)

    /**
     * Yandex auth
     */
    implementation(libs.authsdk)

    /**
     * Retrofit
     */
    implementation(libs.retrofit)
    implementation(libs.converter.gson)

    /**
     * Google auth
     */
    implementation(libs.play.services.auth)
    implementation(libs.androidx.credentials)
    implementation(libs.androidx.credentials.play.services.auth)
    implementation(libs.googleid)

    /**
     * VK ID
     */
    implementation("com.vk:android-sdk-core:4.1.0")
    implementation("com.vk:android-sdk-api:4.1.0")

    /**
     * Test dependencies
     */
    testImplementation(libs.junit.jupiter.api)
    testRuntimeOnly(libs.junit.jupiter.engine)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.mockito.core)
    testImplementation(libs.mockito.kotlin)
    testImplementation(libs.mockito.inline)
    implementation(libs.androidx.fragment.testing)
}