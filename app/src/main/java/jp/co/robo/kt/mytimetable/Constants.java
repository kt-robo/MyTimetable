package jp.co.robo.kt.mytimetable;

import android.content.Context;

/**
 * 定数を定義するクラス。
 * 複数のクラスにまたがるような定数、または多言語対応が必要なものをここで定義する。
 * 特定のクラスに固有で、かつ多言語対応が不要なものは、そのクラスに定義する。
 * Created by kouta on 15/05/08.
 */
public class Constants {
    public static class ArgumentNames {
        public static final String IS_HOLIDAY   = "ARGUMENT_IS_HOLIDAY";
        public static final String FROMTO       = "ARGUMENT_FROMTO";
        public static final String SELECTED     = "ARGUMENT_SELECTED";
        public static final String HOLIDAY      = "ARGUMENT_HOLIDAY";
        public static final String TITLE        = "ARGUMENT_TITLE";
        public static final String MESSAGE      = "ARGUMENT_MESSAGE";
        public static final String FIRST_FROMTO = "ARGUMENT_FIRST_FROMTO";
    }

    public static class DialogTitles {
        public static String ERROR;
        public static String WARN;
        public static String ADD_HOLIDAY;
        public static String EDIT_HOLIDAY;
        public static String DELETE_HOLIDAY;

        public static void init(Context context) {
            ERROR = context.getString(R.string.dialog_title_error);
            WARN = context.getString(R.string.dialog_title_warn);
            ADD_HOLIDAY = context.getString(R.string.dialog_title_add_holiday);
            EDIT_HOLIDAY = context.getString(R.string.dialog_title_edit_holiday);
            DELETE_HOLIDAY = context.getString(R.string.dialog_title_delete_holiday);
        }
    }

    public static class Messages {
        public static String DOWNLOADING_TIMETABLE;
        public static String SAVING_TIMETABLE;
        public static String UPDATE_TIMETABLE_COMPLETED;
        public static String HOLIDAY_IS_DUPLICATED;
        public static String HOLIDAY_NAME_IS_NOT_SPECIFIED;
        public static String SAVING_HOLIDAYS;
        public static String HOLIDAYS_SAVED;
        public static String DETAILS_SAVED;

        public static String ERROR_NETWORK;

        public static void init(Context context) {
            DOWNLOADING_TIMETABLE = context.getString(R.string.message_downloading_timetable);
            SAVING_TIMETABLE = context.getString(R.string.message_saving_timetable);
            UPDATE_TIMETABLE_COMPLETED = context.getString(R.string.message_update_timetable_completed);
            HOLIDAY_IS_DUPLICATED = context.getString(R.string.message_holiday_is_duplicated);
            HOLIDAY_NAME_IS_NOT_SPECIFIED = context.getString(R.string.message_holiday_name_is_not_specified);
            SAVING_HOLIDAYS = context.getString(R.string.message_saving_holidays);
            HOLIDAYS_SAVED = context.getString(R.string.message_holidays_saved);
            DETAILS_SAVED = context.getString(R.string.message_details_saved);
            ERROR_NETWORK = context.getString(R.string.message_error_network);
        }
    }

    public static class CommonStrings {
        public static String OK;
        public static String CANCEL;
        public static final String SLASH = "/";
        public static final String PERCENT = "%";
        public static final String LEFT_BRACKET = "[";
        public static final String RIGHT_BRACKET = "]";

        public static void init(Context context) {
            OK = context.getString(R.string.ok);
            CANCEL = context.getString(R.string.cancel);
        }

        public static String boolean2String(boolean bool) {
            return((bool)? "1":"0");
        }
    }

    public static class WeekdayOrHoliday {
        private static String WEEKDAY;
        private static String HOLIDAY;
        private static String WEEKDAY_FOR_URL;
        private static String HOLIDAY_FOR_URL;

        public static void init(Context context) {
            WEEKDAY = context.getString(R.string.weekday);
            HOLIDAY = context.getString(R.string.holiday);
            WEEKDAY_FOR_URL = context.getString(R.string.weekday_for_url);
            HOLIDAY_FOR_URL = context.getString(R.string.holiday_for_url);
        }

        public static String boolean2String(boolean isHoliday) {
            return((isHoliday)? HOLIDAY:WEEKDAY);
        }

        public static String position2String(int position) {
            return(boolean2String(position2Boolean(position)));
        }

