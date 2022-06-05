package com.euzhene.ktorandroidchat.di

import com.euzhene.ktorandroidchat.data.mapper.ChatMapper
import com.euzhene.ktorandroidchat.data.remote.ChatSocketService
import com.euzhene.ktorandroidchat.data.remote.ChatSocketServiceImpl
import com.euzhene.ktorandroidchat.data.remote.MessageService
import com.euzhene.ktorandroidchat.data.remote.MessageServiceImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.logging.*
import io.ktor.client.features.websocket.*
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Provides
    @Singleton
    fun provideHttpClient(): HttpClient =
        HttpClient(CIO) { //we use cio, not android, because the android engine doesn't support websockets
            install(Logging)
            install(WebSockets)
            install(JsonFeature) {
                serializer = KotlinxSerializer()
            }
        }

    @Provides
    @Singleton
    fun provideMessageService(client: HttpClient, mapper: ChatMapper): MessageService =
        MessageServiceImpl(client, mapper)

    @Provides
    @Singleton
    fun provideChatSocketService(client: HttpClient, mapper: ChatMapper): ChatSocketService =
        ChatSocketServiceImpl(client, mapper)
}