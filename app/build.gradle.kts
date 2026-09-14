plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

val gstreamerRoot = providers.gradleProperty("GSTREAMER_ROOT_ANDROID")
    .orElse(providers.environmentVariable("GSTREAMER_ROOT_ANDROID"))
    .orElse("/opt/gstreamer")

android {
    namespace = "dev.ivan.gstapp"
    compileSdk = 35
    ndkVersion = "29.0.14206865"

    defaultConfig {
        applicationId = "dev.ivan.gstapp"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        externalNativeBuild {
            cmake {
                arguments += listOf(
                    "-DANDROID_STL=c++_shared",
                    "-DGSTREAMER_ROOT_ANDROID=${gstreamerRoot.get()}"
                )
                abiFilters += "arm64-v8a"
                targets += "gst-android-app"
            }
        }
    }

    externalNativeBuild {
        cmake {
            path = file("src/main/jni/CMakeLists.txt")
            version = "3.22.1"
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.appcompat:appcompat:1.7.0")
}
