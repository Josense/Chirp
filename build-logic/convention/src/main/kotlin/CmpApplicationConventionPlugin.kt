import com.josense.chirp.convention.configureAndroidTarget
import com.josense.chirp.convention.configureIosTarget
import com.josense.chirp.convention.configureJvmTarget
import org.gradle.api.Plugin
import org.gradle.api.Project

class CmpApplicationConventionPlugin: Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("org.jetbrains.kotlin.multiplatform")
                apply("com.android.kotlin.multiplatform.library")
                apply("org.jetbrains.compose")
                apply("org.jetbrains.kotlin.plugin.compose")
            }

            configureAndroidTarget()
            configureIosTarget()
            configureJvmTarget()
        }
    }
}