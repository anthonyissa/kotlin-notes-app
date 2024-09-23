package com.example.intro
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.intro.model.Note

class EditNoteActivity : AppCompatActivity() {

    private lateinit var noteViewModel: NoteViewModel
    private lateinit var editTextTitle: EditText
    private lateinit var editTextContent: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_note)

        editTextTitle = findViewById(R.id.edit_text_title)
        editTextContent = findViewById(R.id.edit_text_content)
        val buttonSave: Button = findViewById(R.id.button_save)
        val buttonDelete: Button = findViewById(R.id.button_delete)

        // Récupérer les données de la note passées via l'intent
        val intent = intent
        editTextTitle.setText(intent.getStringExtra("note_title"))
        editTextContent.setText(intent.getStringExtra("note_content"))

        noteViewModel = ViewModelProvider(this).get(NoteViewModel::class.java)

        // Sauvegarder les modifications
        buttonSave.setOnClickListener {
            val updatedTitle = editTextTitle.text.toString()
            val updatedContent = editTextContent.text.toString()

            val updatedNote = Note(updatedTitle, updatedContent)
            noteViewModel.update(updatedNote) // Mettre à jour la note via le ViewModel
            finish() // Fermer l'activité après la mise à jour
        }

        buttonDelete.setOnClickListener {
            val noteToDelete = Note(editTextTitle.text.toString(), editTextContent.text.toString())
            noteViewModel.delete(noteToDelete) // Supprimer la note via le ViewModel
            Toast.makeText(this, "Note supprimée", Toast.LENGTH_SHORT).show()
            finish() // Fermer l'activité après la suppression
        }
    }
}
