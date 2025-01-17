package org.umbrellahq.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import org.umbrellahq.database.daos.ErrorNetworkDatabaseDao
import org.umbrellahq.database.daos.PostDatabaseDao
import org.umbrellahq.database.models.ErrorNetworkDatabaseEntity
import org.umbrellahq.database.models.PostDatabaseEntity
import org.umbrellahq.database.type_converters.ErrorNetworkTypeConverter

@Database(entities = [
    PostDatabaseEntity::class,
    ErrorNetworkDatabaseEntity::class
], version = 1)

@TypeConverters(ErrorNetworkTypeConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun postDao(): PostDatabaseDao
    abstract fun errorNetworkDao(): ErrorNetworkDatabaseDao

    companion object {
        @Volatile private var instance: AppDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context)= instance ?: synchronized(LOCK){
            instance ?: buildDatabase(context).also { instance = it}
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java, "simplyToDo.db"
        ).build()

        fun destroyInstance() {
            instance = null
        }
    }
}