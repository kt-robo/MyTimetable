package jp.co.robo.kt.mytimetable;

/**
 * 始発/終電情報クラス。
 * Created by kouta on 15/06/14.
 */
public class FirstLastInformation {
    private String mTime;
    private int mFromTo;
    private boolean mIsHoliday;
    private boolean mIsFirst;

    public FirstLastInformation(String time, int fromto, boolean isHoliday, boolean isFirst) {
        mTime = time;
        mFromTo = fromto;
        mIsHoliday = isHoliday;
        mIsFirst = isFirst;
    }

    public String getTime() {
        return(mTime);

    }

    public int getFromTo() {
        return(mFromTo);
    }

    public boolean isHoliday() {
        return(mIsHoliday);
    }

    public boolean isFirst() {
        return(mIsFirst);
    }
}
