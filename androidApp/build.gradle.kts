plugins {
    alias(libs.plugins.convention.android.application.compose)
    alias(libs.plugins.compose.compiler)
}

dependencies {
    implementation(project(":shared"))
}
