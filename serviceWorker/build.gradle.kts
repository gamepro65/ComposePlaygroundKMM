import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackOutput.Target
plugins {
    kotlin("multiplatform")
}

kotlin {
    js(IR) {
        browser {
            webpackTask {
                output.libraryTarget = Target.SELF
            }
        }
        binaries.executable()
    }

    sourceSets {
        val jsMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-js:1.6.0")
            }
        }
    }
}

tasks.register<Copy>("copyServiceWorker") {
    dependsOn("jsBrowserDistribution")

    group = "build"
    description = "Copies unprocessed .js output to client's development build directory."

    from("$buildDir/distributions")
    into("${project(":client").buildDir}/distributions")
}

tasks.register<Copy>("copyServiceWorkerWebpack") {
    dependsOn("jsBrowserDistribution")

    group = "build"
    description = "Copies unprocessed .js output to client's development build directory."

    from("$buildDir/distributions")
    into("${project(":client").buildDir}/processedResources/js/main")
}
