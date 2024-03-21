package com.david.pruebanoxos.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.david.pruebanoxos.db.entities.TransaccionEntity

@Dao
interface TransaccionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(transaction: TransaccionEntity)

    @Query("SELECT * FROM 'transaction' WHERE authorization = 1")
    suspend fun getAll():List<TransaccionEntity>

    @Update
    suspend fun updateTransaction(vararg users: TransaccionEntity)
}