package com.foo.dictionary.translations.command;

import com.foo.dictionary.translations.client.BablaDictionaryClient;
import com.foo.dictionary.translations.client.DictDictionaryClient;
import com.foo.dictionary.translations.client.DictionaryClient;
import com.foo.dictionary.translations.client.wrappers.ArtificialSleepWrapper;
import com.foo.dictionary.translations.client.wrappers.FallbackStubClient;
import com.foo.dictionary.translations.client.wrappers.LoggingWrapper;
import com.foo.dictionary.translations.profanity.ProfanityCheckClient;
import com.foo.dictionary.translations.profanity.ProfanityFallbackStubClient;
import com.foo.dictionary.translations.profanity.PurgoProfanityCheckClient;

public class ClientsFactory {

    static DictionaryClient getDictDictionary() {
        return new FallbackStubClient(
                new LoggingWrapper(
                        new ArtificialSleepWrapper(
                                new DictDictionaryClient()
                        )
                )
        );
    }

    static DictionaryClient getBablaDictionary() {
        return new FallbackStubClient(
                new LoggingWrapper(
                        new ArtificialSleepWrapper(
                                new BablaDictionaryClient()
                        )
                )
        );
    }

    static ProfanityCheckClient getProfanityClient() {
        return new ProfanityFallbackStubClient(
                new com.foo.dictionary.translations.profanity.LoggingWrapper(
                    new PurgoProfanityCheckClient()
                )
        );
    }
}
