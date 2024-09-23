## **Titre du Projet : Application de Prise de Notes**

### **Objectif :**

Développer une application Android simple, intuitive et efficace qui permet aux utilisateurs de créer, afficher, modifier et supprimer des notes.

### **Groupe :**

- Anthony ISSA
- Nicolas MATUSZAK
- Firmin EXERTIER
- Nathanael DESFORGES

## **Fonctionnalités Principales :**

L'application permet les actions suivantes :

1. **Afficher toutes les notes** dans une liste.
2. **Ajouter une nouvelle note** avec un titre et un contenu.
3. **Modifier une note existante**.
4. **Supprimer une note existante**.
5. **Utiliser une interface utilisateur simple et réactive**, avec un RecyclerView pour afficher les notes et un Floating Action Button (FAB) pour en ajouter de nouvelles.

## **Structure du Projet :**

### 1. **Modèle de données : Note**

- **Classe `Note`** :
Cette classe représente chaque note dans la base de données. Elle contient les informations suivantes :
    - `title` : le titre de la note.
    - `content` : le contenu de la note.

```kotlin
@Entity(tableName = "note_table")
public class Note {
    @PrimaryKey @NonNull
    public String title;
    public String content;

    public Note(String title, String content) {

        this.title = title;
        this.content = content;
    }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
}
```

### 2. **Data Access Object (DAO) : NoteDao**

L'interface **NoteDao** est utilisée pour interagir avec la base de données Room. Elle fournit des méthodes pour insérer, mettre à jour, supprimer et récupérer les notes.

```kotlin
public interface NoteDao {

    @Insert
    void insert(Note note);

    @Update
    void update(Note note);

    @Delete
    void delete(Note note);

    @Query("SELECT * FROM note_table")
    LiveData<List<Note>> getAllNotes();
}
```

### 3. **Base de données Room : NoteDatabase**

Cette classe utilise **Room** pour créer et gérer la base de données locale où les notes sont stockées.

```kotlin
@Database(entities = {Note.class}, version = 1)
public abstract class NoteDatabase extends RoomDatabase {

    private static NoteDatabase instance;

    public abstract NoteDao noteDao();

    public static synchronized NoteDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            NoteDatabase.class, "note_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}
```

### 4. **Repository : NoteRepository**

Le **NoteRepository** sert de couche intermédiaire entre le ViewModel et la base de données, facilitant l'accès aux données.

```kotlin
public class NoteRepository {

    private NoteDao noteDao;
    private LiveData<List<Note>> allNotes;
    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    public NoteRepository(Application application) {
        NoteDatabase database = NoteDatabase.getInstance(application);
        noteDao = database.noteDao();
        allNotes = noteDao.getAllNotes();
    }

    public void insert(Note note) {
        executorService.execute(() -> noteDao.insert(note));
    }

    public void update(Note note) {
        executorService.execute(() -> noteDao.update(note));
    }

    public void delete(Note note) {
        executorService.execute(() -> noteDao.delete(note));
    }

    public LiveData<List<Note>> getAllNotes() {
        return allNotes;
    }
}
```

### 5. **ViewModel : NoteViewModel**

Le **NoteViewModel** est responsable de fournir les données à l'interface utilisateur, tout en respectant l'architecture MVVM. Il observe les changements de données via **LiveData**.

```kotlin
public class NoteViewModel extends AndroidViewModel {

    private NoteRepository repository;
    private LiveData<List<Note>> allNotes;

    public NoteViewModel(@NonNull Application application) {
        super(application);
        repository = new NoteRepository(application);
        allNotes = repository.getAllNotes();
    }

    public void insert(Note note) {
        repository.insert(note);
    }

    public void update(Note note) {
        repository.update(note);
    }

    public void delete(Note note) {
        repository.delete(note);
    }

    public LiveData<List<Note>> getAllNotes() {
        return allNotes;
    }
}
```

### 6. **Interface utilisateur :**

- **MainActivity** : Gère l'affichage des notes dans un **RecyclerView**, ainsi que l'ajout de nouvelles notes via un **Floating Action Button (FAB)**.
- **EditNoteActivity** : Permet de lire, modifier ou supprimer une note spécifique.

![image](https://github.com/user-attachments/assets/92167239-c47d-4cea-a528-8cbe7e174867)
![image](https://github.com/user-attachments/assets/8c70592f-58d9-47da-850e-f16cc6941e1d)
![image](https://github.com/user-attachments/assets/fa1f8dab-d4c0-4731-beea-f8c5bf8c31b6)
![image](https://github.com/user-attachments/assets/9a48d97f-9b78-426f-bff4-1ec928e25681)
![image](https://github.com/user-attachments/assets/90e74b36-82d0-44c5-91ab-e2de31b2dc55)


## **Manuel d'utilisation :**

### 1. **Lancement de l'application :**

- Lorsque l'utilisateur lance l'application, il est redirigé vers la page d'accueil (**MainActivity**), qui affiche toutes les notes existantes sous forme de liste.

### 2. **Ajouter une nouvelle note :**

- Appuyez sur le bouton **"+"** flottant (FAB) en bas de l'écran.
- Une fenêtre popup s'affiche, permettant à l'utilisateur de saisir un **titre** et un **contenu** pour la nouvelle note.
- Appuyez sur le bouton **Ajouter** pour enregistrer la note.

### 3. **Lire et modifier une note :**

- Appuyez sur une note dans la liste pour ouvrir la note dans la **EditNoteActivity**.
- Une fois dans cette activité, l'utilisateur peut :
    - Modifier le titre et/ou le contenu de la note.
    - Appuyer sur **Sauvegarder** pour enregistrer les modifications.

### 4. **Supprimer une note :**

- Depuis l'interface de modification (EditNoteActivity), un bouton **Supprimer** est disponible.
- Lorsque l'utilisateur appuie sur **Supprimer**, la note est définitivement supprimée de la base de données.

## **Architecture du projet :**

L'application suit l'architecture **MVVM (Model-View-ViewModel)**, assurant une séparation claire des responsabilités et une meilleure gestion des données.

### **Composants :**

- **Model** : Gère les données (classe `Note`, base de données `NoteDatabase`, DAO `NoteDao`).
- **ViewModel** : Fournit les données à l'interface utilisateur et encapsule la logique métier.
- **View** : L'interface utilisateur représentée par les activités et le RecyclerView.

## **Bibliothèques utilisées :**

- **Room** : Pour la gestion de la base de données locale.
- **LiveData** : Pour observer les changements de données.
- **ViewModel** : Pour maintenir et gérer les données lors de changements d'UI.
- **RecyclerView** : Pour afficher la liste des notes.
- **Material Design** : Pour le Floating Action Button et la structure de l'interface.

## **Suivi des versions :**

1. **Version 1.0 :**
    - Fonctionnalités principales implémentées (création, modification, suppression de notes).
    - Interface utilisateur basique avec RecyclerView et FAB.
2. **Version future (à améliorer) :**
    - Implémentation d'un système de tags ou de catégories pour les notes.
    - Recherche de notes par titre ou contenu.
    - Synchronisation des notes avec une base de données cloud.
