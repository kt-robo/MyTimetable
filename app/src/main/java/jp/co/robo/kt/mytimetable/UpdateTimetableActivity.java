package jp.co.robo.kt.mytimetable;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;

import java.util.Calendar;

/**
 * 時刻表更新画面。
 */
public class UpdateTimetableActivity extends ActionBarActivity {
    private static final String ACTION_UPDATE_TIMETABLE_FINISHED = "jp.co.robo.kt.mytimetable.ACTION_UPDATE_TIMETABLE_FINISHED";

    private static final int COUNT = Constants.Urls.COUNT;

    private NumberPicker mYearPicker;
    private NumberPicker mMonthPicker;
    private EditText[] mUrlEditText;
    private String mUrlPrefix;
    private String[] mUrlSuffixes;

    public UpdateTimetableActivity() {
        super();
        mUrlSuffixes = new String[COUNT];
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(getString(R.string.update_timetable));
        setContentView(R.layout.activity_update_timetable);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        NumberPicker.OnValueChangeListener listener = new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                // 月/日のNumberPickerが変化したらURLの表示を変える。
                // URLを直接入力で書き換えても、NumberPickerを変えたら元に戻る仕様。
                setUrlTexts();
            }
        };
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;

        mYearPicker = (NumberPicker)findViewById(R.id.urlYearPicker);
        mYearPicker.setMinValue(getResources().getInteger(R.integer.year_min));
        mYearPicker.setMaxValue(getResources().getInteger(R.integer.year_max));
        mYearPicker.setWrapSelectorWheel(false);
        if (year <= mYearPicker.getMaxValue()) {
            mYearPicker.setValue(year);
        }
        mYearPicker.setOnValueChangedListener(listener);

        mMonthPicker = (NumberPicker)findViewById(R.id.urlMonthPicker);
        mMonthPicker.setMinValue(1);
        mMonthPicker.setMaxValue(Constants.Holidays.MONTHS.length);
        mMonthPicker.setDisplayedValues(Constants.Holidays.MONTHS);
        mMonthPicker.setValue(month);
        mMonthPicker.setOnValueChangedListener(listener);

        LayoutInflater inflater = (LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout parent = (LinearLayout)findViewById(R.id.urlsContent);
        mUrlEditText = new EditText[COUNT];
        for (int i = 0;i < COUNT;i++) {
            LinearLayout child = (LinearLayout)inflater.inflate(R.layout.url_list_item, null);
            ((TextView)child.findViewById(R.id.urlStationTextView)).setText(Constants.Urls.getStationStringForUrl(i));
            ((TextView)child.findViewById(R.id.urlLineTextView)).setText(Constants.Lines.line2StringForUrl(Constants.Urls.getLine(i)));
            ((TextView)child.findViewById(R.id.urlUpDownTextView)).setText(Constants.Urls.getUpDownStringForUrl(i));
            ((TextView)child.findViewById(R.id.urlDayTextView)).setText(Constants.WeekdayOrHoliday.boolean2String(Constants.Urls.isHoliday(i)));
            mUrlEditText[i] = (EditText)child.findViewById(R.id.urlEditText);
            parent.addView(child);
        }
        (findViewById(R.id.updateTimetableButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getId() == R.id.updateTimetableButton) {
                    update();
                }
            }
        });
    }

    @Override
    public void onResume() {
        // 詳細設定画面からもどったときのためにプリファレンスの更新はここで行う。
        setUrlPrefixAndSuffixes();
        setUrlTexts();
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_update_timetable, menu);
        return(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return(true);
        }
        return(super.onOptionsItemSelected(item));
    }

    private void setUrlTexts() {
        int year = mYearPicker.getValue();
        int month = mMonthPicker.getValue();
        char[] ymchars = new char[4];
        ymchars[0] = (char)(((year % 100) / 10) + '0');
        ymchars[1] = (char)((year % 10) + '0');
        ymchars[2] = (char)((month / 10) + '0');
        ymchars[3] = (char)((month % 10) + '0');

        for (int i = 0;i < mUrlEditText.length;i++) {
            mUrlEditText[i].setText(mUrlPrefix + new String(ymchars) + mUrlSuffixes[i]);
        }
    }

    private void setUrlPrefixAndSuffixes() {
        PreferencesUrl preferences = new PreferencesUrl(this);
        mUrlPrefix = preferences.getUrlPrefix();
        for (int i = 0;i < mUrlSuffixes.length;i++) {
            mUrlSuffixes[i] = preferences.getUrlSuffix(i);
        }
    }

    private void update() {
        try {
            NetworkInfo ni = ((ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
            if ((ni == null) || (!ni.isConnected())) {
                throw new MyTimetableException(MyTimetableException.TYPE_ERROR, Constants.Messages.ERROR_NETWORK);
            }
            String[] urls = new String[mUrlEditText.length];
            for (int i = 0;i < mUrlEditText.length;i++) {
                urls[i] = mUrlEditText[i].getText().toString();
            }
            new TimetableSaver(this).save(urls);
        } catch (MyTimetableException e) {
            MessageDialog.newInstance(e.getTitle(), e.getMessage());
        }
    }

    public static String getFinishAction() {
        return(ACTION_UPDATE_TIMETABLE_FINISHED);
    }
}
