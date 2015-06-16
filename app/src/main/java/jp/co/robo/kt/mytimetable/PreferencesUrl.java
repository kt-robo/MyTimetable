package jp.co.robo.kt.mytimetable;

import android.content.Context;

/**
 * URL定義のPreferencesを操作する。
 * Created by kouta on 15/05/15.
 */
public class PreferencesUrl extends Preferences {

    public PreferencesUrl(Context context) {
        super(context);
    }

    public String getUrlPrefix() {
        return(getString(Constants.Urls.KEY_PREFIX, Constants.Urls.DEFAULT_PREFIX));
    }

    public String getUrlSuffix(int i) {
        return(getString(Constants.Urls.KEY_SUFFIXES[i], Constants.Urls.DEFAULT_SUFFIXES[i]));
    }

    public void setUrlPrefix(String prefix) {
        putString(Constants.Urls.KEY_PREFIX, prefix);
    }

    public void setUrlSuffixes(String[] suffixes) {
        for (int i = 0;i < suffixes.length;i++) {
            putString(Constants.Urls.KEY_SUFFIXES[i], suffixes[i]);
        }
    }
}