        public static String boolean2StringForUrl(boolean isHoliday) {
            return((isHoliday)? HOLIDAY_FOR_URL:WEEKDAY_FOR_URL);
        }

        public static String position2StringForUrl(int position) {
            return(boolean2StringForUrl(position2Boolean(position)));
        }

        // boolean値とposition（DB中の値や配列の添え字）の関係は、
        // booleanToPosition()とpositionToBoolean()で決める。
        public static int boolean2Position(boolean isHoliday) {
            return((isHoliday)? 1:0);
        }

        public static boolean position2Boolean(int position) {
            return(position == 1);
        }

    }

    public static class FromTo {
        public static final int FROMTO_O2Y      = 1;
        public static final int FROMTO_Y2O      = 2;
        public static final int FROMTO_T2Y      = 3;
        public static final int FROMTO_T2O      = 4;
        public static final int FROMTO_INVALID  = -1;
        public static final int DEFAULT_FROMTO  = FROMTO_O2Y;
        // FROMTO_*の値（DB中の値）とposition（配列の添え字）の関係はFROMTO配列で決める。
        public static final int[] FROMTO        = { FROMTO_O2Y, FROMTO_Y2O, FROMTO_T2Y, FROMTO_T2O };
        private static String FROMTO_STRING_O2Y;
        private static String FROMTO_STRING_Y2O;
        private static String FROMTO_STRING_T2Y;
        private static String FROMTO_STRING_T2O;

        public static void init(Context context) {
            FROMTO_STRING_O2Y = context.getString(R.string.fromto_o2y);
            FROMTO_STRING_Y2O = context.getString(R.string.fromto_y2o);
            FROMTO_STRING_T2Y = context.getString(R.string.fromto_t2y);
            FROMTO_STRING_T2O = context.getString(R.string.fromto_t2o);
        }

        public static String fromTo2String(int fromto) {
            switch (fromto) {
                case FROMTO_O2Y:
                    return(FROMTO_STRING_O2Y);
                case FROMTO_Y2O:
                    return(FROMTO_STRING_Y2O);
                case FROMTO_T2Y:
                    return(FROMTO_STRING_T2Y);
                case FROMTO_T2O:
                    return(FROMTO_STRING_T2O);
                default:
                    return(null);
            }
        }

        public static String position2String(int position) {
            return(fromTo2String(position2FromTo(position)));
        }

        public static int position2FromTo(int position) {
            return(FROMTO[position]);
        }

        public static int fromTo2Position(int fromto) {
            for (int i = 0;i < FROMTO.length;i ++) {
                if (fromto == FROMTO[i]) {
                    return(i);
                }
            }
            return(0);
        }

        public static int string2Position(String str) {
            if (FROMTO_STRING_O2Y.equals(str)) {
                return(fromTo2Position(FROMTO_O2Y));
            } else if (FROMTO_STRING_Y2O.equals(str)) {
                return(fromTo2Position(FROMTO_Y2O));
            } else if (FROMTO_STRING_T2Y.equals(str)) {
                return(fromTo2Position(FROMTO_T2Y));
            } else if (FROMTO_STRING_T2O.equals(str)) {
                return(fromTo2Position(FROMTO_T2O));
            } else {
                return(FROMTO_INVALID);
            }
        }
    }

    public static class Lines {
        public static final int LINE_T = 1;
        public static final int LINE_Y = 2;
        public static final int LINE_S = 3;
        private static String LINE_NAME_T;
        private static String LINE_NAME_Y;
        private static String LINE_NAME_S;
        private static String LINE_NAME_T_FOR_URL;
        private static String LINE_NAME_Y_FOR_URL;
        private static String LINE_NAME_S_FOR_URL;

        public static void init(Context context) {
            LINE_NAME_T = context.getString(R.string.line_t);
            LINE_NAME_Y = context.getString(R.string.line_y);
            LINE_NAME_S = context.getString(R.string.line_s);
            LINE_NAME_T_FOR_URL = context.getString(R.string.line_t_for_url);
            LINE_NAME_Y_FOR_URL = context.getString(R.string.line_y_for_url);
            LINE_NAME_S_FOR_URL = context.getString(R.string.line_s_for_url);
        }

        public static String line2String(int line) {
            switch (line) {
                case LINE_T:
                    return(LINE_NAME_T);
                case LINE_Y:
                    return(LINE_NAME_Y);
                case LINE_S:
                    return(LINE_NAME_S);
                default:
                    return(null);
            }
        }

