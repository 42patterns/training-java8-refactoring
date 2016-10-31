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
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DictDictionaryClient implements DictionaryClient {

    private static final Logger log = LoggerFactory.getLogger(DictDictionaryClient.class);
    private final String urlString;

    public DictDictionaryClient() {
        this.urlString = "http://www.dict.pl/dict?word={%%%}&words=&lang=PL";
    }

    public DictDictionaryClient(String urlString) {
        this.urlString = urlString;
    }

    @Override
    public Optional<DictionaryWord> firstTranslationFor(String wordToFind) {
        List<DictionaryWord> foundWords = allTranslationsFor(wordToFind);
        if (foundWords == null || foundWords.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(foundWords.get(0));
    }

    @Override
    //TODO: replace while loop and matching with a single method based on streams
    //tips: - look at `BufferedReader.lines()`
    //      - first convert whole html into a List<String> and convert it
    //        to <DictionaryWord> in the additional step
    public List<DictionaryWord> allTranslationsFor(String wordToFind) {
        List<DictionaryWord> results = new ArrayList<DictionaryWord>();

        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new URL(
                    url(wordToFind)).openStream()));

            String word = moveToNextWord(bufferedReader);
            while (hasNextWord(word)) {
                String englishWord = moveToNextWord(bufferedReader);
                results.add(new DictionaryWord(englishWord, word));
                word = moveToNextWord(bufferedReader);
            }

        } catch (IOException e) {
            log.warn("Couldn't process the stream {}. Empty list", url(wordToFind));
            throw new RuntimeException("No words found");
        }

        return results;
    }

    private String url(String wordToFind) {
        return urlString.replace("{%%%}", wordToFind);
    }

    private String moveToNextWord(BufferedReader bufferedReader) {
        try {

            String line = bufferedReader.readLine();
            Pattern pat = Pattern
                    .compile(".*<a href=\"dict\\?words?=(.*)&lang.*");

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

    private boolean hasNextWord(String line) {
        return (line != null);
    }

    private boolean hasNextLine(String line) {
        return (line != null);
    }

}