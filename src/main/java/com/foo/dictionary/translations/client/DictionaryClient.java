package com.foo.dictionary.translations.client;

import com.foo.dictionary.translations.DictionaryWord;

import java.util.List;

public interface DictionaryClient {

	DictionaryWord firstTranslationFor(String word);
	List<DictionaryWord> allTranslationsFor(String word);

}