        public static String line2StringForUrl(int line) {
            switch (line) {
                case LINE_T:
                    return(LINE_NAME_T_FOR_URL);
                case LINE_Y:
                    return(LINE_NAME_Y_FOR_URL);
                case LINE_S:
                    return(LINE_NAME_S_FOR_URL);
                default:
                    return(null);
            }
        }
    }

    public static class Marks {
        private static final int MARK_FLAG_NONE             = 0;
        private static final int MARK_FLAG_DAY_ATTENTION    = 0x00000001;
        private static final int MARK_FLAG_CHANGE_TIME      = 0x00000002;
        private static final int MARK_FLAG_CHANGE_DEST      = 0x00000004;
        private static final int MARK_FLAG_START_STATION    = 0x00000008;
        private static final int MARK_FLAG_OTHER            = 0x00010000;
        private static final int[] MARK_FLAGS = {
                MARK_FLAG_DAY_ATTENTION,
                MARK_FLAG_CHANGE_TIME,
                MARK_FLAG_CHANGE_DEST,
                MARK_FLAG_START_STATION,
                MARK_FLAG_OTHER
        };

        private static final String MARK_STRING_NONE = "";
        private static String MARK_STRING_DAY_ATTENTION;
        private static String MARK_STRING_CHANGE_TIME;
        private static String MARK_STRING_CHANGE_DEST;
        private static String MARK_STRING_START_STATION;
        private static String MARK_STRING_OTHER;
        private static String[] MARK_STRINGS;

        private static final char MARK_CHAR_DAY_ATTENTION   = '◆';
        private static final char MARK_CHAR_CHANGE_TIME     = '■';
        private static final char MARK_CHAR_CHANGE_DEST     = '▲';
        private static final char MARK_CHAR_START_STATION   = '●';

        public static void init(Context context) {
            MARK_STRING_DAY_ATTENTION = context.getString(R.string.mark_day_attention);
            MARK_STRING_CHANGE_TIME = context.getString(R.string.mark_change_time);
            MARK_STRING_CHANGE_DEST = context.getString(R.string.mark_change_dest);
            MARK_STRING_START_STATION = context.getString(R.string.mark_start_station);
            MARK_STRING_OTHER = context.getString(R.string.mark_other);
            MARK_STRINGS = new String[] {
                    MARK_STRING_DAY_ATTENTION,
                    MARK_STRING_CHANGE_TIME,
                    MARK_STRING_CHANGE_DEST,
                    MARK_STRING_START_STATION,
                    MARK_STRING_OTHER
            };
        }

        public static String mark2String(int mark) {
            String ret = MARK_STRING_NONE;
            for (int i = 0;i < MARK_FLAGS.length;i++) {
                if ((mark & MARK_FLAGS[i]) != 0) {
                    ret += MARK_STRINGS[i];
                }
            }
            return(ret);
        }

        public static int string2Mark(String mark) {
            int ret = MARK_FLAG_NONE;
            for (char c : mark.toCharArray()) {
                if (c == MARK_CHAR_DAY_ATTENTION) {
                    ret |= MARK_FLAG_DAY_ATTENTION;
                } else if (c == MARK_CHAR_CHANGE_TIME) {
                    ret |= MARK_FLAG_CHANGE_TIME;
                } else if (c == MARK_CHAR_CHANGE_DEST) {
                    ret |= MARK_FLAG_CHANGE_DEST;
                } else if (c == MARK_CHAR_START_STATION) {
                    ret |= MARK_FLAG_START_STATION;
                } else if (!Character.isSpaceChar(c) && !Character.isWhitespace(c)) {
                    ret |= MARK_FLAG_OTHER;
                }
            }
            return(ret);
        }
    }

    public static class Colors {
        private static int BACKGROUND_COLOR_NORMAL;
        private static int BACKGROUND_COLOR_EXPRESS;
        private static int BACKGROUND_COLOR_RAPID;
        private static int BACKGROUND_COLOR_LINER;
        private static int BACKGROUND_COLOR_SPECIAL;
        private static int BACKGROUND_COLOR_OTHER;

        private static int TEXT_COLOR;
        private static int TEXT_COLOR_T;
        private static int TEXT_COLOR_Y;
        private static int TEXT_COLOR_S;

        private static int TEXT_COLOR_W;
        private static int TEXT_COLOR_W_T;
        private static int TEXT_COLOR_W_Y;
        private static int TEXT_COLOR_W_S;

