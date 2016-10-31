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
import java.util.Optional;

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

    @Test
    public void should_throw_exception_when_no_single_word_found() {
        DictDictionaryClient client = new DictDictionaryClient("wrong-url");
        Optional<Throwable> thrown = assertThrown(() -> client.firstTranslationFor("home"));

        assertThat(thrown.isPresent(), equalTo(true));
    }

    @Test
    public void should_fail_when_no_single_word_found() {
        DictDictionaryClient client = new DictDictionaryClient("wrong-url");
        Optional<Throwable> e = assertThrown(() -> client.firstTranslationFor("home"));

        assertThat(e.isPresent(), equalTo(true));
        assertThat(e.get().getClass(), typeCompatibleWith(RuntimeException.class));
        assertThat(e.get().getMessage(), equalTo("No words found"));
    }

    private Optional<Throwable> assertThrown(Runnable command) {
        try {
            command.run();
            return Optional.empty();
        } catch (Throwable t) {
            return Optional.of(t);
        }
    }

}