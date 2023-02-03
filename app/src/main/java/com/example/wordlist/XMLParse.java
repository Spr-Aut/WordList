package com.example.wordlist;

import com.example.wordlist.entity.WordInfo;
import com.example.wordlist.util.MyTools;

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

    public static WordInfo parseXmlWithPull(String data,boolean showTrans){
        WordInfo wordInfo=new WordInfo();

        String name="";//单词名
        StringBuilder desc=new StringBuilder();//释义
        StringBuilder sentence=new StringBuilder();//例句
        String symbol_uk="";//英式音标
        String sound_uk="";//英式读音
        String symbol_us="";//美式音标
        String sound_us="";//美式读音
        boolean symbol_flag=false;//存了一个音标后，置true
        boolean sound_flag=false;//存了一个读音后，置true
        String orig="";
        String trans="";

        long time= MyTools.getCurrentTimeMillis();

        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser pullParser=factory.newPullParser();
            pullParser.setInput(new StringReader(data));
            int eventType=pullParser.getEventType();



            while (eventType!=XmlPullParser.END_DOCUMENT){
                switch (eventType){
                    case XmlPullParser.START_TAG: {
                        String str=pullParser.getName();
                        if (str.equals("key")){//单词名
                            name=pullParser.nextText();
                        }else if (str.equals("ps")){//音标
                            if (symbol_flag){
                                symbol_us=pullParser.nextText();
                            }else {
                                symbol_uk=pullParser.nextText();
                                symbol_flag=true;
                            }
                        }else if (str.equals("pron")){//读音
                            if (sound_flag){
                                sound_us=pullParser.nextText();
                            }else {
                                sound_uk=pullParser.nextText();
                                sound_flag=true;
                            }
                        }else if (str.equals("pos")||str.equals("acceptation")){
                            desc.append(pullParser.nextText());
                        }else if (str.equals("orig")){
                            orig = pullParser.nextText();
                        }else if (str.equals("trans")){
                            trans = pullParser.nextText();
                        }

                        /*if (pullParser.getName().equals("orig")) {
                            orig = pullParser.nextText();
                        } else if (pullParser.getName().equals("trans")) {
                            trans = pullParser.nextText();
                        }else if (pullParser.getName().equals("acceptation")){
                            desc=pullParser.nextText();
                            stringBuilder.append(desc);
                        }*/
                        break;
                    }
                    case XmlPullParser.END_TAG:{
                        if (pullParser.getName().equals("sent")){
                            sentence.append(orig);
                            //if (showTrans)
                            sentence.append(trans);
                            sentence.append("\n");
                        }
                    }
                    default:
                        break;
                }

                eventType=pullParser.next();
            }
        }catch (Exception e){
            e.printStackTrace();
            sentence.append(e.getMessage());
        }

        wordInfo.setName(name);
        wordInfo.setDesc(desc.toString());
        wordInfo.setSymbol_uk(MyTools.proSymbol(symbol_uk));
        //https://res.iciba.com/resource/amp3/oxford/0/6f/0a/6f0a0924a725e59aa0104109317cfa09.mp3   sound
        wordInfo.setSound_uk(MyTools.proURL(sound_uk));
        wordInfo.setSymbol_us(MyTools.proSymbol(symbol_us));
        wordInfo.setSound_us(MyTools.proURL(sound_us));
        wordInfo.setSentence(sentence.toString());
        //剩下的都是默认值

        //return sqliteEscape(sound_uk);
        time= MyTools.getCurrentTimeMillis()-time;
        return wordInfo;
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