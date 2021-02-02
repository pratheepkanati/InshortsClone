package com.easycodingg.inshortsclone.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.easycodingg.inshortsclone.models.Article
import com.easycodingg.inshortsclone.models.BreakingNews
import com.easycodingg.inshortsclone.models.CategoryNews
import com.easycodingg.inshortsclone.models.SearchNews

@Database(
        entities = [Article::class, BreakingNews::class, CategoryNews::class, SearchNews::class],
        version = 1
)
abstract class NewsDatabase: RoomDatabase() {

    abstract fun getNewsDao(): NewsDao
}