package com.example.dataclass

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "note_table")

class Note
    (@PrimaryKey (autoGenerate= true) val id: Int? =null,
    @ColumnInfo(name = "note") val note: String,
    @ColumnInfo(name="priority")val priority: String)
   // @ColumnInfo(name= "data")val date: String)


