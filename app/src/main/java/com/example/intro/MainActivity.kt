package com.example.intro

import android.os.Bundle
import android.content.Intent
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.intro.model.Note
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private lateinit var noteViewModel: NoteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialiser le RecyclerView
        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        // Initialiser l'Adapter
        val adapter = NoteAdapter { note ->
            // Ouvrir une nouvelle activité pour afficher/modifier la note
            val intent = Intent(this, EditNoteActivity::class.java)
            intent.putExtra("note_title", note.title)
            intent.putExtra("note_content", note.content)
            startActivity(intent)
        }

        recyclerView.adapter = adapter

        // Initialiser le ViewModel
        noteViewModel = ViewModelProvider(this).get(NoteViewModel::class.java)

        // Observer les changements dans la liste des notes
        noteViewModel.allNotes.observe(this, Observer { notes ->
            notes?.let { adapter.setNotes(it) }
        })

        // Floating Action Button pour ajouter une nouvelle note
        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener {
            showAddNoteDialog()
        }
    }

    private fun showAddNoteDialog() {
        // Utiliser LayoutInflater pour gonfler le layout de la popup
        val inflater: LayoutInflater = LayoutInflater.from(this)
        val view = inflater.inflate(R.layout.dialog_add_note, null)

        val editTextTitle = view.findViewById<EditText>(R.id.edit_text_title)
        val editTextContent = view.findViewById<EditText>(R.id.edit_text_content)

        // Créer l'AlertDialog
        val dialog = AlertDialog.Builder(this)
            .setTitle("Ajouter une nouvelle note")
            .setView(view)  // Associer le layout à la popup
            .setPositiveButton("Ajouter") { _, _ ->
                val title = editTextTitle.text.toString()
                val content = editTextContent.text.toString()

                // Validation des champs
                if (title.trim().isEmpty() || content.trim().isEmpty()) {
                    Toast.makeText(this, "Les champs ne peuvent pas être vides", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                // Créer une nouvelle note et l'insérer dans la base de données
                val newNote = Note(title, content)
                noteViewModel.insert(newNote) // Insérer la note via le ViewModel

                Toast.makeText(this, "Note ajoutée", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Annuler") { dialogInterface, _ ->
                // Fermer le dialog sans ajouter de note
                dialogInterface.dismiss()
            }
            .create()

        dialog.show()
    }
}