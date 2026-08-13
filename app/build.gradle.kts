plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
     id("com.google.gms.google-services")  // Temporarily disabled
//    alias(libs.plugins.kotlin.compose)

}

android {
    namespace = "com.example.machina"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.machina"
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

    packaging {
        jniLibs.keepDebugSymbols.addAll(listOf("**/*.so"))
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.composeCompiler.get()
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
        isCoreLibraryDesugaringEnabled = true
    }
    kotlinOptions {
        jvmTarget = "17"
    }

    lint {
        // Disable NewApi lint check for Compose
        // NavigationGraph requires API 26+ but our minSdk is 24
        // Compose handles this gracefully on older devices
        disable.add("NewApi")
    }
}

tasks.whenTaskAdded {
    if (name.contains("stripDebugDebugSymbols")) {
        enabled = false
    }
}

dependencies {
    implementation(libs.koin.android)
    implementation(libs.koin.compose)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.gson)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.activity.ktx)

//    splash screen and shared preference

    implementation(libs.androidx.core.splashscreen)
    implementation(libs.androidx.core.datastore)

    //for statemanagement
    implementation(libs.lifecycle.viewmodel.compose)
    implementation(libs.compose.livedata)

    //material icon
    implementation(libs.compose.icons)

    //async image
    implementation(libs.coil.compose)
    implementation(libs.jsch)
    
    // Termux terminal libraries - full stack
    implementation(project(":termux-shared"))
    implementation(project(":terminal-emulator"))
    implementation(project(":terminal-view"))
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.1.5")
    
    // Firebase Cloud Messaging
    implementation(platform("com.google.firebase:firebase-bom:33.6.0"))
    implementation("com.google.firebase:firebase-messaging-ktx")
    implementation("com.google.firebase:firebase-analytics-ktx")
    
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}
