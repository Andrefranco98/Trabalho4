package com.example.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.dataclass.Note


@Dao
   public interface NoteDao {

        @Query("SELECT * FROM note_table ORDER BY note ASC")
        fun getAlphabetizedWords(): LiveData<List<Note>>

        @Insert(onConflict = OnConflictStrategy.IGNORE)
        suspend fun insert(note: Note)

        @Query("DELETE FROM note_table")
        suspend fun deleteAll()

        @Delete
        fun delete(note: Note?)



}
