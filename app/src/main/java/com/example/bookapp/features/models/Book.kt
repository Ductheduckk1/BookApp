package com.example.bookapp.features.models

data class Book(
    val title: String,
    val category: String,
    val coverUrl: String,
    val currentChapter: Int,
    val totalChapters: Int
) {
    val progressPercent: Int
        get() = if (totalChapters > 0) (currentChapter * 100 / totalChapters) else 0
}

