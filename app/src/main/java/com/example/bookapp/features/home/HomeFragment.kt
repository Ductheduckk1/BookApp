package com.example.bookapp.features.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.example.bookapp.core.BaseFragment
import com.example.bookapp.databinding.FragmentHomeBinding
import com.example.bookapp.features.adapter.BookAdapter
import com.example.bookapp.features.models.Book

class HomeFragment : BaseFragment<FragmentHomeBinding>(){
    private lateinit var adapter: BookAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val books = listOf(
            Book("Eragon", "Tiểu thuyết", "https://imgur.com/gallery/lego-totoro-lpvDvDS#/t/wallpaper", 63, 63),
            Book("Eldest", "Fantasy", "https://imgur.com/gallery/lego-totoro-lpvDvDS#/t/wallpaper", 10, 40)
        )
        adapter = BookAdapter(books) { book ->
            Toast.makeText(requireContext(), "Clicked: ${book.title}", Toast.LENGTH_SHORT).show()
        }

        views.recyclerNovel.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = this@HomeFragment.adapter
        }
        
        views.recyclerSuggestion.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = this@HomeFragment.adapter
        }
    }
    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentHomeBinding {
        return FragmentHomeBinding.inflate(inflater, container, false)
    }

}