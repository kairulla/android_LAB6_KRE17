package kz.lab6_kre17.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public interface IMySQLite {

    static final String DATABASE_NAME = "religiya"; // Имя базы данных
    static final String TABLE_NAME = "religioznie_obedineniya"; // Имя таблицы
    static final String ID = "id"; // Поле с ID
    static final String TIP = "tip"; // Поле с типом религиозного объединения
    static final String TIP_LC = "tip_lc"; //Поле с типом религиозного объединения
    static final String NAZVANIE = "nazvanie"; // Поле с названием религиозного объединения
    static final String TITUL = "titul"; // Поле с титулом человека
    static final String FIO = "fio"; // Поле с ФИО
    static final String GOROD = "gorod"; // Поле с названием города
    static final String ULITZA = "ulitza"; // Поле с названием улицы
    static final String NOMER_DOMA = "nomer_doma"; // Поле с номером дома
    static final String TELEFONNIY_NOMER = "telefonniy_nomer"; // Поле с телефонным номером
    static final String ASSETS_FILE_NAME = "religioznieObedineniya.txt"; // Имя файла из ресурсов с данными для БД
    static final String DATA_SEPARATOR = "|"; // Разделитель данных в файле ресурсов

    public void addData(SQLiteDatabase db, String tip, String nazvanie, String titul, String fio, String gorod, String ulitza, String nomer_doma, String telefonniy_nomer);
    public void loadDataFromAsset(Context context, String fileName, SQLiteDatabase db);
    public String getData(String filter);
}
