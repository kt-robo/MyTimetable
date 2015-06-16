package jp.co.robo.kt.mytimetable;

import android.content.Context;
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

/**
 * 詳細設定画面。
 */
public class SettingDetailsActivity extends ActionBarActivity implements View.OnClickListener {
    NumberPicker mAppWidgetListSizePicker;
    EditText mUrlPrefix;
    EditText[] mUrlSuffixes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(getString(R.string.setting_details));
        setContentView(R.layout.activity_setting_details);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mAppWidgetListSizePicker = (NumberPicker)findViewById(R.id.appWidgetListSizePicker);
        mAppWidgetListSizePicker.setMinValue(getResources().getInteger(R.integer.appwidget_list_size_min));
        mAppWidgetListSizePicker.setMaxValue(getResources().getInteger(R.integer.appwidget_list_size_max));
        mAppWidgetListSizePicker.setValue(new PreferencesAppWidget(this).getAppWidgetListSize());

        LinearLayout parent = (LinearLayout)findViewById(R.id.settingUrlsContent);
        LayoutInflater inflater = (LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout child_prefix = (LinearLayout)inflater.inflate(R.layout.setting_url_prefix_item, null);

        PreferencesUrl preferencesUrl = new PreferencesUrl(this);
        mUrlPrefix = (EditText)child_prefix.findViewById(R.id.settingUrlPrefixEditText);
        mUrlPrefix.setText(preferencesUrl.getUrlPrefix());
        parent.addView(child_prefix);
        mUrlSuffixes = new EditText[Constants.Urls.COUNT];

        for (int i = 0;i < mUrlSuffixes.length;i++) {
            LinearLayout child_suffix = (LinearLayout)inflater.inflate(R.layout.setting_url_suffix_item, null);
            ((TextView)child_suffix.findViewById(R.id.urlSuffixStationTextView)).setText(Constants.Urls.getStationStringForUrl(i));
            ((TextView)child_suffix.findViewById(R.id.urlSuffixLineTextView)).setText(Constants.Lines.line2StringForUrl(Constants.Urls.getLine(i)));
            ((TextView)child_suffix.findViewById(R.id.urlSuffixUpDownTextView)).setText(Constants.Urls.getUpDownStringForUrl(i));
            ((TextView)child_suffix.findViewById(R.id.urlSuffixDayTextView)).setText(Constants.WeekdayOrHoliday.boolean2String(Constants.Urls.isHoliday(i)));
            mUrlSuffixes[i] = (EditText)child_suffix.findViewById(R.id.urlSuffixEditText);
            mUrlSuffixes[i].setText(preferencesUrl.getUrlSuffix(i));
            parent.addView(child_suffix);
        }

        (findViewById(R.id.saveSettingDetailsButton)).setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_setting_details, menu);
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

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.saveSettingDetailsButton) {
            String[] values = new String[mUrlSuffixes.length + 2];
            values[0] = Integer.toString(mAppWidgetListSizePicker.getValue());

            values[1] = mUrlPrefix.getText().toString().trim();
            if (!values[1].endsWith(Constants.CommonStrings.SLASH)) {
                values[1] += Constants.CommonStrings.SLASH;
            }

            for (int i = 0;i < mUrlSuffixes.length;i++) {
                values[i + 2] = mUrlSuffixes[i].getText().toString().trim();
                if (!values[i + 2].startsWith(Constants.CommonStrings.SLASH)) {
                    values[i + 2] = Constants.CommonStrings.SLASH + values[i + 2];
                }
            }

            new PreferenceSaver(this).save(values);
        }
    }

}
