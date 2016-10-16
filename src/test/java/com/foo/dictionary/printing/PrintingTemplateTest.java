package com.foo.dictionary.printing;

import com.foo.dictionary.translations.DictionaryWord;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

public class PrintingTemplateTest {

    PrintingTemplate template = new PrintingTemplate(Arrays.asList(
            new DictionaryWord("hello", "cześć"),
            new DictionaryWord("good morning", "dzień dobry"),
            new DictionaryWord("hi", "cześć")
    ));

    @Test
    public void should_draw_table() {
        System.out.println(template.draw());
    }
}