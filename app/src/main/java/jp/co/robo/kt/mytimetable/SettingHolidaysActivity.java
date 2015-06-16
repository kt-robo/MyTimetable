package jp.co.robo.kt.mytimetable;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

/**
 * 休日設定画面。
 */
public class SettingHolidaysActivity extends ActionBarActivity implements View.OnClickListener {
    private int mSelected;
    private Button mEditButton;
    private Button mDeleteButton;
    private Button mSaveButton;
    private HolidayListAdapter mListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(getString(R.string.setting_holidays));
        setContentView(R.layout.activity_setting_holidays);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mSelected = -1;

        findViewById(R.id.addHolidayButton).setOnClickListener(this);
        mEditButton = (Button)findViewById(R.id.editHolidayButton);
        mEditButton.setEnabled(false);
        mEditButton.setOnClickListener(this);
        mDeleteButton = (Button)findViewById(R.id.deleteHolidayButton);
        mDeleteButton.setEnabled(false);
        mDeleteButton.setOnClickListener(this);
        mSaveButton = (Button)findViewById(R.id.saveHolidaysButton);
        mSaveButton.setEnabled(false);
        mSaveButton.setOnClickListener(this);
        mListAdapter = new HolidayListAdapter(this);
        ListView listView = (ListView)findViewById(R.id.holidayList);
        listView.setAdapter(mListAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mSelected = position;
                mEditButton.setEnabled(true);
                mDeleteButton.setEnabled(true);
            }
        });
        new HolidayLoader(this).load(mListAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_setting_holidays, menu);
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
        switch (view.getId()) {
            case R.id.addHolidayButton:
                showEditDialog(-1, null);
                break;
            case R.id.editHolidayButton:
                showEditDialog(mSelected, mListAdapter.getItem(mSelected));
                break;
            case R.id.deleteHolidayButton:
                showDeleteDialog(mSelected, mListAdapter.getItem(mSelected));
                break;
            case R.id.saveHolidaysButton:
                HolidayInformation[] list = new HolidayInformation[mListAdapter.getCount()];
                for (int i = 0;i < mListAdapter.getCount();i++) {
                    list[i] = mListAdapter.getItem(i);
                }
                new HolidaySaver(this).save(list);
                break;
        }
    }

    private void showEditDialog(int selected, HolidayInformation original) {
        EditHolidayDialog.newInstance(selected, original).show(getFragmentManager(), Constants.DialogTitles.EDIT_HOLIDAY);
    }

    private void showDeleteDialog(int selected, HolidayInformation original) {
        DeleteHolidayDialog.newInstance(selected, original).show(getFragmentManager(), Constants.DialogTitles.DELETE_HOLIDAY);
    }

    public void endDialog(int selected, HolidayInformation target) {
        // (selected == -1)ならadd、(target == null)ならdelete、
        // ((selected != -1) && (target != null))ならedit
        if ((target != null) && (target.getName().length() == 0)) {
            Toast.makeText(this, Constants.Messages.HOLIDAY_NAME_IS_NOT_SPECIFIED, Toast.LENGTH_LONG).show();
            return;
        }
        HolidayInformation original = null;
        if (selected != -1) {
            original = mListAdapter.getItem(selected);
            mListAdapter.remove(original);
        }
        if (target != null) {
            int i;
            for (i = 0;i < mListAdapter.getCount();i++) {
                int compare = target.compareTo(mListAdapter.getItem(i));
                if (compare == 0) {
                    Toast.makeText(this, Constants.Messages.HOLIDAY_IS_DUPLICATED, Toast.LENGTH_LONG).show();
                    if (selected != -1) {
                        mListAdapter.insert(original, selected);
                    }
                    return;
                } else if (compare < 0) {
                    break;
                }
            }
            mListAdapter.insert(target, i);
        }
        mSelected = -1;
        mEditButton.setEnabled(false);
        mDeleteButton.setEnabled(false);
        mSaveButton.setEnabled(true);
    }
}
