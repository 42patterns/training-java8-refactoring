package com.foo.dictionary.translations.client.wrappers;

import com.foo.dictionary.translations.DictionaryWord;
import com.foo.dictionary.translations.client.BablaDictionaryClient;
import com.foo.dictionary.translations.client.DictDictionaryClient;
import com.foo.dictionary.translations.client.DictionaryClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;

public class FallbackStubClient implements DictionaryClient {

	private static final Logger log = LoggerFactory.getLogger(FallbackStubClient.class);

	private final DictionaryClient target;

	public FallbackStubClient(DictionaryClient target) {
		this.target = target;
	}

	@Override
	public DictionaryWord firstTranslationFor(String word) {
		try {
			return target.firstTranslationFor(word);
		} catch (Exception e) {
			log.warn("Problem retrieving word `{}`", word, e);
			if ("good morning".equals(word)) {
				DictionaryWord dictionaryWord = new DictionaryWord("good morning", "dzień dobry");
				log.warn("Matching default {}", dictionaryWord);
				return dictionaryWord;
			} else if ("home".equals(word)) {
				DictionaryWord dictionaryWord = new DictionaryWord("home", "dom");
				log.warn("Matching default {}", dictionaryWord);
				return dictionaryWord;
			} else {
				throw new RuntimeException(e);
			}
		}
	}

	@Override
	public List<DictionaryWord> allTranslationsFor(String word) {
		try {
			return target.allTranslationsFor(word);
		} catch (Exception e) {
			log.warn("Problem retrieving word `{}`", word, e);
			return new BablaDictionaryClient(loadStubHtmlFromDisk(word)).allTranslationsFor(word);
		}
	}

	private String loadStubHtmlFromDisk(String word) {
		final String filename = "/" + word + "-translations.html";
		try {
            return getClass().getResource(filename).toURI().toString();
		} catch (Exception e) {
			log.warn("File {} not found. Returning empty list", filename);
			return new String();
		}
	}
}
