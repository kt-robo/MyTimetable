package jp.co.robo.kt.mytimetable;

import android.content.Context;

import java.util.Calendar;

/**
 * 日時を保持するクラス。
 * 現在日時と、祝日DB、始発/終電DBから、表示すべき日時を返す。
 * 用語の定義：
 *      週末（Weekend）＝土曜または日曜
 *      祝日（NationalHoliday）＝祝日DBにある、または春分の日/秋分の日。
 *      振替休日（MakeupHoliday）＝祝日かつ日曜の日の後の、最初の祝日でない日。
 *      国民の休日（PeoplesHoliday）＝前日が祝日かつ次の日が祝日の日。
 *      休日（Holiday）＝週末、または祝日、または振替休日、または国民の休日
 *
 * 表示日の決定：
 * （終電は24:00を超える場合があり、始発は00:00より前になることはないという前提）
 *        00:00                  24:00
 * ---------+----------------------+------------
 *    前日   |         当日         |    翌日
 * ---------+----------------------+------------
 *       ↑    ①   ↑   ②     ↑  ③
 *      終電      始発        終電
 * ① ＝ (前日の終電 =< 24:00) && (現在時刻 < 当日の始発) → 表示日は当日のまま
 * ② ＝ (当日の終電 < 24:00) && (当日の始発 < 現在時刻 < 当日の終電） → 表示日は当日のまま
 * ③ ＝ (当日の終電 < 24:00) && (当日の終電 < 現在時刻) → 表示日は翌日のAM00:00
 *
 *        00:00                  24:00
 * ---------+----------------------+------------
 *    前日   |         当日         |    翌日
 * ---------+----------------------+------------
 *           ④ ↑   ⑤   ↑   ⑥      ↑
 *             終電     始発        終電
 * ④ ＝ (前日の終電 > 24:00) && (現在時刻 < 前日の終電 - 24:00) → 表示日は前日の24:00以降（25:00,26:00,...)
 * ⑤ ＝ (前日の終電 > 24:00) && (前日の終電 < 現在時刻 < 当日の始発） → 表示日は当日のまま
 * ⑥ ＝ (当日の始発 < 現在時刻 < 当日の終電) → 表示日は当日のまま
 *
 * 上記を整理すると、
 * (a) (現在時刻 < 当日の始発)
 *      (i) (現在時刻 < 前日の終電 - 24:00) → ④
 *      (ii) (現在時刻 >= 前日の終電 - 24:00) → ①または⑤
 * (b) (現在時刻 >= 当日の始発)
 *      (i) (現在時刻 < 当日の終電) → ②または⑥
 *      (ii) (現在時刻 >= 当日の終電) → ③
 *
 * Created by kouta on 15/06/10.
 */
public class DateTime {
    private Context mContext;

    private int mDay;                   // 1=Sunday, 2=Monday, 3=Tuesday, ...
    private int mHour;                  // 0 - 23
    private int mMinute;                // 0 - 59
    private long mMillis;
    private boolean mIsNationalHoliday;

    private DateTime mYesterday;
    private DateTime mTomorrow;

    private DateTime(Context context, Calendar calendar) {
        mContext = context;
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int date = calendar.get(Calendar.DATE);
        int week = calendar.get(Calendar.WEEK_OF_MONTH);
        mDay = calendar.get(Calendar.DAY_OF_WEEK);
        mHour = calendar.get(Calendar.HOUR_OF_DAY);
        mMinute = calendar.get(Calendar.MINUTE);
        mMillis = calendar.getTimeInMillis();

        HolidayDbHelper helper = HolidayDbHelper.getInstance(mContext);
        if (helper.contains(month, date, mDay, week)) {
            mIsNationalHoliday = true;
        } else if (isVernalEquinox(year, month, date) || isAutumnalEquinox(year, month, date)) {
            mIsNationalHoliday = true;
        }

        mYesterday = null;
        mTomorrow = null;
    }

