package org.umbrellahq.database.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "posts")
data class PostDatabaseEntity(
        @PrimaryKey(autoGenerate = false) var id: String,
        @ColumnInfo(name = "title") var title: String,
        @ColumnInfo(name = "author") var author: String,
        @ColumnInfo(name = "thumbnailUrl") var thumbnailUrl: String
)