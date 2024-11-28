plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.devtools.ksp)
    alias(libs.plugins.navigation.safeargs.kotlin)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.parcelize)
}

android {
    namespace = "com.example.thalestestandroidapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.thalestestandroidapp"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        debug {
            buildConfigField("String", "PRODUCT_BASE_URL", "\"https://673f398fa9bc276ec4b7b67c.mockapi.io\"")
            buildConfigField("String", "PRODUCT_IMAGE_BASE_URL", "\"https://673f398fa9bc276ec4b7b67c.mockapi.io/image/\"")
        }
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            buildConfigField("String", "PRODUCT_BASE_URL", "\"https://673f398fa9bc276ec4b7b67c.mockapi.io\"")
            buildConfigField("String", "PRODUCT_IMAGE_BASE_URL", "\"https://673f398fa9bc276ec4b7b67c.mockapi.io/image/\"")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        buildConfig = true
        viewBinding = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // ViewModel
    implementation(libs.androidx.lifecycle.viewmodel.ktx)

    // LiveData
    implementation(libs.androidx.lifecycle.livedata.ktx)
    testImplementation(libs.androidx.core.testing)

    // For view binding delegation
    // Upgrading to version 1.5.8 and above might cause errors (https://github.com/androidbroadcast/ViewBindingPropertyDelegate/issues/113)
    implementation(libs.viewbindingpropertydelegate.noreflection)

    // Fragments
    implementation(libs.androidx.fragment.ktx)

    // RecyclerView (for some reason, bindingAdapterPosition sometimes becomes unresolved without this)
    implementation(libs.androidx.recyclerview)
    implementation(libs.flexbox)

    // Navigation
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)

    // Coroutines
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)

    // Retrofit2
    implementation(libs.retrofit)
    implementation(libs.converter.moshi)
    implementation(libs.logging.interceptor)

    // Moshi
    implementation(libs.moshi)
    ksp(libs.moshi.kotlin.codegen)

    // Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)

    // Coil
    implementation(libs.coil.network.okhttp)
    implementation(libs.coil)

    // Logging
    implementation(libs.timber)
}

// Invoke Hilt processors only when necessary
hilt {
    enableAggregatingTask = true
}