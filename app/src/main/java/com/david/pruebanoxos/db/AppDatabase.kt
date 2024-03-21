package com.david.pruebanoxos.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.david.pruebanoxos.db.dao.TransaccionDao
import com.david.pruebanoxos.db.entities.TransaccionEntity

@Database(entities = [TransaccionEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase(){
    abstract val transactionDao: TransaccionDao
}