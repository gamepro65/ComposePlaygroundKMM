plugins {
    kotlin("multiplatform") apply false
    id("org.jetbrains.compose") apply false
}

group = "org.example"
version = "1.0-SNAPSHOT"

subprojects {

    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }

    afterEvaluate {
        tasks.register<Copy>("copyDistributionToRoot") {
            group = "build"
            description = "Copies the distribution files to the root project distribution directory."

            from("$buildDir/distributions")
            into("${parent?.buildDir}/distributions")
        }

        tasks["build"].finalizedBy("copyDistributionToRoot")
    }
}

