package com.foo.dictionary.translations.profanity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;

public class PurgoProfanityCheckClient implements ProfanityCheckClient {

    private static final Logger log = LoggerFactory.getLogger(PurgoProfanityCheckClient.class);
    final String url = "http://www.purgomalum.com/service/containsprofanity?text=";

    @Override
    public boolean isObscenityWord(String word) {

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new URL(url + URLEncoder.encode(word, "UTF-8")).openStream()));
            String check = reader.readLine();
            return Boolean.valueOf(check);
        } catch (IOException e) {
            log.warn("Problem getting profanity check for {}", word, e);
            throw new RuntimeException(e);
        }

    }
}
