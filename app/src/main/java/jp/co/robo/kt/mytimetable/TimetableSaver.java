package jp.co.robo.kt.mytimetable;

import android.app.Activity;
import android.util.Log;

import org.xml.sax.InputSource;
import org.ccil.cowan.tagsoup.Parser;
import org.xml.sax.SAXException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;


/**
 * 時刻表をダウンロードし、時刻表DBと始発/終電DBに格納する。
 * HTMLパーサーは、オープンソースのtagSoapを使用する。
 * Created by kouta on 15/05/19.
 */
public class TimetableSaver extends SaverWithProgress<String, Integer, Void> {
    private boolean mError;
    private String mMessage;

    public TimetableSaver(Activity activity) {
        super(activity);
    }

    @Override
    @SuppressWarnings({"unchecked"})
    protected Void doInBackground(String... urls) {
        setProgressMessage(Constants.Messages.DOWNLOADING_TIMETABLE);
        setProgressMax(urls.length);
        setProgressProgress(0);
        ArrayList<TrainInformation>[] list = new ArrayList[urls.length];
        int i, j;
        // URLの数分、HTMLを解析する。
        for (i = 0;i < list.length;i++) {
            list[i] = parseHtml(urls[i], Constants.Urls.getLine(i));
            if (mError) {
                // エラーがあったら中断。
                return(null);
            }
            // プログレスバーを進める。
            publishProgress(i + 1);
        }

        // 解析結果から始発/終電を取り出す。
        ArrayList<FirstLastInformation> first_last_list = new ArrayList<>();
        int all_size = 0;
        for (i = 0;i < list.length;i++) {
            int fromto = Constants.Urls.getFromTo(i);
            boolean isHoliday = Constants.Urls.isHoliday(i);
            FirstLastInformation first = new FirstLastInformation(list[i].get(0).getTime(), fromto, isHoliday, true);
            FirstLastInformation last = new FirstLastInformation(list[i].get(list[i].size() - 1).getTime(), fromto, isHoliday, false);
            for (j = 0;j < first_last_list.size();j++) {
                FirstLastInformation dest = first_last_list.get(j);
                if ((dest.getFromTo() == fromto) && (dest.isHoliday() == isHoliday)) {
                    // fromtoとisHolidayが同じものがあったら、始発/終電をそれぞれ比較して、
                    // 始発がより前のもの、終電がより後のものに置き換える。
                    if (dest.isFirst()) {
                        if (first.getTime().compareTo(dest.getTime()) < 0) {
                            first_last_list.remove(dest);
                            first_last_list.add(first);
                        }
                    } else {
                        if (last.getTime().compareTo(dest.getTime()) > 0) {
                            first_last_list.remove(dest);
                            first_last_list.add(last);
                        }
                    }
                    break;
                }
            }
            if (j >= first_last_list.size()) {
                // fromtoとisHolidayが同じものがなかったら、始発と終電を追加する。
                first_last_list.add(first);
                first_last_list.add(last);
            }
            all_size += list[i].size();
        }

        // プログレスバーを０に戻して、メッセージとmaxを変更する。
        publishProgress(0, all_size + first_last_list.size());

        // 時刻表データをDBに書き込む。
        TimetableDbHelper.getInstance(getActivity()).renew(list, this);

        // 始発/終電情報をDBに書き込む。
        FirstLastDbHelper.getInstance(getActivity()).renew(first_last_list, this, all_size);

        return(null);
    }

    @Override
    protected void onPostExecute(Void args) {
        if (mError) {
            MessageDialog dialog = MessageDialog.newInstance(Constants.DialogTitles.ERROR, mMessage);
            dialog.show(getActivity().getFragmentManager());
            getProgressDialog().dismiss();
            // エラーのときは「完了しました」メッセージは出さないし、このActivityを閉じないので、
            // super.onPostExecute()は呼ばない。
        } else {
            super.onPostExecute(args);
        }
    }

    @Override
    public void onProgressUpdate(Integer... args) {
        if (args[0] == 0) {
            // 第1引数が0の場合は、「時刻表のダウンロード」から「DBに保存」に切り替わるとき。
            // そのため、メッセージとmaxも変更する。max値は第2引数となる。
            // ちなみに、doInBackground()の中でメッセージの変更を行おうとすると例外が発生する
            // （UIスレッドではないため）ので、わざわざここ（UIスレッドで動作する）で行う。
            setProgressMessage(Constants.Messages.SAVING_TIMETABLE);
            setProgressMax(args[1]);
        }
        setProgressProgress(args[0]);
    }

    private ArrayList<TrainInformation> parseHtml(String url, int line) {
        InputStream is = null;
        ArrayList<TrainInformation> ret = new ArrayList<>();
        try {
            is = new URL(url).openStream();
            Parser parser = new Parser();
            TimetableHtmlHandler handler = new TimetableHtmlHandler(line);
            parser.setContentHandler(handler);
            parser.parse(new InputSource(new InputStreamReader(is, "UTF-8")));
            ret = handler.getResult();
            if (ret == null) {
                // 解析結果がなかったらエラー。不正なURLの可能性があるため。
                mError = true;
                mMessage = getActivity().getResources().getString(R.string.message_error_no_timetable, url);
            }
        } catch (MalformedURLException e) {
            Log.d(getClass().getName(), "MalformedURLException(e=" + e.toString() + ")");
            Log.d(getClass().getName(), "MalformedURLException(msg=" + e.getMessage() + ")");
            mError = true;
            mMessage = getActivity().getResources().getString(R.string.message_error_url_format, url);
        } catch (UnknownHostException e) {
            Log.d(getClass().getName(), "UnknownHostException(e=" + e.toString() + ")");
            Log.d(getClass().getName(), "UnknownHostException(msg=" + e.getMessage() + ")");
            mError = true;
            mMessage = getActivity().getResources().getString(R.string.message_error_url_connect, url);
        } catch (FileNotFoundException e) {
            Log.d(getClass().getName(), "FileNotFoundException(e=" + e.toString() + ")");
            Log.d(getClass().getName(), "FileNotFoundException(msg=" + e.getMessage() + ")");
            mError = true;
            mMessage = getActivity().getResources().getString(R.string.message_error_url_connect, url);
        } catch (IOException e) {
            mError = true;
            e.printStackTrace();
            mMessage = getActivity().getResources().getString(R.string.message_error_communicate, url, e.toString());
        } catch (SAXException e) {
            mError = true;
            e.printStackTrace();
            mMessage = getActivity().getResources().getString(R.string.message_error_parse_html, url, e.toString());
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (Exception e) {
                    // close()時のエラーはログだけとって無視。
                    e.printStackTrace();
                }
            }
        }
        return(ret);
    }

    @Override
    public String getFinishMessage() {
        return(Constants.Messages.UPDATE_TIMETABLE_COMPLETED);
    }

    @Override
    public String getFinishAction() {
        return(UpdateTimetableActivity.getFinishAction());
    }

    public void setProgress(int progress) {
        publishProgress(progress);
    }

}
