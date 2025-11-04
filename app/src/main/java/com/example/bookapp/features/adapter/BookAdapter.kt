package com.example.bookapp.features.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.bookapp.R
import com.example.bookapp.databinding.ItemBookBinding
import com.example.bookapp.features.models.Book

class BookAdapter(
    private val books: List<Book>,
    private val onItemClick: (Book) -> Unit
) : RecyclerView.Adapter<BookAdapter.BookViewHolder>() {

    inner class BookViewHolder(private val binding: ItemBookBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(book: Book) = with(binding) {
            tvCategory.text = book.category
            tvTitle.text = book.title
            tvProgressPercent.text = "${book.progressPercent}% completed"
            tvProgressChapters.text = "${book.currentChapter}/${book.totalChapters} Chapters"

            Glide.with(root.context)
                .load(book.coverUrl)
                .placeholder(R.drawable.ic_book_placeholder)
                .into(ivCover)

            root.setOnClickListener { onItemClick(book) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val binding = ItemBookBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BookViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        holder.bind(books[position])
    }

    override fun getItemCount() = books.size
}
