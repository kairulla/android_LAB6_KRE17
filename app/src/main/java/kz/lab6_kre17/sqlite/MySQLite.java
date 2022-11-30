package kz.lab6_kre17.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class MySQLite extends SQLiteOpenHelper implements IMySQLite {
    private static final int DATABASE_VERSION = 1; // НОМЕР ВЕРСИИ БАЗЫ ДАННЫХ И ТАБЛИЦ !

    private Context context; // Контекст приложения

    public MySQLite(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    // Метод создания базы данных и таблиц в ней
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + ID + " INTEGER PRIMARY KEY,"
                + TIP + " TEXT,"
                + TIP_LC + " TEXT,"
                + NAZVANIE + " TEXT,"
                + TITUL + " TEXT,"
                + FIO + " TEXT,"
                + GOROD + " TEXT,"
                + ULITZA + " TEXT,"
                + NOMER_DOMA + " TEXT,"
                + TELEFONNIY_NOMER + " TEXT" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
        System.out.println(CREATE_CONTACTS_TABLE);
        loadDataFromAsset(context, ASSETS_FILE_NAME,  db);
    }

    // Метод при обновлении структуры базы данных и/или таблиц в ней
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        System.out.println("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // Добавление нового контакта в БД
    @Override
    public void addData(SQLiteDatabase db, String tip, String nazvanie, String titul, String fio, String gorod, String ulitza, String nomer_doma, String telefonniy_nomer) {
        ContentValues values = new ContentValues();
        values.put(TIP, tip);
        values.put(TIP_LC, tip.toLowerCase());
        values.put(NAZVANIE, nazvanie);
        values.put(TITUL, titul);
        values.put(FIO, fio);
        values.put(GOROD, gorod);
        values.put(ULITZA, ulitza);
        values.put(NOMER_DOMA, nomer_doma);
        values.put(TELEFONNIY_NOMER, telefonniy_nomer);
        db.insert(TABLE_NAME, null, values);
    }

    // Добавление записей в базу данных из файла ресурсов
    @Override
    public void loadDataFromAsset(Context context, String fileName, SQLiteDatabase db) {
        BufferedReader in = null;

        try {
            // Открываем поток для работы с файлом с исходными данными
            InputStream is = context.getAssets().open(fileName);
            // Открываем буфер обмена для потока работы с файлом с исходными данными
            in = new BufferedReader(new InputStreamReader(is));

            String str;
            while ((str = in.readLine()) != null) { // Читаем строку из файла
                String strTrim = str.trim(); // Убираем у строки пробелы с концов
                if (!strTrim.equals("")) { // Если строка не пустая, то
                    StringTokenizer st = new StringTokenizer(strTrim, DATA_SEPARATOR); // Нарезаем ее на части
                    String tip = st.nextToken().trim();
                    String nazvanie = st.nextToken().trim();
                    String titul = st.nextToken().trim();
                    String fio = st.nextToken().trim();
                    String gorod = st.nextToken().trim();
                    String ulitza = st.nextToken().trim();
                    String nomer_doma = st.nextToken().trim();
                    String telefonniy_nomer = st.nextToken().trim();
                    addData(db, tip, nazvanie, titul, fio, gorod, ulitza, nomer_doma, telefonniy_nomer);
                }
            }

        // Обработчики ошибок
        } catch (IOException ignored) {
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException ignored) {
                }
            }
        }

    }

    // Получение значений данных из БД в виде строки с фильтром
    @Override
    public String getData(String filter) {

        String selectQuery; // Переменная для SQL-запроса

        if (filter.equals("")) {
            selectQuery = "SELECT * FROM " + TABLE_NAME;
//            selectQuery = "SELECT * FROM " + TABLE_NAME + " ORDER BY " + TIP;
        } else {
            selectQuery = "SELECT * FROM " + TABLE_NAME + " WHERE (" + TIP_LC + " LIKE '%" + filter.toLowerCase() + "%'" + " OR " + TELEFONNIY_NOMER + " LIKE '%" + filter + "%'" + ")";
//            selectQuery = "SELECT * FROM " + TABLE_NAME + " WHERE (" + TIP_LC + " LIKE '%" + filter.toLowerCase() + "%'" + " OR " + TELEFONNIY_NOMER + " LIKE '%" + filter + "%'" + ") ORDER BY " + TIP;
        }
        SQLiteDatabase db = this.getReadableDatabase(); // Доступ к БД
        Cursor cursor = db.rawQuery(selectQuery, null); // Выполнение SQL-запроса

        StringBuilder data = new StringBuilder(); // Переменная для формирования данных из запроса

        int num = 0;
        if (cursor.moveToFirst()) { // Если есть хоть одна запись, то
            do { // Цикл по всем записям результата запроса
                int a = cursor.getColumnIndex(TIP);
                int b = cursor.getColumnIndex(NAZVANIE);
                int c = cursor.getColumnIndex(TITUL);
                int d = cursor.getColumnIndex(FIO);
                int e = cursor.getColumnIndex(GOROD);
                int f = cursor.getColumnIndex(ULITZA);
                int g = cursor.getColumnIndex(NOMER_DOMA);
                int h = cursor.getColumnIndex(TELEFONNIY_NOMER);
                String tip = cursor.getString(a);
                String nazvanie = cursor.getString(b);
                String titul = cursor.getString(c);
                String fio = cursor.getString(d);
                String gorod = cursor.getString(e);
                String ulitza = cursor.getString(f);
                String nomer_doma = cursor.getString(g);
                String telefonniy_nomer = cursor.getString(h);
                data.append(String.valueOf(++num) + ") " + tip + ", " + nazvanie + ", " + titul + ", " + fio + ", " + gorod + ", " + ulitza + ", " + nomer_doma + ", " + telefonniy_nomer + "\n");
            } while (cursor.moveToNext()); // Цикл пока есть следующая запись
        }
        return data.toString(); // Возвращение результата
    }

}