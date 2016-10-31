package com.foo.dictionary.translations.client;

import com.foo.dictionary.translations.DictionaryWord;
import one.util.streamex.EntryStream;
import one.util.streamex.StreamEx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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
    public Optional<DictionaryWord> firstTranslationFor(String wordToFind) {
        List<DictionaryWord> foundWords = allTranslationsFor(wordToFind);
        return foundWords.stream().findFirst();
    }

    @Override
    public List<DictionaryWord> allTranslationsFor(String wordToFind) {
        List<DictionaryWord> results = new ArrayList<DictionaryWord>();

        Pattern englishWordPattern = Pattern.compile(".*result-left.*/slownik/angielski-polski/.*\">" +
                "<strong>(.*)</strong>" +
                ".*");

        Pattern polishWordPattern = Pattern.compile(".*result-right.*/slownik/polski-angielski/.*\">" +
                "(.*)" +
                "</a>.*");

        try (InputStream io = new URL(url(wordToFind)).openStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(io))) {

            List<String> wholeFile = reader.lines().collect(Collectors.toList());

            return StreamEx.of(wholeFile)
                    .map(englishWordPattern::matcher)
                    .filter(Matcher::find)
                    .map(m -> m.group(m.groupCount()))
                    .zipWith(StreamEx.of(wholeFile)
                            .map(polishWordPattern::matcher)
                            .filter(Matcher::find)
                            .map(m -> m.group(m.groupCount()))
                    )
                    .map(e -> new DictionaryWord(e.getKey(), e.getValue()))
                    .collect(Collectors.toList());

        } catch (IOException e) {
            log.warn("Couldn't process the stream {}. Empty list", url(wordToFind));
            return Collections.emptyList();
        }
    }

    private String url(String wordToFind) {
        return urlString.replace("{%%%}", wordToFind.replace(" ", "-"));
    }

}