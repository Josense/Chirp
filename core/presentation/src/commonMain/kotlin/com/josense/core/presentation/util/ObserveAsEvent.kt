package com.josense.core.presentation.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

/**
 * 在 Compose 中安全监听来自 ViewModel 的单次事件流（One-time Events）。
 *
 * 适用于处理不需要持久化在 UI 状态（UiState）中的一次性通知，例如：
 * 弹 Toast/Snackbar 提示、页面跳转、播放动画或弹出系统对话框等。
 *
 * 本函数具备生命周期感知能力：
 * 1. 当应用切到后台（低于 STARTED 状态）时，会自动暂停事件收集以节省资源。
 * 2. 当应用回到前台（进入 STARTED 状态）时，会自动重启收集。
 * 3. 所有的事件都会在 [Dispatchers.Main.immediate] 中无延迟地立即执行。
 *
 * @param T 事件的类型。
 * @param flow 要监听的事件流，通常是 ViewModel 中的 Channel.receiveAsFlow() 或 SharedFlow。
 * @param key1 可选的重置键。当此值改变时，会强行取消当前的事件监听并重新开始。
 * @param key2 可选的重置键。作用同 [key1]。
 * @param onEvent 事件触发时的回调函数，负责执行具体的 UI 操作。
 */
@Composable
fun <T> ObserveAsEvents(
    flow: Flow<T>,
    key1: Any? = null,
    key2: Any? = null,
    onEvent: (T) -> Unit
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(lifecycleOwner.lifecycle, key1, key2) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            withContext(Dispatchers.Main.immediate) {
                flow.collect(onEvent)
            }
        }
    }
}