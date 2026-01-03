package com.example.frenchdictionary

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class DictionaryEntry(
    @PrimaryKey val word: String,
    val translation: String
)
