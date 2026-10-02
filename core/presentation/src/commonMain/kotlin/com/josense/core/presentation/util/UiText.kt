package com.josense.core.presentation.util

import androidx.compose.runtime.Composable
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.stringResource

/**
 * 统一的文本包装接口，用于在架构中解耦业务逻辑与 UI 文本展示。
 *
 * **核心目的**：
 * 让 ViewModel 或业务逻辑层无需关心平台上下文（Context）和 UI 渲染环境，
 * 即可自由组合动态字符串与多语言资源文件，完美支持国际化（i18n）。
 */
sealed interface UiText {

    /**
     * 表示直接硬编码的动态字符串。
     * 适用于后端 API 返回的错误提示、用户输入的内容或不需要国际化的文本。
     */
    data class DynamicString(val value: String): UiText

    /**
     * 表示本地化的字符串资源。
     * 适用于定义在资源文件（strings.xml 或 KMP 资源）中的多语言文本。
     *
     * @property id 资源 ID（例如 `Res.string.app_name`）。
     * @property args 格式化参数数组（例如用于填充 `欢迎你，%s！` 中的用户名）。
     */
    class Resource(
        val id: StringResource,
        val args: Array<Any> = arrayOf()
    ): UiText

    /**
     * **[UI 层调用]** 将 [UiText] 解析为实际的 [String]。
     *
     * 必须在 Compose 组合式函数（Composable）内部调用。它通过 Compose 运行时
     * 的环境变量（如 CompositionLocal）隐式获取上下文并自动响应语言切换。
     */
    @Composable
    fun UiText.asString(): String {
        return when(this) {
            is DynamicString -> value
            is Resource -> stringResource(id, *args)
        }
    }

    /**
     * **[非 UI 层/异步环境调用]** 将 [UiText] 异步解析为实际的 [String]。
     *
     * 这是一个挂起（suspend）函数，适用于无法使用 `@Composable` 的协程环境（如 ViewModelScope、
     * 后台服务、系统通知、日志记录等）。在跨平台环境（KMP）中，它会通过异步文件 I/O 读取底层资源。
     */
    suspend fun UiText.asStringAsync(): String {
        return when(this) {
            is DynamicString -> value
            is Resource -> getString(id, *args)
        }
    }
}

