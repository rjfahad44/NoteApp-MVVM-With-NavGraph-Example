package com.example.noteapp_mvvm_with_navgraph_example.data.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.noteapp_mvvm_with_navgraph_example.data.local.entities.Note
import com.example.noteapp_mvvm_with_navgraph_example.databinding.NoteLayoutAdapterBinding
import com.example.noteapp_mvvm_with_navgraph_example.presentation.fragment.HomeFragmentDirections


class NoteAdapter : RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    class NoteViewHolder(val itemBinding: NoteLayoutAdapterBinding) :
        RecyclerView.ViewHolder(itemBinding.root)

    private val differCallback =
        object : DiffUtil.ItemCallback<Note>() {
            override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
                return oldItem == newItem
            }

        }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        return NoteViewHolder(
            NoteLayoutAdapterBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val currentNote = differ.currentList[position]

        holder.itemBinding.tvNoteTitle.text = currentNote.noteTitle
        holder.itemBinding.tvNoteBody.text = currentNote.noteBody
        holder.itemBinding.dateTime.text = currentNote.updatedAt

        //Generate Random Color//
        //val random = Random()
        //val randomColor = Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256))

        holder.itemBinding.cardColor.setCardBackgroundColor(currentNote.noteColor)

        holder.itemView.setOnClickListener { view ->
            view.findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToUpdateNoteFragment(currentNote))
        }
    }

    override fun getItemCount(): Int = differ.currentList.size

}