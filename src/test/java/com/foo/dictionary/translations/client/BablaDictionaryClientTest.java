package com.foo.dictionary.translations.client;

import com.foo.dictionary.translations.DictionaryWord;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.object.IsCompatibleType.typeCompatibleWith;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

public class BablaDictionaryClientTest {

    BablaDictionaryClient client;

    @Before
    public void setupUrl() {
        String baseName = getClass().getResource("/").toExternalForm();
        String url = baseName + "babla-{%%%}.html";
        client = new BablaDictionaryClient(url);
    }

    @Test
    public void should_return_home_for_first_translation() throws Exception {
        assertThat(client.firstTranslationFor("good morning").get().polishWord, is(equalTo("dzień dobry")));
    }

    @Test
    public void should_return_all_translation() throws Exception {
        List<DictionaryWord> words = client.allTranslationsFor("home");
        assertThat(words, hasSize(23));
    }


    @Ignore
    // tag::test-exception[]
    @Test(expected = RuntimeException.class)
    public void should_throw_exception_when_no_single_word_found() {
        DictDictionaryClient client = new DictDictionaryClient("wrong-url");
        client.firstTranslationFor("home");
    }
    // end::test-exception[]

    @Ignore
    // tag::test-try-catch[]
    @Test
    public void should_fail_when_no_single_word_found() {
        DictDictionaryClient client = new DictDictionaryClient("wrong-url");

        try {
            client.firstTranslationFor("home");
            fail("Exception expected");
        } catch (Exception e) {
            assertThat(e.getMessage(), equalTo("No words found"));
        }
    }
    // end::test-try-catch[]
}
