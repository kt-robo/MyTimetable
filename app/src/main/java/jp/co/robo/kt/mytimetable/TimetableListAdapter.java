package jp.co.robo.kt.mytimetable;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


/**
 * 時刻表のArrayAdapter。時刻表のListの各行を表示する。
 * Created by kouta on 15/05/04.
 */
public class TimetableListAdapter extends ArrayAdapter<TrainInformation> {
    private LayoutInflater mLayoutInflater;

    public TimetableListAdapter(Context context) {
        super(context, R.layout.timetable_item);
        mLayoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TrainInformation item = getItem(position);
        int background_color = Constants.Colors.kind2BackgroundColor(item.getKind());
        int text_color = Constants.Colors.line2TextColor(item.getLine());

        // convertViewは使い回しされている可能性があるのでnullのときだけ新しく作る。
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.timetable_item, null);
        }

        TextView timeView = (TextView)convertView.findViewById(R.id.timeTextView);
        timeView.setBackgroundColor(background_color);
        timeView.setTextColor(text_color);
        timeView.setText(item.getTime());

        TextView lineView = (TextView)convertView.findViewById(R.id.lineTextView);
        lineView.setBackgroundColor(background_color);
        lineView.setTextColor(text_color);
        lineView.setText(Constants.Lines.line2String(item.getLine()));

        TextView trainView = (TextView)convertView.findViewById(R.id.trainTextView);
        trainView.setBackgroundColor(background_color);
        trainView.setTextColor(text_color);
        trainView.setText(item.getTrain());

        TextView destView = (TextView)convertView.findViewById(R.id.destTextView);
        destView.setBackgroundColor(background_color);
        destView.setTextColor(text_color);
        destView.setText(item.getDest());

        TextView markView = (TextView)convertView.findViewById(R.id.markTextView);
        markView.setBackgroundColor(background_color);
        markView.setTextColor(text_color);
        markView.setText(Constants.Marks.mark2String(item.getMark()));

        return(convertView);
    }
}
