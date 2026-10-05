plugins {
    alias(libs.plugins.convention.cmp.application)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.core.designsystem)
            implementation(projects.core.domain)
            implementation(projects.core.data)
            implementation(projects.core.presentation)

            implementation(projects.feature.auth.domain)
            implementation(projects.feature.auth.presentation)

            implementation(projects.feature.chat.database)
            implementation(projects.feature.chat.data)
            implementation(projects.feature.chat.domain)
            implementation(projects.feature.chat.presentation)


            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(libs.jetbrains.compose.material3)
            implementation(compose.ui)
            implementation(libs.jetbrains.compose.ui.tooling.preview)
            implementation(compose.components.resources)
            implementation(libs.jetbrains.compose.viewmodel)
            implementation(libs.jetbrains.lifecycle.compose)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

dependencies {
    androidRuntimeClasspath(libs.jetbrains.compose.ui.tooling)
}
