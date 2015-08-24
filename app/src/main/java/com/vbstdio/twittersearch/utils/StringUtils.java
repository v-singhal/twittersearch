package com.vbstdio.twittersearch.utils;

import java.util.regex.Pattern;

/**
 * Created by vaibhav on 23/8/15.
 */
public class StringUtils {

    public static String extractValidString(String text) {
        for(int counter = 0; counter < text.length(); counter++) {
            if(text.length() > 0) {
                int indexOfFirstSpace = text.indexOf(" ");
                if (indexOfFirstSpace == -1 || indexOfFirstSpace > 0) {
                    break;
                }
                else if (indexOfFirstSpace == 0 && text.split(" ").length > 0 && text.split(" ")[1] != null) {
                    //text = text.replaceFirst(" ", "");
                    text = text.replaceFirst(Pattern.quote("  "), "");
                }
                else {
                    text = "";
                    break;
                }
            }
        }

        return text;
    }

    public static boolean isValidString(String testString) {
        if(testString != null && !("").equals(testString) && !("null").equals(testString)){
            return true;
        }
        else {
            return false;
        }
    }
}
