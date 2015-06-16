package jp.co.robo.kt.mytimetable;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Preferencesを操作するスーパークラス。
 * Created by kouta on 15/06/09.
 */
public class Preferences {
    private SharedPreferences mPreferences;

    public Preferences(Context context) {
        mPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public String getString(String key, String defaultValue) {
        return(mPreferences.getString(key, defaultValue));
    }

    public int getInt(String key, int defaultValue) {
        return(mPreferences.getInt(key, defaultValue));
    }

    public void putString(String key, String value) {
        mPreferences.edit().putString(key, value).apply();
    }

    public void putInt(String key, int value) {
        mPreferences.edit().putInt(key, value).apply();
    }
}
