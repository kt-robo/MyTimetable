package jp.co.robo.kt.mytimetable;

/**
 * 時刻表の内容を表すクラス。
 * Created by kouta on 15/05/05.
 */
public class TrainInformation {
    private String mTime;
    private int mLine;
    private int mKind;
    private String mTrain;
    private String mDest;
    private int mMark;

    public TrainInformation(String time, int line, int kind, String train, String dest, int mark) {
        mTime = time;
        mLine = line;
        mKind = kind;
        mTrain = train;
        mDest = dest;
        mMark = mark;
    }

    public String getTime() {
        return(mTime);
    }
    public int getLine() {
        return(mLine);
    }
    public int getKind() {
        return(mKind);
    }
    public String getTrain() {
        return(mTrain);
    }
    public String getDest() {
        return(mDest);
    }
    public int getMark() {
        return(mMark);
    }

}
