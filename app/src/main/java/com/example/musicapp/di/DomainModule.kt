package com.example.musicapp.di

import com.example.musicapp.domain.valid.EmailValid
import com.example.musicapp.domain.valid.PasswordValid
import org.koin.dsl.module

val domainModule = module {
    factory<EmailValid> {
        EmailValid()
    }

    factory<PasswordValid> {
        PasswordValid()
    }
}