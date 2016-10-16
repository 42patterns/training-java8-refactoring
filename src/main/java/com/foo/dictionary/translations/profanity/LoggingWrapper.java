package com.foo.dictionary.translations.profanity;

import com.foo.dictionary.LoggerConfiguration;
import com.foo.dictionary.translations.DictionaryWord;
import com.foo.dictionary.translations.client.DictionaryClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

public class LoggingWrapper implements ProfanityCheckClient {

    private static final Logger log = LoggerFactory.getLogger(LoggingWrapper.class);

    private final ProfanityCheckClient target;

    public LoggingWrapper(ProfanityCheckClient target) {
        LoggerConfiguration.configureLogger();
        this.target = target;
    }

    @Override
    public boolean isObscenityWord(String word) {
        log.debug("Entering profanityCheck({})", word);
        final boolean isObscene = target.isObscenityWord(word);
        log.debug("Leaving profanityCheck({}): obscene={}", word, isObscene);
        return isObscene ;

    }
}
