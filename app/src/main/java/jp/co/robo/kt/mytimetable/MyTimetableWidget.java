package jp.co.robo.kt.mytimetable;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.util.ArrayMap;
import android.widget.RemoteViews;


/**
 * AppWidgetのメイン
 * Implementation of App Widget functionality.
 */
public class MyTimetableWidget extends AppWidgetProvider {

    private static final String UP_ACTION = "jp.co.robo.kt.mytimetable.UP_BUTTON_PUSHED";
    private static final String DOWN_ACTION = "jp.co.robo.kt.mytimetable.DOWN_BUTTON_PUSHED";
    private static final String MIDDLE_ACTION = "jp.co.robo.kt.mytimetable.MIDDLE_BUTTON_PUSHED";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId, StateHolder.setState(appWidgetId, ""));
        }
    }

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
    }

    @Override
    public void onReceive(@NonNull Context context, @NonNull Intent intent) {
        String action = intent.getAction();
        if (UP_ACTION.equals(action) || DOWN_ACTION.equals(action) || MIDDLE_ACTION.equals(action)) {
            int appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
            StateHolder.State state = StateHolder.setState(appWidgetId, action);
            updateAppWidget(context, AppWidgetManager.getInstance(context), appWidgetId, state);
        } else {
            super.onReceive(context, intent);
        }
    }

    public void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId, StateHolder.State state) {
        final RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_main);

        Constants.initForAppWidget(context);

        int upButtonImageId;
        int downButtonImageId;
        if (state.mIsUp) {
            upButtonImageId = R.mipmap.button_up_on;
            downButtonImageId = R.mipmap.button_down_off;
        } else {
            upButtonImageId = R.mipmap.button_up_off;
            downButtonImageId = R.mipmap.button_down_on;
        }
        int fromto = state.getFromTo();

        Intent upIntent = new Intent(UP_ACTION);
        upIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        PendingIntent upPendingIntent = PendingIntent.getBroadcast(context, appWidgetId, upIntent, 0);
        remoteViews.setOnClickPendingIntent(R.id.imageUpButton, upPendingIntent);
        remoteViews.setImageViewResource(R.id.imageUpButton, upButtonImageId);

        Intent downIntent = new Intent(DOWN_ACTION);
        downIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        PendingIntent downPendingIntent = PendingIntent.getBroadcast(context, appWidgetId, downIntent, 0);
        remoteViews.setOnClickPendingIntent(R.id.imageDownButton, downPendingIntent);
        remoteViews.setImageViewResource(R.id.imageDownButton, downButtonImageId);

        Intent middleIntent = new Intent(MIDDLE_ACTION);
        middleIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        PendingIntent middlePendingIntent = PendingIntent.getBroadcast(context, appWidgetId, middleIntent, 0);
        remoteViews.setOnClickPendingIntent(R.id.imageMiddleButton, middlePendingIntent);
        int middleButtonImageId = (state.mIsMiddleOn)? R.mipmap.button_middle_on:R.mipmap.button_middle_off;
        remoteViews.setImageViewResource(R.id.imageMiddleButton, middleButtonImageId);

        Intent appIntent = new Intent(context, MyTimetableActivity.class);
        PendingIntent appPendingIntent = PendingIntent.getActivity(context, appWidgetId, appIntent, 0);
        remoteViews.setOnClickPendingIntent(R.id.imageAppButton, appPendingIntent);

        remoteViews.setRemoteAdapter(R.id.widgetListView, new Intent(context, MyTimetableWidgetService.class));

        remoteViews.setTextViewText(R.id.widgetFromToTextView, Constants.FromTo.fromTo2String(fromto));
        boolean isHoliday = DateTime.getDisplayDateTime(context, fromto).isHoliday();
        remoteViews.setTextViewText(R.id.widgetDayTextView, Constants.WeekdayOrHoliday.boolean2String(isHoliday));

        appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
        MyTimetableWidgetService.MyTimetableWidgetFactory.setFromTo(fromto);
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.widgetListView);
    }

    private static class StateHolder {
        private static ArrayMap<Integer, State> mStates = new ArrayMap<>();

        public static State setState(int appWidgetId, String action) {
            State state = getState(appWidgetId);
            if (state == null) {
                state = new State();
            }
            switch (action) {
                case UP_ACTION:
                    state.mIsUp = true;
                    break;
                case DOWN_ACTION:
                    state.mIsUp = false;
                    break;
                case MIDDLE_ACTION:
                    state.mIsMiddleOn = !state.mIsMiddleOn;
                    break;
            }
            mStates.put(appWidgetId, state);
            return(state);
        }

        public static State getState(int appWidgetId) {
            return(mStates.get(appWidgetId));
        }

        private static class State {
            public boolean mIsUp;
            public boolean mIsMiddleOn;

            public State() {
                mIsUp = true;
                mIsMiddleOn = false;
            }

            public int getFromTo() {
                if (mIsMiddleOn) {
                    return((mIsUp)? Constants.FromTo.FROMTO_T2Y:Constants.FromTo.FROMTO_T2O);
                } else {
                    return((mIsUp)? Constants.FromTo.FROMTO_O2Y:Constants.FromTo.FROMTO_Y2O);
                }
            }
        }
    }

}

