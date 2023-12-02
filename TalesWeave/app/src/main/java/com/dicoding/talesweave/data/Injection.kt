package com.dicoding.talesweave.data

import android.content.Context
import com.dicoding.talesweave.api.APIconfig
import com.dicoding.talesweave.repository.UsrRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {

    fun provideRepository(context: Context): UsrRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val user = runBlocking {
            pref.getSession().first() }
        val apiService = APIconfig.getApiService(user.token)
        return UsrRepository.getInstance(pref, apiService)

    }
}