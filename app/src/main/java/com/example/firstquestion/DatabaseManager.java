package com.example.firstquestion;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DatabaseManager extends SQLiteOpenHelper {
    public static final String TABLE_NAME = "word";
    public static final String ID_FIELD = "_id";
    public static final String NAME_FIELD = "word";

    public DatabaseManager(Context context){
        super(context, "Food and Drink", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Log.d("db", "onCreate");
        String sql = "CREATE TABLE " + TABLE_NAME
                + " (" + ID_FIELD + " INTEGER, "
                + NAME_FIELD + " TEXT,"
                + " PRIMARY KEY (" + ID_FIELD + "));";
        sqLiteDatabase.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        Log.d("db", "onUpdate");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public Word addWord(Word word){
        Log.d("db", "addWord");
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NAME_FIELD, word.getName());
        long id = db.insert(TABLE_NAME, null , values);
        word.setId(id);
        db.close();
        return word;
    }

    public Word getWord(long id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME,
                new String[]{ID_FIELD, NAME_FIELD},
                ID_FIELD + " =?", new String[]{String.valueOf(id)},
                null,
                null,
                null,
                null);
        if(cursor != null){
            cursor.moveToFirst();
            Word word = new Word(cursor.getString(1));
            word.setId(cursor.getLong(0));
            return word;
        }
        return null;
    }

    public List<Word> getAllWord(){
        List<Word> words = new ArrayList<Word>();
        String selectQuery = "SELECT * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        while(cursor.moveToNext()){
            Word word = new Word();
            word.setId(Integer.parseInt(cursor.getString(0)));
            word.setName(cursor.getString(1));
            words.add(word);
        }
        return words;
    }

    public Cursor getWordCursor(){
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_NAME;
        return db.rawQuery(selectQuery, null);
    }

    public int updateWord(Word word){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NAME_FIELD, word.getName());

        return db.update(TABLE_NAME, values, ID_FIELD + " = ?",
                new String[]{String.valueOf(word.getId())});
    }

    public void deleteWord(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, null, null);
        db.close();
    }

    public Cursor searchWord(String word){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + NAME_FIELD + " LIKE '%" + word + "%'";
        return db.rawQuery(query, null);
    }
}