        private static final int KIND_NORMAL            = 0;
        private static final int KIND_EXPRESS           = 1;
        private static final int KIND_RAPID             = 2;
        private static final int KIND_LINER             = 3;
        private static final int KIND_SPECIAL           = 4;
        private static final int KIND_OTHER             = 5;

        public static final int KIND_COUNT              = KIND_OTHER + 1;

        private static final String COLOR_STRING_BLACK  = "color:black;";
        private static final String COLOR_STRING_RED    = "color:red;";
        private static final String COLOR_STRING_BLUE   = "color:blue;";
        private static final String COLOR_STRING_GREEN  = "color:green;";
        private static final String COLOR_STRING_BROWN  = "color:brown;";

        public static void init(Context context) {
            BACKGROUND_COLOR_NORMAL = context.getResources().getColor(R.color.background_normal);
            BACKGROUND_COLOR_EXPRESS = context.getResources().getColor(R.color.background_express);
            BACKGROUND_COLOR_RAPID = context.getResources().getColor(R.color.background_rapid);
            BACKGROUND_COLOR_LINER = context.getResources().getColor(R.color.background_liner);
            BACKGROUND_COLOR_SPECIAL = context.getResources().getColor(R.color.background_special);
            BACKGROUND_COLOR_OTHER = context.getResources().getColor(R.color.background_other);

            TEXT_COLOR = context.getResources().getColor(R.color.text_color);
            TEXT_COLOR_T = context.getResources().getColor(R.color.text_color_t);
            TEXT_COLOR_Y = context.getResources().getColor(R.color.text_color_y);
            TEXT_COLOR_S = context.getResources().getColor(R.color.text_color_s);

            TEXT_COLOR_W = context.getResources().getColor(R.color.text_color_w);
            TEXT_COLOR_W_T = context.getResources().getColor(R.color.text_color_w_t);
            TEXT_COLOR_W_Y = context.getResources().getColor(R.color.text_color_w_y);
            TEXT_COLOR_W_S = context.getResources().getColor(R.color.text_color_w_s);
        }

        public static int kind2BackgroundColor(int kind) {
            switch (kind) {
                case KIND_NORMAL:
                    return(BACKGROUND_COLOR_NORMAL);
                case KIND_EXPRESS:
                    return(BACKGROUND_COLOR_EXPRESS);
                case KIND_RAPID:
                    return(BACKGROUND_COLOR_RAPID);
                case KIND_LINER:
                    return(BACKGROUND_COLOR_LINER);
                case KIND_SPECIAL:
                    return(BACKGROUND_COLOR_SPECIAL);
                default:
                    return(BACKGROUND_COLOR_OTHER);
            }
        }

        public static int kind2Layout(int kind) {
            switch (kind) {
                case KIND_NORMAL:
                    return(R.layout.widget_list_item_normal);
                case KIND_EXPRESS:
                    return(R.layout.widget_list_item_express);
                case KIND_RAPID:
                    return(R.layout.widget_list_item_rapid);
                case KIND_LINER:
                    return(R.layout.widget_list_item_liner);
                case KIND_SPECIAL:
                    return(R.layout.widget_list_item_special);
                default:
                    return(R.layout.widget_list_item_other);
            }
        }

        public static int line2TextColor(int line) {
            switch (line) {
                case Lines.LINE_T:
                    return(TEXT_COLOR_T);
                case Lines.LINE_Y:
                    return(TEXT_COLOR_Y);
                case Lines.LINE_S:
                    return(TEXT_COLOR_S);
                default:
                    return(TEXT_COLOR);
            }
        }

        public static int line2AppWidgetTextColor(int line) {
            switch (line) {
                case Lines.LINE_T:
                    return(TEXT_COLOR_W_T);
                case Lines.LINE_Y:
                    return(TEXT_COLOR_W_Y);
                case Lines.LINE_S:
                    return(TEXT_COLOR_W_S);
                default:
                    return(TEXT_COLOR_W);
            }
        }

        public static int string2Kind(String color) {
            if (color != null) {
                if (color.equalsIgnoreCase(COLOR_STRING_BLACK)) {
                    return(KIND_NORMAL);
                } else if (color.equalsIgnoreCase(COLOR_STRING_RED)) {
                    return(KIND_EXPRESS);
                } else if (color.equalsIgnoreCase(COLOR_STRING_BLUE)) {
                    return(KIND_RAPID);
                } else if (color.equalsIgnoreCase(COLOR_STRING_GREEN)) {
                    return(KIND_LINER);
                } else if (color.equalsIgnoreCase(COLOR_STRING_BROWN)) {
                    return(KIND_SPECIAL);
                }
            }
            return(KIND_OTHER);
        }
    }

