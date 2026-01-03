package com.example.frenchdictionary

import androidx.room.Dao
import androidx.room.Query

@Dao
interface DictionaryDao {
    @Query("SELECT * FROM DictionaryEntry WHERE word LIKE :query || '%' LIMIT 5")
    suspend fun searchFrenchToEnglish(query: String): List<DictionaryEntry>

    @Query("SELECT * FROM DictionaryEntry WHERE translation LIKE :query || '%' LIMIT 5")
    suspend fun searchEnglishToFrench(query: String): List<DictionaryEntry>
}
