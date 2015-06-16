package jp.co.robo.kt.mytimetable;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * FromToタブの中身。時刻表本体を表示するListFragment。
 * Created by kouta on 15/05/04.
 */
public class FromToFragment extends ListFragment {

    public static FromToFragment newInstance(boolean isHoliday, int fromto) {
        FromToFragment fragment = new FromToFragment();
        Bundle args = new Bundle();
        args.putBoolean(Constants.ArgumentNames.IS_HOLIDAY, isHoliday);
        args.putInt(Constants.ArgumentNames.FROMTO, fromto);
        fragment.setArguments(args);
        return(fragment);
    }

    public FromToFragment() {
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        return(inflater.inflate(R.layout.fragment_fromto, viewGroup, false));
    }

    @Override
    public void onStart() {
        // Timetableが更新されてMyTimetableActivityに戻ってきた時、
        // 時刻表を再読み込む必要があるため、onCreateView()ではなくここで表示を行う。
        // onResume()だとタブ切り替えで毎回通ってしまうので、onStart()がベスト。
        Bundle args = getArguments();
        boolean isHoliday = args.getBoolean(Constants.ArgumentNames.IS_HOLIDAY);
        int fromto = args.getInt(Constants.ArgumentNames.FROMTO);
        setListAdapter(((MyTimetableActivity)getActivity()).getListAdapter(isHoliday, fromto));
        super.onStart();
    }
}
