package jp.co.robo.kt.mytimetable;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TabWidget;

/**
 * 平日/土休のタブ（ActionBarTab）の中身。
 * 中にはFromToを表すタブ（FragmentTabHost）とViewPagerを持つ。
 * Created by kouta on 15/05/04.
 */
public class DayFragment extends Fragment {
    private FromToPagerAdapter mPagerAdapter;
    private ViewPager mViewPager;
    private FragmentTabHost mTabHost;
    private int mPositionForGet;        // position = 0, 1, 2, ...
    private int mPositionForSet;        // position = 0, 1, 2, ...

    public DayFragment() {
        super();
        mPositionForGet = 0;
        mPositionForSet = 0;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_day, viewGroup, false);

        boolean isHoliday = getArguments().getBoolean(Constants.ArgumentNames.IS_HOLIDAY);

        if (mPagerAdapter == null) {
            mPagerAdapter = new FromToPagerAdapter(getChildFragmentManager(), isHoliday);
        }

        mViewPager = (ViewPager)rootView.findViewById(R.id.fromtoViewPager);
        mViewPager.setAdapter(mPagerAdapter);
        // 下記はページのキャッシュ（表示されていないときにとなりの何ページを読み込むか）を指定する
        // ものだが、'0'を指定しても1ページとなりが読み込まれてしまう。原因は不明。
        mViewPager.setOffscreenPageLimit(0);

        mTabHost = (FragmentTabHost)rootView.findViewById(android.R.id.tabhost);
        mTabHost.setup(getActivity(), getChildFragmentManager(), android.R.id.content);
        TabWidget tabWidget = (TabWidget)rootView.findViewById(android.R.id.tabs);
        tabWidget.setStripEnabled(false);
        tabWidget.setShowDividers(LinearLayout.SHOW_DIVIDER_NONE);  // タブの区切り線を消す。

        mPagerAdapter.createTabs(inflater, mTabHost, isHoliday);

        mTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                // タブが切り替わった時にViewPagerを切り替える。
                int position = Constants.FromTo.string2Position(tabId);
                mViewPager.setCurrentItem(position);
                // DayTabが切り替わったときに、同じFromToを表示するために、子のタブpositionを覚えておく。
                mPositionForGet = position;
                Log.d(getClass().getName(), "onTabChanged(" + tabId + "):position=" + position);
            }
        });

        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                // ViewPagerが切り替わった時にタブを切り替える。
                mTabHost.setCurrentTab(position);
                // DayTabが切り替わったときに、同じFromToを表示するために、子のタブpositionを覚えておく。
                mPositionForGet = position;
                Log.d(getClass().getName(), "onTabChanged(" + position + ")");
            }
        });

        return(rootView);
    }

    @Override
    public void onResume() {
        // DayTabが切り替わった時に呼ばれるので、表示するFromToタブのpositionをセットする。
        // mPositionがForSetとForGetに分かれていることに注意。こうしないとうまく動作しない。
        // ForGetはDayTabListenerに通知するためのもので、ForSetはDayTabListenerから設定されるためのもの。
        mViewPager.setCurrentItem(mPositionForSet);
        mTabHost.onTabChanged(mPagerAdapter.getPageTitle(mPositionForSet).toString());
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        mTabHost = null;
        mViewPager = null;
        mPagerAdapter = null;
        super.onDestroyView();
    }

    public int getPage() {
        return(mPositionForGet);
    }

    public void setPage(int page) {
        mPositionForSet = page;
    }
}
