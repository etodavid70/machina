// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.hilt) apply false
//    alias(libs.plugins.kotlin.compose) apply false
     id("com.google.gms.google-services") version "4.5.0" apply false  // Temporarily disabled
}

// Shared ext properties for all subprojects
allprojects {
    project.ext.apply {
        set("kotlin_version", "2.0.21")
        set("detekt_version", "1.23.7")
        set("markwonVersion", "4.6.2")
    }
}
