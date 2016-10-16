package com.foo.dictionary.commands;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Commands {

    public static boolean isNull(String s) {
        return s == null?true:false;
    }

    public static String trimCommandWord(String commandStr) {
        Pattern p = Pattern.compile("([a-zA-Z]+)\\s?(.*)");
        Matcher matcher = p.matcher(commandStr);
        if (matcher.matches()) {
            return matcher.group(2);
        }
        return new String();
    }
}
