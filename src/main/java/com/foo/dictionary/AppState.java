package com.foo.dictionary;

import com.foo.dictionary.translations.DictionaryWord;

import java.util.Collections;
import java.util.List;

public class AppState {
    public final boolean isRunning;
    public final List<DictionaryWord> words;
    public final List<DictionaryWord> defaultWords;

    public AppState(boolean isRunning) {
        this.isRunning = isRunning;
        this.words = Collections.emptyList();
        this.defaultWords = Collections.emptyList();
    }

    public AppState(AppState currentState) {
        this.isRunning = currentState.isRunning;
        this.words = currentState.words;
        this.defaultWords = currentState.defaultWords;
    }

    public AppState(AppState currentState, List<DictionaryWord> words) {
        this.isRunning = currentState.isRunning;
        this.words = words;
        this.defaultWords = currentState.defaultWords;
    }

    public AppState(boolean isRunning, List<DictionaryWord> dictionaryWords, List<DictionaryWord> defaultWords) {
        this.isRunning = isRunning;
        this.words = dictionaryWords;
        this.defaultWords = defaultWords;
    }

    public AppStateBuiler fromCurrent() {
        return new AppStateBuiler(this);
    }

    public static class AppStateBuiler {
        public boolean isRunning;
        public List<DictionaryWord> words;
        public List<DictionaryWord> defaultWords;

        public AppStateBuiler(AppState appState) {
            this.isRunning = appState.isRunning;
            this.words = appState.words;
        }

        public AppStateBuiler withDefaultWords(List<DictionaryWord> words) {
            this.defaultWords = words;
            return this;
        }

        public AppState build() {
            return new AppState(isRunning, words, defaultWords);
        }
    }

}
