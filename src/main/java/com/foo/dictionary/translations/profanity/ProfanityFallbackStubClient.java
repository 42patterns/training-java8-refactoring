package com.foo.dictionary.translations.profanity;

import com.foo.dictionary.translations.DictionaryWord;
import com.foo.dictionary.translations.client.BablaDictionaryClient;
import com.foo.dictionary.translations.client.DictionaryClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ProfanityFallbackStubClient implements ProfanityCheckClient {

	private static final Logger log = LoggerFactory.getLogger(ProfanityFallbackStubClient.class);

	private final ProfanityCheckClient target;

	public ProfanityFallbackStubClient(ProfanityCheckClient target) {
		this.target = target;
	}

	@Override
	public boolean isObscenityWord(String word) {
		try {
			return target.isObscenityWord(word);
		} catch (Exception e) {
			log.warn("Problem w. profanity check for '{}'", word, e);
			if ("shit".equals(word)) {
				return true;
			} else {
				return false;
			}
		}
	}
}
