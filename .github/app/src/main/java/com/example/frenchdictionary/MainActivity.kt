package com.example.frenchdictionary

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.frenchdictionary.databinding.ActivityMainBinding
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var db: DictionaryDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = DictionaryDatabase.getDatabase(this)

        binding.searchEditText.addTextChangedListener(object : android.text.TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: android.text.Editable?) {
                val query = s.toString().trim()
                if (query.isNotEmpty()) {
                    search(query.lowercase())
                } else {
                    binding.resultText.text = "Translation will appear here..."
                    binding.noResultText.visibility = android.view.View.GONE
                }
            }
        })
    }

    private fun search(query: String) {
        lifecycleScope.launch {
            val frenchResults = db.dictionaryDao().searchFrenchToEnglish(query)
            val englishResults = db.dictionaryDao().searchEnglishToFrench(query)

            val results = if (frenchResults.isNotEmpty()) {
                frenchResults.map { "${it.word} → ${it.translation}" }
            } else if (englishResults.isNotEmpty()) {
                englishResults.map { "${it.translation} → ${it.word}" }
            } else emptyList()

            if (results.isNotEmpty()) {
                binding.resultText.text = results.joinToString("\n\n")
                binding.noResultText.visibility = android.view.View.GONE
            } else {
                binding.resultText.text = ""
                binding.noResultText.visibility = android.view.View.VISIBLE
            }
        }
    }
}
