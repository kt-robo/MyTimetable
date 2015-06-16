package jp.co.robo.kt.mytimetable;

import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;

/**
 * 平日/土休タブのリスナー。
 * Created by kouta on 15/05/04.
 */
@SuppressWarnings({"deprecation"})
public class DayTabListener implements ActionBar.TabListener{
    private DayFragment mFragment;
    private boolean mFragmentAdded;
    private String mTabName;

    // 平日/土休のタブが切り替わったときに、切り替え後の子fragment（＝FromToFragment）のpositionを、
    // 切り替え前に選択されていた子fragment（＝FromtoFragment）のpositionと同じにするために、
    // 子fragmentのpositionを憶えておく。
    // 平日/土休のタブでTabListenerのインスタンスが異なるので、staticにしておく必要がある。
    private static int smChildPosition;    // position = 0, 1, 2, ...

    public DayTabListener(Context context, boolean isHoliday, int init_fromto) {
        mTabName = Constants.WeekdayOrHoliday.boolean2String(isHoliday);
        Bundle args = new Bundle();
        args.putBoolean(Constants.ArgumentNames.IS_HOLIDAY, isHoliday);
        mFragment = (DayFragment)Fragment.instantiate(context, DayFragment.class.getName(), args);
        // 子fragment（＝FromToFragment）の初期positionをセットする。
        mFragment.setPage(Constants.FromTo.fromTo2Position(init_fromto));
        mFragmentAdded = false;
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        if (mFragmentAdded) {
            if (mFragment.isDetached()) {
                fragmentTransaction.attach(mFragment);
            }
        } else {
            fragmentTransaction.add(android.R.id.content, mFragment, mTabName);
            mFragmentAdded = true;
        }
        // Dayタブ切り替え後の子fragment（＝FromToFragment）のpositionをセットする。
        mFragment.setPage(smChildPosition);
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        fragmentTransaction.detach(mFragment);
        // Dayタブ切り替え前の子fragment（＝FromToFragment）のpostionを憶える。
        smChildPosition = mFragment.getPage();
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    public String getTabName() {
        return(mTabName);
    }

    public static DayTabListener[] create(Context context, int init_fromto) {
        DayTabListener[] dayTabListeners = new DayTabListener[2];
        for (int i = 0;i < dayTabListeners.length;i++) {
            // iはタブのposition。これを元に平日/土休を判断するには、positionToBoolean()を使う。
            dayTabListeners[i] = new DayTabListener(context, Constants.WeekdayOrHoliday.position2Boolean(i), init_fromto);
        }
        return(dayTabListeners);
    }
}
