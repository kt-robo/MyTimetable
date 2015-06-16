package jp.co.robo.kt.mytimetable;

/**
 * テスト用の時刻表情報クラス。
 * Created by kouta on 15/05/23.
 */
public class TrainInformation2 {
    private int mId;
    private int mFromto;
    private int mIsHoliday;
    private String mTime;
    private int mLine;
    private int mKind;

    public TrainInformation2(int id, int fromto, int isHoliday, String time, int line, int kind) {
        mId = id;
        mFromto = fromto;
        mIsHoliday = isHoliday;
        mTime = time;
        mLine = line;
        mKind = kind;
    }

    public String getId() {
        return(Integer.toString(mId));
    }

    public String getFromto() {
        return(Integer.toString(mFromto));
    }

    public String getIsHoliday() {
        return(Integer.toString(mIsHoliday));
    }

    public String getTime() {
        return(mTime);
    }

    public String getLine() {
        return(Integer.toString(mLine));
    }

    public String getKind() {
        return(Integer.toString(mKind));
    }
}
