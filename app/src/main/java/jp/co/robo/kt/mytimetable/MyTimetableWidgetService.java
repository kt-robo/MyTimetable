package jp.co.robo.kt.mytimetable;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.util.ArrayList;

/**
 * MyTimetableWidgetFactoryを作成するクラス。
 * Created by kouta on 15/06/14.
 */
public class MyTimetableWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsService.RemoteViewsFactory onGetViewFactory(Intent intent) {
        return(new MyTimetableWidgetFactory(this));
    }

    /**
     * AppWidgetのListViewを表示する。
     * Created by kouta on 15/06/08.
     */
    public static class MyTimetableWidgetFactory implements RemoteViewsService.RemoteViewsFactory {
        private Context mContext;
        private ArrayList<TrainInformation> mTrainInformation;

        private static int smFromTo = 0;

        public MyTimetableWidgetFactory(Context context) {
            super();
            mContext = context;
            mTrainInformation = new ArrayList<>();
        }

        @Override
        public void onCreate() {
        }

        @Override
        public void onDestroy() {
        }

        @Override
        public int getCount() {
            return((mTrainInformation == null)? 0:mTrainInformation.size());
        }

        @Override
        public long getItemId(int position) {
            return((long)position);
        }

        @Override
        public void onDataSetChanged() {
            if (smFromTo > 0) {
                TimetableDbHelper helper = TimetableDbHelper.getInstance(mContext);
                int size = new PreferencesAppWidget(mContext).getAppWidgetListSize();
                DateTime dateTime = DateTime.getDisplayDateTime(mContext, smFromTo);
                mTrainInformation = helper.select(dateTime.isHoliday(), smFromTo, dateTime.getTime(), size);
            }
        }

        @Override
        public RemoteViews getViewAt(int position) {
            TrainInformation item = mTrainInformation.get(position);

            int layout = Constants.Colors.kind2Layout(item.getKind());
            int text_color = Constants.Colors.line2TextColor(item.getLine());

            RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(), layout);

            remoteViews.setTextColor(R.id.widgetListTime, text_color);
            remoteViews.setTextViewText(R.id.widgetListTime, item.getTime());

            remoteViews.setTextColor(R.id.widgetListTrain, text_color);
            remoteViews.setTextViewText(R.id.widgetListTrain, item.getTrain());

            remoteViews.setTextColor(R.id.widgetListDest, text_color);
            remoteViews.setTextViewText(R.id.widgetListDest, item.getDest());

            return(remoteViews);
        }

        @Override
        public RemoteViews getLoadingView() {
            return(null);
        }

        @Override
        public int getViewTypeCount() {
            return(Constants.Colors.KIND_COUNT);
        }

        @Override
        public boolean hasStableIds() {
            return(true);
        }

        public static void setFromTo(int fromto) {
            smFromTo = fromto;
        }
    }

}
