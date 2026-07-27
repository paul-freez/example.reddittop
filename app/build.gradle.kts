import com.android.build.api.dsl.ApplicationExtension
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.protobuf)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kapt)
    alias(libs.plugins.hilt)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
    alias(libs.plugins.secrets)
}

configure<ApplicationExtension> {
    compileSdk = 37
    namespace = "com.testsite.reddittop"

    defaultConfig {
        applicationId = "com.testsite.reddittop"
        minSdk = 28
        targetSdk = 37
        versionCode = 3
        versionName = "1.2"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildFeatures {
        dataBinding = true
        compose = true
        buildConfig = true
    }

    sourceSets {
        getByName("main") {
            java.srcDir("src/main/kotlin")
        }
    }
}

secrets {
    propertiesFileName = "secrets.properties"
}

kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_17)
    }
}

protobuf {
    protoc {
        artifact = libs.protoc.get().toString()
    }

    generateProtoTasks {
        all().forEach { task ->
            task.builtins {
                create("java") {
                    option("lite")
                }
                create("kotlin") {
                    option("lite")
                }
            }
        }
    }
}

dependencies {
    implementation(libs.datastore.preferences.core)
    // Compose
    val composeBom = platform(libs.compose.bom)
    implementation(composeBom)

    // Material Design
    implementation(libs.compose.material3)
    // Android Studio Preview support
    implementation(libs.compose.ui.tooling.preview)
    debugImplementation(libs.compose.ui.tooling)
    // Optional - Integration with activities
    implementation(libs.compose.activity)
    // Optional - Integration with ViewModels
    implementation(libs.compose.lifecycle.viewmodel)
    // Optional - Integration with LiveData
    implementation(libs.compose.runtime.livedata)

    implementation(libs.appcompat)
    implementation(libs.recyclerview)
    implementation(libs.swiperefreshlayout)
    implementation(libs.material)
    implementation(libs.constraintlayout)
    implementation(libs.databinding.common)
    implementation(libs.browser)
    implementation(libs.core.ktx)
    implementation(libs.lifecycle.viewmodel.ktx)

    implementation(libs.datastore)
    implementation(libs.protobuf.javalite)
    implementation(libs.protobuf.kotlin.lite)

    implementation(libs.android.customtabs)

    implementation(libs.lifecycle.livedata.ktx)
    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.paging.runtime)

    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.gson)
    implementation(libs.okhttp.logging)

    implementation(libs.glide)
    ksp(libs.glide.ksp)

    implementation(libs.coil.compose)

    implementation(libs.timber)

    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    implementation(libs.hilt.navigation.compose)
    implementation(libs.navigation.compose)
}
