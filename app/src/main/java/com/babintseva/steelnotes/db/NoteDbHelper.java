package com.babintseva.steelnotes.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.babintseva.steelnotes.R;

public class NoteDbHelper extends SQLiteOpenHelper {
    Context context;

    public NoteDbHelper(@Nullable Context context) {
        super(context, NoteConstants.DB_NAME, null, NoteConstants.DB_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(NoteConstants.TABLE_STRUCTURE);

        String header = context.getString(R.string.firstNoteHeader);
        String exp = context.getString(R.string.firstNoteExp);

        ContentValues cv = new ContentValues();
        cv.put(NoteConstants.HEADER, header);
        cv.put(NoteConstants.URI, "empty");
        cv.put(NoteConstants.EXPOSITION, exp);
        db.insert(NoteConstants.TABLE_NAME, null, cv);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(NoteConstants.DROP_TABLE);
        onCreate(db);
    }
}
