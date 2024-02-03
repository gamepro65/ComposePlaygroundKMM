package serviceWorker

import kotlinx.coroutines.*
import org.w3c.fetch.Response
import org.w3c.workers.*

external val self: ServiceWorkerGlobalScope
val scope = MainScope()

fun main() {
    installServiceWorker()
}

const val MAIN_CACHE = "mainCache"

fun installServiceWorker() {
    val offlineContent = arrayOf(
            "./index.html",
            "./index.css",
            "./client.js",
            "./kotlin-192.png",
            "./kotlin-512.png",
            "./manifest.webmanifest",
            "./skiko.js",
            "./skiko.wasm",
            "./serviceWorker.js"
    )

    self.addEventListener("install", { event ->
        event as InstallEvent
        console.log("I am installed.")
        scope.async {
            val cache = self.caches.open(MAIN_CACHE).await()
            cache.addAll(offlineContent).await()
            console.log("Offline cache loaded.")
        }.let {
            event.waitUntil(it.asPromise())
        }
    })

    // using the "Network falling back to cache" strategy (https://developers.google.com/web/ilt/pwa/caching-files-with-service-worker#network_falling_back_to_the_cache)
    self.addEventListener("fetch",  { event ->
        event as FetchEvent
        if (event.request.url.contains("http").not()) return@addEventListener

        scope.async {
            val cache = self.caches.open(MAIN_CACHE).await()
            try {
                val response = self.fetch(event.request).await()
                cache.put(event.request, response.clone()).await()
                return@async response
            } catch (e: Throwable) {
                return@async self.caches.match(event.request).await().unsafeCast<Response>()
            }
        }.let {
            event.respondWith(it.asPromise())
        }
    });
}
