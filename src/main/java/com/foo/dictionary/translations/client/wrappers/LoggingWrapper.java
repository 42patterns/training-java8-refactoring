package com.foo.dictionary.translations.client.wrappers;

import com.foo.dictionary.LoggerConfiguration;
import com.foo.dictionary.translations.DictionaryWord;
import com.foo.dictionary.translations.client.DictionaryClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

public class LoggingWrapper implements DictionaryClient {

    private static final Logger log = LoggerFactory.getLogger(LoggingWrapper.class);

    private final DictionaryClient target;

    public LoggingWrapper(DictionaryClient target) {
        LoggerConfiguration.configureLogger();
        this.target = target;
    }

    @Override
    public Optional<DictionaryWord> firstTranslationFor(String word) {
        log.debug("Entering firstTranslationFor({})", word);
        final Optional<DictionaryWord> translation = target.firstTranslationFor(word);
        log.debug("Leaving firstTranslationFor({}): {}", word, translation);
        return translation;
    }

    @Override
    public List<DictionaryWord> allTranslationsFor(String word) {
        long start = System.currentTimeMillis();
        log.debug("Entering allTranslationsFor({})", word);
        final List<DictionaryWord> translations = target.allTranslationsFor(word);
        log.debug("Leaving allTranslationsFor({}) after {}ms: results.size({})", word, (System.currentTimeMillis() - start), translations.size());
        return translations;
    }

}
