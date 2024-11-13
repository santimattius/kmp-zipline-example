import app.cash.zipline.loader.SignatureAlgorithmId
import org.jetbrains.kotlin.gradle.targets.js.yarn.YarnPlugin
import org.jetbrains.kotlin.gradle.targets.js.yarn.YarnRootExtension

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.kotlinSerialization)
    id("app.cash.zipline")
}

kotlin {
    applyDefaultHierarchyTemplate()

    js {
        browser()
        binaries.executable()
    }


    sourceSets {
        commonMain.dependencies {
            api(libs.zipline.core)
            api(libs.kotlinx.coroutines.core)
            implementation(projects.core)
        }

        jsMain.dependencies {}
    }
}

zipline {
    mainFunction.set("com.santimattius.server.main")
    signingKeys{
        create("key1"){
            privateKeyHex.set("48d701dfbcf924c71f3a1c36e40c7a881092b1cfd2f1a0d60fc8e2df8606d02d")
            algorithmId.set(SignatureAlgorithmId.Ed25519)
        }
        create("key2"){
            privateKeyHex.set("3041020100301306072a8648ce3d020106082a8648ce3d030107042730250201010420fa385f4496620d77120f5fcec0568c205cb842374bf3faf45cdbd6aede73c755")
            algorithmId.set(SignatureAlgorithmId.EcdsaP256)
        }
    }
}

plugins.withType<YarnPlugin> {
    the<YarnRootExtension>().yarnLockAutoReplace = true
}