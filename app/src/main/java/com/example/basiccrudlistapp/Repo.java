package com.example.basiccrudlistapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.Toast;
import com.example.basiccrudlistapp.model.Note;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Repo {
    private static FirebaseFirestore db;
    private static List<Note> items = new ArrayList<>();
    private static String NOTES = "notes";
    private static Updatable caller;
    private static Note currentNote;
    private static FirebaseStorage storage = FirebaseStorage.getInstance();

    public static void createNote(String text, String content) {
        UUID uuid = UUID.randomUUID(); // world-wide unique (almost)
        DocumentReference ref =  db.collection(NOTES).document(uuid.toString());
        Map<String,String> map = new HashMap<>();
        map.put("title", text);
        map.put("content", content);
        System.out.println("createNote....");
        ref.set(map).addOnCompleteListener(obj -> {
            System.out.println("added new note");
        }).addOnFailureListener(exception -> {
            System.out.println("Failed to add new note " + exception);
        });
    }

    public static void init(Context context) {
        db = FirebaseFirestore.getInstance();
        caller = (Updatable)context;
        startListener();
    }

    private static void startListener() {
        db.collection(NOTES).addSnapshotListener((value,error) -> {
            if(error == null) {
                items.clear();
                for(DocumentSnapshot snap: value.getDocuments()) {
                    if(snap.get("title") != null) {
                        String title = (String) snap.get("title");
                        String content = (String) snap.get("content");
                        Note note = new Note(title, content, snap.getId());
                        items.add(note);
                    }
                }
                caller.update(null);
            }else {
                System.out.println("Error reading firebase " + error);
            }
        });
    }

    public static void updateNote(String newText, String newContent) {
        currentNote.setTitle(newText);
        currentNote.setContent(newContent);
        DocumentReference ref =  db.collection(NOTES).document(currentNote.getId());
        Map<String,String> map = new HashMap<>();
        map.put("title", currentNote.getTitle());
        map.put("content", currentNote.getContent());
        if(currentNote.hasNewImage()) {
            uploadBitmapToCurrentNote(currentNote.getBitmap());
        }
        ref.set(map).addOnCompleteListener(obj -> {
            System.out.println("updated note");
        }).addOnFailureListener(exception -> {
            System.out.println("Failed to update note " + exception);
        });
    }

    public static List<Note> getItems() {
        return items;
    }

    public static Note getCurrentNote() {
        return currentNote;
    }

    public static void setCurrentNote(int index) {
        Repo.currentNote = items.get(index);
    }

    public static void uploadBitmapToCurrentNote(Bitmap bitmap) {
        StorageReference ref = storage.getReference(currentNote.getId()); // new reference
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        ref.putBytes(baos.toByteArray()).addOnCompleteListener(snap -> {
            System.out.println("Image uploaded. " + snap);
        }).addOnFailureListener(exception -> {
            System.out.println("Failed to upload. " + exception);
        });
    }

    public static void downloadBitmapForCurrentNote(Updatable caller) {
        StorageReference ref = storage.getReference(currentNote.getId());
        int max = 1024 * 1024;
        ref.getBytes(max).addOnSuccessListener(bytes -> {
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            currentNote.setBitmap(bitmap);
            caller.update(true);
        }).addOnFailureListener(exception -> {
            System.out.println("No bitmap in DB for this note");
        });
    }

    public static void deleteNote() {
        storage.getReference(currentNote.getId()).delete();
        db.collection(NOTES).document(currentNote.getId()).delete();
    }
}
