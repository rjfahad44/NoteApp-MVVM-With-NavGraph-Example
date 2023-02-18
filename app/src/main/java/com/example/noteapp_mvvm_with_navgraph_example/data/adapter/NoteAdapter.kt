package com.example.noteapp_mvvm_with_navgraph_example.data.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.noteapp_mvvm_with_navgraph_example.R
import com.example.noteapp_mvvm_with_navgraph_example.data.local.entities.Note
import com.example.noteapp_mvvm_with_navgraph_example.databinding.NoteLayoutAdapterBinding
import com.example.noteapp_mvvm_with_navgraph_example.presentation.fragment.HomeFragmentDirections
import com.example.noteapp_mvvm_with_navgraph_example.utils.getDateTimeIntoLong


class NoteAdapter : RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    private var list = ArrayList<Note>()
    @SuppressLint("NotifyDataSetChanged")
    fun setData(newNotes: ArrayList<Note>) {
        list = newNotes
        notifyDataSetChanged()
    }
    class NoteViewHolder( private val itemBinding: NoteLayoutAdapterBinding) : RecyclerView.ViewHolder(itemBinding.root){
        fun bind(note: Note){
            itemBinding.tvNoteTitle.text = note.noteTitle
            itemBinding.tvNoteBody.text = note.noteBody
            itemBinding.dateTime.text = note.updatedAt
            itemBinding.cardColor.setCardBackgroundColor(note.noteColor)

            note.time?.let {
                itemBinding.alertTimeDate.text = it.getDateTimeIntoLong(itemBinding.root.context)
                itemBinding.setAlert.apply {
                    isVisible = true
                    setImageResource(R.drawable.ic_alarm_set)
                }
            }

            itemView.setOnClickListener { view ->
                view.findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToUpdateNoteFragment(note))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        return NoteViewHolder(
            NoteLayoutAdapterBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size

}