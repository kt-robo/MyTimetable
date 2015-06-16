package jp.co.robo.kt.mytimetable;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * 時刻表DB操作クラス。
 * Created by kouta on 15/05/20.
 */
public class TimetableDbHelper extends SQLiteOpenHelper {
    private static final String TABLE_NAME = "timetable";
    private static final String FILE_NAME = TABLE_NAME + ".db";
    private static final int VERSION = 1;

    private static final String COLUMN_ID = "_ID";
    private static final String COLUMN_FROMTO = "FROMTO";
    private static final String COLUMN_IS_HOLIDAY = "IS_HOLIDAY";
    private static final String COLUMN_TIME = "TIME";
    private static final String COLUMN_LINE = "LINE";
    private static final String COLUMN_KIND = "KIND";
    private static final String COLUMN_TRAIN = "TRAIN";
    private static final String COLUMN_DEST = "DEST";
    private static final String COLUMN_MARK = "MARK";

    private static TimetableDbHelper mInstance;

    public static TimetableDbHelper getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new TimetableDbHelper(context);
        }
        return(mInstance);
    }

    private TimetableDbHelper(Context context) {
        super(context, FILE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String CREATE_SQL = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_FROMTO + " INTEGER NOT NULL, " +
                COLUMN_IS_HOLIDAY + " INTEGER NOT NULL, " +
                COLUMN_TIME + " TEXT NOT NULL, " +
                COLUMN_LINE + " INTEGER NOT NULL, " +
                COLUMN_KIND  + " INTEGER NOT NULL, " +
                COLUMN_TRAIN + " TEXT NOT NULL, " +
                COLUMN_DEST + " TEXT NOT NULL, " +
                COLUMN_MARK + " INTEGER NOT NULL);";
        db.execSQL(CREATE_SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public ArrayList<TrainInformation> selectAll(Integer... args) {
        return(select(Constants.WeekdayOrHoliday.position2Boolean(args[0]), args[1], null, 0));
    }

    public ArrayList<TrainInformation> select(boolean isHoliday, int fromto, String thisTime, int size) {
        final String[] SELECT_COLUMNS = {COLUMN_TIME, COLUMN_LINE, COLUMN_KIND, COLUMN_TRAIN, COLUMN_DEST, COLUMN_MARK};
        final String SELECT_ALL_WHERE = COLUMN_FROMTO + " = ? AND " + COLUMN_IS_HOLIDAY + " = ?";
        final String SELECT_WHERE = SELECT_ALL_WHERE + " AND " + COLUMN_TIME + " > ?";

        String where;
        String[] where_params;
        String limit;
        if (size == 0) {
            // sizeが0なら全件取得。
            where = SELECT_ALL_WHERE;
            where_params = new String[2];
            limit = null;
        } else {
            // sizeが0でなければ指定件数を取得。このときthisTimeはnullでないこと。
            where = SELECT_WHERE;
            where_params = new String[3];
            where_params[2] = thisTime;
            limit = Integer.toString(size);
        }
        where_params[0] = Integer.toString(fromto);
        where_params[1] = Constants.CommonStrings.boolean2String(isHoliday);

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, SELECT_COLUMNS, where, where_params, null, null, null, limit);
        ArrayList<TrainInformation> list = new ArrayList<>((size == 0)? 100:size);
        if (cursor.moveToFirst()) {
            do {
                list.add(new TrainInformation(
                        cursor.getString(cursor.getColumnIndex(COLUMN_TIME)),
                        cursor.getInt(cursor.getColumnIndex(COLUMN_LINE)),
                        cursor.getInt(cursor.getColumnIndex(COLUMN_KIND)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_TRAIN)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_DEST)),
                        cursor.getInt(cursor.getColumnIndex(COLUMN_MARK))));

            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        list.trimToSize();
        return(list);
    }

    public ArrayList<TrainInformation2> selectAll() {
        final String[] SELECT_COLUMNS2 = {COLUMN_ID, COLUMN_FROMTO, COLUMN_IS_HOLIDAY, COLUMN_TIME, COLUMN_LINE, COLUMN_KIND};

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, SELECT_COLUMNS2, null, null, null, null, null);
        ArrayList<TrainInformation2> list = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                list.add(new TrainInformation2(
                        cursor.getInt(cursor.getColumnIndex(COLUMN_ID)),
                        cursor.getInt(cursor.getColumnIndex(COLUMN_FROMTO)),
                        cursor.getInt(cursor.getColumnIndex(COLUMN_IS_HOLIDAY)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_TIME)),
                        cursor.getInt(cursor.getColumnIndex(COLUMN_LINE)),
                        cursor.getInt(cursor.getColumnIndex(COLUMN_KIND)))
                );
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        list.trimToSize();
        return(list);
    }

    // 各URLから取得したArrayList<TrainInformation>を、並び替えてDBに保存する。
    // 第1引数の配列は各URLを示す。ArrayListの中身は時刻順に並んでいることを前提とする。
    public void renew(ArrayList<TrainInformation>[] args, TimetableSaver saver) {
        final String DELETE_TABLE = "DELETE FROM " + TABLE_NAME;
        final String INSERT = "INSERT INTO " + TABLE_NAME + " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        int i;
        int[] ii = new int[args.length];        // argsの各要素のArrayListの位置。
        int[] size = new int[args.length];      // argsの各要素のArrayListの長さ。
        for (i = 0;i < args.length;i++) {
            ii[i] = 0;
            size[i] = args[i].size();
        }
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        db.execSQL(DELETE_TABLE);
        int cnt = 1;
        try {
            TrainInformation target;
            int fromto = 0;
            boolean isHoliday = false;
            int j;
            while (true) {
                // 並び替えつつDBに格納する。つまり「まだ格納していない最小の時刻のもの」を格納する。
                target = null;
                j = -1;
                // このループで、配列の中で最小の時刻のものを選択する。
                for (i = 0;i < ii.length;i++) {
                    if (ii[i] < size[i]) {
                        // ArrayListをまだ読み切っていないもの。
                        if ((target == null) || (args[i].get(ii[i]).getTime().compareTo(target.getTime()) < 0)) {
                            // まだtargetが設定されていないか、またはすでに設定されているtargetより小さければ、
                            // targetを入れ替える。fromtoとisHolidayの計算はConstants.Urlsに従う。
                            target = args[i].get(ii[i]);
                            fromto = Constants.Urls.getFromTo(i);
                            isHoliday = Constants.Urls.isHoliday(i);
                            j = i;
                        }
                    }
                }
                if (target == null) {
                    // targetがないということは全部読み終わった。
                    break;
                } else {
                    String[] params = {
                            Integer.toString(cnt),
                            Integer.toString(fromto),
                            Constants.CommonStrings.boolean2String(isHoliday),
                            target.getTime(),
                            Integer.toString(target.getLine()),
                            Integer.toString(target.getKind()),
                            target.getTrain(),
                            target.getDest(),
                            Integer.toString(target.getMark())
                    };
                    db.execSQL(INSERT, params);
                    saver.setProgress(cnt);
                    cnt++;
                    ii[j]++;
                }
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
            db.close();
        }
    }
}
