package com.foo.dictionary.translations.client.wrappers;

import com.foo.dictionary.translations.DictionaryWord;
import com.foo.dictionary.translations.client.BablaDictionaryClient;
import com.foo.dictionary.translations.client.DictionaryClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class FallbackStubClient implements DictionaryClient {

	private static final Logger log = LoggerFactory.getLogger(FallbackStubClient.class);

	private final DictionaryClient target;
	private final Map<String, List<DictionaryWord>> fallbackTranslations;

	public FallbackStubClient(Map<String, List<DictionaryWord>> translations, DictionaryClient target) {
		this.fallbackTranslations = translations;
		this.target = target;
	}

	@Override
	public DictionaryWord firstTranslationFor(String word) {
		try {
			return target.firstTranslationFor(word);
		} catch (Exception e) {
			log.warn("Problem retrieving word `{}`", word, e);
			if (fallbackTranslations.containsKey(word)) {
				return fallbackTranslations.get(word).get(0);
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
			if (fallbackTranslations.containsKey(word)) {
				log.warn("Returning defaults for word: `{}`", word);
				return fallbackTranslations.get(word);
			} else if ("home".equals(word)) {
				return new BablaDictionaryClient(loadStubHtmlFromDisk(word)).allTranslationsFor(word);
			} else {
				throw new RuntimeException(e);
			}
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
