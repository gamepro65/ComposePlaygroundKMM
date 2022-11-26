# Compose Playground KMM + PWA

Multiplatform Compose Playground progressive web app using 100% Kotlin.

Demo: TBD

## Get started

Run app: `./gradlew :client:jsBrowserDevelopmentRun`

Run app with hot reload: `./gradlew :client:jsBrowserDevelopmentRun --continuous` _*will not hot reload service worker_

Build for production: `./gradlew build`. The bundled files reside in `build/distributions` directory.