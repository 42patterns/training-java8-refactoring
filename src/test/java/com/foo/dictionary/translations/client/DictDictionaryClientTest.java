package com.foo.dictionary.translations.client;

import com.foo.dictionary.translations.DictionaryWord;
import org.hamcrest.CoreMatchers;
import org.hamcrest.collection.IsCollectionWithSize;
import org.hamcrest.core.IsCollectionContaining;
import org.hamcrest.object.IsCompatibleType;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.object.IsCompatibleType.typeCompatibleWith;
import static org.junit.Assert.*;

public class DictDictionaryClientTest {

    DictDictionaryClient client;

    @Before
    public void setupUrl() {
        String baseName = getClass().getResource("/").toExternalForm();
        String url = baseName + "dict-{%%%}.html";
        client = new DictDictionaryClient(url);
    }

    @Test
    public void should_return_home_for_first_translation() throws Exception {
        assertThat(client.firstTranslationFor("home").polishWord, is(equalTo("dom")));
    }

    @Test
    public void should_return_all_translation() throws Exception {
        List<DictionaryWord> words = client.allTranslationsFor("home");
        assertThat(words, hasSize(24));
    }

    @Test(expected = RuntimeException.class)
    public void should_throw_exception_when_no_single_word_found() {
        DictDictionaryClient client = new DictDictionaryClient("wrong-url");
        client.firstTranslationFor("home");
    }

    @Test
    public void should_fail_when_no_single_word_found() {
        DictDictionaryClient client = new DictDictionaryClient("wrong-url");

        try {
            client.firstTranslationFor("home");
            fail("Expected RuntimeException wasn't thrown");
        } catch (Exception e) {
            assertThat(e.getClass(), typeCompatibleWith(RuntimeException.class));
            assertThat(e.getMessage(), equalTo("No words found"));
        }
    }

}