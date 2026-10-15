plugins {
    alias(libs.plugins.convention.android.application.compose)
    alias(libs.plugins.compose.compiler)
}

dependencies {
    implementation(project(":shared"))

    implementation(libs.koin.android)
    implementation(libs.core.splashscreen)
}
