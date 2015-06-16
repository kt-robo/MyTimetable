package jp.co.robo.kt.mytimetable;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 休日設定で使用する、休日を表すクラス。
 * Created by kouta on 15/05/20.
 */
public class HolidayInformation implements Parcelable {
    private int mMonth;     // 1 - 12
    private int mDate;      // 1 - 31
    private boolean mIsHappyMonday;
    private String mName;

    public HolidayInformation(int month, int date, boolean isHappyMonday, String name) {
        mMonth = month;
        mDate = date;
        mIsHappyMonday = isHappyMonday;
        mName = name;
    }

    public int getMonth() {
        return(mMonth);
    }

    public int getDate() {
        return(mDate);
    }

    public boolean isHappyMonday() {
        return(mIsHappyMonday);
    }

    public String getName() {
        return(mName);
    }

    public String getMonthString() {
        return(Constants.Holidays.MONTHS[mMonth - 1]);
    }

    public String getDateString() {
        if (mIsHappyMonday) {
            return(Constants.Holidays.MONDAYS[mDate - 1]);
        } else {
            return(Constants.Holidays.DATES[mDate - 1]);
        }
    }

    public int compareTo(HolidayInformation dest) {
        int ret = mMonth - dest.mMonth;
        if (ret == 0) {
            // HappyMondayは最小の日（第1月曜なら1日、第2月曜なら8日・・・）として数える。
            int date = (mIsHappyMonday)? (((mDate - 1) * 7) + 1):mDate;
            int dest_date = (dest.mIsHappyMonday)? (((dest.mDate - 1) * 7) + 1):dest.mDate;
            ret = date - dest_date;
            if (ret == 0) {
                if (mIsHappyMonday != dest.mIsHappyMonday) {
                    // HappyMondayの最小日と非HappyMondayの日が同一の場合、HappyMondayが小さいとする。
                    if (mIsHappyMonday) {
                        ret = -1;
                    } else {
                        ret = 1;
                    }
                }
            }
        }
        return(ret);
    }

    @Override
    public int describeContents() {
        return(0);
    }

    @Override
    public void writeToParcel(Parcel out, int flag) {
        out.writeInt(mMonth);
        out.writeInt(mDate);
        out.writeInt((mIsHappyMonday)? 1:0);
        out.writeString(mName);
    }

    public static final Parcelable.Creator<HolidayInformation> CREATOR =
            new Parcelable.Creator<HolidayInformation>() {
                @Override
                public HolidayInformation createFromParcel(Parcel in) {
                    return(new HolidayInformation(in.readInt(), in.readInt(), (in.readInt() == 1), in.readString()));
                }
                @Override
                public HolidayInformation[] newArray(int size) {
                    return(new HolidayInformation[size]);
                }
            };
}
