package com.example.roomDatabase

import androidx.lifecycle.LiveData
import com.example.dao.NoteDao
import com.example.dataclass.Note
import android.os.AsyncTask

// Declares the DAO as a private property in the constructor. Pass in the DAO
// instead of the whole database, because you only need access to the DAO
class NoteRepository(private val noteDao: NoteDao) {

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    val allNotes: LiveData<List<Note>> = noteDao.getAlphabetizedWords()

    suspend fun insert(note: Note) {
        noteDao.insert(note)
    }

     fun delete(note: Note?) {
        noteDao.delete(note)
    }
    suspend fun deleteAll(){
        noteDao.deleteAll()
    }

}


