package np.com.nirab.templerunmapper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by nirab on 5/19/16. Database Helper Class.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "mapper.db";
    public static final String TABLE_NAME = "temple_table";
    public static final String COL_ID = "_id";
    public static final String COL_RELIGION = "religion";
    public static final String COL_NAME = "name";
    public static final String COL_NUM_BUILDINGS = "num_buildings";
    public static final String COL_WATER = "water";
    public static final String COL_TOILET = "toilet";
    public static final String COL_WHEELCHAIR = "wheelchair";
    public static final String COL_ESTABLISHED = "established";
    public static final String COL_OPENING_HOUR = "opening_hour";
    public static final String COL_LATITUDE = "latitude";
    public static final String COL_LONGITUDE = "longitude";
    public static final String COL_ACCURACY = "accuracy";
    public static final String COL_STATUS = "status";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + " (" + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_RELIGION + " TEXT, " + COL_NAME + " TEXT, " + COL_NUM_BUILDINGS + " TEXT, " +
                COL_WATER + " TEXT, " + COL_TOILET + " TEXT, " + COL_WHEELCHAIR + " TEXT," +
                COL_LATITUDE + " TEXT," + COL_LONGITUDE + " TEXT," + COL_ACCURACY + " TEXT," +
                 COL_OPENING_HOUR + " TEXT," + COL_ESTABLISHED + " TEXT," + COL_STATUS + " TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXIST " + TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData(String religion, String name, String buildings, String water, String toilet, String wheelchair, String established, String opening_hour, String latitude, String longitude, String accuracy, String status){
        SQLiteDatabase db = this.getWritableDatabase();
        System.out.println(religion);
        System.out.println(name);
        System.out.println(buildings);
        System.out.println(water);
        System.out.println(toilet);
        System.out.println(wheelchair);
        System.out.println(established);
        System.out.println(opening_hour);
        System.out.println(status);
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_RELIGION, religion);
        contentValues.put(COL_NAME, name);
        contentValues.put(COL_NUM_BUILDINGS, buildings);
        contentValues.put(COL_WATER, water);
        contentValues.put(COL_TOILET, toilet);
        contentValues.put(COL_WHEELCHAIR, wheelchair);
        contentValues.put(COL_ESTABLISHED, established);
        contentValues.put(COL_OPENING_HOUR, opening_hour);
        contentValues.put(COL_LATITUDE, latitude);
        contentValues.put(COL_LONGITUDE, longitude);
        contentValues.put(COL_ACCURACY, accuracy);
        contentValues.put(COL_STATUS, status);
        long result = db.insert(TABLE_NAME, null, contentValues);
        if (result == -1) {
            return false;
        }else {
            return true;
        }
    }


    public Cursor getAllRows(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from " + TABLE_NAME, null);
        return cursor;
    }

    public Cursor getUnsentRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        String selection = COL_STATUS + "=?";
        String selectionArgs[] = {"unsent"};
        Cursor cursor = db.query(TABLE_NAME, null, selection, selectionArgs, null, null, null);
        return cursor;
    }

    public int updateRow(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_STATUS,"sent");
        return db.update(TABLE_NAME, values, COL_ID + " = ?", new String[]{id});

    }


}
