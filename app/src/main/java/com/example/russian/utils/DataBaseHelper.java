package com.example.russian.utils;


import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.russian.tables.Item;
import com.example.russian.tables.Reward;
import com.example.russian.tables.Task;
import com.example.russian.tables.Unit;
import com.example.russian.tables.Word;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class DataBaseHelper extends SQLiteOpenHelper {

    // путь к базе данных вашего приложения
    private static String DB_PATH;
    private static String DB_NAME = "tasks.db";
    private SQLiteDatabase myDataBase;
    private final Context mContext;
    private static final String TABLE_ITEM = "items";
    private static final String KEY_ID = "Id";
    private static final String KEY_NAME = "Name";
    private static final String KEY_COST = "Cost";

    private static final String TABLE_UNIT = "unit";
    private static final String KEY_ID_CLASS = "IdClass";

    private static final String TABLE_TASK = "task";
    private static final String KEY_CONDITION = "Condition";

    private static final String TABLE_WORD = "words";
    private static final String KEY_ID_UNIT= "IdUnit";
    private static final String KEY_TASK_VALUE= "TaskValue";
    private static final String KEY_CORRECT_VALUE= "CorrectValue";
    private static final String KEY_ID_TASK= "IdTask";

    private static final String TABLE_REWARD = "reward";

    private static DataBaseHelper mInstance = null;

    /**
     * Конструктор
     * Принимает и сохраняет ссылку на переданный контекст для доступа к ресурсам приложения
     * @param context
     */
    private DataBaseHelper(Context context) {
        super(context, DB_NAME, null, 1);
        this.mContext = context;
        DB_PATH=context.getApplicationInfo().dataDir+"/databases/";

    }
    public static DataBaseHelper getDB(Context context){
        if (mInstance == null) {
            mInstance = new DataBaseHelper(context.getApplicationContext());
        }
        return mInstance;
    }

    /**
     * Создает пустую базу данных и перезаписывает ее нашей собственной базой
     * */
    public void createDataBase() throws IOException {
        boolean dbExist = checkDataBase();

        if(dbExist){
            //ничего не делать - база уже есть
        }else{
            //вызывая этот метод создаем пустую базу, позже она будет перезаписана
            this.getReadableDatabase();

            try {
                this.copyDataBase();
            } catch (IOException e) {
                throw new Error("Error copying database");
            }
        }
    }

    /**
     * Проверяет, существует ли уже эта база, чтобы не копировать каждый раз при запуске приложения
     * @return true если существует, false если не существует
     */
    private boolean checkDataBase(){
        SQLiteDatabase checkDB = null;

        try{
            String myPath = DB_PATH + DB_NAME;
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
        }catch(SQLiteException e){
            //база еще не существует
        }
        if(checkDB != null){
            checkDB.close();
        }
        return checkDB != null ? true : false;
    }

    /**
     * Копирует базу из папки assets заместо созданной локальной БД
     * Выполняется путем копирования потока байтов.
     * */
    private void copyDataBase() throws IOException{
        //Открываем локальную БД как входящий поток
        InputStream myInput = mContext.getResources().getAssets().open(DB_NAME);

        //Путь ко вновь созданной БД
        String outFileName = DB_PATH + DB_NAME;

        //Открываем пустую базу данных как исходящий поток
        OutputStream myOutput = new FileOutputStream(outFileName);

        //перемещаем байты из входящего файла в исходящий
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer))>0){
            myOutput.write(buffer, 0, length);
        }

        //закрываем потоки
        myOutput.flush();
        myOutput.close();
        myInput.close();
    }

    public void openDataBase() throws SQLException {
        //открываем БД
        String myPath = DB_PATH + DB_NAME;
        myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
    }

    @Override
    public synchronized void close() {
        if(myDataBase != null)
            myDataBase.close();
        super.close();
    }

    public Item getContact(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_ITEM, new String[] {KEY_ID,
                        KEY_NAME, KEY_COST }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);

        if (cursor != null){
            cursor.moveToFirst();
        }

        Item contact = new Item(Integer.parseInt(cursor.getString(0)), cursor.getString(1), Integer.parseInt(cursor.getString(2)));

        return contact;
    }

    public List<Item> getAllContacts() {
        List<Item> contactList = new ArrayList<Item>();
        String selectQuery = "SELECT  * FROM " + TABLE_ITEM;
        this.openDataBase();
        SQLiteDatabase db = this.myDataBase;
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Item contact = new Item();
                contact.setID(Integer.parseInt(cursor.getString(0)));
                contact.setName(cursor.getString(1));
                contact.setCost(Integer.parseInt(cursor.getString(2)));
                contactList.add(contact);
            } while (cursor.moveToNext());
        }

        return contactList;
    }
    public Unit getUnit(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_UNIT, new String[] {KEY_ID,KEY_ID_CLASS,
                        KEY_NAME  }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);

        if (cursor != null){
            cursor.moveToFirst();
        }

        Unit unit = new Unit(Integer.parseInt(cursor.getString(0)), Integer.parseInt(cursor.getString(1)), cursor.getString(2));

        return unit;
    }

    public List<Unit> getAllUnits() {
        List<Unit> unitList = new ArrayList<Unit>();
        String selectQuery = "SELECT  * FROM " + TABLE_UNIT;
        this.openDataBase();
        SQLiteDatabase db = this.myDataBase;
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Unit unit = new Unit();
                unit.setID(Integer.parseInt(cursor.getString(0)));
                unit.setIdClass(Integer.parseInt(cursor.getString(1)));
                unit.setName(cursor.getString(2));

                unitList.add(unit);
            } while (cursor.moveToNext());
        }
        return unitList;
    }
    public List<Unit> getUnitsByClassID(String idClass) {
        List<Unit> unitList = new ArrayList<Unit>();
        String selectQuery = "SELECT  * FROM " + TABLE_UNIT+" WHERE " +KEY_ID_CLASS+"="+idClass;
        this.openDataBase();
        SQLiteDatabase db = this.myDataBase;
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Unit unit = new Unit();
                unit.setID(Integer.parseInt(cursor.getString(0)));
                unit.setIdClass(Integer.parseInt(cursor.getString(1)));
                unit.setName(cursor.getString(2));

                unitList.add(unit);
            } while (cursor.moveToNext());
        }
        return unitList;
    }
    public List<Task> getAllTasks() {
        List<Task> taskList = new ArrayList<Task>();
        String selectQuery = "SELECT  * FROM " + TABLE_TASK;
        this.openDataBase();
        SQLiteDatabase db = this.myDataBase;
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Task task = new Task();
                task.setID(Integer.parseInt(cursor.getString(0)));
                task.setCondition(cursor.getString(1));

                taskList.add(task);
            } while (cursor.moveToNext());
        }
        return taskList;
    }
    public List<Word> getWordsByIdUnit(String idUnit) {
        List<Word> wordList = new ArrayList<Word>();
        String selectQuery = "SELECT  * FROM " + TABLE_WORD+" WHERE " +KEY_ID_UNIT+"="+idUnit;
        this.openDataBase();
        SQLiteDatabase db = this.myDataBase;
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Word word = new Word();
                word.setID(Integer.parseInt(cursor.getString(0)));
                word.setIdUnit(Integer.parseInt(cursor.getString(1)));
                word.setTaskValue(cursor.getString(2));
                word.setCorrectValue(cursor.getString(3));
                word.setIdTask(Integer.parseInt(cursor.getString(4)));

                wordList.add(word);
            } while (cursor.moveToNext());
        }
        return wordList;
    }

    public Reward getRewardById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_REWARD, new String[] {KEY_ID,KEY_NAME,
                        KEY_ID_UNIT  }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);

        if (cursor != null){
            cursor.moveToFirst();
        }

        Reward reward = new Reward(Integer.parseInt(cursor.getString(0)), cursor.getString(1), Integer.parseInt(cursor.getString(2)));

        return reward;
    }
    public Reward getRewardByIdUnit(int idUnit) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_REWARD, new String[] {KEY_ID,KEY_NAME,
                        KEY_ID_UNIT  }, KEY_ID_UNIT + "=?",
                new String[] { String.valueOf(idUnit) }, null, null, null, null);

        if (cursor != null){
            cursor.moveToFirst();
        }

        Reward reward = new Reward(Integer.parseInt(cursor.getString(0)), cursor.getString(1), Integer.parseInt(cursor.getString(2)));

        return reward;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    // Здесь можно добавить вспомогательные методы для доступа и получения данных из БД
    // вы можете возвращать курсоры через "return myDataBase.query(....)", это облегчит их использование
    // в создании адаптеров для ваших view
}