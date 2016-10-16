package com.foo.dictionary.translations.profanity;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class PurgoProfanityCheckClientTest {

    PurgoProfanityCheckClient profanityCheck = new PurgoProfanityCheckClient();

    @Test
    public void should_reject_obscenity_words() {
        assertThat(profanityCheck.isObscenityWord("shit"), equalTo(true));
        assertThat(profanityCheck.isObscenityWord("home"), equalTo(false));
    }


}