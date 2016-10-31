package com.foo.dictionary.translations.client;

import com.foo.dictionary.translations.DictionaryWord;

import java.util.List;
import java.util.Optional;

public interface DictionaryClient {

	//TODO: replace with Optional<DictionaryWord> as the value might not be present
	//and RuntimeException is not the most elegant thing
	Optional<DictionaryWord> firstTranslationFor(String word);
	List<DictionaryWord> allTranslationsFor(String word);

}
