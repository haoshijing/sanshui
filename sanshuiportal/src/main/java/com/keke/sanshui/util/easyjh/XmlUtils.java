package com.keke.sanshui.util.easyjh;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class XmlUtils {

    private static String dW = "Problem parsing API response";

    public static String toXml(Map paramMap) {
        StringBuilder localStringBuilder = new StringBuilder();
        Object localObject = new ArrayList(paramMap.keySet());
        Collections.sort((ArrayList) localObject);
        localStringBuilder.append("<xml>");
        Iterator localIterator = ((ArrayList) localObject).iterator();
        while (localIterator.hasNext()) {
            localObject = (String) localIterator.next();
            localStringBuilder.append("<").append((String) localObject)
                    .append(">");
            localStringBuilder.append("<![CDATA[")
                    .append((String) paramMap.get(localObject)).append("]]>");
            localStringBuilder.append("</").append((String) localObject)
                    .append(">\n");
        }
        localStringBuilder.append("</xml>");
        return localStringBuilder.toString();
    }

    public static HashMap parse(String paramString)
            throws Exception {
        HashMap localHashMap = new HashMap();
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        factory.setNamespaceAware(false);
        XmlPullParser localXmlPullParser = factory.newPullParser();
        int i = 0;
        try {
            localXmlPullParser.setInput(new StringReader(paramString));
            String str = null;
            do {
                i = localXmlPullParser.next();
                switch (i) {
                    case 2:
                        if (!localXmlPullParser.getName().equals("root")) {
                            str = localXmlPullParser.getName().trim();
                        }
                        break;
                    case 4:
                        if (str != null) {
                            localHashMap.put(str, localXmlPullParser.getText()
                                    .trim());
                        }
                        break;
                    case 3:
                        str = null;
                }
            } while (i != 1);
        } catch (Exception ec) {
            throw new Exception(dW, ec);
        }
        return localHashMap;
    }
}