package com.babintseva.steelnotes.db;


public class NoteConstants {
    public static final String EDIT_STATE = "edit_state";
    public static final String NOTE_INTENT = "note_intent";
    public static final String TABLE_NAME = "notes_table";
    public static final String _ID = "_id";
    public static final String HEADER = "header";
    public static final String EXPOSITION = "exp";
    public static final String URI = "uri";
    public static final String DB_NAME = "notes_db.db";
    public static final int DB_VERSION = 1;

    public static final String TABLE_STRUCTURE = "CREATE TABLE IF NOT EXISTS "
            + TABLE_NAME + " (" + _ID + " INTEGER PRIMARY KEY," + HEADER + " TEXT,"
            + URI + " TEXT," + EXPOSITION + " TEXT)";

    public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;


}
