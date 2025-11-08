@file:Suppress("DEPRECATION")

package com.example.bookapp.features.browser

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.example.bookapp.core.BaseFragment
import com.example.bookapp.databinding.FragmentBookBowserBinding
import com.example.bookapp.features.adapter.BookAdapter
import com.example.bookapp.features.models.Book

class BookBrowserFragment : BaseFragment<FragmentBookBowserBinding>() {
    private lateinit var adapter: BookAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val books = listOf(
            Book("Eragon", "Tiểu thuyết", "https://images.dog.ceo//breeds//labrador//fudji.jpg", 63, 63),
            Book("Eldest", "Fantasy", "https://images.dog.ceo//breeds//labrador//fudji.jpg", 10, 40),
            Book("Eldest", "Fantasy", "https://images.dog.ceo//breeds//labrador//fudji.jpg", 10, 40),
            Book("Eldest", "Fantasy", "https://images.dog.ceo//breeds//labrador//fudji.jpg", 10, 40),
            Book("Eldest", "Fantasy", "https://images.dog.ceo//breeds//labrador//fudji.jpg", 10, 40),
        )
        adapter = BookAdapter(books) { book ->
            Toast.makeText(context, "Clicked: ${book.title}", Toast.LENGTH_SHORT).show()
        }

        views.rcBook.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = this@BookBrowserFragment.adapter
        }

        views.btnBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()

    }

    override fun onLowMemory() {
        super.onLowMemory()

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

    }

    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentBookBowserBinding {
        return FragmentBookBowserBinding.inflate(inflater, container, false)
    }
}