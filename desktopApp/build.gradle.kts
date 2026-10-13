import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.compose.compiler)
}

dependencies {
    implementation(project(":shared"))

    implementation(compose.desktop.currentOs)
    implementation(libs.kotlinx.coroutines.swing)
    implementation(libs.koin.core)
    implementation(libs.koin.logger.slf4j)
}

compose.desktop {
    application {
        mainClass = "com.josense.chirp.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "com.josense.chirp"
            packageVersion = "1.0.0"

            // 1. macOS 配置：插件会自动将此转化为 Info.plist 中的 CFBundleURLTypes
            macOS {
                bundleID = "com.josense.chirp"
                infoPlist {
                    extraKeysRawXml = """
                        <key>CFBundleURLTypes</key>
                        <array>
                            <dict>
                                <key>CFBundleURLName</key>
                                <string>https://chirp.pl-coding.com/</string>
                                <key>CFBundleURLSchemes</key>
                                <array>
                                    <string>chirp</string>
                                </array>
                            </dict>
                        </array>
                    """.trimIndent()
                }
            }
        }
    }
}