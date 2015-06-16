package jp.co.robo.kt.mytimetable;

/**
 * このプログラムで使用する例外。
 * Created by kouta on 15/06/03.
 */
public class MyTimetableException extends Exception {
    public static final int TYPE_ERROR = 1;
    public static final int TYPE_WARN = 2;

    private int mType;

    public MyTimetableException(int type, String message) {
        super(message);
        mType = type;
    }

    public MyTimetableException(int type, String message, Throwable e) {
        super(message, e);
        mType = type;
    }

    public String getTitle() {
        return((mType == TYPE_WARN)? Constants.DialogTitles.WARN:Constants.DialogTitles.ERROR);
    }
}
