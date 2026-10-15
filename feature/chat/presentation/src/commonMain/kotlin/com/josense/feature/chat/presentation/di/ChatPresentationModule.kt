package com.josense.feature.chat.presentation.di

import com.josense.feature.chat.presentation.chat_list.ChatListViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val chatPresentationModule = module {
    singleOf(::ChatListViewModel)
}