package com.foo.dictionary.translations.client.wrappers;

import com.foo.dictionary.translations.DictionaryWord;
import com.foo.dictionary.translations.client.DictionaryClient;
import org.hamcrest.CoreMatchers;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.junit.Assert.assertThat;

public class FallbackStubClientTest {

    final FallbackStubClient fallbackStubClient = new FallbackStubClient(new DictionaryClient() {
        @Override
        public DictionaryWord firstTranslationFor(String word) {
            throw new RuntimeException("Not implemented");
        }

        @Override
        public List<DictionaryWord> allTranslationsFor(String word) {
            throw new RuntimeException("Not implemented");
        }
    });

    @Test
    public void should_return_empty_list_when_no_file() {
        List<DictionaryWord> emptyList = fallbackStubClient.allTranslationsFor("non-existing-file");
        assertThat(emptyList, empty());
    }

    @Test
    public void should_fallback_to_local_file() {
        List<DictionaryWord> emptyList = fallbackStubClient.allTranslationsFor("home");
        assertThat(emptyList.get(0), CoreMatchers.equalTo(new DictionaryWord("home", "dom")));
    }

}