    public static DateTime getDisplayDateTime(Context context, int fromto) {
        DateTime today = new DateTime(context, Calendar.getInstance());
        boolean isHoliday = today.isHoliday();
        String todays_first = today.getFirstTime(fromto);
        if (todays_first != null) {
            if (today.compareTo(todays_first) < 0) {
                DateTime yesterday = today.getYesterday();
                String yesterdays_last = yesterday.getLastTime(fromto);
                if (yesterdays_last != null) {
                    if (today.compareTo(yesterdays_last, -24) < 0) {
                        yesterday.mHour += 24;
                        return(yesterday);
                    }
                }
            } else {
                String todays_last = today.getLastTime(fromto);
                if (todays_last != null) {
                    if (today.compareTo(todays_last) >= 0) {
                        DateTime tomorrow = today.getTomorrow();
                        tomorrow.mHour = 0;
                        tomorrow.mMinute = 0;
                        return(tomorrow);
                    }
                }
            }
        }
        return(today);
    }

    public String getTime() {
        char[] time = { (char)((mHour / 10) + '0'), (char)((mHour % 10) + '0'), ':', (char)((mMinute / 10) + '0'), (char)((mMinute % 10) + '0') };
        return(new String(time));
    }

    public boolean isHoliday() {
        return((mDay == Calendar.SATURDAY) || (mDay == Calendar.SUNDAY) || mIsNationalHoliday || isMakeupHoliday() || isPeoplesHoliday());
    }

    private boolean isMakeupHoliday() {
        DateTime current = getYesterday();
        while (current.mIsNationalHoliday) {
            if (current.mDay == Calendar.SUNDAY) {
                return(true);
            } else {
                current = current.getYesterday();
            }
        }
        return(false);
    }

    private boolean isPeoplesHoliday() {
        return(getYesterday().mIsNationalHoliday && getTomorrow().mIsNationalHoliday);
    }

    private DateTime getYesterday() {
        if (mYesterday == null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(mMillis - 24 * 60 * 60 * 1000);
            mYesterday = new DateTime(mContext, calendar);
        }
        return(mYesterday);
    }

    private DateTime getTomorrow() {
        if (mTomorrow == null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(mMillis + 24 * 60 * 60 * 1000);
            mTomorrow = new DateTime(mContext, calendar);
        }
        return(mTomorrow);
    }

    private int compareTo(String time) {
        return(compareTo(time, 0));
    }

    private int compareTo(String time, int add) {
        char[] train_time = time.toCharArray();
        int train_hour = (train_time[0] - '0') * 10 + (train_time[1] - '0') + add;
        int ret = mHour - train_hour;
        if (ret == 0) {
            int train_minute = (train_time[3] - '0') * 10 + (train_time[4] - '0');
            ret = mMinute - train_minute;
        }
        return(ret);
    }

    private String getFirstTime(int fromto) {
        return(getTime(fromto, true));
    }

    private String getLastTime(int fromto) {
        return(getTime(fromto, false));
    }

    private String getTime(int fromto, boolean isFirst) {
        FirstLastDbHelper helper = FirstLastDbHelper.getInstance(mContext);
        return(helper.select(fromto, isHoliday(), isFirst));
    }

    private static boolean isVernalEquinox(int year, int month, int date) {
        if ((month - 1) == Calendar.MARCH) {
            int vernalEquinoxDate = (int)((float)(20.8431 + 0.242194 * (year - 1980)) - ((year - 1980) / 4));
            return(date == vernalEquinoxDate);
        } else {
            return(false);
        }
    }

    private static boolean isAutumnalEquinox(int year, int month, int date) {
        if ((month - 1) == Calendar.SEPTEMBER) {
            int autumnalEquinoxDate = (int)((float)(23.2488 + 0.242194 * (year - 1980)) - ((year - 1980) / 4));
            return(date == autumnalEquinoxDate);
        } else {
            return(false);
        }
    }
}
