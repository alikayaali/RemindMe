package com.example.remindme.RemindMe.view.Model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class HatirlaticiModel(
    @ColumnInfo(name = "Konu")
    var Konu:String,

    @ColumnInfo(name = "Aciklama")
    var Aciklama:String,

    @ColumnInfo(name = "Tarih")
    var Tarih:String
) {

@PrimaryKey(autoGenerate = true)
var id=0
}