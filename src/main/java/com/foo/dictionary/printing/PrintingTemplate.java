package com.foo.dictionary.printing;

import com.foo.dictionary.translations.DictionaryWord;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class PrintingTemplate {

    private final List<DictionaryWord> words;

    public PrintingTemplate(List<DictionaryWord> words) {
        this.words = words;
    }

    public String draw() {
        StringBuilder b = new StringBuilder();
        b.append(header());
        b.append(System.lineSeparator());

        Tuple<Integer> maxWidths = getMaxWidths();
        if (maxWidths.left == 0 && maxWidths.right == 0) {
            return "[No words found]";
        }

        for (DictionaryWord word: words) {
            b.append(separator());
            b.append(" ").append(word.englishWord);

            for (int i = 0; i<maxWidths.left - word.englishWord.length(); i++)
                b.append(" ");

            b.append(" * ");
            b.append(word.polishWord);

            for (int i = 0; i<maxWidths.right - word.polishWord.length(); i++)
                b.append(" ");

            b.append(" ").append(separator());
            b.append(System.lineSeparator());
        }

        b.append(footer());
        return b.toString();
    }

    protected StringBuilder header() {
        StringBuilder b = new StringBuilder();

        Tuple<Integer> maxWidths = getMaxWidths();
        if (maxWidths.left == 0 && maxWidths.right == 0) {
            return b;
        }

        for (int i = 0; i < (maxWidths.left+maxWidths.right+7); i++) {
            b.append(separator());
        }

        b.append(System.lineSeparator());
        b.append(separator());

        for (int i = 0; i <= maxWidths.left; i++) {
            b.append(" ");
        }

        b.append(" ").append(separator()).append(" ");

        for (int i = 0; i <= maxWidths.right; i++) {
            b.append(" ");
        }

        b.append(separator());
        return b;
    }
    protected StringBuilder footer() {
        StringBuilder b = new StringBuilder();

        Tuple<Integer> maxWidths = getMaxWidths();
        if (maxWidths.left == 0 && maxWidths.right == 0) {
            return b;
        }

        for (int i = 0; i < (maxWidths.left+maxWidths.right+7); i++) {
            b.append(separator());
        }

        return b;
    }
    protected String separator() {
        return "*";
    }

    protected Tuple<Integer> getMaxWidths() {
        if (words.isEmpty()) {
            return new Tuple<Integer>(0,0);
        }

        List<DictionaryWord> words = new ArrayList<DictionaryWord>(this.words);
        words.sort(new Comparator<DictionaryWord>() {
            @Override
            public int compare(DictionaryWord o1, DictionaryWord o2) {
                return Integer.compare(o2.englishWord.length(), o1.englishWord.length());
            }
        });

        Integer englishLength = words.get(0).englishWord.length();

        words.sort(new Comparator<DictionaryWord>() {
            @Override
            public int compare(DictionaryWord o1, DictionaryWord o2) {
                return Integer.compare(o2.polishWord.length(), o1.polishWord.length());
            }
        });

        Integer polishLength = words.get(0).polishWord.length();

        return new Tuple<Integer>(englishLength, polishLength);
    }

    private class Tuple<T> {
        final T left;
        final T right;

        public Tuple(T left, T right) {
            this.left = left;
            this.right = right;
        }
    }
}
