package jp.co.robo.kt.mytimetable;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * テスト表示用のArrayAdapter。
 * Created by kouta on 15/05/23.
 */
public class TestListAdapter extends ArrayAdapter<TrainInformation2> {
    private LayoutInflater mLayoutInflater;

    public TestListAdapter(Context context) {
        super(context, R.layout.test_list_item);
        mLayoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.test_list_item, null);
        }
        TrainInformation2 item = getItem(position);
        ((TextView)convertView.findViewById(R.id.testIdTextView)).setText(item.getId());
        ((TextView)convertView.findViewById(R.id.testFromtoTextView)).setText(item.getFromto());
        ((TextView)convertView.findViewById(R.id.testIsHolildayTextView)).setText(item.getIsHoliday());
        ((TextView)convertView.findViewById(R.id.testTimeTextView)).setText(item.getTime());
        ((TextView)convertView.findViewById(R.id.testLineTextView)).setText(item.getLine());
        ((TextView)convertView.findViewById(R.id.testKindTextView)).setText(item.getKind());
        return(convertView);
    }
}
