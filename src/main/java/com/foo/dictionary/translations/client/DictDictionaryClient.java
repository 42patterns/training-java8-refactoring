package com.foo.dictionary.translations.client;

import com.foo.dictionary.translations.DictionaryWord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DictDictionaryClient implements DictionaryClient {

    private static final Logger log = LoggerFactory.getLogger(DictDictionaryClient.class);
    private final String urlString;

    public DictDictionaryClient() {
        this.urlString = "http://www.dict.pl/dict?word={%%%}&words=&lang=PL";
    }

    public DictDictionaryClient(String urlString) {
        this.urlString = urlString;
    }

    //tag::pre-optional-exception[]
    @Deprecated
    public DictionaryWord firstTranslationFor_Java7(String wordToFind) throws RuntimeException {
        List<DictionaryWord> foundWords = allTranslationsFor(wordToFind);
        if (foundWords != null && !foundWords.isEmpty()) {
            return foundWords.get(0);
        }

        throw new RuntimeException("No words found");
        //return null; //<1>
    }
    //end::pre-optional-exception[]

    @Override
    //tag::optional[]
    public Optional<DictionaryWord> firstTranslationFor(String wordToFind) {
        List<DictionaryWord> foundWords = allTranslationsFor(wordToFind);
        return foundWords.stream().findFirst();
    }
    //end::optional[]

    @Override
    //TODO: replace while loop and matching with a single method based on streams
    //tips: - look at `BufferedReader.lines()`
    //      - first convert whole html into a List<String> and convert it
    //        to <DictionaryWord> in the additional step
    public List<DictionaryWord> allTranslationsFor(String wordToFind) {
        try (InputStream io = new URL(url(wordToFind)).openStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(io))) {

            List<String> collect = reader.lines()
                .map(DictDictionaryClient::match) ////JDK9 will has Optional.stream() so we would be able to do flatMap here
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());

            return IntStream.range(0, collect.size())
                    .filter(i -> i%2==1)
                    .mapToObj(i -> new DictionaryWord(collect.get(i),collect.get(i-1)))
                    .collect(Collectors.toList());

        } catch (IOException e) {
            log.warn("Couldn't process the stream {}. Empty list", url(wordToFind));
            throw new RuntimeException("No words found");
        }
    }

    private static Optional<String> match(String s) {
        Pattern pattern = Pattern
                .compile(".*<a href=\"dict\\?words?=(.*)&lang.*");

        Matcher m = pattern.matcher(s);
        if (m.find()) {
            return Optional.of(m.group(m.groupCount()));
        } else {
            return Optional.empty();
        }
    }

    private String url(String wordToFind) {
        return urlString.replace("{%%%}", wordToFind);
    }
}