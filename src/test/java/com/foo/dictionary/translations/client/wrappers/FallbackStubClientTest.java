package com.foo.dictionary.translations.client.wrappers;

import com.foo.dictionary.translations.DictionaryWord;
import com.foo.dictionary.translations.client.DictionaryClient;
import org.hamcrest.CoreMatchers;
import org.junit.Test;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertThat;

public class FallbackStubClientTest {

    final Map<String, List<DictionaryWord>> fallback = Collections.emptyMap();

    final FallbackStubClient fallbackStubClient = new FallbackStubClient(fallback, new DictionaryClient() {
        @Override
        public DictionaryWord firstTranslationFor(String word) {
            throw new RuntimeException("Not implemented");
        }

        @Override
        public List<DictionaryWord> allTranslationsFor(String word) {
            throw new RuntimeException("Not implemented");
        }
    });

    @Test(expected = RuntimeException.class)
    public void should_return_empty_list_when_no_file() {
        List<DictionaryWord> emptyList = fallbackStubClient.allTranslationsFor("non-existing-file");
    }

    @Test
    public void should_fallback_to_local_file() {
        List<DictionaryWord> emptyList = fallbackStubClient.allTranslationsFor("home");
        assertThat(emptyList.get(0), CoreMatchers.equalTo(new DictionaryWord("home", "dom")));
    }

}