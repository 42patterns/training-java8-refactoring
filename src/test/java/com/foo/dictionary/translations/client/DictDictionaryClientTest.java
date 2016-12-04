package com.foo.dictionary.translations.client;

import com.foo.dictionary.translations.DictionaryWord;
import org.hamcrest.CoreMatchers;
import org.hamcrest.collection.IsCollectionWithSize;
import org.hamcrest.core.IsCollectionContaining;
import org.hamcrest.object.IsCompatibleType;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
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
        assertThat(client.firstTranslationFor("home").get().polishWord, is(equalTo("dom")));
    }

    @Test
    public void should_return_all_translation() throws Exception {
        List<DictionaryWord> words = client.allTranslationsFor("home");
        assertThat(words, hasSize(24));
    }

    @Test
    @Ignore
    public void should_return_optional_when_no_single_word_found() {
        DictDictionaryClient client = new DictDictionaryClient("wrong-url");
        Optional<DictionaryWord> maybeHome = client.firstTranslationFor("home");
        assertThat(maybeHome.isPresent(), equalTo(false));
    }

    @Ignore
    // tag::test-method-reference[]
    @Test
    public void should_throw_exception_when_no_single_word_found() {
        DictDictionaryClient client = new DictDictionaryClient("wrong-url");
        Optional<Throwable> thrown = assertThrown(()
                -> client.firstTranslationFor("home"));

        assertThat(thrown.isPresent(), equalTo(true));
    }
    // end::test-method-reference[]

    @Ignore
    @Test
    public void should_fail_when_no_single_word_found() {
        DictDictionaryClient client = new DictDictionaryClient("wrong-url");
        Optional<Throwable> e = assertThrown(() -> client.firstTranslationFor("home"));

        assertThat(e.isPresent(), equalTo(true));
        assertThat(e.get().getClass(), typeCompatibleWith(RuntimeException.class));
        assertThat(e.get().getMessage(), equalTo("No words found"));
    }

    // tag::test-method-catcher[]
    private Optional<Throwable> assertThrown(Runnable command) {
        try {
            command.run();
            return Optional.empty();
        } catch (Throwable t) {
            return Optional.of(t);
        }
    }
    // end::test-method-catcher[]

    // tag::test-checked-method-catcher[]
    private interface CheckedRunnable {
        public void run() throws Exception;
    }

    private Optional<Throwable> checkedAssertThrown(CheckedRunnable command) {
        try {
            command.run();
            return Optional.empty();
        } catch (Throwable t) {
            return Optional.of(t);
        }
    }
    // end::test-checked-method-catcher[]

}