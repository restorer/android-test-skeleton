package com.example.test.feature.di

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class IsLogTag

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class IsImdbApiKey

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class IsMainDispatcher

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class IsIoDispatcher