    public static class Stations {
        public static String STATION_O;
        public static String STATION_Y;
        public static String STATION_T;

        public static void init(Context context) {
            STATION_O = context.getString(R.string.station_o);
            STATION_Y = context.getString(R.string.station_y);
            STATION_T = context.getString(R.string.station_t);
        }
    }

    public static class UpDown {
        public static String UP;
        public static String DOWN;

        public static void init(Context context) {
            UP = context.getString(R.string.up);
            DOWN = context.getString(R.string.down);
        }
    }

    public static class Holidays {
        public static String[] MONTHS;
        public static String[] DATES;
        public static String[] MONDAYS;

        public static void init(Context context) {
            MONTHS = context.getResources().getStringArray(R.array.month_names);
            DATES = new String[31];
            String[] dateSuffixes = context.getResources().getStringArray(R.array.date_suffixes);
            for (int i = 1;i < DATES.length + 1;i++) {
                String suffix = dateSuffixes[0];
                if ((i % 10) < 4) {
                    // 1st, 2nd, 3rdの対応。ただし(10 < i < 20)は11th, 12th, 13th
                    if ((i < 10) || (i > 19)) {
                        suffix = dateSuffixes[(i % 10)];
                    }
                }
                DATES[i - 1] = Integer.toString(i) + suffix;
            }
            MONDAYS = context.getResources().getStringArray(R.array.mondays);
        }
    }

    public static class Urls {
        public static final String KEY_PREFIX               = "URL_PREFIX";

        private static final String KEY_SUFFIX_BASE         = "URL_SUFFIX";

        private static final String KEY_SUFFIX_STATION_O    = "O";
        private static final String KEY_SUFFIX_STATION_Y    = "Y";
        private static final String KEY_SUFFIX_STATION_T    = "T";

        private static final String KEY_SUFFIX_UP           = "UP";
        private static final String KEY_SUFFIX_DOWN         = "DOWN";

        private static final String KEY_SUFFIX_LINE_T       = "T";
        private static final String KEY_SUFFIX_LINE_Y       = "Y";
        private static final String KEY_SUFFIX_LINE_S       = "S";

        private static final String KEY_SUFFIX_WEEKDAY      = "W";
        private static final String KEY_SUFFIX_HOLIDAY      = "H";

        /**
         * URLの並びは次のとおり。
         *      駅＝大船/横浜/戸塚 {
         *          上り/下り（大船は上りだけ、横浜は下りだけ、戸塚は上り/下り両方） {
         *              路線＝東海道/横須賀/湘南新宿 {
         *                  平日/土休
         *              }
         *          }
         *      }
         * この並びにしたがって、0〜23の番号をふり、それが添え字となる。
         * 逆に、番号から発着、路線、平日/土休を取得するメソッドを提供する。使用者は順序を知る必要がない。
         */

        public static int getFromTo(int num) {
            // 発着は番号6個ずつで、O2Y->Y2O->T2Y->T2Oと変化する。
            switch (num / 6) {
                case 0:
                    return(FromTo.FROMTO_O2Y);
                case 1:
                    return(FromTo.FROMTO_Y2O);
                case 2:
                    return(FromTo.FROMTO_T2Y);
                case 3:
                    return(FromTo.FROMTO_T2O);
                default:
                    return(FromTo.FROMTO_INVALID);
            }
        }

        public static int getLine(int num) {
            // 路線は番号3個ずつで、T->Y->S->T->Y->S->...と変化する。
            switch ((num / 2) % 3) {
                case 0:
                    return(Lines.LINE_T);
                case 1:
                    return(Lines.LINE_Y);
                case 2:
                    return(Lines.LINE_S);
                default:
                    return(0);
            }
        }

        public static boolean isHoliday(int num) {
            // 平日/土休は番号1個ずつで、平日->土休->平日->土休->...と変化する。
            return((num % 2) == 1);
        }

