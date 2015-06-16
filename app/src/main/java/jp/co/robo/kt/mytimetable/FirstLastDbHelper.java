package jp.co.robo.kt.mytimetable;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * 始発/終電DB操作クラス。
 * select()はDateTimeクラスで表示日時を決定するとき、
 * Created by kouta on 15/06/10.
 */
public class FirstLastDbHelper extends SQLiteOpenHelper {
    private static final String TABLE_NAME = "firstlast";
    private static final String FILE_NAME = TABLE_NAME + ".db";
    private static final int VERSION = 1;

    private static final String COLUMN_ID = "_ID";
    private static final String COLUMN_TIME = "TIME";
    private static final String COLUMN_FROMTO = "FROMTO";
    private static final String COLUMN_IS_HOLIDAY = "IS_HOLIDAY";
    private static final String COLUMN_IS_FIRST = "IS_FIRST";

    private static FirstLastDbHelper mInstance;

    public static FirstLastDbHelper getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new FirstLastDbHelper(context);
        }
        return(mInstance);
    }

    private FirstLastDbHelper(Context context) {
        super(context, FILE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String CREATE_SQL = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TIME + " TEXT NOT NULL, " +
                COLUMN_FROMTO + " INTEGER NOT NULL, " +
                COLUMN_IS_HOLIDAY + " INTEGER NOT NULL, " +
                COLUMN_IS_FIRST + " INTEGER NOT NULL);";
        db.execSQL(CREATE_SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public String select(int fromto, boolean isHoliday, boolean isFirst) {
        final String[] SELECT_COLUMNS = { COLUMN_TIME };
        final String SELECT_WHERE = COLUMN_FROMTO + " = ? AND " + COLUMN_IS_HOLIDAY + " = ? AND " + COLUMN_IS_FIRST + " = ?";
        String[] where_params = { Integer.toString(fromto), Constants.CommonStrings.boolean2String(isHoliday), Constants.CommonStrings.boolean2String(isFirst) };

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, SELECT_COLUMNS, SELECT_WHERE, where_params, null, null, null, "1");
        String time = null;
        if (cursor.moveToFirst()) {
            time = cursor.getString((cursor.getColumnIndex(COLUMN_TIME)));
        }
        cursor.close();
        db.close();
        return(time);
    }

    public void renew(ArrayList<FirstLastInformation> list, TimetableSaver saver, int progress_offset) {
        final String DELETE_TABLE = "DELETE FROM " + TABLE_NAME;
        final String INSERT = "INSERT INTO " + TABLE_NAME + " VALUES (?, ?, ?, ?, ?)";

        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        db.execSQL(DELETE_TABLE);
        try {
            int cnt = 1;
            for (FirstLastInformation firstLast : list) {
                String[] params = {
                        Integer.toString(cnt),
                        firstLast.getTime(),
                        Integer.toString(firstLast.getFromTo()),
                        Constants.CommonStrings.boolean2String(firstLast.isHoliday()),
                        Constants.CommonStrings.boolean2String(firstLast.isFirst())
                };
                db.execSQL(INSERT, params);
                // 時刻表DB更新と始発/終電DB更新で同じプログレスバーを使うため、
                // 時刻表DB更新で進めたプログレスの値をプラスする。
                saver.setProgress(cnt + progress_offset);
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
