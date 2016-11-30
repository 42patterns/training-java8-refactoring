package com.foo.dictionary;

import com.foo.dictionary.translations.DictionaryWord;

import java.util.List;
import java.util.Map;

public interface AppState {
    void stop();

    boolean isRunning();

    void setTranslations(String phrase, List<DictionaryWord> translations);

    void setDefaults(String phrase, DictionaryWord translation);

    Map<String, List<DictionaryWord>> getTranslations();
}
