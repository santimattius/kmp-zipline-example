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
}

plugins.withType<YarnPlugin> {
    the<YarnRootExtension>().yarnLockAutoReplace = true
}