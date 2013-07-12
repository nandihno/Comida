package org.nando.comida.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by deleonf on 28/05/13.
 */
public class  HelperUtils {

    public static String qryStringBuilder(String... qryStrings) throws UnsupportedEncodingException {

        StringBuffer sbuff = new StringBuffer();
        for(int i =0; i < qryStrings.length; i++) {
            if(i == 0) {
                sbuff.append("?"+encodeQryString(qryStrings[i]));
            }
            else {
                sbuff.append("&"+encodeQryString(qryStrings[i]));
            }
        }
        return sbuff.toString();
    }

    public static String encodeQryString(String qryString) throws UnsupportedEncodingException {
        String [] str = qryString.split("=");
        String encoded = URLEncoder.encode(str[1], "UTF-8");
        return str[0] + "="+encoded;
    }



}
