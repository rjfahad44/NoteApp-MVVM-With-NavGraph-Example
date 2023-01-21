package com.example.noteapp_mvvm_with_navgraph_example.presentation.fragment


import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.noteapp_mvvm_with_navgraph_example.R
import com.example.noteapp_mvvm_with_navgraph_example.data.adapter.NoteAdapter
import com.example.noteapp_mvvm_with_navgraph_example.data.local.entities.Note
import com.example.noteapp_mvvm_with_navgraph_example.data.viewmodel.NoteViewModel
import com.example.noteapp_mvvm_with_navgraph_example.databinding.FragmentHomeBinding
import com.example.noteapp_mvvm_with_navgraph_example.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(), SearchView.OnQueryTextListener {


    private val notesViewModel by activityViewModels<NoteViewModel>()

    private lateinit var noteAdapter: NoteAdapter

    override fun setBinding(): FragmentHomeBinding = FragmentHomeBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpRecyclerView()

        binding?.fabAddNote?.setOnClickListener {
            it.findNavController().navigate(R.id.action_homeFragment_to_newNoteFragment)
        }
    }

    private fun setUpRecyclerView() {
        noteAdapter = NoteAdapter()

        binding?.recyclerView?.apply {
            layoutManager = StaggeredGridLayoutManager(
                2,
                StaggeredGridLayoutManager.VERTICAL
            )
            setHasFixedSize(true)
            adapter = noteAdapter
        }

        activity?.let {
            notesViewModel.allNotes().observe(it) { note ->
                noteAdapter.differ.submitList(note)
                updateUI(note)
            }
        }

    }

    private fun updateUI(note: List<Note>) {
        if (note.isNotEmpty()) {
            binding?.cardView?.visibility = View.GONE
            binding?.recyclerView?.visibility = View.VISIBLE
        } else {
            binding?.cardView?.visibility = View.VISIBLE
            binding?.recyclerView?.visibility = View.GONE
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear()
        inflater.inflate(R.menu.home_menu, menu)
        val mMenuSearch = menu.findItem(R.id.menu_search).actionView as SearchView
        mMenuSearch.isSubmitButtonEnabled = false
        mMenuSearch.setOnQueryTextListener(this)

    }


    override fun onQueryTextSubmit(query: String?): Boolean {
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        if (newText != null) {
            searchNote(newText)
        }
        return true
    }


    private fun searchNote(query: String?) {
        val searchQuery = "%$query%"
        notesViewModel.searchNote(searchQuery).observe(this) { list ->
            noteAdapter.differ.submitList(list)
        }
    }
}