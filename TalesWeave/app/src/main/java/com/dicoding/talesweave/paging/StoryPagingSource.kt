package com.dicoding.talesweave.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.dicoding.talesweave.api.APIconfig
import com.dicoding.talesweave.data.UserPreference
import com.dicoding.talesweave.response.ListStoryItem
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class StoryPagingSource(private val userPreference: UserPreference) : PagingSource<Int, ListStoryItem>() {

    private companion object {
        const val INITIAL_PAGE_INDEX = 1

    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ListStoryItem> {
        return try {
            val position = params.key ?: INITIAL_PAGE_INDEX
            val pref = runBlocking { userPreference.getSession().first() }
            val responseData = APIconfig.getApiService(pref.token).getStories(position, params.loadSize).listStory

            LoadResult.Page(
                data = responseData,
                prevKey = if (position == INITIAL_PAGE_INDEX) null else position - 1,
                nextKey = if (responseData.isNullOrEmpty()) null else position + 1
            )

        } catch (exception: Exception) {
            return LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, ListStoryItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }

    }
}