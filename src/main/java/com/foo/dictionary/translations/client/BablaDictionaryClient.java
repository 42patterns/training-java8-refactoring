package com.foo.dictionary.translations.client;

import com.foo.dictionary.translations.DictionaryWord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BablaDictionaryClient implements DictionaryClient {

    private static final Logger log = LoggerFactory.getLogger(BablaDictionaryClient.class);
    private final String urlString;

    public BablaDictionaryClient() {
        this.urlString = "http://pl.bab.la/slownik/angielski-polski/{%%%}";
    }

    public BablaDictionaryClient(String urlString) {
        this.urlString = urlString;
    }

    @Override
    public DictionaryWord firstTranslationFor(String wordToFind) {
        List<DictionaryWord> foundWords = allTranslationsFor(wordToFind);
        if (foundWords == null || foundWords.isEmpty()) {
            throw new RuntimeException("No translations found for word " +  wordToFind);
        }

        return foundWords.get(0);
    }

    @Override
    public List<DictionaryWord> allTranslationsFor(String wordToFind) {
        List<DictionaryWord> results = new ArrayList<DictionaryWord>();

        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new URL(
                    url(wordToFind)).openStream()));

            String word = moveToEnglishWord(bufferedReader);
            while (hasNextWord(word)) {
                String polishWord = moveToPolishWord(bufferedReader);
                results.add(new DictionaryWord(word, polishWord));
                word = moveToEnglishWord(bufferedReader);
            }
        } catch (IOException e) {
            log.warn("Couldn't process the stream {}. Empty list", url(wordToFind));
            throw new RuntimeException("No words found");
        }

        return results;
    }

    private String moveToPolishWord(BufferedReader bufferedReader) {
        Pattern pat = Pattern.compile(".*/slownik/polski-angielski/.*\">" +
                "(.*)" +
                "</a>.*");
        return moveToNextWord(bufferedReader, pat);
    }

    private String moveToEnglishWord(BufferedReader bufferedReader) {
        Pattern pat = Pattern.compile(".*/slownik/angielski-polski/.*\">" +
                "<strong>(.*)</strong>" +
                ".*");
        return moveToNextWord(bufferedReader, pat);
    }


    private String moveToNextWord(BufferedReader bufferedReader, Pattern pat) {
        try {

            String line = bufferedReader.readLine();

            while (hasNextLine(line)) {
                Matcher matcher = pat.matcher(line);
                if (matcher.find()) {
                    return matcher.group(matcher.groupCount());
                } else {
                    line = bufferedReader.readLine();
                }
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    private String url(String wordToFind) {
        return urlString.replace("{%%%}", wordToFind.replace(" ", "-"));
    }

    private boolean hasNextWord(String line) {
        return (line != null);
    }

    private boolean hasNextLine(String line) {
        return (line != null);
    }

}