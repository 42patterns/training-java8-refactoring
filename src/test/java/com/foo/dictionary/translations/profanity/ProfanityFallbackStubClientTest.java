package com.foo.dictionary.translations.profanity;

import org.hamcrest.CoreMatchers;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;

public class ProfanityFallbackStubClientTest {

    final ProfanityFallbackStubClient stub = new ProfanityFallbackStubClient(new ProfanityCheckClient() {
        @Override
        public boolean isObscenityWord(String word) {
            throw new RuntimeException("Not implemented");
        }
    });

    @Test
    public void should_fallback_on_exception() throws Exception {
       assertThat(stub.isObscenityWord("shit"), equalTo(true));
       assertThat(stub.isObscenityWord("any-other"), equalTo(false));
    }
}