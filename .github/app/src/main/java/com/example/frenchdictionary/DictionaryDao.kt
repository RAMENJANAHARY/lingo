package com.example.frenchdictionary

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface DictionaryDao {

    // Search French → English (user types French word, gets English translation)
    @Query("SELECT * FROM DictionaryEntry WHERE word LIKE :query || '%' LIMIT 10")
    suspend fun searchFrenchToEnglish(query: String): List<DictionaryEntry>

    // Search English → French (user types English word, gets French translation)
    @Query("SELECT * FROM DictionaryEntry WHERE translation LIKE :query || '%' LIMIT 10")
    suspend fun searchEnglishToFrench(query: String): List<DictionaryEntry>

    // Helper: Check if database is empty (used for initial population)
    @Query("SELECT COUNT(*) FROM DictionaryEntry")
    suspend fun getCount(): Int

    // Insert multiple entries at once (used to pre-fill the database)
    @Insert
    suspend fun insertAll(entries: List<DictionaryEntry>)
}
