package com.example.remindme.RemindMe.view.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.remindme.RemindMe.view.Model.HatirlaticiModel

@Dao
interface HatirlaticiDao {

    // Tüm hatırlatıcıları alır
    @Query("SELECT * FROM HatirlaticiModel")
    suspend fun getAllHatirlat(): List<HatirlaticiModel>

    // Belirli bir ID'ye göre hatırlatıcıyı alır
    @Query("SELECT * FROM HatirlaticiModel WHERE id = :id")
    suspend fun getHatilarById(id: Int): HatirlaticiModel?

    // Yeni bir hatırlatıcı ekler
    @Insert
    suspend fun insert(hatirlat: HatirlaticiModel)

    // Var olan bir hatırlatıcıyı günceller
    @Update
    suspend fun update(hatirlat: HatirlaticiModel)

    // Belirli bir hatırlatıcıyı siler
    @Delete
    suspend fun delete(hatirlat: HatirlaticiModel)
}

