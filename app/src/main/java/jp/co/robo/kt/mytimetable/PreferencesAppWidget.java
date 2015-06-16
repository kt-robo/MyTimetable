package jp.co.robo.kt.mytimetable;

import android.content.Context;

/**
 * AppWidget用のPreferencesを操作する。
 * Created by kouta on 15/06/09.
 */
public class PreferencesAppWidget extends Preferences {
    private static final String KEY_APP_WIDGET_LIST_SIZE = "APP_WIDGET_LIST_SIZE";
    private static final int DEFAULT_APP_WIDGET_LIST_SIZE = 10;

    public PreferencesAppWidget(Context context) {
        super(context);
    }

    public int getAppWidgetListSize() {
        return(getInt(KEY_APP_WIDGET_LIST_SIZE, DEFAULT_APP_WIDGET_LIST_SIZE));
    }

    public void setAppWidgetListSize(int size) {
        putInt(KEY_APP_WIDGET_LIST_SIZE, size);
    }

    public static int getDefaultAppWidgetListSize() {
        return(DEFAULT_APP_WIDGET_LIST_SIZE);
    }
}
