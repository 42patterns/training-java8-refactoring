package com.foo.dictionary;

import com.foo.dictionary.translations.DictionaryWord;
import com.foo.dictionary.translations.client.DictionaryClient;
import com.foo.dictionary.translations.profanity.ProfanityCheckClient;

import java.util.List;
import java.util.Map;

public interface AppState {
    void stop();

    boolean isRunning();

    void setTranslations(String phrase, List<DictionaryWord> translations);

    void setTranslation(String phrase, DictionaryWord translation);

    Map<String, List<DictionaryWord>> getTranslations();

    ClientsFactory clients();
    interface ClientsFactory {
        DictionaryClient getDictDictionary();
        DictionaryClient getBablaDictionary();
        ProfanityCheckClient getProfanityClient();
    }
}
