package jp.co.robo.kt.mytimetable;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.widget.TabHost;
import android.widget.TextView;

/**
 * FromToFragmentを表示するためのViewPagerのAdapter。
 * Created by kouta on 15/05/04.
 */
public class FromToPagerAdapter extends FragmentPagerAdapter {
    private FromToFragment[] mFragments;

    public FromToPagerAdapter(FragmentManager fragmentManager, boolean isHoliday) {
        super(fragmentManager);
        mFragments = new FromToFragment[Constants.FromTo.FROMTO.length];
        for (int i = 0;i < mFragments.length;i++) {
            mFragments[i] = FromToFragment.newInstance(isHoliday, Constants.FromTo.position2FromTo(i));
        }
    }

    @Override
    public Fragment getItem(int position) {
        return(mFragments[position]);
    }

    @Override
    public int getCount() {
        return(mFragments.length);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return(Constants.FromTo.position2String(position));
    }

    public void createTabs(LayoutInflater inflater, FragmentTabHost tabHost, boolean isHoliday) {
        for (int i = 0;i < Constants.FromTo.FROMTO.length;i++) {
            Bundle args = new Bundle();
            args.putBoolean(Constants.ArgumentNames.IS_HOLIDAY, isHoliday);
            args.putInt(Constants.ArgumentNames.FROMTO, Constants.FromTo.position2FromTo(i));
            TextView textView = (TextView)inflater.inflate(R.layout.fromto_tab_title, null);
            String tabName = Constants.FromTo.position2String(i);
            textView.setText(tabName);
            TabHost.TabSpec tabSpec = tabHost.newTabSpec(tabName);
            tabSpec.setIndicator(textView);
            tabHost.addTab(tabSpec, FromToFragment.class, args);
        }
    }
}
