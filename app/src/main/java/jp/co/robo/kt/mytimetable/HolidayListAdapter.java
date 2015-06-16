package jp.co.robo.kt.mytimetable;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


/**
 * 休日一覧を表示するArrayAdapter。
 * Created by kouta on 15/05/20.
 */
public class HolidayListAdapter extends ArrayAdapter<HolidayInformation> {
    private LayoutInflater mLayoutInflater;

    public HolidayListAdapter(Context context) {
        super(context, R.layout.holiday_item);
        mLayoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // convertViewは使い回しされている可能性があるのでnullのときだけ新しく作る。
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.holiday_item, null);
        }
        HolidayInformation item = getItem(position);
        ((TextView)convertView.findViewById(R.id.holidayMonthTextView)).setText(item.getMonthString());
        ((TextView)convertView.findViewById(R.id.holidayDateTextView)).setText(item.getDateString());
        ((TextView)convertView.findViewById(R.id.holidayNameTextView)).setText(item.getName());
        return(convertView);
    }
}
