package com.josense.core.presentation.util

import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.window.core.layout.WindowSizeClass


@Composable
fun currentDeviceConfiguration(): DeviceConfiguration {
    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
    return DeviceConfiguration.fromWindowSizeClass(windowSizeClass)
}

enum class DeviceConfiguration {
    MOBILE_PORTRAIT,
    MOBILE_LANDSCAPE,
    TABLET_PORTRAIT,
    TABLET_LANDSCAPE,
    DESKTOP,
    ;


    companion object {
        /**
         * 这个算法很奇怪
         *  ------------------------- 600 dp ------------------ 840 dp --------------------------
         *  ------                    ------                    ------                     ------
         *  ------     Desktop        ------      Desktop       ------   Mobile Landscape  ------
         *  ------                    ------                    ------                     ------
         *  490 dp ------------------ 600 dp ------------------ 840 dp ------------------- 490 dp
         *  ------                    ------                    ------                     ------
         *  ------  Mobile Portrait   ------      Desktop       ------  Tablet Landscape   ------
         *  ------                    ------                    ------                     ------
         *  900 dp ------------------ 600 dp ------------------ 840 dp ------------------- 900 dp
         *  ------                    ------                    ------                     ------
         *  ------  Mobile Portrait   ------   Tablet Portrait  ------     Desktop         ------
         *  ------                    ------                    ------                     ------
         *  ------------------------- 600 dp ------------------ 840 dp --------------------------
         *
         *  感觉下面这样更合理一点，暂时先保持上面和课程对齐吧
         *  ------------------------- 600 dp ------------------ 840 dp --------------------------
         *  ------                    ------                    ------                     ------
         *  ------  Mobile Portrait   ------  Mobile Landscape  ------   Mobile Landscape  ------
         *  ------                    ------                    ------                     ------
         *  490 dp ------------------ 600 dp ------------------ 840 dp ------------------- 490 dp
         *  ------                    ------                    ------                     ------
         *  ------  Mobile Portrait   ------   Tablet Portrait  ------  Tablet Landscape   ------
         *  ------                    ------                    ------                     ------
         *  900 dp ------------------ 600 dp ------------------ 840 dp ------------------- 900 dp
         *  ------                    ------                    ------                     ------
         *  ------  Mobile Portrait   ------   Tablet Portrait  ------     Desktop         ------
         *  ------                    ------                    ------                     ------
         *  ------------------------- 600 dp ------------------ 840 dp --------------------------
         */
        fun fromWindowSizeClass(windowSizeClass: WindowSizeClass): DeviceConfiguration {
            return with(windowSizeClass) {
                when {
                    minWidthDp < WindowSizeClass.WIDTH_DP_MEDIUM_LOWER_BOUND &&
                            minHeightDp >= WindowSizeClass.HEIGHT_DP_MEDIUM_LOWER_BOUND -> MOBILE_PORTRAIT

                    minWidthDp >= WindowSizeClass.WIDTH_DP_EXPANDED_LOWER_BOUND &&
                            minHeightDp < WindowSizeClass.HEIGHT_DP_MEDIUM_LOWER_BOUND -> MOBILE_LANDSCAPE

                    minWidthDp in WindowSizeClass.WIDTH_DP_MEDIUM_LOWER_BOUND..WindowSizeClass.WIDTH_DP_EXPANDED_LOWER_BOUND &&
                            minHeightDp >= WindowSizeClass.HEIGHT_DP_EXPANDED_LOWER_BOUND -> TABLET_PORTRAIT

                    minWidthDp >= WindowSizeClass.WIDTH_DP_EXPANDED_LOWER_BOUND &&
                            minHeightDp in WindowSizeClass.HEIGHT_DP_MEDIUM_LOWER_BOUND..WindowSizeClass.HEIGHT_DP_EXPANDED_LOWER_BOUND -> TABLET_LANDSCAPE

                    else -> DESKTOP
                }
            }
        }
    }
}
