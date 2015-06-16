package jp.co.robo.kt.mytimetable;

import android.text.TextUtils;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * SAXベースの時刻表解析クラス。
 *
 * 次の形式を解析する。
 * <ul class="sectionList" ...>
 * ...
 * <li>
 * ...
 * <span class="time" style="color:red;">10:48</span>   ← 時刻（hh:mm）
 * ...
 * <span class="train" style="color:red;">きぬがわ93号 鬼怒川温泉行</span> ← "電車"空白"行き先"
 * または
 * <span class="train2" style="color:red;">         ← "電車"空白"行き先"が２つある
 *     <span style="color:red;">踊り子105号 伊豆急下田行</span>   ← "電車"空白"行き先"
 *     <span style="color:red;">踊り子105号 修善寺行</span>      ← "電車"空白"行き先" ← ２つ目の"電車"は無視
 * </span>
 * ...
 * <span class="mark" style="color:red;">◆</span>
 * ...
 * </li>
 * ...
 * </ul>
 *
 * 「style="color:XXX;"」のカラーは、black=普通、red=急行、blue=快速、green=ライナー、brown=特別快速。
 * カラーは複数箇所で記載されているが、"train"のときの値を採用する。（今のところすべて同じカラーのはず）
 *
 * "mark"で記載されるマークは、「マークは'◆'=運転日注意、'■'=時刻変更、'▲'=運転区間変更、'●'=当駅始発」とする。
 *
 * 解析するカラーとマークは、それぞれConstants.Colors、Constants.Marksクラスで定義してある。
 *
 * 最初のレコードより後で、かつ"00:00"以上のものは、"hh"に24を足す（"24:00", "25:00",...とする）。
 *
 * Created by kouta on 15/05/20.
 */
public class TimetableHtmlHandler implements ContentHandler {
    private ArrayList<TrainInformation> mTrainInformationList;

    private ParseResult current;
    private boolean ul_flag;
    private boolean li_flag;
    private boolean time_flag;
    private boolean train_flag;
    private boolean train2_flag;
    private boolean mark_flag;
    private String color;
    private int span_count;
    private int mLine;
    private String mFirstTime;

    public TimetableHtmlHandler(int line) {
        mTrainInformationList = new ArrayList<>();
        ul_flag = false;
        li_flag = false;
        time_flag = false;
        train_flag = false;
        train2_flag = false;
        mark_flag = false;
        color = null;
        span_count = 0;
        current = null;
        mLine = line;
        mFirstTime = null;
    }

    @Override
    public void setDocumentLocator(Locator locator) {
    }

    @Override
    public void startDocument() throws SAXException {
    }

    @Override
    public void endDocument() throws SAXException {
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        String attr_class;
        if (!ul_flag) {
            if (qName.equalsIgnoreCase("ul")) {
                attr_class = attributes.getValue("class");
                if ((!TextUtils.isEmpty(attr_class)) && (attr_class.equalsIgnoreCase("sectionList"))) {
                    ul_flag = true;
                }
            }
        } else if (!li_flag) {
            if (qName.equalsIgnoreCase("li")) {
                current = new ParseResult();
                li_flag = true;
            }
        } else if (qName.equalsIgnoreCase("span")) {
            span_count ++;
            attr_class = attributes.getValue("class");
            if (train2_flag) {
                if ((!TextUtils.isEmpty(attr_class)) && (span_count > 1)) {
                    color = attributes.getValue("style");
                }
            } else if (!TextUtils.isEmpty(attr_class)) {
                if (attr_class.equalsIgnoreCase("time")) {
                    time_flag = true;
                } else if (attr_class.equalsIgnoreCase("train")) {
                    train_flag = true;
                    color = attributes.getValue("style");
                } else if (attr_class.equalsIgnoreCase("train2")) {
                    train2_flag = true;
                } else if (attr_class.equalsIgnoreCase("mark")) {
                    mark_flag = true;
                }
            }
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (ul_flag && qName.equalsIgnoreCase("ul")) {
            ul_flag = false;
        } else if (li_flag && qName.equalsIgnoreCase("li")) {
            if ((current != null) && (current.isInitialized())) {
                mTrainInformationList.add(new TrainInformation(
                        current.getTime(),
                        mLine,
                        current.getKind(),
                        current.getTrain(),
                        current.getDest(),
                        current.getMark()));
                current = null;
            }
            li_flag = false;
        } else if (qName.equalsIgnoreCase("span")) {
            if (span_count > 0) {
                span_count --;
            }
            if (span_count == 0) {
                if (time_flag) {
                    time_flag = false;
                } else if (train_flag) {
                    train_flag = false;
                } else if (train2_flag) {
                    train2_flag = false;
                } else if (mark_flag) {
                    mark_flag = false;
                }
            }
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        String str = new String(ch, start, length);
        int i;
        for (i = 0;i < length;i++) {
            if (!Character.isSpaceChar(ch[start + i]) && !Character.isWhitespace(ch[start + i])) {
                break;
            }
        }
        if ((i < length) && (current != null)) {
            if (time_flag) {
                if (mFirstTime == null) {
                    mFirstTime = str;
                } else if (str.compareTo(mFirstTime) < 0) {
                    char[] time_chars = str.toCharArray();
                    int hour = ((time_chars[0] - '0') * 10) + (time_chars[1] - '0');
                    hour += 24;
                    time_chars[0] = (char)((hour / 10) + '0');
                    time_chars[1] = (char)((hour % 10) + '0');
                    str = new String(time_chars);
                }
                current.setTime(str);
            } else if (train_flag || (train2_flag && (span_count > 0))) {
                StringTokenizer strtok = new StringTokenizer(str);
                current.setTrain(strtok.nextToken());
                current.setDest(strtok.nextToken());
                current.setKind(color);
            } else if (mark_flag) {
                current.setMark(str);
            }
        }
    }

    @Override
    public void startPrefixMapping(String prefix, String uri) {
    }

    @Override
    public void endPrefixMapping(String prefix) {
    }

    @Override
    public void skippedEntity(String name) {
    }

    @Override
    public void processingInstruction(String target, String data) {
    }

    @Override
    public void ignorableWhitespace(char[] ch, int start, int length) {
    }

    public ArrayList<TrainInformation> getResult() {
        return(mTrainInformationList);
    }


    /**
     * 解析結果を保持するクラス。
     */
    private class ParseResult {
        private String mTime;
        private int mKind;
        private String mTrain;
        private String mDest;
        private int mMark;

        public ParseResult() {
            mTime = null;
            mKind = 0;
            mTrain = null;
            mDest = null;
            mMark = 0;
        }

        public void setTime(String time) {
            mTime = time;
        }

        public void setKind(String color) {
            mKind = Constants.Colors.string2Kind(color);
        }

        public void setTrain(String train) {
            mTrain = train;
        }

        public void setDest(String dest) {
            if (mDest == null) {
                mDest = dest;
            } else {
                mDest += Constants.CommonStrings.LEFT_BRACKET + dest + Constants.CommonStrings.RIGHT_BRACKET;
            }
        }

        public void setMark(String mark) {
            mMark = Constants.Marks.string2Mark(mark);
        }

        public String getTime() {
            return(mTime);
        }

        public int getKind() {
            return(mKind);
        }

        public String getTrain() {
            return(mTrain);
        }

        public String getDest() {
            return(mDest);
        }

        public int getMark() {
            return(mMark);
        }

        public boolean isInitialized() {
            return((mTime != null) && (mTrain != null) && (mDest != null));
        }
    }
}
