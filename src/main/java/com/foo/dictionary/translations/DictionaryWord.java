package com.foo.dictionary.translations;

public class DictionaryWord {

    public final String englishWord;
    public final String polishWord;

    public DictionaryWord(String englishWord, String polishWord) {
        this.englishWord = englishWord;
        this.polishWord = polishWord;
    }

    @Override
    public String toString() {
        return "DictionaryWord{" +
                "englishWord='" + englishWord + '\'' +
                ", polishWord='" + polishWord + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DictionaryWord that = (DictionaryWord) o;

        if (englishWord != null ? !englishWord.equals(that.englishWord) : that.englishWord != null) return false;
        return polishWord != null ? polishWord.equals(that.polishWord) : that.polishWord == null;

    }

    @Override
    public int hashCode() {
        int result = englishWord != null ? englishWord.hashCode() : 0;
        result = 31 * result + (polishWord != null ? polishWord.hashCode() : 0);
        return result;
    }
}
