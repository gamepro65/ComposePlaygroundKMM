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
    js(IR) {
        browser {
            testTask {
                testLogging.showStandardStreams = true
                useKarma {
                    useChromeHeadless()
                    useFirefox()
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

tasks["jsBrowserDistribution"].dependsOn(":serviceWorker:copyServiceWorker")
tasks["jsBrowserDevelopmentRun"].dependsOn(":serviceWorker:copyServiceWorkerWebpack")
