package org.umbrellahq.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import org.umbrellahq.database.models.PostDatabaseEntity

@Dao
interface PostDatabaseDao {
    @Query("SELECT * from posts")
    fun getAll(): Flow<List<PostDatabaseEntity>>

    @Query("SELECT * FROM posts WHERE id = :id LIMIT 1")
    suspend fun getPostById(id: String): PostDatabaseEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg postDatabaseEntity: PostDatabaseEntity)

    @Query("DELETE from posts")
    suspend fun deleteAll()
}