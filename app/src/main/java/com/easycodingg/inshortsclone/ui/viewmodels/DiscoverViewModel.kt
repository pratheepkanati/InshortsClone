package com.easycodingg.inshortsclone.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.easycodingg.inshortsclone.models.Query
import com.easycodingg.inshortsclone.repo.NewsRepository
import com.easycodingg.inshortsclone.util.Constants.BOOKMARKS_QUERY_TYPE
import com.easycodingg.inshortsclone.util.Constants.CATEGORY_QUERY_TYPE
import com.easycodingg.inshortsclone.util.Constants.COUNTRY_QUERY_TYPE
import com.easycodingg.inshortsclone.util.Constants.SEARCH_QUERY_TYPE
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class DiscoverViewModel @Inject constructor(
    private val repository: NewsRepository
): ViewModel() {

    var queryList: List<Query> = listOf()

    init {
        queryList = getQueriesList()

        viewModelScope.launch {
            repository.deleteCachedArticlesOlderThanTimeStamp(
                    timeStampInMillis = System.currentTimeMillis() - TimeUnit.DAYS.toMillis(1)
            )
        }
    }

    private fun getQueriesList() =
            listOf(
                    Query("Trending", COUNTRY_QUERY_TYPE, "in"),
                    Query("Bookmarked", BOOKMARKS_QUERY_TYPE, "bookmarks"),
                    Query("Business", CATEGORY_QUERY_TYPE, "business"),
                    Query("Entertainment", CATEGORY_QUERY_TYPE, "entertainment"),
                    Query("Health", CATEGORY_QUERY_TYPE, "health"),
                    Query("Sports", CATEGORY_QUERY_TYPE, "sports"),
                    Query("Technology", CATEGORY_QUERY_TYPE, "technology"),
                    Query("Science", CATEGORY_QUERY_TYPE, "science"),
                    Query("Travel", SEARCH_QUERY_TYPE, "travel"),
                    Query("Automobile", SEARCH_QUERY_TYPE, "automobile"),
                    Query("Startups", SEARCH_QUERY_TYPE, "startups"),
            )
}