        public static String getStationStringForUrl(int num) {
            switch (getFromTo(num)) {
                case FromTo.FROMTO_O2Y:
                    return(Stations.STATION_O);
                case FromTo.FROMTO_Y2O:
                    return(Stations.STATION_Y);
                case FromTo.FROMTO_T2Y:
                case FromTo.FROMTO_T2O:
                    return(Stations.STATION_T);
                default:
                    return(null);
            }
        }

        public static String getUpDownStringForUrl(int num) {
            switch (getFromTo(num)) {
                case FromTo.FROMTO_O2Y:
                case FromTo.FROMTO_T2Y:
                    return(UpDown.UP);
                case FromTo.FROMTO_Y2O:
                case FromTo.FROMTO_T2O:
                    return(UpDown.DOWN);
                default:
                    return(null);
            }
        }

        public static final String[] KEY_SUFFIXES = {
                // 大船は上りのみ
                KEY_SUFFIX_BASE + "_" + KEY_SUFFIX_STATION_O + "_" + KEY_SUFFIX_UP + "_" + KEY_SUFFIX_LINE_T + "_" + KEY_SUFFIX_WEEKDAY,
                KEY_SUFFIX_BASE + "_" + KEY_SUFFIX_STATION_O + "_" + KEY_SUFFIX_UP + "_" + KEY_SUFFIX_LINE_T + "_" + KEY_SUFFIX_HOLIDAY,
                KEY_SUFFIX_BASE + "_" + KEY_SUFFIX_STATION_O + "_" + KEY_SUFFIX_UP + "_" + KEY_SUFFIX_LINE_Y + "_" + KEY_SUFFIX_WEEKDAY,
                KEY_SUFFIX_BASE + "_" + KEY_SUFFIX_STATION_O + "_" + KEY_SUFFIX_UP + "_" + KEY_SUFFIX_LINE_Y + "_" + KEY_SUFFIX_HOLIDAY,
                KEY_SUFFIX_BASE + "_" + KEY_SUFFIX_STATION_O + "_" + KEY_SUFFIX_UP + "_" + KEY_SUFFIX_LINE_S + "_" + KEY_SUFFIX_WEEKDAY,
                KEY_SUFFIX_BASE + "_" + KEY_SUFFIX_STATION_O + "_" + KEY_SUFFIX_UP + "_" + KEY_SUFFIX_LINE_S + "_" + KEY_SUFFIX_HOLIDAY,

                // 横浜は下りのみ
                KEY_SUFFIX_BASE + "_" + KEY_SUFFIX_STATION_Y + "_" + KEY_SUFFIX_DOWN + "_" + KEY_SUFFIX_LINE_T + "_" + KEY_SUFFIX_WEEKDAY,
                KEY_SUFFIX_BASE + "_" + KEY_SUFFIX_STATION_Y + "_" + KEY_SUFFIX_DOWN + "_" + KEY_SUFFIX_LINE_T + "_" + KEY_SUFFIX_HOLIDAY,
                KEY_SUFFIX_BASE + "_" + KEY_SUFFIX_STATION_Y + "_" + KEY_SUFFIX_DOWN + "_" + KEY_SUFFIX_LINE_Y + "_" + KEY_SUFFIX_WEEKDAY,
                KEY_SUFFIX_BASE + "_" + KEY_SUFFIX_STATION_Y + "_" + KEY_SUFFIX_DOWN + "_" + KEY_SUFFIX_LINE_Y + "_" + KEY_SUFFIX_HOLIDAY,
                KEY_SUFFIX_BASE + "_" + KEY_SUFFIX_STATION_Y + "_" + KEY_SUFFIX_DOWN + "_" + KEY_SUFFIX_LINE_S + "_" + KEY_SUFFIX_WEEKDAY,
                KEY_SUFFIX_BASE + "_" + KEY_SUFFIX_STATION_Y + "_" + KEY_SUFFIX_DOWN + "_" + KEY_SUFFIX_LINE_S + "_" + KEY_SUFFIX_HOLIDAY,

                // 戸塚の上り
                KEY_SUFFIX_BASE + "_" + KEY_SUFFIX_STATION_T + "_" + KEY_SUFFIX_UP + "_" + KEY_SUFFIX_LINE_T + "_" + KEY_SUFFIX_WEEKDAY,
                KEY_SUFFIX_BASE + "_" + KEY_SUFFIX_STATION_T + "_" + KEY_SUFFIX_UP + "_" + KEY_SUFFIX_LINE_T + "_" + KEY_SUFFIX_HOLIDAY,
                KEY_SUFFIX_BASE + "_" + KEY_SUFFIX_STATION_T + "_" + KEY_SUFFIX_UP + "_" + KEY_SUFFIX_LINE_Y + "_" + KEY_SUFFIX_WEEKDAY,
                KEY_SUFFIX_BASE + "_" + KEY_SUFFIX_STATION_T + "_" + KEY_SUFFIX_UP + "_" + KEY_SUFFIX_LINE_Y + "_" + KEY_SUFFIX_HOLIDAY,
                KEY_SUFFIX_BASE + "_" + KEY_SUFFIX_STATION_T + "_" + KEY_SUFFIX_UP + "_" + KEY_SUFFIX_LINE_S + "_" + KEY_SUFFIX_WEEKDAY,
                KEY_SUFFIX_BASE + "_" + KEY_SUFFIX_STATION_T + "_" + KEY_SUFFIX_UP + "_" + KEY_SUFFIX_LINE_S + "_" + KEY_SUFFIX_HOLIDAY,

                // 戸塚の下り
                KEY_SUFFIX_BASE + "_" + KEY_SUFFIX_STATION_T + "_" + KEY_SUFFIX_DOWN + "_" + KEY_SUFFIX_LINE_T + "_" + KEY_SUFFIX_WEEKDAY,
                KEY_SUFFIX_BASE + "_" + KEY_SUFFIX_STATION_T + "_" + KEY_SUFFIX_DOWN + "_" + KEY_SUFFIX_LINE_T + "_" + KEY_SUFFIX_HOLIDAY,
                KEY_SUFFIX_BASE + "_" + KEY_SUFFIX_STATION_T + "_" + KEY_SUFFIX_DOWN + "_" + KEY_SUFFIX_LINE_Y + "_" + KEY_SUFFIX_WEEKDAY,
                KEY_SUFFIX_BASE + "_" + KEY_SUFFIX_STATION_T + "_" + KEY_SUFFIX_DOWN + "_" + KEY_SUFFIX_LINE_Y + "_" + KEY_SUFFIX_HOLIDAY,
                KEY_SUFFIX_BASE + "_" + KEY_SUFFIX_STATION_T + "_" + KEY_SUFFIX_DOWN + "_" + KEY_SUFFIX_LINE_S + "_" + KEY_SUFFIX_WEEKDAY,
                KEY_SUFFIX_BASE + "_" + KEY_SUFFIX_STATION_T + "_" + KEY_SUFFIX_DOWN + "_" + KEY_SUFFIX_LINE_S + "_" + KEY_SUFFIX_HOLIDAY,
        };

