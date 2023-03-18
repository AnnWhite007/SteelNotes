package com.babintseva.steelnotes.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.babintseva.steelnotes.adapter.Note;

import java.util.ArrayList;
import java.util.List;

public class NoteDbManager {
    private Context context;
    private NoteDbHelper noteDbHelper;
    private SQLiteDatabase db;

    public NoteDbManager(Context context) {
        this.context = context;
        noteDbHelper = new NoteDbHelper(context);
    }

    public void openDb() {
        db = noteDbHelper.getWritableDatabase();
    }

    public void insertToDb(String header, String uri, String exp) {
        ContentValues cv = new ContentValues();
        cv.put(NoteConstants.HEADER, header);
        cv.put(NoteConstants.URI, uri);
        cv.put(NoteConstants.EXPOSITION, exp);
        db.insert(NoteConstants.TABLE_NAME, null, cv);
    }

    public List<Note> readFromDb() {
        List<Note> notesList = new ArrayList<>();
        Cursor cursor = db.query(NoteConstants.TABLE_NAME, null, null,
                null, null, null, null);
        while (cursor.moveToNext()) {
            Note note = new Note();
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(NoteConstants._ID));
            String header = cursor.getString(cursor.getColumnIndexOrThrow(NoteConstants.HEADER));
            String uri = cursor.getString(cursor.getColumnIndexOrThrow(NoteConstants.URI));
            String exp = cursor.getString(cursor.getColumnIndexOrThrow(NoteConstants.EXPOSITION));
            note.setId(id);
            note.setHeader(header);
            note.setUri(uri);
            note.setExp(exp);
            notesList.add(note);
        }
        cursor.close();
        return notesList;
    }

    public void updateItemInDb(int id, String header, String uri, String exp) {
        String selection = NoteConstants._ID + "=" + id;
        ContentValues cv = new ContentValues();
        cv.put(NoteConstants.HEADER, header);
        cv.put(NoteConstants.URI, uri);
        cv.put(NoteConstants.EXPOSITION, exp);
        db.update(NoteConstants.TABLE_NAME, cv, selection, null);
    }

    public void deleteItemFromDb(int id) {
        String selection = NoteConstants._ID + "=" + id;
        db.delete(NoteConstants.TABLE_NAME, selection, null);
    }

    public void closeDb() {
        noteDbHelper.close();
    }
}
