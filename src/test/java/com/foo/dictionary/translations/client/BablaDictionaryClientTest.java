package com.foo.dictionary.translations.client;

import com.foo.dictionary.translations.DictionaryWord;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.assertThat;

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
}
