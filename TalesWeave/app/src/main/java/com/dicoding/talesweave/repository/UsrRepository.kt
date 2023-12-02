package com.dicoding.talesweave.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.dicoding.talesweave.data.UIState
import com.dicoding.talesweave.data.UsrModel
import com.dicoding.talesweave.data.UserPreference
import com.dicoding.talesweave.api.APIservice
import com.dicoding.talesweave.api.APIconfig
import com.dicoding.talesweave.paging.StoryPagingSource
import com.dicoding.talesweave.response.AddResp
import com.dicoding.talesweave.response.ErrorResp
import com.dicoding.talesweave.response.ListStoryItem
import com.dicoding.talesweave.response.LoginResponse
import com.dicoding.talesweave.response.RegistResp
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import java.io.File
import java.net.SocketTimeoutException

class UsrRepository private constructor(
    private val userPreference: UserPreference,
    private val apiService: APIservice
) {

    suspend fun saveSession(user: UsrModel) {
        userPreference.saveSession(user)
    }

    fun getSession(): Flow<UsrModel> {
        return userPreference.getSession()
    }

    suspend fun logout() {
        userPreference.logout()
    }

    fun getStory(): LiveData<UIState<List<ListStoryItem>>> = liveData {
        emit(UIState.Loading)
        try {
            val pref = runBlocking {
                userPreference.getSession().first()
            }
            val response = APIconfig.getApiService(pref.token)
            val responseStory = response.getStories()
            val story = responseStory.listStory
            val storyList = story.map { data ->
                ListStoryItem(
                    data?.photoUrl,
                    data?.createdAt,
                    data?.name,
                    data?.description,
                    data?.lon,
                    data?.id,
                    data?.lat

                )
            }
            if (responseStory.error == false) {
                emit(UIState.Success(storyList))
            } else {
                emit(UIState.Error(responseStory.message ?: "Error"))
            }
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, ErrorResp::class.java)
            val errorMessage = errorBody?.message ?: "An error occurred"
            emit(UIState.Error("Failed: $errorMessage"))
        } catch (e: Exception) {
            emit(UIState.Error("Internet Issues"))
        } catch (e: SocketTimeoutException) {
            emit(UIState.Error("Read timeout occurred"))
        }

    }

    fun getStoriesPager(): LiveData<PagingData<ListStoryItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            pagingSourceFactory = {
                StoryPagingSource(userPreference)
            }
        ).liveData
    }

    companion object {
        @Volatile
        private var instance: UsrRepository? = null
        fun getInstance(
            userPreference: UserPreference, apiService: APIservice
        ): UsrRepository =
            instance ?: synchronized(this) {
                instance ?: UsrRepository(userPreference, apiService)
            }.also { instance = it }
    }

    fun getRegister(
        name: String,
        email: String,
        password: String
    ): LiveData<UIState<RegistResp>> = liveData {
        emit(UIState.Loading)
        try {
            val response = apiService.register(name, email, password)
            if (response.error == false) {
                emit(UIState.Success(response))
            } else {
                emit(UIState.Error(response.message ?: "An error occurred"))
            }
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, ErrorResp::class.java)
            val errorMessage = errorBody?.message ?: "An error occurred"
            emit(UIState.Error("Login error: $errorMessage"))
        }
    }

    fun getLogin(
        email: String,
        password: String
    ): LiveData<UIState<LoginResponse>> = liveData {
        emit(UIState.Loading)
        try {
            val response = apiService.login(email, password)
            if (response.error == false) {
                val tokenAuth = UsrModel(
                    name = response.loginResult.name ?: "",
                    userId = response.loginResult.userId ?: " ",
                    token = response.loginResult.token ?: "",
                    isLogin = true
                )
                APIconfig.token = response.loginResult.token
                userPreference.saveSession(tokenAuth)
                emit(UIState.Success(response))
            } else {
                emit(UIState.Error(response.message ?: "An error occurred"))
            }
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, ErrorResp::class.java)
            val errorMessage = errorBody?.message ?: "An error occurred"
            emit(UIState.Error("Login error: $errorMessage"))
        }
    }

    fun postStory(photoFile: File, description: String) = liveData {
        emit(UIState.Loading)
        val requestBody = description.toRequestBody("text/plain".toMediaType())
        val requestPhotoProfile = photoFile.asRequestBody("image/jpeg".toMediaType())
        val multipartBody =
            MultipartBody.Part.createFormData("photo", photoFile.name, requestPhotoProfile)
        try {
            val userPref = runBlocking {
                userPreference.getSession().first()
            }
            val response = APIconfig.getApiService(userPref.token)
            val successResponse = response.uploadImage(multipartBody, requestBody)
            emit(UIState.Success(successResponse))

        } catch (ex: HttpException) {
            val errorBody = ex.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, AddResp::class.java)
            emit(errorResponse?.message?.let { UIState.Error(it) })
        }
    }

}
