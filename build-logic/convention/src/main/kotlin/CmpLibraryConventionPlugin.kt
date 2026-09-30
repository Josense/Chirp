import com.josense.chirp.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.attributes.Attribute
import org.gradle.kotlin.dsl.dependencies

class CmpLibraryConventionPlugin: Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.josense.convention.kmp.library")
                apply("org.jetbrains.kotlin.plugin.compose")
                apply("org.jetbrains.compose")
            }

            // The JavaCompile task generated for an empty KMP JVM source set does not
            // request Kotlin's platform attribute. Add it to JVM classpaths so
            // multiplatform dependencies with Android and desktop variants resolve
            // to their desktop artifact instead of remaining ambiguous.
            val kotlinPlatformType = Attribute.of("org.jetbrains.kotlin.platform.type", String::class.java)
            configurations.matching {
                it.name == "jvmMainCompileClasspath" || it.name == "jvmMainRuntimeClasspath"
            }.configureEach {
                attributes.attribute(kotlinPlatformType, "jvm")
            }

            dependencies {
                "commonMainImplementation"(libs.findLibrary("jetbrains-compose-ui").get())
                "commonMainImplementation"(libs.findLibrary("jetbrains-compose-foundation").get())
                "commonMainImplementation"(libs.findLibrary("jetbrains-compose-material3").get())
                "commonMainImplementation"(libs.findLibrary("jetbrains-compose-material-icons-core").get())
            }
        }
    }
}