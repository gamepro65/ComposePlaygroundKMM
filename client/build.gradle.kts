import org.jetbrains.kotlin.gradle.targets.js.dsl.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.jetbrainsCompose)
}

group "com.drury.composeplayground"
version "1.0-SNAPSHOT"

repositories {
    mavenLocal()
    google()
    mavenCentral()
}

kotlin {
    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        moduleName = "client"
        browser {
            commonWebpackConfig {
                outputFileName = "client.js"
                devServer = (devServer ?: KotlinWebpackConfig.DevServer()).apply {
                    static = (static ?: mutableListOf()).apply {
                        // Serve sources to debug inside browser
                        add(project.projectDir.path)
                        add(project.projectDir.path + "/commonMain/")
                        add(project.projectDir.path + "/wasmJsMain/")
                        add(parent?.path + "/serviceWorker/build/dist/js/productionExecutable/")
                    }
                }
            }
        }
        binaries.executable()
    }

    sourceSets {
        commonMain.dependencies {
            implementation(compose.ui)
            implementation(compose.foundation)
            @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
            implementation(compose.material3)
            implementation(compose.material)
            implementation(compose.runtime)
            implementation(libs.kotlin.datetime)
        }

        jsMain.dependencies {
            implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-js:1.6.0")
        }

        jsTest.dependencies {
            implementation(kotlin("test-js"))
        }
    }
}

compose.experimental {
    web.application {}
}

tasks["wasmJsBrowserDistribution"].dependsOn(":serviceWorker:copyServiceWorker")
//tasks["wasmJsBrowserDevelopmentRun"].dependsOn(":serviceWorker:copyServiceWorkerWebpack")