        public static final String DEFAULT_PREFIX = "https://www.jreast-timetable.jp/sp/";
        public static final String[] DEFAULT_SUFFIXES = {
                // KEY_SUFFIXESと同じ並びとする。
                "/timetable/tt0342/0342020.html",
                "/timetable/tt0342/0342021.html",
                "/timetable/tt0342/0342050.html",
                "/timetable/tt0342/0342051.html",
                "/timetable/tt0342/0342030.html",
                "/timetable/tt0342/0342031.html",
                "/timetable/tt1638/1638010.html",
                "/timetable/tt1638/1638011.html",
                "/timetable/tt1638/1638060.html",
                "/timetable/tt1638/1638061.html",
                "/timetable/tt1638/1638030.html",
                "/timetable/tt1638/1638031.html",
                "/timetable/tt1057/1057020.html",
                "/timetable/tt1057/1057021.html",
                "/timetable/tt1057/1057060.html",
                "/timetable/tt1057/1057061.html",
                "/timetable/tt1057/1057040.html",
                "/timetable/tt1057/1057041.html",
                "/timetable/tt1057/1057010.html",
                "/timetable/tt1057/1057011.html",
                "/timetable/tt1057/1057050.html",
                "/timetable/tt1057/1057051.html",
                "/timetable/tt1057/1057030.html",
                "/timetable/tt1057/1057031.html"
        };

        public static final int COUNT = KEY_SUFFIXES.length;
    }

    public static void init(Context context) {
        DialogTitles.init(context);
        Messages.init(context);
        CommonStrings.init(context);
        Stations.init(context);
        Holidays.init(context);
        UpDown.init(context);
        initForAppWidget(context);
    }

    public static void initForAppWidget(Context context) {
        WeekdayOrHoliday.init(context);
        FromTo.init(context);
        Lines.init(context);
        Marks.init(context);
        Colors.init(context);
    }
}
