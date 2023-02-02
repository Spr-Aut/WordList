package com.example.wordlist;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;

/*
* <dict num="219" id="219" name="219">
<key>world</key>
<ps>wɜ:ld</ps>
<pron>http://res.iciba.com/resource/amp3/0/0/7d/79/7d793037a0760186574b0282f2f435e7.mp3</pron>
<ps>wɜrld</ps>
<pron>http://res.iciba.com/resource/amp3/1/0/7d/79/7d793037a0760186574b0282f2f435e7.mp3</pron>
<pos>n.</pos>
<acceptation>世界；地球；领域；尘世； </acceptation>
<sent>
<orig> First world construction, Third world facility, nineth world management. First in the world! </orig>
<trans> 一流的建筑, 三流的设施, 九流的管理, 举世无双! </trans>
</sent>
<sent>
<orig> Spatial world discriminates physical world, cognitive world and linguistic world. </orig>
<trans> 空间世界区分物理空间世界 、 认知空间世界和语言空间世界. </trans>
</sent>
<sent>
<orig> Today's world is an open world, a world full of both opportunities and challenges. </orig>
<trans> 当今的世界是开放的世界, 是充满机遇与挑战的世界. </trans>
</sent>
<sent>
<orig> The world is a unity of the material world and the meaningful world. </orig>
<trans> 世界是物质世界和意义世界的统一. </trans>
</sent>
<sent>
<orig> Bruce Lee has created China World record Association world most movie fan's martial arts world record. </orig>
<trans> 李小龙创造了中国世界纪录协会世界最多影迷的武术家的世界纪录. </trans>
</sent>
</dict>
*
* 世界；地球；领域；尘世；

    First world construction, Third world facility, nineth world management. First in the world!

    一流的建筑, 三流的设施, 九流的管理, 举世无双!


    Spatial world discriminates physical world, cognitive world and linguistic world.

    空间世界区分物理空间世界 、 认知空间世界和语言空间世界.


    Today''s world is an open world, a world full of both opportunities and challenges.

    当今的世界是开放的世界, 是充满机遇与挑战的世界.


    The world is a unity of the material world and the meaningful world.

    世界是物质世界和意义世界的统一.


    Bruce Lee has created China World record Association world most movie fan''s martial arts world record.

    李小龙创造了中国世界纪录协会世界最多影迷的武术家的世界纪录.
    * */

public class XMLParse {
    private static final String TAG = "myTAG";

    public static String parseXmlWithPull(String data,boolean showTrans){
        StringBuilder stringBuilder=new StringBuilder();
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser pullParser=factory.newPullParser();
            pullParser.setInput(new StringReader(data));
            int eventType=pullParser.getEventType();
            String orig="";
            String trans="";
            String desc="";
            while (eventType!=XmlPullParser.END_DOCUMENT){
                switch (eventType){
                    case XmlPullParser.START_TAG: {
                        if (pullParser.getName().equals("orig")) {
                            orig = pullParser.nextText();
                        } else if (pullParser.getName().equals("trans")) {
                            trans = pullParser.nextText();
                        }else if (pullParser.getName().equals("acceptation")){
                            desc=pullParser.nextText();
                            stringBuilder.append(desc);
                        }
                        break;
                    }
                    case XmlPullParser.END_TAG:{
                        if (pullParser.getName().equals("sent")){
                            stringBuilder.append(orig);
                            if (showTrans)
                                stringBuilder.append(trans);
                            stringBuilder.append("\n");
                        }else if(pullParser.getName().equals("acceptation")){
                            stringBuilder.append(desc);
                        }
                    }
                    default:
                        break;
                }

                eventType=pullParser.next();
            }
        }catch (Exception e){
            e.printStackTrace();
            stringBuilder.append(e.getMessage());
        }
        return sqliteEscape(stringBuilder.toString());
    }

    public static String sqliteEscape(String keyWord) {
        keyWord = keyWord.replace("/", "//");
        keyWord = keyWord.replace("'", "''");
        keyWord = keyWord.replace("[", "/[");
        keyWord = keyWord.replace("]", "/]");
        keyWord = keyWord.replace("%", "/%");
        keyWord = keyWord.replace("&", "/&");
        keyWord = keyWord.replace("_", "/_");
        keyWord = keyWord.replace("(", "/(");
        keyWord = keyWord.replace(")", "/)");
        return keyWord;
    }
}