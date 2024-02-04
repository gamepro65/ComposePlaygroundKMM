plugins {
    // this is necessary to avoid the plugins to be loaded multiple times
    // in each subproject's classloader
    alias(libs.plugins.jetbrainsCompose) apply false
    alias(libs.plugins.kotlinMultiplatform) apply false
}

group = "org.example"
version = "1.0-SNAPSHOT"

subprojects {

    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }

    afterEvaluate {
        tasks.register<Copy>("copyDistributionToRoot") {
            group = "build"
            description = "Copies the distribution files to the root project distribution directory."

            from("$buildDir/dist/js/productionExecutable")
            into("${parent?.buildDir}/distributions")
        }

        tasks["build"].finalizedBy("copyDistributionToRoot")
    }
}

