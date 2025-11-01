plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kapt)
    alias(libs.plugins.hilt)
    alias(libs.plugins.realm.android)
    alias(libs.plugins.kotlin.parcelize)
}

android {
    namespace = "com.example.bookapp"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.bookapp"
        minSdk = 24
        targetSdk = 36
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
        viewBinding = true
    }
}

dependencies {
    implementation("androidx.core:core-splashscreen:1.0.1")
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.recyclerview)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    api(libs.mavericks)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.preference.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation(libs.javax.inject)
    implementation(libs.flowbinding.android)
    implementation(libs.dagger)
    kapt(libs.dagger.compiler)
    implementation(libs.hilt)
    kapt(libs.hilt.compiler)
    androidTestImplementation(libs.hilt.android.testing)
    implementation(libs.realm.monarchy)
    implementation(platform(libs.okhttp.bom))
    implementation(libs.okhttp)
    implementation(libs.logging.interceptor)
    implementation(libs.moshi)
    implementation(libs.retrofit)
    implementation(libs.retrofit.moshi)
    implementation(libs.moshi.kt)
    implementation(libs.moshi.adapters)
    kapt(libs.moshi.kotlin.codegen)
    implementation(libs.epoxy)
    implementation(libs.epoxy.glide)
    kapt(libs.epoxy.processor)
    implementation(libs.epoxy.paging)
    implementation(libs.android.map.sdk)
    implementation(libs.android.sdk.geojson)
    implementation(libs.gms.play.services.location)
    implementation(libs.graphhopper.core)
    implementation(libs.glide)
    implementation(libs.blurry)

    implementation(libs.androidx.media3.exoplayer)
    implementation(libs.androidx.media3.exoplayer.rtsp)
    implementation(libs.androidx.media3.ui)
    implementation (libs.rtsp.server)
    implementation(libs.library)

    implementation(libs.camera.core)
    implementation(libs.camera.camera2)
    implementation(libs.camera.lifecycle)
    implementation(libs.camera.view)
}

hilt {
    enableAggregatingTask = false
}

kapt {
    correctErrorTypes = true
}