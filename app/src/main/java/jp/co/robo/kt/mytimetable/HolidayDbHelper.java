package jp.co.robo.kt.mytimetable;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * 休日DB操作クラス。
 * Created by kouta on 15/05/20.
 */
public class HolidayDbHelper extends SQLiteOpenHelper {
    private static final String TABLE_NAME = "holiday";
    private static final String FILE_NAME = TABLE_NAME + ".db";
    private static final int VERSION = 1;

    private static final String COLUMN_ID = "_ID";
    private static final String COLUMN_NAME = "NAME";
    private static final String COLUMN_MONTH = "MONTH";
    private static final String COLUMN_DATE = "DATE";
    private static final String COLUMN_IS_HAPPYMONDAY = "IS_HAPPYMONDAY";

    private static HolidayDbHelper mInstance;

    public static HolidayDbHelper getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new HolidayDbHelper(context);
        }
        return(mInstance);
    }

    private HolidayDbHelper(Context context) {
        super(context, FILE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String CREATE_SQL = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT NOT NULL, " +
                COLUMN_MONTH + " INTEGER NOT NULL, " +
                COLUMN_DATE + " INTEGER NOT NULL, " +
                COLUMN_IS_HAPPYMONDAY  + " INTEGER NOT NULL);";
        db.execSQL(CREATE_SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public ArrayList<HolidayInformation> selectAll() {
        final String[] SELECT_COLUMNS = { COLUMN_NAME, COLUMN_MONTH, COLUMN_DATE, COLUMN_IS_HAPPYMONDAY };
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, SELECT_COLUMNS, null, null, null, null, null);
        ArrayList<HolidayInformation> list = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                list.add(new HolidayInformation(
                        cursor.getInt(cursor.getColumnIndex(COLUMN_MONTH)),
                        cursor.getInt(cursor.getColumnIndex(COLUMN_DATE)),
                        (cursor.getInt(cursor.getColumnIndex(COLUMN_IS_HAPPYMONDAY)) == 1),
                        cursor.getString(cursor.getColumnIndex(COLUMN_NAME))));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        list.trimToSize();
        return(list);
    }

    public boolean contains(int month, int date, int day, int weekOfMonth) {
        final String[] CONTAINS_COLUMNS = { COLUMN_NAME };
        final String CONTAINS_WHERE = COLUMN_MONTH + " = ? AND " + COLUMN_DATE + " = ? AND " + COLUMN_IS_HAPPYMONDAY + " = ?";
        boolean ret = false;
        SQLiteDatabase db = getReadableDatabase();
        String[] where_param = new String[] { Integer.toString(month), Integer.toString(date), "0" };
        Cursor cursor = db.query(TABLE_NAME, CONTAINS_COLUMNS, CONTAINS_WHERE, where_param, null, null, null);
        if (cursor.getCount() > 0) {
            ret = true;
        } else if (day == Calendar.MONDAY) {
            where_param[1] = Integer.toString(weekOfMonth);
            where_param[2] = "1";
            Cursor cursor2 = db.query(TABLE_NAME, CONTAINS_COLUMNS, CONTAINS_WHERE, where_param, null, null, null);
            if (cursor2.getCount() > 0) {
                ret = true;
            }
            cursor2.close();
        }
        cursor.close();
        db.close();
        return(ret);
    }

    public void renew(HolidayInformation[] list, HolidaySaver saver) {
        final String DELETE_TABLE = "DELETE FROM " + TABLE_NAME;
        final String INSERT = "INSERT INTO " + TABLE_NAME + " VALUES (?, ?, ?, ?, ?)";
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        db.execSQL(DELETE_TABLE);
        try {
            int cnt = 1;
            for (HolidayInformation holiday : list) {
                String[] params = {
                        Integer.toString(cnt),
                        holiday.getName(),
                        Integer.toString(holiday.getMonth()),
                        Integer.toString(holiday.getDate()),
                        Constants.CommonStrings.boolean2String(holiday.isHappyMonday())
                };
                db.execSQL(INSERT, params);
                saver.setProgress(cnt);
                cnt++;
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            db.endTransaction();
            db.close();
        }
    }
}
