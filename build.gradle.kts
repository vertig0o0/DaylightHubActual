plugins {
    id("com.android.application") version "9.5.0" apply false
    // AGP 9.5 supplies Kotlin for Android modules; this plugin configures Compose compiler support.
    id("org.jetbrains.kotlin.plugin.compose") version "2.4.21" apply false
    id("com.google.devtools.ksp") version "2.3.12" apply false
}
