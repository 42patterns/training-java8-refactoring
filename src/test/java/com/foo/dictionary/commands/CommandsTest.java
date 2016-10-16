package com.foo.dictionary.commands;

import org.hamcrest.core.Is;
import org.hamcrest.core.IsEqual;
import org.junit.Assert;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.*;

public class CommandsTest {

    @Test
    public void should_return_true_for_null_command() throws Exception {
        assertThat(Commands.isNull(null), is(true));
    }

    @Test
    public void should_trim_first_word() throws Exception {
        assertThat(Commands.trimCommandWord("translate some words"), is(equalTo("some words")));
    }

    @Test
    public void should_return_empty_string_for_single_words() throws Exception {
        assertThat(Commands.trimCommandWord("translate "), is(equalTo("")));
        assertThat(Commands.trimCommandWord("translate"), is(equalTo("")));
    }

}