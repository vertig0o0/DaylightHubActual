plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.plugin.compose")
    id("com.google.devtools.ksp")
}

android {
    namespace = "dev.daylight.hub"
    compileSdk = 37

    defaultConfig {
        applicationId = "dev.daylight.hub"
        minSdk = 26
        targetSdk = 37
        versionCode = 3
        versionName = "v2"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        ndk { abiFilters += listOf("arm64-v8a", "x86_64") }
        externalNativeBuild { cmake { arguments += listOf("-DANDROID_STL=c++_shared") } }
    }

    buildFeatures { compose = true }
    ndkVersion = "30.0.16248370"
    if (rootProject.file(".tools/android-ndk-r30").exists()) ndkPath = rootProject.file(".tools/android-ndk-r30").absolutePath
    externalNativeBuild { cmake { path = file("src/main/cpp/CMakeLists.txt"); version = "3.31.6" } }
    testOptions { unitTests.isReturnDefaultValues = true }
    // AGP 9.5's BidiSpoofing visitor loops in Kotlin PSI comment traversal here.
    // verifySourceText below checks the actual source characters without that visitor.
    lint { disable += "BidiSpoofing" }
    packaging { resources.excludes += "/META-INF/{AL2.0,LGPL2.1}" }
}

dependencies {
    val composeBom = platform("androidx.compose:compose-bom:2026.09.00")
    implementation(composeBom)
    implementation("androidx.core:core-ktx:1.19.1")
    implementation("androidx.activity:activity-compose:1.13.0")
    implementation("androidx.navigationevent:navigationevent-compose:1.0.0")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.11.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.11.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.11.0")
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.material:material-icons-extended")
    implementation("androidx.room:room-runtime:2.8.5")
    implementation("androidx.room:room-ktx:2.8.5")
    ksp("androidx.room:room-compiler:2.8.5")
    implementation("com.mohamedrejeb.richeditor:richeditor-compose:1.2.1")
    implementation("com.mohamedrejeb.richeditor:richeditor-compose-json:1.2.1")
    implementation("org.dmfs:lib-recur:0.17.1")
    testImplementation("junit:junit:4.13.2")
    testImplementation("org.json:json:20250517")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.10.2")
    androidTestImplementation("androidx.test.ext:junit:1.3.0")
    androidTestImplementation("androidx.test:runner:1.7.0")
    // Compose's transitive Espresso version still reflects removed InputManager APIs.
    androidTestImplementation("androidx.test.espresso:espresso-core:3.7.0")
    androidTestImplementation("androidx.room:room-testing:2.8.5")
    androidTestImplementation(composeBom)
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
    debugImplementation("androidx.compose.ui:ui-tooling")
}
ksp { arg("room.schemaLocation", "$projectDir/schemas") }

val verifySourceText by tasks.registering {
    inputs.files(fileTree("src") { include("**/*.kt", "**/*.java", "**/*.cpp", "**/*.xml") })
    doLast {
        inputs.files.forEach { source ->
            check(source.readText().none { it.code in 0x202A..0x202E || it.code in 0x2066..0x2069 }) {
                "Unexpected bidirectional control character in $source"
            }
        }
    }
}
tasks.named("preBuild") { dependsOn(verifySourceText) }
