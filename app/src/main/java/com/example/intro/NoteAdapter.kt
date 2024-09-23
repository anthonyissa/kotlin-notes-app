package com.example.intro
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.intro.model.Note

class NoteAdapter(private val onItemClickListener: (Note) -> Unit) : RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    private var notes: List<Note> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.note_item, parent, false)
        return NoteViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val currentNote = notes[position]
        holder.textViewTitle.text = currentNote.title
        holder.textViewContent.text = currentNote.content

        // Détection du clic sur un élément
        holder.itemView.setOnClickListener {
            onItemClickListener(currentNote)
        }
    }

    override fun getItemCount(): Int {
        return notes.size
    }

    // Met à jour les notes et notifie le RecyclerView
    fun setNotes(notes: List<Note>) {
        this.notes = notes
        notifyDataSetChanged() // Mise à jour du RecyclerView
    }

    class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewTitle: TextView = itemView.findViewById(R.id.text_view_title)
        val textViewContent: TextView = itemView.findViewById(R.id.text_view_content)
    }
}
