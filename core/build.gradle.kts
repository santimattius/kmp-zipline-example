plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlinSerialization)
    id("app.cash.zipline")
}
kotlin {
    androidTarget()
    iosArm64()
    iosX64()
    iosSimulatorArm64()
    js {
        browser()
        binaries.executable()
    }

    applyDefaultHierarchyTemplate()

    sourceSets {
        val commonMain by getting {
            dependencies {
                api(libs.zipline.core)
            }
        }
        val hostMain by creating {
            dependsOn(commonMain)
            dependencies {
                implementation(libs.zipline.loader)
                api(libs.okio.core)
            }
        }

        val androidMain by getting {
            dependsOn(hostMain)
            dependencies {
                implementation(libs.okHttp.core)
            }
        }
        val iosMain by getting {
            dependsOn(hostMain)
        }

    }
}

android {
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    namespace = "com.santimattius.kmp.core"
    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}