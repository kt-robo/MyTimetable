package jp.co.robo.kt.mytimetable;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

/**
 * 時刻表を表示するメイン画面。
 * Created by kouta on 15/05/08.
 */
public class MyTimetableActivity extends ActionBarActivity {
    private BroadcastReceiver mReceiver;
    private TimetableListAdapter[][] mTimetableListAdapters;

    @Override
    @SuppressWarnings({"deprecation"})
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(getClass().getName(), "onCreate()");
        super.onCreate(savedInstanceState);
        setTitle(getString(R.string.my_timetable));
        setContentView(R.layout.activity_my_timetable);

        Constants.init(this);

        int init_fromto = Constants.FromTo.DEFAULT_FROMTO;
        Intent intent = getIntent();
        if (intent != null) {
            // Intentが指定された（＝AppWidgetから呼ばれた）ら、指定されたfromtoを初期値とする。
            init_fromto = intent.getIntExtra(Constants.ArgumentNames.FIRST_FROMTO, init_fromto);
            Log.d(getClass().getName(), "init_fromto=" + init_fromto);
        }

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        DayTabListener[] dayTabListeners = DayTabListener.create(this, init_fromto);
        mTimetableListAdapters = new TimetableListAdapter[dayTabListeners.length][Constants.FromTo.FROMTO.length];
        for (DayTabListener dayTabListener : dayTabListeners) {
            ActionBar.Tab tab = actionBar.newTab();
            tab.setTabListener(dayTabListener);
            tab.setText(dayTabListener.getTabName());
            actionBar.addTab(tab);
        }
        if (intent != null) {
            // Intentが指定された（＝AppWidgetから呼ばれた）ら、現在日時から平日/土休の初期値を決定する。
            actionBar.setSelectedNavigationItem(Constants.WeekdayOrHoliday.boolean2Position(DateTime.getDisplayDateTime(this, init_fromto).isHoliday()));
        }

        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (UpdateTimetableActivity.getFinishAction().equals(intent.getAction())) {
                    // 時刻表更新が完了したときに呼ばれる。
                    // ここでArrayAdapterをクリアすることで、以降の表示時に時刻表を再読み込みする。
                    // onResume()とかでクリアすると、必要ない場合（＝時刻表が変更されてない場合）
                    // も再読み込みが走ってしまうので、明示的に「時刻表が変わった」場合のみクリアする。
                    clearTimetableListAdapters();
                }
            }
        };
        registerReceiver(mReceiver, new IntentFilter(UpdateTimetableActivity.getFinishAction()));
    }

    @Override
    public void onDestroy() {
        clearTimetableListAdapters();
        mTimetableListAdapters = null;
        if  (mReceiver != null) {
            unregisterReceiver(mReceiver);
            mReceiver = null;
        }
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_my_timetable, menu);
        return(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        //noinspection SimplifiableIfStatement
        String action;
        switch (item.getItemId()) {
            case R.id.action_update_timetable:
                action = UpdateTimetableActivity.class.getName();
                break;
            case R.id.action_setting_holidays:
                action = SettingHolidaysActivity.class.getName();
                break;
            case R.id.action_setting_details:
                action = SettingDetailsActivity.class.getName();
                break;
            case R.id.action_test:
                action = TestActivity.class.getName();
                break;
            default:
                return(super.onOptionsItemSelected(item));
        }
        startActivity(new Intent().setClassName(getApplicationContext(), action));
        return(true);
    }

    // isHolidayとfromtoから、TimetableListAdapterを返す。
    // isHoliday -> true/false、fromto -> 1, 2, 3, ...
    public TimetableListAdapter getListAdapter(boolean isHoliday, int fromto) {
        int i = Constants.WeekdayOrHoliday.boolean2Position(isHoliday);
        int j = Constants.FromTo.fromTo2Position(fromto);
        if (mTimetableListAdapters[i][j] == null) {
            // nullならば、新しくTimetableListAdapterを作り、DB読み込みを実行し、Listを作成する。
            TimetableListAdapter adapter = new TimetableListAdapter(this);
            new TimetableLoader(this).load(adapter, i, fromto);
            mTimetableListAdapters[i][j] = adapter;
        }
        return(mTimetableListAdapters[i][j]);
    }

    // 持っているすべてのTimetableListAdapterをクリアする。
    // 時刻表DBが更新されたときに呼ばれ、この後の最初のgetListAdapter()実行時にDBを読み直す。
    public void clearTimetableListAdapters() {
        if (mTimetableListAdapters != null) {
            for (int i = 0;i < mTimetableListAdapters.length;i++) {
                for (int j = 0;j < mTimetableListAdapters[i].length;j++) {
                    mTimetableListAdapters[i][j] = null;
                }
            }
        }
    }

}
