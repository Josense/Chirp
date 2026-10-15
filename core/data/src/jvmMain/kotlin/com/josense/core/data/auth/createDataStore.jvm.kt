package com.josense.core.data.auth

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import io.ktor.http.ContentDisposition.Companion.File
import java.io.File

fun createDataStore(): DataStore<Preferences> {
    return createDataStore {
        val appDataDir = getJvmAppDataDirectory("YourAppName")
        if (!appDataDir.exists()) {
            appDataDir.mkdirs()
        }
        File(appDataDir, DATA_STORE_FILE_NAME).absolutePath
    }

}

private fun getJvmAppDataDirectory(appName: String): File {
    val osName = System.getProperty("os.name").lowercase()
    val userHome = System.getProperty("user.home")

    return when {
        // Windows: C:\Users\Username\AppData\Roaming\YourAppName
        osName.contains("win") -> {
            val appData = System.getenv("APPDATA") ?: "$userHome\\AppData\\Roaming"
            File(appData, appName)
        }
        // macOS: /Users/Username/Library/Application Support/YourAppName
        osName.contains("mac") -> {
            File("$userHome/Library/Application Support", appName)
        }
        // Linux / Unix: /home/username/.local/share/YourAppName
        else -> {
            val xdgData = System.getenv("XDG_DATA_HOME") ?: "$userHome/.local/share"
            File(xdgData, appName)
        }
    }
}