package com.foo.dictionary.translations.client.wrappers;

import com.foo.dictionary.translations.DictionaryWord;
import com.foo.dictionary.translations.client.DictionaryClient;

import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class ArtificialSleepWrapper implements DictionaryClient {

	private static final Random RANDOM = new Random();

	private final DictionaryClient target;

	public ArtificialSleepWrapper(DictionaryClient target) {
		this.target = target;
	}

	@Override
	public DictionaryWord firstTranslationFor(String word) {
		final long start = System.currentTimeMillis();
		final DictionaryWord result = target.firstTranslationFor(word);
		artificialSleep(1000 - (System.currentTimeMillis() - start));
		return result;
	}

	@Override
	public List<DictionaryWord> allTranslationsFor(String word) {
		final long start = System.currentTimeMillis();
		final List<DictionaryWord> result = target.allTranslationsFor(word);
		artificialSleep(1000 - (System.currentTimeMillis() - start));
		return result;
	}

	protected static void artificialSleep(long expected) {
		try {
			TimeUnit.MILLISECONDS.sleep((long) (expected + RANDOM.nextGaussian() * expected / 2));
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

}
