package com.example.frenchdictionary

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [DictionaryEntry::class], version = 1, exportSchema = false)
abstract class DictionaryDatabase : RoomDatabase() {

    abstract fun dictionaryDao(): DictionaryDao

    companion object {
        @Volatile
        private var INSTANCE: DictionaryDatabase? = null

        fun getDatabase(context: Context): DictionaryDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DictionaryDatabase::class.java,
                    "dictionary_db"
                )
                    .fallbackToDestructiveMigration()
                    .build()

                // Pre-populate with common French-English words on first creation
                CoroutineScope(Dispatchers.IO).launch {
                    val dao = instance.dictionaryDao()
                    // Only insert if database is empty
                    if (dao.getCount() == 0) {
                        val words = listOf(
                            // French → English
                            DictionaryEntry("bonjour", "hello"),
                            DictionaryEntry("au revoir", "goodbye"),
                            DictionaryEntry("merci", "thank you"),
                            DictionaryEntry("s'il vous plaît", "please"),
                            DictionaryEntry("oui", "yes"),
                            DictionaryEntry("non", "no"),
                            DictionaryEntry("amour", "love"),
                            DictionaryEntry("ami", "friend"),
                            DictionaryEntry("famille", "family"),
                            DictionaryEntry("maison", "house"),
                            DictionaryEntry("eau", "water"),
                            DictionaryEntry("pain", "bread"),
                            DictionaryEntry("vin", "wine"),
                            DictionaryEntry("fromage", "cheese"),
                            DictionaryEntry("école", "school"),
                            DictionaryEntry("travail", "work"),
                            DictionaryEntry("temps", "time / weather"),
                            DictionaryEntry("jour", "day"),
                            DictionaryEntry("nuit", "night"),
                            DictionaryEntry("beau", "beautiful / handsome"),

                            // English → French (for bidirectional search)
                            DictionaryEntry("hello", "bonjour"),
                            DictionaryEntry("goodbye", "au revoir"),
                            DictionaryEntry("thank you", "merci"),
                            DictionaryEntry("please", "s'il vous plaît"),
                            DictionaryEntry("yes", "oui"),
                            DictionaryEntry("no", "non"),
                            DictionaryEntry("love", "amour"),
                            DictionaryEntry("friend", "ami / amie"),
                            DictionaryEntry("family", "famille"),
                            DictionaryEntry("house", "maison"),
                            DictionaryEntry("water", "eau"),
                            DictionaryEntry("bread", "pain"),
                            DictionaryEntry("wine", "vin"),
                            DictionaryEntry("cheese", "fromage"),
                            DictionaryEntry("school", "école"),
                            DictionaryEntry("work", "travail"),
                            DictionaryEntry("time", "temps"),
                            DictionaryEntry("day", "jour"),
                            DictionaryEntry("night", "nuit"),
                            DictionaryEntry("beautiful", "beau / belle")
                        )
                        dao.insertAll(words)
                    }
                }

                INSTANCE = instance
                instance
            }
        }
    }
}
